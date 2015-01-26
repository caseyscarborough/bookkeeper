package com.caseyscarborough.budget

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured('IS_AUTHENTICATED_FULLY')
@Transactional(readOnly = true)
class TransactionController {

  def springSecurityService
  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

  def index(Integer max) {
    params.max = Math.min(max ?: 20, 100)
    params.sort = "date"
    params.order = "desc"
    def transactions = Transaction.findAllByUser(springSecurityService.currentUser, params)
    def accounts = Account.findAllByUser(springSecurityService.currentUser)
    [transactions: transactions, transactionInstanceCount: Transaction.count(), accounts: accounts, categories: Category.all?.sort { it.name }]
  }
}
