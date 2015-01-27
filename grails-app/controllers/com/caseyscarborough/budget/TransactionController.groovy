package com.caseyscarborough.budget

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import org.springframework.http.HttpStatus

import java.text.ParseException

@Secured('IS_AUTHENTICATED_FULLY')
@Transactional(readOnly = true)
class TransactionController {

  def transactionService
  def springSecurityService

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

  def index(Integer max) {
    params.max = Math.min(max ?: 20, 100)
    def transactions = Transaction.findAllByUser(springSecurityService.currentUser, params)
    def accounts = Account.findAllByUser(springSecurityService.currentUser)
    [transactions: transactions, transactionInstanceCount: Transaction.count(), accounts: accounts, categories: Category.all]
  }

  @Transactional
  def save() {
    try {
      def transaction = transactionService.createTransaction(
          params.description, new BigDecimal(params.amount), Account.get(params.account), SubCategory.get(params.subCategory), Date.parse("MM/dd/yyyy", params.date), springSecurityService.currentUser)
      response.status = HttpStatus.CREATED.value()
      render transaction as JSON
    } catch (TransactionException e) {
      response.status = HttpStatus.BAD_REQUEST.value()
      render([message: e.message, field: e.transaction.errors.fieldError.field] as JSON)
    } catch (NumberFormatException e) {
      response.status = HttpStatus.BAD_REQUEST.value()
      render([message: "Please enter a valid amount for the transaction.", field: "transaction"] as JSON)
    } catch (ParseException e) {
      response.status = HttpStatus.BAD_REQUEST.value()
      render([message: "Please enter a valid date in the format YYYY-MM-DD.", field: "date"] as JSON)
    }
  }

  @Transactional
  def delete(Long id) {
    def transactionInstance = Transaction.get(id)
    transactionService.deleteTransaction(transactionInstance)
    response.status = HttpStatus.NO_CONTENT.value()
    render([] as JSON)
  }

  def queryDescription() {
    def transactions = Transaction.findAllByUserAndDescriptionIlike(springSecurityService.currentUser, params.query + "%")
    def output = []
    transactions.each { t ->
      output << [ value: t.description, data: t.subCategory.id ]
    }
    output = output.unique()
    render([suggestions: output] as JSON)
  }
}
