package com.caseyscarborough.budget

import com.caseyscarborough.budget.security.User
import grails.transaction.Transactional
import org.springframework.web.multipart.commons.CommonsMultipartFile

class TransactionException extends RuntimeException {
  String message
  Transaction transaction
}

@Transactional
class TransactionService {
  def messageSource
  def receiptService
  def springSecurityService

  def createTransaction(String description, BigDecimal amount, Account fromAccount, Account toAccount, SubCategory subCategory, Date date, User user, CommonsMultipartFile receipt) {
    def transaction = new Transaction(description: description, amount: amount, fromAccount: fromAccount, toAccount: toAccount, subCategory: subCategory, date: date, user: user)

    if (!transaction.save(flush: true)) {
      def message = messageSource.getMessage(transaction.errors.fieldError, Locale.default)
      throw new TransactionException(message: message, transaction: transaction)
    }

    updateAccountBalance(transaction, false)

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
    updateAccountBalance(transaction, true)
    transaction.description = description
    transaction.amount = amount
    transaction.fromAccount = fromAccount
    transaction.toAccount = toAccount
    transaction.subCategory = subCategory
    transaction.date = date
    transaction.save(flush: true)
    receiptService.updateReceipt(transaction)
    updateAccountBalance(transaction, false)

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
    updateAccountBalance(transaction, true)
  }

  private void updateAccountBalance(Transaction transaction, Boolean deletion) {
    def amount = transaction.amount
    if (deletion) {
      amount = -amount
    }

    if (transaction.subCategory.type == CategoryType.CREDIT) {
      amount = -amount
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
