package com.caseyscarborough.budget

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import org.springframework.http.HttpStatus

@Secured('IS_AUTHENTICATED_FULLY')
@Transactional(readOnly = true)
class AccountController {

  def accountService
  def springSecurityService

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

  def index() {
    def accountList = Account.findAllByUser(springSecurityService.currentUser)
    [accountList: accountList, accountListCount: accountList.size(), accountTypes: AccountType.findAll()?.sort { it.name }]
  }

  @Transactional
  def save() {
    try {
      def account = accountService.createAccount(params.description, new BigDecimal(params.balance), AccountType.valueOf(params.type), springSecurityService.currentUser)
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

  def edit(Account accountInstance) {
    respond accountInstance
  }

  @Transactional
  def delete(Long id) {
    def accountInstance = Account.get(id)
    accountInstance.delete(flush: true)
    response.status = HttpStatus.NO_CONTENT.value()
    render([] as JSON)
  }
}
