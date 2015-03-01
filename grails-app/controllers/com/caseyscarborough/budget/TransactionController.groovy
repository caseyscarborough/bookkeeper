package com.caseyscarborough.budget

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import org.springframework.http.HttpStatus

import java.text.ParseException

@Secured('IS_AUTHENTICATED_REMEMBERED')
@Transactional(readOnly = true)
class TransactionController {

  def transactionService
  def springSecurityService

  static allowedMethods = [save: "POST", update: "POST", delete: "DELETE"]

  def index(Integer max) {
    params.max = Math.min(max ?: 30, 100)
    def category = SubCategory.get(params.category)
    def account = Account.get(params.account)
    def transactions
    def transactionCount

    // Yeah, this kinda sucks
    if (account && category) {
      transactionCount = Transaction.findAllByFromAccountAndSubCategoryAndUser(account, category, springSecurityService.currentUser).size()
      transactions = Transaction.findAllByFromAccountAndSubCategoryAndUser(account, category, springSecurityService.currentUser, params)
    } else if (account) {
      transactionCount = Transaction.findAllByFromAccountAndUser(account, springSecurityService.currentUser).size()
      transactions = Transaction.findAllByFromAccountAndUser(account, springSecurityService.currentUser, params)
    } else if (category) {
      transactionCount = Transaction.findAllBySubCategoryAndUser(category, springSecurityService.currentUser).size()
      transactions = Transaction.findAllBySubCategoryAndUser(category, springSecurityService.currentUser, params)
    } else {
      transactionCount = Transaction.findAllByUser(springSecurityService.currentUser).size()
      transactions = Transaction.findAllByUser(springSecurityService.currentUser, params)
    }

    def accounts = Account.findAllByUser(springSecurityService.currentUser)
    [transactions: transactions, transactionInstanceCount: transactionCount, accounts: accounts, categories: Category.all]
  }

  @Transactional
  def save() {
    try {
      def transaction = transactionService.createTransaction(
          params.description, new BigDecimal(params.amount as String), Account.get(params.fromAccount), Account.get(params.toAccount), SubCategory.get(params.subCategory), Date.parse("MM/dd/yyyy", params.date), springSecurityService.currentUser, params.receipt)
      response.status = HttpStatus.CREATED.value()
      render transaction as JSON
    } catch (TransactionException e) {
      response.status = HttpStatus.BAD_REQUEST.value()
      render([message: e.message, field: e.transaction.errors.fieldError.field] as JSON)
    } catch (NumberFormatException e) {
      response.status = HttpStatus.BAD_REQUEST.value()
      render([message: "Please enter a valid amount for the transaction.", field: "amount"] as JSON)
    } catch (ParseException e) {
      response.status = HttpStatus.BAD_REQUEST.value()
      render([message: "Please enter a valid date in the format DD/MM/YYYY.", field: "date"] as JSON)
    }
  }

  @Transactional
  def update() {
    try {
      def transaction = Transaction.get(params.id)
      transaction = transactionService.updateTransaction(
          transaction, params.description, new BigDecimal(params.amount as String), Account.get(params.fromAccount), Account.get(params.toAccount), SubCategory.get(params.subCategory), Date.parse("MM/dd/yyyy", params.date), params.receipt
      )
      response.status = HttpStatus.OK.value()
      render transaction as JSON
    } catch (TransactionException e) {
      response.status = HttpStatus.BAD_REQUEST.value()
      render([message: e.message, field: e.transaction.errors.fieldError.field] as JSON)
    } catch (NumberFormatException e) {
      response.status = HttpStatus.BAD_REQUEST.value()
      render([message: "Please enter a valid amount for the transaction.", field: "amount"] as JSON)
    } catch (ParseException e) {
      response.status = HttpStatus.BAD_REQUEST.value()
      render([message: "Please enter a valid date in the format DD/MM/YYYY.", field: "date"] as JSON)
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
      output << [value: t.description, data: [id: t.subCategory.id, category: t.subCategory, description: t.description]]
    }
    output = output.unique()
    render([suggestions: output] as JSON)
  }
}
