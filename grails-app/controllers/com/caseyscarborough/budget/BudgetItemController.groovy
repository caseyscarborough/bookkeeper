package com.caseyscarborough.budget

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured('IS_AUTHENTICATED_REMEMBERED')
class BudgetItemController {

  static allowedMethods = [update: 'POST']

  def update() {
    def budgetItem = BudgetItem.get(params.id)
    budgetItem.budgetedAmount = new BigDecimal(params.amount as String)
    budgetItem.save(flush: true)
    render budgetItem as JSON
  }
}
