package com.caseyscarborough.budget

import com.caseyscarborough.budget.security.User
import grails.transaction.Transactional

class AccountException extends RuntimeException {
  String message
  Account account
}

@Transactional
class AccountService {

  def messageSource

  Account createAccount(String description, BigDecimal balance, AccountType type, User user) {
    def account = new Account(description: description, balance: balance, type: type, user: user)
    if (!account.save(flush: true)) {
      def message = messageSource.getMessage(account.errors.fieldError, Locale.default)
      log.debug("Account has errors: ${message}")
      throw new AccountException(message: message, account: account)
    }

    return account
  }

  void deleteAccount(Long id) {
    def accountInstance = Account.get(id)

    if (!accountInstance) {
      throw new AccountException(message: "Could not find account with ID: ${id}")
    }

    try {
      accountInstance.delete(flush: true)
    } catch (Exception e) {
      throw new AccountException(message: "Can't delete account due to relationship with other entities.")
    }
  }
}
