package com.caseyscarborough.bookkeeper

import com.caseyscarborough.bookkeeper.security.User
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import org.springframework.http.HttpStatus

@Secured('IS_AUTHENTICATED_REMEMBERED')
@Transactional(readOnly = true)
class AccountController {

  def accountService
  def springSecurityService

  static allowedMethods = [save: "POST", update: "POST", delete: "DELETE"]

  def index() {
    def accountList = Account.findAllByUser(springSecurityService.currentUser as User, params)
    [accountList: accountList, accountListCount: accountList.size(), accountTypes: AccountType.findAll()?.sort {it.name}]
  }

  @Transactional
  def save() {
    try {
      def account = accountService.createAccount(params.description, new BigDecimal(params.balance as String), AccountType.valueOf(params.type), springSecurityService.currentUser)
      response.status = HttpStatus.CREATED.value()
      render account as JSON
    } catch (AccountException e) {
      response.status = HttpStatus.BAD_REQUEST.value()
      render([message: e.message, field: e.account.errors.fieldError.field] as JSON)
    } catch (NumberFormatException e) {
      response.status = HttpStatus.BAD_REQUEST.value()
      render([message: "Please enter a valid balance for the account.", field: "balance"] as JSON)
    }
  }

  @Transactional
  def update() {
    log.info(params)
    try {
      def account = accountService.updateAccount(params.id as Long, params.description as String, new BigDecimal(params.balance as String), params.active == "true")
      render account as JSON
    } catch (AccountException e) {
      response.status = HttpStatus.BAD_REQUEST.value()
      render([message: e.message, field: e.account?.errors?.fieldError?.field] as JSON)
    } catch (NumberFormatException e) {
      response.status = HttpStatus.BAD_REQUEST.value()
      render([message: "Please enter a valid balance for the account.", field: "balance"] as JSON)
    }
  }

  @Transactional
  def delete(Long id) {
    try {
      accountService.deleteAccount(id)
      response.status = HttpStatus.NO_CONTENT.value()
      render ""
    } catch (AccountException e) {
      response.status = HttpStatus.BAD_REQUEST.value()
      render([message: e.message, field: e.account?.errors?.fieldError?.field] as JSON)
    }
  }
}
