package com.caseyscarborough.budget

import grails.transaction.Transactional

@Transactional
class BudgetService {

  def springSecurityService

  def getBudgetForDate(Date date) {
    def budget = Budget.findByStartDateLessThanAndEndDateGreaterThan(date, date)

    if (!budget) {
      budget = createBudget(date)
    }
    return budget
  }

  def createBudget(Date date) {
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

    def budget = new Budget(user: springSecurityService.currentUser, startDate: startCal.time, endDate: endCal.time)
    budget.save(flush: true)
    return budget
  }
}
