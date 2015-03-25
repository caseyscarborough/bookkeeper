package com.caseyscarborough.bookkeeper

import com.caseyscarborough.bookkeeper.security.User
import grails.transaction.Transactional

@Transactional
class BudgetService {

  def springSecurityService

  def getBudgetForDate(Date date) {
    log.info("Looking up budget for user ${springSecurityService.currentUser}")
    def budget = Budget.findByStartDateLessThanAndEndDateGreaterThanAndUser(date, date, springSecurityService.currentUser as User)

    if (!budget) {
      budget = createBudget(date)
    }
    return budget
  }

  Budget createBudget(Date date) {
    log.info("Creating new budget for ${date}...")
    def startCal = Calendar.instance
    startCal.setTime(date)
    startCal.set(Calendar.DAY_OF_MONTH, 1)
    startCal.set(Calendar.HOUR_OF_DAY, 0)
    startCal.set(Calendar.MINUTE, 0)
    startCal.set(Calendar.SECOND, 0)
    startCal.set(Calendar.MILLISECOND, 0)

    def endCal = Calendar.instance
    endCal.setTime(startCal.time)
    endCal.add(Calendar.MONTH, 1)
    endCal.add(Calendar.MILLISECOND, -1)

    def budget = new Budget(user: springSecurityService.currentUser as User, startDate: startCal.time, endDate: endCal.time, budgetItems: [], slug: startCal.time.format("yyyyMM"))
    budget.save(flush: true)

    def budgets = Budget.findAllByUser(springSecurityService.currentUser)
    if (budgets.size() > 1) {
      log.info("Previous budgets exist, duplicating last budget...")
      budgets = budgets.sort { it.startDate }.reverse()
      duplicateBudget(budgets[1], budget)
    }

    return budget
  }

  Budget duplicateBudget(Budget oldBudget, Budget newBudget) {
    oldBudget.budgetItems.each { BudgetItem b ->
      BudgetItem newBudgetItem = new BudgetItem()
      newBudgetItem.budgetedAmount = b.budgetedAmount
      newBudgetItem.category = b.category
      newBudgetItem.budget = newBudget
      newBudgetItem.save(flush: true)
      newBudget.addToBudgetItems(newBudgetItem)
    }
    log.info("Duplication complete.")
    newBudget.save(flush: true)
    return newBudget
  }

  void synchronizeBudget(Budget budget) {
    log.info("Synchronizing budget with ID: ${budget.id}")
    budget.budgetItems.each { BudgetItem item ->
      def transactions = Transaction.findAllBySubCategoryAndDateGreaterThanEqualsAndDateLessThanEqualsAndUser(item.category, budget.startDate, budget.endDate, springSecurityService.currentUser as User)
      BigDecimal sum = 0
      transactions.each { Transaction t ->
        sum += t.amount
      }
      item.actualAmount = sum
    }
    budget.save(flush: true)
  }

  def getDataForBudget(Budget budget) {
    def data = [:]
    budget.budgetItems.each { BudgetItem b ->
      if (!data."${b.category.category}") {
        data."${b.category.category}" = []
      }

      data."${b.category.category}" << b
    }

    def output = []
    data.each { name, items ->
      items = items.sort { it.category.name }
      def budgetedAmount = items*.budgetedAmount.sum() as BigDecimal
      def actualAmount = items*.actualAmount.sum() as BigDecimal

      def percentage = budgetedAmount == 0 as BigDecimal ? 0 : new BigDecimal((actualAmount / budgetedAmount) * 100).setScale(2, BigDecimal.ROUND_HALF_UP)
      output << [name: name, items: items, budgetedAmount: budgetedAmount, actualAmount: actualAmount, percentage: percentage]
    }
    return output
  }
}
