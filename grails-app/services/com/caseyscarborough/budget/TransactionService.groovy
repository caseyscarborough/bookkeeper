package com.caseyscarborough.budget

import com.caseyscarborough.budget.security.User
import grails.transaction.Transactional

class TransactionException extends RuntimeException {
  String message
  Transaction transaction
}

@Transactional
class TransactionService {
  def messageSource

  def createTransaction(String description, BigDecimal amount, Account fromAccount, Account toAccount, SubCategory subCategory, Date date, User user) {
    def transaction = new Transaction(description: description, amount: amount, fromAccount: fromAccount, toAccount: toAccount, subCategory: subCategory, date: date, user: user)

    if (!transaction.save(flush: true)) {
      def message = messageSource.getMessage(transaction.errors.fieldError, Locale.default)
      throw new TransactionException(message: message, transaction: transaction)
    }

    updateAccountBalance(transaction, false)
    return transaction
  }

  def updateTransaction(Transaction transaction, String description, BigDecimal amount, Account fromAccount, Account toAccount, SubCategory subCategory, Date date) {
    // Subtract amount from original account and add to new account,
    // whether it be the same account or a different one.
    transaction.fromAccount.receivePayment(transaction.amount)
    fromAccount.sendPayment(amount)

    transaction.description = description
    transaction.amount = amount
    transaction.fromAccount = fromAccount
    transaction.toAccount = toAccount
    transaction.subCategory = subCategory
    transaction.date = date
    transaction.save(flush: true)
    return transaction
  }

  def deleteTransaction(Transaction transaction) {
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
      transaction.fromAccount.sendPayment(amount)
      transaction.toAccount.receivePayment(amount)
      transaction.toAccount?.save(flush: true)
      transaction.fromAccount?.save(flush: true)
    } else {
      transaction.fromAccount.sendPayment(amount)
      transaction.fromAccount.save(flush: true)
    }
  }
}
