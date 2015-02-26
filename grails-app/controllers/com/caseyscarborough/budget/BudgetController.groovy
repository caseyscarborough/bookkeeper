package com.caseyscarborough.budget

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

class BudgetController {

  def budgetService

  def index() {
    def now = new Date()
    def budget = budgetService.getBudgetForDate(now)
    [budget: budget, categories: Category.all]
  }

  def addCategoryToBudget() {
    def now = new Date()
    def category = SubCategory.get(params.id)
    def budget = budgetService.getBudgetForDate(now)
    budget.addToBudgetItems(new BudgetItem(category: category))
    budget.save(flush: true)
    render budget as JSON
  }
}
