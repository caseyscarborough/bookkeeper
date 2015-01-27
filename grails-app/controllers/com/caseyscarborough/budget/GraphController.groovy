package com.caseyscarborough.budget

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured('IS_AUTHENTICATED_FULLY')
class GraphController {

  def springSecurityService

  def index() {}

  def spendingByDay() {
    def data = []
    def now = new Date()
    def startCal = Calendar.instance
    def endCal = Calendar.instance
    startCal.add(Calendar.YEAR, -1)
    endCal.setTime(startCal.time)
    endCal.add(Calendar.DAY_OF_YEAR, 1)

    while (endCal.time <= now) {
      def transactions = Transaction.findAllByDateBetweenAndUser(startCal.time, endCal.time, springSecurityService.currentUser)
      BigDecimal total = 0
      transactions?.each { Transaction t ->
        if (t.subCategory.type == CategoryType.DEBIT) {
          total += t.amount
        }
      }
      data << total

      startCal.add(Calendar.DAY_OF_YEAR, 1)
      endCal.add(Calendar.DAY_OF_YEAR, 1)
    }

    render data as JSON
  }

  def spendingByCategory() {
    def data = [:]

    def now = new Date()
    def startCal = Calendar.instance
    def endCal = Calendar.instance
    startCal.add(Calendar.MONTH, -11)
    startCal.set(Calendar.DAY_OF_MONTH, 1)
    startCal.add(Calendar.DAY_OF_YEAR, -1)
    startCal.set(Calendar.HOUR_OF_DAY, 23)
    startCal.set(Calendar.MINUTE, 59)
    startCal.set(Calendar.SECOND, 59)
    startCal.set(Calendar.MILLISECOND, 59)

    endCal.setTime(startCal.time)
    endCal.add(Calendar.MONTH, 1)

    int i = 0
    def months = []
    while (startCal.time <= now) {
      months << endCal.time.format("MMM. yyyy")
      log.debug("Generating transactions for ${endCal.get(Calendar.MONTH)} ${endCal.get(Calendar.YEAR)} starting on day ${startCal.get(Calendar.DAY_OF_MONTH)} and ending on ${endCal.get(Calendar.DAY_OF_MONTH)}")
      def transactions = Transaction.findAllByDateGreaterThanAndDateLessThanAndUser(startCal.time, endCal.time, springSecurityService.currentUser)
      transactions?.each { Transaction t ->
        if (t.subCategory.type == CategoryType.DEBIT) {
          if (!data."${t.subCategory.category}") {
            data."${t.subCategory.category}" = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
          }
          try {
            if (t.subCategory.name == "Wages") log.debug(t.amount)
            //log.debug("Adding ${t.amount} to ${t.subCategory.category}")
            data."${t.subCategory.category}"[i] += t.amount
          } catch (NullPointerException e) {}
        }
      }
      i++
      startCal.add(Calendar.MONTH, 1)
      endCal.add(Calendar.MONTH, 1)
    }
    def categories = []
    data.each { k, v ->
      categories << [name: k, data: v]
    }
    render([months: months, categories: categories] as JSON)
  }
}
