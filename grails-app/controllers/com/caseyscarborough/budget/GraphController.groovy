package com.caseyscarborough.budget

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured('IS_AUTHENTICATED_FULLY')
class GraphController {

  def springSecurityService

  def index() {}

  def spendingOverPastThreeMonths() {
    def data = []
    def now = new Date()
    def startCal = Calendar.instance
    def endCal = Calendar.instance
    startCal.add(Calendar.DAY_OF_YEAR, -90)
    endCal.add(Calendar.DAY_OF_YEAR, -89)

    while (endCal.time < now) {
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
}
