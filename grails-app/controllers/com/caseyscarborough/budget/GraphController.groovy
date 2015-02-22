package com.caseyscarborough.budget

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured('IS_AUTHENTICATED_REMEMBERED')
class GraphController {

  def springSecurityService

  private static final String DATE_FORMAT_FOR_MONTH_SELECTION = "yyyyMM"
  private static final String MONTH_FORMAT = "MMMMM yyyy"
  private static final Integer NUMBER_OF_MONTHS_TO_ANALYZE = 12
  private final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

  def index() {
    def months = []
    def transactions = Transaction.findAllByUser(springSecurityService.currentUser)?.sort { it.date }
    def startCal = Calendar.instance
    startCal.setTime(transactions.first()?.date)
    startCal.set(Calendar.DAY_OF_MONTH, 1)
    startCal = setCalendarToMidnight(startCal)
    def endCal = Calendar.instance
    endCal.setTime(transactions.last().date)

    while (startCal.time <= endCal.time) {
      months << [name: startCal.time.format(MONTH_FORMAT), value: startCal.time.format(DATE_FORMAT_FOR_MONTH_SELECTION)]
      startCal.add(Calendar.MONTH, 1)
    }
    [months: months.reverse(), categories: Category.all]
  }

  def spendingByDay() {
    def now = new Date()
    def startCal = Calendar.instance
    startCal.add(Calendar.MONTH, -(NUMBER_OF_MONTHS_TO_ANALYZE))
    def time = startCal.getTimeInMillis()
    def data = [0] * getDaysInPast(startCal.time)

    def transactions = Transaction.findAllByDateBetweenAndUser(startCal.time, now, springSecurityService.currentUser)
    transactions?.each { Transaction t ->
      if (t.subCategory.type == CategoryType.DEBIT) {
        data[data.size() - getDaysInPast(t.date)] += t.amount
      }
    }

    render([data: data, time: time] as JSON)
  }

  private getDaysInPast(Date date) {
    def now = new Date()
    def daysInPast = (int) ((now.getTime() - date.getTime()) / DAY_IN_MILLIS)
    return daysInPast
  }

  def spendingByCategory() {
    def data = [:]
    def now = new Date()
    def startCal = Calendar.instance
    def endCal = Calendar.instance
    startCal.add(Calendar.MONTH, -(NUMBER_OF_MONTHS_TO_ANALYZE - 1))
    startCal.set(Calendar.DAY_OF_MONTH, 1)
    startCal = setCalendarToMidnight(startCal)
    endCal.setTime(startCal.time)
    endCal.add(Calendar.MONTH, 1)

    int i = 0
    def months = []
    while (startCal.time <= now) {
      months << startCal.time.format(MONTH_FORMAT)
      def transactions = Transaction.findAllByDateGreaterThanEqualsAndDateLessThanAndUser(startCal.time, endCal.time, springSecurityService.currentUser)
      transactions?.each { Transaction t ->
        if (t.subCategory.type == CategoryType.DEBIT) {
          if (!data."${t.subCategory.category}") {
            data."${t.subCategory.category}" = [(BigDecimal) 0] * NUMBER_OF_MONTHS_TO_ANALYZE
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
    startCal = setCalendarToMidnight(startCal)
    endCal.setTime(startCal.time)
    endCal.add(Calendar.MONTH, 1)

    def transactions = Transaction.findAllByDateGreaterThanEqualsAndDateLessThanAndUser(startCal.time, endCal.time, springSecurityService.currentUser)
    def totalTransactionsSum = getSumForDebitTransactions(transactions)
    Category.all?.eachWithIndex { Category c, i ->
      def categoryTransactions = Transaction.where {
        date < endCal.time && date >= startCal.time && user == springSecurityService.currentUser && subCategory.category == c
      }
      def categoryTransactionsSum = getSumForDebitTransactions(categoryTransactions)
      if (categories.size() != i + 1) {
        if (categoryTransactionsSum > 0) {
          categories << [drilldown: "${c.name} - ${startCal.time.format(MONTH_FORMAT)}", name: c.name, categoryTotal: "\$${categoryTransactionsSum}", grandTotal: "\$${totalTransactionsSum}", visible: true, y: ((categoryTransactionsSum / totalTransactionsSum) * 100)]
          drilldown << [id: "${c.name} - ${startCal.time.format(MONTH_FORMAT)}", name: c.name, categoryTotal: "\$${categoryTransactionsSum}", grandTotal: "\$${totalTransactionsSum}", data: []]
        }
      }

      SubCategory.findAllByCategory(c)?.each { SubCategory s ->
        def subCategoryTransactions = Transaction.findAllByDateGreaterThanEqualsAndDateLessThanAndUserAndSubCategory(startCal.time, endCal.time, springSecurityService.currentUser, s)
        def subCategoryTransactionsSum = getSumForDebitTransactions(subCategoryTransactions)
        if (subCategoryTransactionsSum > 0) {
          if (categoryTransactionsSum > 0) {
            drilldown.last().data << [name: s.name, y: ((subCategoryTransactionsSum / categoryTransactionsSum) * 100), categoryTotal: "\$${subCategoryTransactionsSum}", grandTotal: "\$${categoryTransactionsSum}"]
          }
        }
      }
    }
    render([month: startCal.time.format(MONTH_FORMAT), categories: categories, drilldown: drilldown] as JSON)
  }

  def monthlySpendingByCategory() {
    def data = [:]
    def months = []
    def now = new Date()
    def category = Category.get(params.id)
    def firstTransaction = Transaction.where {
      user == springSecurityService.currentUser && subCategory.category == category
    }?.sort { it.date }?.first()

    def startCal = Calendar.instance
    startCal.setTime(firstTransaction.date)
    startCal.set(Calendar.DAY_OF_MONTH, 1)
    startCal = setCalendarToMidnight(startCal)
    def endCal = Calendar.instance
    endCal.setTime(startCal.time)
    endCal.add(Calendar.MONTH, 1)

    int i = 0
    while (startCal.time <= now) {
      def transactions = Transaction.where {
        user == springSecurityService.currentUser && subCategory.category == category && date >= startCal.time && date < endCal.time
      }

      category.subcategories?.each { SubCategory s ->
        if (!data."${s.name}") {
          data."${s.name}" = []
        }
        if (data."${s.name}".size() != i + 1) {
          data."${s.name}" << 0
        }
      }

      transactions?.each { Transaction t ->
        try {
          data."${t.subCategory.name}"[i] += t.amount
        } catch (NullPointerException e) {
        }
      }

      months << startCal.format(MONTH_FORMAT)
      startCal.add(Calendar.MONTH, 1)
      endCal.add(Calendar.MONTH, 1)
      i++
    }

    def categoryData = []
    data.each { k, v ->
      if (v != [0] * (i))
        categoryData << [name: k, data: v]
    }
    render([data: categoryData, months: months, category: category.name] as JSON)
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
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal
  }
}
