package com.caseyscarborough.bookkeeper

import com.caseyscarborough.bookkeeper.security.User
import grails.transaction.Transactional
import org.springframework.web.multipart.commons.CommonsMultipartFile
import pl.touk.excel.export.WebXlsxExporter

import javax.servlet.http.HttpServletResponse
import java.text.ParseException

class TransactionException extends RuntimeException {
  String message
  Transaction transaction
}

@Transactional
class TransactionService {
  def messageSource
  def receiptService
  def springSecurityService

  static enum TransactionType {
    CREATION, DELETION
  }

  def createTransaction(String description, BigDecimal amount, Account fromAccount, Account toAccount, SubCategory subCategory, Date date, User user, CommonsMultipartFile receipt) {
    def transaction = new Transaction(description: description, amount: amount, fromAccount: fromAccount, toAccount: toAccount, subCategory: subCategory, date: date, user: user)

    if (!transaction.save(flush: true)) {
      def message = messageSource.getMessage(transaction.errors.fieldError, Locale.default)
      throw new TransactionException(message: message, transaction: transaction)
    }

    updateAccountBalance(transaction, TransactionType.CREATION)

    if (receipt) {
      receiptService.createReceipt(receipt, transaction)
    }
    return transaction
  }

  def updateTransaction(Long id, String description, BigDecimal amount, Account fromAccount, Account toAccount, SubCategory subCategory, Date date, CommonsMultipartFile receipt) {
    def transaction = Transaction.get(id)

    if (!transaction) {
      throw new TransactionException(message: "Could not find transaction with ID: ${id}")
    }

    if (transaction.user != springSecurityService.currentUser) {
      throw new TransactionException(message: "Unauthorized")
    }
    // Subtract amount from original account and add to new account,
    // whether it be the same account or a different one.
    updateAccountBalance(transaction, TransactionType.DELETION)
    transaction.description = description
    transaction.amount = amount
    transaction.fromAccount = fromAccount
    transaction.toAccount = toAccount
    transaction.subCategory = subCategory
    transaction.date = date
    transaction.save(flush: true)
    receiptService.updateReceipt(transaction)
    updateAccountBalance(transaction, TransactionType.CREATION)

    if (receipt) {
      receiptService.createReceipt(receipt, transaction)
    }
    return transaction
  }

  def deleteTransaction(Long id) {
    def transaction = Transaction.get(id)

    if (!transaction) {
      throw new TransactionException(message: "Could not find transaction with ID: ${id}")
    }

    if (transaction.user != springSecurityService.currentUser) {
      throw new TransactionException(message: "Unauthorized")
    }

    if (transaction.receipt) {
      def receipt = transaction.receipt
      transaction.receipt = null
      receipt.delete(flush: true)
    }
    transaction.delete(flush: true)
    updateAccountBalance(transaction, TransactionType.DELETION)
  }

  def duplicateTransaction(Long id, String dateString) {
    def transaction = Transaction.get(id)

    if (!transaction) {
      throw new TransactionException(message: "Could not find transaction with ID: ${id}")
    }

    if (transaction.user != springSecurityService.currentUser) {
      throw new TransactionException(message: "Unauthorized")
    }

    Date date
    try {
      date = Date.parse('MM/dd/yyyy', dateString)
    } catch (ParseException e) {
      throw new TransactionException("Date must be in the following format: 'MM/dd/yyyy")
    }

    def newTransaction = new Transaction(
        description: transaction.description,
        amount: transaction.amount,
        date: date,
        fromAccount: transaction.fromAccount,
        toAccount: transaction.toAccount,
        subCategory: transaction.subCategory,
        user: transaction.user)

    if (!newTransaction.save(flush: true)) {
      def message = messageSource.getMessage(newTransaction.errors.fieldError, Locale.default)
      throw new TransactionException(message: message, transaction: newTransaction)
    }

    updateAccountBalance(newTransaction, TransactionType.CREATION)
    return newTransaction
  }

  public synchronizeBalances() {
    Account.findAllByUser(springSecurityService.currentUser).each { Account a ->
      log.info("Processing transactions for account ${a}")
      def amount = a.balance
      def transactions = Transaction.findAllByFromAccountOrToAccount(a, a)

      if (transactions.size() > 0) {
        transactions.each { Transaction t ->
          if (t.subCategory.type == CategoryType.TRANSFER) {
            if (t.fromAccount == a) {
              t.accountBalance = amount
              amount += a.type.isDebt ? -t.amount : t.amount
            } else if (t.toAccount == a) {
              amount -= a.type.isDebt ? -t.amount : t.amount
            }
          } else if (t.subCategory.type == CategoryType.CREDIT) {
            t.accountBalance = amount
            amount -= a.type.isDebt ? -t.amount : t.amount
          } else if (t.subCategory.type == CategoryType.DEBIT) {
            t.accountBalance = amount
            amount += a.type.isDebt ? -t.amount : t.amount
          }
          t.save(flush: true)
        }
      }
    }
    log.info("Complete.")
  }

  public exportTransactionsToExcel(List<Transaction> transactions, HttpServletResponse response) {
    def headers = ['Date', 'Description', 'Amount', 'Category', 'Subcategory', 'From Account', 'To Account', 'Transaction Type', 'Account Balance']
    def properties = ['date', 'description', 'amount', 'subCategory.category', 'subCategory', 'fromAccount', 'toAccount', 'subCategory.type', 'accountBalance']

    new WebXlsxExporter().with {
      setResponseHeaders(response)
      fillHeader(headers)
      add(transactions, properties)
      save(response.outputStream)
    }
  }

  private void updateAccountBalance(Transaction transaction, TransactionType type) {
    def amount = transaction.amount

    if (type == TransactionType.DELETION) {
      amount *= -1
    }

    if (transaction.subCategory.type == CategoryType.CREDIT) {
      amount *= -1
    }

    if (transaction.subCategory.type == CategoryType.TRANSFER) {
      log.info("Deducting ${amount} dollars from ${transaction.fromAccount}")
      transaction.fromAccount.sendPayment(amount)
      log.info("Receiving ${amount} dollars to ${transaction.toAccount}")
      transaction.toAccount?.receivePayment(amount)
      transaction.toAccount?.save(flush: true)
      transaction.fromAccount?.save(flush: true)
    } else {
      transaction.fromAccount.sendPayment(amount)
      transaction.fromAccount.save(flush: true)
    }
  }
}
