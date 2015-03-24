package com.caseyscarborough.budget

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured('IS_AUTHENTICATED_REMEMBERED')
class BudgetItemController {

  static allowedMethods = [update: 'POST', delete: 'DELETE']

  def springSecurityService

  def update() {
    def budgetItem = BudgetItem.get(params.id)
    if (budgetItem && budgetItem.budget.user == springSecurityService.currentUser) {
      budgetItem.budgetedAmount = new BigDecimal(params.amount as String)
      budgetItem.save(flush: true)
      render budgetItem as JSON
    }
  }

  def delete(Long id) {
    def budgetItem = BudgetItem.get(id)
    if (budgetItem && budgetItem.budget.user == springSecurityService.currentUser) {
      budgetItem.delete(flush: true)
      render ""
    }
  }
}
