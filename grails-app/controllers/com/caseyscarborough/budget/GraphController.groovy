package com.caseyscarborough.budget

import com.caseyscarborough.budget.highcharts.DateUtils
import com.caseyscarborough.budget.highcharts.StackedColumn
import com.caseyscarborough.budget.highcharts.TimeSeries
import com.caseyscarborough.budget.security.User
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import org.springframework.util.StopWatch

@Secured('IS_AUTHENTICATED_REMEMBERED')
class GraphController {

  def springSecurityService

  private static final String DATE_FORMAT_FOR_MONTH_SELECTION = "yyyyMM"
  private static final String MONTH_FORMAT = "MMMMM yyyy"
  private static final String DAY_FORMAT = "MM/dd/yy"
  private static final Integer NUMBER_OF_MONTHS_TO_ANALYZE = 12
  private static final Integer SPENDING_BY_PAYEE_COUNT = 40

  def index() {
    def months = []
    def transactions = Transaction.findAllByUser(springSecurityService.currentUser)?.sort { it.date }

    if (transactions.size() > 0) {
      def startCal = Calendar.instance
      startCal.setTime(transactions.first()?.date)
      startCal.set(Calendar.DAY_OF_MONTH, 1)
      DateUtils.setCalendarToMidnight(startCal)
      def endCal = Calendar.instance
      endCal.setTime(transactions.last().date)

      while (startCal.time <= endCal.time) {
        months << [name: startCal.time.format(MONTH_FORMAT), value: startCal.time.format(DATE_FORMAT_FOR_MONTH_SELECTION)]
        startCal.add(Calendar.MONTH, 1)
      }
    }

    [months: months.reverse(), categories: Category.all]
  }

  def spendingByDay() {
    def startCal = DateUtils.calendarAtMidnight
    startCal.add(Calendar.MONTH, -(NUMBER_OF_MONTHS_TO_ANALYZE))
    def timeSeries = new TimeSeries('Spending By Day', 'USD', 24 * 3600 * 1000, startCal.time)

    def transactions = Transaction.findAllByDateBetweenAndUser(startCal.time, new Date(), springSecurityService.currentUser as User)
    transactions?.each { Transaction t ->
      if (t.subCategory.type == CategoryType.DEBIT) {
        timeSeries.addToDataForDate(t.amount, t.date)
      }
    }
    render timeSeries as JSON
  }

  def spendingByCategory() {
    def now = new Date()
    def startCal = DateUtils.calendarAtMidnight
    startCal.add(Calendar.MONTH, -(NUMBER_OF_MONTHS_TO_ANALYZE - 1))
    startCal.set(Calendar.DAY_OF_MONTH, 1)
    def endCal = DateUtils.getCalendarFromCalendar(startCal)
    endCal.add(Calendar.MONTH, 1)

    def stackedColumn = new StackedColumn('Spending By Category', NUMBER_OF_MONTHS_TO_ANALYZE)
    int i = 0
    while (startCal.time <= now) {
      stackedColumn.addToXAxisCategories(startCal.time.format(MONTH_FORMAT))
      def transactions = Transaction.findAllByDateGreaterThanEqualsAndDateLessThanAndUser(startCal.time, endCal.time, springSecurityService.currentUser as User)
      transactions?.each { Transaction t ->
        if (t.subCategory.type == CategoryType.DEBIT) {
          stackedColumn.addToSeries(t.subCategory.category.name, t.amount, i)
        }
      }
      i++
      startCal.add(Calendar.MONTH, 1)
      endCal.add(Calendar.MONTH, 1)
    }
    render stackedColumn as JSON
  }

  def spendingWithSubcategory() {
    def categories = []
    def drilldown = []
    def time = Date.parse(DATE_FORMAT_FOR_MONTH_SELECTION, params.time)
    def startCal = Calendar.instance
    def endCal = Calendar.instance
    startCal.setTime(time)
    DateUtils.setCalendarToMidnight(startCal)
    startCal.set(Calendar.DAY_OF_MONTH, 1)
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
    def categoryData = []
    def months = []
    def now = new Date()
    def category = Category.get(params.id)
    def firstTransaction = Transaction.where {
      user == springSecurityService.currentUser && subCategory.category == category
    }?.sort { it.date }

    if (firstTransaction) {
      def startCal = Calendar.instance
      startCal.setTime(firstTransaction.first().date)
      startCal.set(Calendar.DAY_OF_MONTH, 1)
      DateUtils.setCalendarToMidnight(startCal)
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

      data.each { k, v ->
        if (v != [0] * (i))
          categoryData << [name: k, data: v]
      }
    }

    render([data: categoryData, months: months, category: category.name] as JSON)
  }

  def accountBalancesOverTime() {
    def sw = new StopWatch()
    sw.start()
    def data = [:]
    def accountData = []
    def days = []
    def now = new Date()
    def accounts = Account.findAllByUserAndActive(springSecurityService.currentUser as User, true)
    def firstTransaction = Transaction.where {
      user == springSecurityService.currentUser
    }?.sort { it.date }

    if (firstTransaction) {
      def startCal = Calendar.instance
      startCal.setTime(firstTransaction.first().date)
      DateUtils.setCalendarToMidnight(startCal)
      def endCal = Calendar.instance
      endCal.setTime(startCal.time)
      endCal.add(Calendar.DAY_OF_YEAR, 1)

      // Setup accounts arrays
      def daysInPast = DateUtils.getDaysInPast(startCal.time)
      accounts?.each { Account a ->
        data."${a.description}" = [0] * daysInPast
      }

      // Get the array of days for the X-Axis of the graph
      while (startCal.time <= now) {
        days << startCal.format(DAY_FORMAT)
        startCal.add(Calendar.DAY_OF_YEAR, 1)
      }

      // Loop through the transactions and set the account balance by day.
      def transactions = Transaction.findAllByUser(springSecurityService.currentUser as User)
      transactions.each { Transaction t ->
        if (t.fromAccount in accounts) {
          def arrayIndex = daysInPast - DateUtils.getDaysInPast(t.date) - 1
          if (data."${t.fromAccount.description}"[arrayIndex] == 0) {
            data."${t.fromAccount.description}"[arrayIndex] = t.accountBalance
          }
        }
      }

      // Remove all zeroes from original data.
      data.each { k, v ->
        v.eachWithIndex { BigDecimal value, int j ->
          if (value == 0) {
            try {
              v[j] = v[j - 1]
            } catch (ArrayIndexOutOfBoundsException e){
            }
          }
        }
        accountData << [name: k, data: v]
      }
    }

    render([data: accountData, days: days] as JSON)
  }

  def spendingByPayee() {
    def data = [:]

    def transactions = Transaction.findAllByUser(springSecurityService.currentUser)
    transactions?.each { Transaction t ->
      if (t.subCategory.type == CategoryType.DEBIT) {
        if (!data."${t.description}") {
          data."${t.description}" = 0
        }
        data."${t.description}" += t.amount
      }
    }

    data = data.sort { it.value }
    def output = []
    data.each { k, v ->
      output << [k, v]
    }
    output = output.reverse().take(SPENDING_BY_PAYEE_COUNT).reverse()
    render output as JSON
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
}
