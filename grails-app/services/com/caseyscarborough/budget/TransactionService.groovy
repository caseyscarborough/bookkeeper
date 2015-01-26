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

  def createTransaction(String description, Double amount, Account account, SubCategory subCategory, Date date, User user) {
    def transaction = new Transaction(description: description, amount: amount, account: account, subCategory: subCategory, date: date, user: user)

    if (!transaction.save(flush: true)) {
      def message = messageSource.getMessage(transaction.errors.fieldError, Locale.default)
      throw new TransactionException(message: message, transaction: transaction)
    }

    if (transaction.subCategory.category.type == CategoryType.DEBIT) {
      account.balance = account.balance - transaction.amount
    } else {
      account.balance = account.balance + transaction.amount
    }
    account.save(flush: true)
    return transaction
  }
}
