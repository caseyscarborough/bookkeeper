package com.caseyscarborough.bookkeeper

import com.caseyscarborough.bookkeeper.security.User
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import org.springframework.http.HttpStatus

@Secured('IS_AUTHENTICATED_REMEMBERED')
class BudgetController {

  def budgetService
  def springSecurityService

  static allowedMethods = [addCategoryToBudget: 'POST', synchronize: 'POST', delete: 'GET']

  def show(String slug) {
    def budget
    if (slug) {
      budget = Budget.findBySlugAndUser(slug, springSecurityService.currentUser as User)
      if (!budget) {
        response.sendError(404)
        return
      }
    } else {
      budget = budgetService.getBudgetForDate(new Date())
    }

    def budgetItems = budget.budgetItems.sort { it.category.name }
    def budgets = Budget.findAllByUser(springSecurityService.currentUser as User).sort { it.startDate }
    [budget: budget, categories: Category.list(), budgetItems: budgetItems, budgets: budgets]
  }

  def addCategoryToBudget() {
    def category = SubCategory.get(params.category)
    def budget = Budget.get(params.budget)

    if (budget.user == springSecurityService.currentUser && !budget.hasBudgetItemForCategory(category)) {
      def budgetItem = new BudgetItem(category: category, budget: budget)
      budgetItem.save(flush: true)
      render budget as JSON
      return
    }
    response.status = HttpStatus.BAD_REQUEST.value()
    render([message: "This budget already has an item for the ${category} category."] as JSON)
  }

  def synchronize() {
    def budget = Budget.get(params.budget)
    budget.budgetItems.each { BudgetItem item ->
      log.info("Looking up transactions for ${item.category}")
      def transactions = Transaction.findAllBySubCategoryAndDateGreaterThanEqualsAndDateLessThanEqualsAndUser(item.category, budget.startDate, budget.endDate, springSecurityService.currentUser as User)
      BigDecimal sum = 0
      transactions.each { Transaction t ->
        log.info("${item.category} - ${t.amountString}")
        sum += t.amount
      }
      item.actualAmount = sum
    }
    budget.save(flush: true)
    render budget as JSON
  }
}
