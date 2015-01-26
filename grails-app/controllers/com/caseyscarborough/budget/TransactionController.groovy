package com.caseyscarborough.budget

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Secured('IS_AUTHENTICATED_FULLY')
@Transactional(readOnly = true)
class TransactionController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Transaction.list(params), model:[transactionInstanceCount: Transaction.count()]
    }

    def show(Transaction transactionInstance) {
        respond transactionInstance
    }

    def create() {
        respond new Transaction(params)
    }

    @Transactional
    def save(Transaction transactionInstance) {
        if (transactionInstance == null) {
            notFound()
            return
        }

        if (transactionInstance.hasErrors()) {
            respond transactionInstance.errors, view:'create'
            return
        }

        transactionInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'transaction.label', default: 'Transaction'), transactionInstance.id])
                redirect transactionInstance
            }
            '*' { respond transactionInstance, [status: CREATED] }
        }
    }

    def edit(Transaction transactionInstance) {
        respond transactionInstance
    }

    @Transactional
    def update(Transaction transactionInstance) {
        if (transactionInstance == null) {
            notFound()
            return
        }

        if (transactionInstance.hasErrors()) {
            respond transactionInstance.errors, view:'edit'
            return
        }

        transactionInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Transaction.label', default: 'Transaction'), transactionInstance.id])
                redirect transactionInstance
            }
            '*'{ respond transactionInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Transaction transactionInstance) {

        if (transactionInstance == null) {
            notFound()
            return
        }

        transactionInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Transaction.label', default: 'Transaction'), transactionInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'transaction.label', default: 'Transaction'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
