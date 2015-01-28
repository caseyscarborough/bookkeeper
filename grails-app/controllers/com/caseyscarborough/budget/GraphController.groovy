package com.caseyscarborough.budget

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured('IS_AUTHENTICATED_FULLY')
class GraphController {

  def springSecurityService

  private static final String DATE_FORMAT_FOR_MONTH_SELECTION = "yyyyMM"
  private static final Integer NUMBER_OF_MONTHS_TO_ANALYZE = 12

  def index() {
    def months = []
    def transactions = Transaction.all?.sort { it.date }
    def startCal = Calendar.instance
    startCal.setTime(transactions.first()?.date)
    def endCal = Calendar.instance
    endCal.setTime(transactions.last().date)

    while (startCal.time < endCal.time) {
      months << [name: startCal.time.format("MMMMM yyyy"), value: startCal.time.format(DATE_FORMAT_FOR_MONTH_SELECTION)]
      startCal.add(Calendar.MONTH, 1)
    }
    [months: months.reverse()]
  }

  def spendingByDay() {
    def data = []
    def now = new Date()
    def startCal = Calendar.instance
    def endCal = Calendar.instance
    startCal.add(Calendar.MONTH, -(NUMBER_OF_MONTHS_TO_ANALYZE))
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
    startCal.add(Calendar.MONTH, -(NUMBER_OF_MONTHS_TO_ANALYZE - 1))
    startCal.set(Calendar.DAY_OF_MONTH, 1)
    startCal.add(Calendar.DAY_OF_YEAR, -1)
    startCal = setCalendarToMidnight(startCal)
    endCal.setTime(startCal.time)
    endCal.add(Calendar.MONTH, 1)

    int i = 0
    def months = []
    while (startCal.time <= now) {
      months << endCal.time.format("MMM. yyyy")
      def transactions = Transaction.findAllByDateGreaterThanAndDateLessThanAndUser(startCal.time, endCal.time, springSecurityService.currentUser)
      transactions?.each { Transaction t ->
        if (t.subCategory.type == CategoryType.DEBIT) {
          if (!data."${t.subCategory.category}") {
            data."${t.subCategory.category}" = [0] * NUMBER_OF_MONTHS_TO_ANALYZE
          }
          try {
            data."${t.subCategory.category}"[i] += t.amount
          } catch (NullPointerException e) { /* Ignore */
          }
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
    def time = Date.parse(DATE_FORMAT_FOR_MONTH_SELECTION, params.time)
    def startCal = Calendar.instance
    def endCal = Calendar.instance
    startCal.setTime(time)
    startCal.set(Calendar.DAY_OF_MONTH, 1)
    startCal.add(Calendar.DAY_OF_YEAR, -1)
    startCal = setCalendarToMidnight(startCal)
    endCal.setTime(startCal.time)
    endCal.add(Calendar.MONTH, 1)

    def transactions = Transaction.findAllByDateGreaterThanAndDateLessThanAndUser(startCal.time, endCal.time, springSecurityService.currentUser)
    def totalTransactionsSum = getSumForDebitTransactions(transactions)

    Category.all?.eachWithIndex { Category c, i ->
      def categoryTransactions = Transaction.where {
        date < endCal.time && date > startCal.time && user == springSecurityService.currentUser && subCategory.category == c
      }
      def categoryTransactionsSum = getSumForDebitTransactions(categoryTransactions)
      if (categories.size() != i + 1) {
        if (categoryTransactionsSum > 0) {
          categories << [drilldown: "${c.name} - ${startCal.time.format("MMM. yyyy")}", name: c.name, categoryTotal: "\$${categoryTransactionsSum}", grandTotal: "\$${totalTransactionsSum}", visible: true, y: ((categoryTransactionsSum / totalTransactionsSum) * 100)]
          drilldown << [id: "${c.name} - ${startCal.time.format("MMM. yyyy")}", name: c.name, categoryTotal: "\$${categoryTransactionsSum}", grandTotal: "\$${totalTransactionsSum}", data: []]
        }
      }

      SubCategory.findAllByCategory(c)?.each { SubCategory s ->
        def subCategoryTransactions = Transaction.findAllByDateGreaterThanAndDateLessThanAndUserAndSubCategory(startCal.time, endCal.time, springSecurityService.currentUser, s)
        def subCategoryTransactionsSum = getSumForDebitTransactions(subCategoryTransactions)
        if (subCategoryTransactionsSum > 0) {
          if (categoryTransactionsSum > 0) {
            drilldown.last().data << [name: s.name, y: ((subCategoryTransactionsSum / categoryTransactionsSum) * 100), categoryTotal: "\$${subCategoryTransactionsSum}", grandTotal: "\$${categoryTransactionsSum}"]
          }
        }
      }
    }
    render([month: endCal.time.format("MMMMM yyyy"), categories: categories, drilldown: drilldown] as JSON)
  }

  private BigDecimal getSumForDebitTransactions(transactions) {
    def sum = 0
    transactions?.each { Transaction t ->
      if (t.subCategory.type == CategoryType.DEBIT) {
        sum += t.amount
      }
    }
    return sum
  }

  private Calendar setCalendarToMidnight(Calendar cal) {
    cal.set(Calendar.HOUR_OF_DAY, 23)
    cal.set(Calendar.MINUTE, 59)
    cal.set(Calendar.SECOND, 59)
    cal.set(Calendar.MILLISECOND, 59)
    return cal
  }
}
