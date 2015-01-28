package com.caseyscarborough.budget

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured('IS_AUTHENTICATED_FULLY')
class GraphController {

  def springSecurityService

  def index() {
    def months = []
    def transactions = Transaction.all?.sort { it.date }
    def startCal = Calendar.instance
    startCal.setTime(transactions.first()?.date)
    def endCal = Calendar.instance
    endCal.setTime(transactions.last().date)

    while(startCal.time < endCal.time) {
      months << [name: startCal.time.format("MMMMM yyyy"), value: startCal.time.format("yyyyMM")]
      startCal.add(Calendar.MONTH, 1)
    }
    [months: months.reverse()]
  }

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

  def spendingWithSubcategory() {
    def categories = []
    def drilldown = []
    def time = Date.parse("yyyyMM", params.time)
    def startCal = Calendar.instance
    def endCal = Calendar.instance
    startCal.setTime(time)
    startCal.set(Calendar.DAY_OF_MONTH, 1)
    startCal.add(Calendar.DAY_OF_YEAR, -1)
    startCal.set(Calendar.HOUR_OF_DAY, 23)
    startCal.set(Calendar.MINUTE, 59)
    startCal.set(Calendar.SECOND, 59)
    startCal.set(Calendar.MILLISECOND, 59)

    endCal.setTime(startCal.time)
    endCal.add(Calendar.MONTH, 1)

    def transactions = Transaction.findAllByDateGreaterThanAndDateLessThanAndUser(startCal.time, endCal.time, springSecurityService.currentUser)
    def totalTransactionsSum = 0
    transactions.each { if (it.subCategory.type == CategoryType.DEBIT) totalTransactionsSum += it.amount }

    Category.all?.eachWithIndex { Category c, i ->
      def totalCategoryTransactions = Transaction.where {
        date < endCal.time && date > startCal.time && user == springSecurityService.currentUser && subCategory.category == c
      }
      def totalCategoryTransactionsSum = 0
      totalCategoryTransactions.each { if (it.subCategory.type == CategoryType.DEBIT) totalCategoryTransactionsSum += it.amount }

      if (categories.size() != i+1) {
        if (totalCategoryTransactionsSum > 0) {
          categories << [drilldown: c.name, name: c.name, categoryTotal: "\$${totalCategoryTransactionsSum}", grandTotal: "\$${totalTransactionsSum}", visible: true, y: (totalCategoryTransactionsSum / totalTransactionsSum)]
          drilldown << [ id: c.name, name: c.name, categoryTotal: "\$${totalCategoryTransactionsSum}", grandTotal: "\$${totalTransactionsSum}", data: []]
        }
      }

      SubCategory.findAllByCategory(c)?.each { SubCategory s ->
        def subCategoryTransactions = Transaction.findAllByDateGreaterThanAndDateLessThanAndUserAndSubCategory(startCal.time, endCal.time, springSecurityService.currentUser, s)
        def subCategoryTransactionsSum = 0
        subCategoryTransactions.each { if (it.subCategory.type == CategoryType.DEBIT) subCategoryTransactionsSum += it.amount }

        if (subCategoryTransactionsSum > 0) {
          if (totalCategoryTransactionsSum > 0) {
            drilldown.last().data << [name: s.name, y: (subCategoryTransactionsSum / totalCategoryTransactionsSum), categoryTotal: "\$${subCategoryTransactionsSum}", grandTotal: "\$${totalCategoryTransactionsSum}"]
          }
        }
      }
    }
    render([month: endCal.time.format("MMMMM yyyy"), categories: categories, drilldown: drilldown] as JSON)
  }
}
