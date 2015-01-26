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

    updateAccountBalance(account, transaction, false)
    return transaction
  }

  def deleteTransaction(Transaction transaction) {
    transaction.delete(flush: true)
    updateAccountBalance(transaction.account, transaction, true)
  }

  private void updateAccountBalance(Account account, Transaction transaction, Boolean deletion) {
    def actualAmount = (account.type.isDebt) ? -transaction.amount : transaction.amount
    if (!deletion) {
      account.balance -= (transaction.subCategory.category.type == CategoryType.DEBIT) ? actualAmount : -actualAmount
    } else {
      account.balance += (transaction.subCategory.category.type == CategoryType.DEBIT) ? actualAmount : -actualAmount
    }
    account.save(flush: true)
  }
}
