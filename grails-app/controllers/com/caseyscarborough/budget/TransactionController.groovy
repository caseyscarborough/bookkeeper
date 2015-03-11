package com.caseyscarborough.budget

import com.caseyscarborough.budget.security.User
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
    params.offset = params.offset ?: 0

    def results = getTransactionsForParameters(params)
    def accounts = Account.findAllByUserAndActive(springSecurityService.currentUser as User, true)
    [transactions: results, transactionInstanceCount: results.totalCount, accounts: accounts, categories: Category.all]
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
      def transaction = transactionService.updateTransaction(
          params.id as Long, params.description, new BigDecimal(params.amount as String), Account.get(params.fromAccount), Account.get(params.toAccount), SubCategory.get(params.subCategory), Date.parse("MM/dd/yyyy", params.date), params.receipt
      )
      response.status = HttpStatus.OK.value()
      render transaction as JSON
    } catch (TransactionException e) {
      response.status = HttpStatus.BAD_REQUEST.value()
      render([message: e.message, field: e.transaction?.errors?.fieldError?.field] as JSON)
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
    try {
      transactionService.deleteTransaction(id)
      response.status = HttpStatus.NO_CONTENT.value()
      render([] as JSON)
    } catch (TransactionException e) {
      response.status = HttpStatus.BAD_REQUEST.value()
      render([message: e.message, field: e.transaction?.errors?.fieldError?.field] as JSON)
    }
  }

  def queryDescription() {
    def transactions = Transaction.findAllByUserAndDescriptionIlike(springSecurityService.currentUser as User, params.query + "%")
    def output = []
    transactions.each { t ->
      if (t.fromAccount.active)
        output << [value: t.description, data: [id: t.subCategory.id, category: t.subCategory, description: t.description, toAccount: t.toAccount?.id ?: null]]
    }
    output = output.unique()
    render([suggestions: output] as JSON)
  }

  def synchronize() {
    transactionService.synchronizeBalances()
    render ""
  }

  def exportCurrentResults() {
    def transactions = getTransactionsForParameters(params)
    transactionService.exportTransactionsToExcel(transactions, response)
  }

  private getTransactionsForParameters(params) {
    def c = Transaction.createCriteria()
    def results = c.list(params) {
      if (params.account) {
        fromAccount { eq('id', params.account as Long) }
      }
      if (params.category) {
        subCategory { eq('id', params.category as Long) }
      }
      if (params.description) {
        like('description', "%${params.description}%")
      }
      eq('user', springSecurityService.currentUser)
    }
    return results
  }
}
