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
  def springSecurityService

  Account createAccount(String description, BigDecimal balance, AccountType type, User user) {
    def account = new Account(description: description, balance: balance, type: type, user: user)
    if (!account.save(flush: true)) {
      def message = messageSource.getMessage(account.errors.fieldError, Locale.default)
      log.debug("Account has errors: ${message}")
      throw new AccountException(message: message, account: account)
    }

    return account
  }

  Account updateAccount(Long id, String description, BigDecimal balance) {
    def account = Account.get(id)

    if (!account) {
      throw new AccountException(message: "Could not find account with ID: ${id}")
    }

    if (account.user != springSecurityService.currentUser) {
      throw new AccountException(message: "Unauthorized")
    }

    account.description = description
    account.balance = balance

    if (!account.save(flush: true)) {
      def message = messageSource.getMessage(account.errors.fieldError, Locale.default)
      log.debug("Account has errors: ${message}")
      throw new AccountException(message: message, account: account)
    }

    return account
  }

  void deleteAccount(Long id) {
    def account = Account.get(id)

    if (!account) {
      throw new AccountException(message: "Could not find account with ID: ${id}")
    }

    if (account.user != springSecurityService.currentUser) {
      throw new AccountException(message: "Unauthorized")
    }

    try {
      account.delete(flush: true)
    } catch (Exception e) {
      throw new AccountException(message: "Unable to delete account because it has transactions associated with it.")
    }
  }
}
