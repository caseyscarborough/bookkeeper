package com.caseyscarborough.budget.api
import com.caseyscarborough.budget.Account
import com.caseyscarborough.budget.SubCategory
import com.caseyscarborough.budget.Transaction
import com.caseyscarborough.budget.TransactionException
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import org.springframework.http.HttpStatus

import java.text.ParseException

@Secured('IS_AUTHENTICATED_FULLY')
class TransactionController {

  def transactionService
  def springSecurityService

  @Transactional
  def save() {
    try {
      def transaction = transactionService.createTransaction(
          params.description, new BigDecimal(params.amount), Account.get(params.account), SubCategory.get(params.subCategory), Date.parse("MMddyyyy", params.date), springSecurityService.currentUser)
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
}
