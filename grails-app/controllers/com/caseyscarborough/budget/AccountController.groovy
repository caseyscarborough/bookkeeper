package com.caseyscarborough.budget

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured('IS_AUTHENTICATED_FULLY')
@Transactional(readOnly = true)
class AccountController {

  def springSecurityService

  def index() {
    def accountList = Account.findAllByUser(springSecurityService.currentUser)
    [accountList: accountList, accountListCount: accountList.size(), accountTypes: AccountType.findAll()]
  }
}
