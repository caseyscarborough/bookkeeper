package com.caseyscarborough.budget

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Secured('IS_AUTHENTICATED_FULLY')
@Transactional(readOnly = true)
class CategoryController {

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

  def index(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    respond Category.list(params), model: [categoryInstanceCount: Category.count()]
  }

  def show(Category categoryInstance) {
    respond categoryInstance
  }

  def create() {
    respond new Category(params)
  }

  @Transactional
  def save(Category categoryInstance) {
    if (categoryInstance == null) {
      notFound()
      return
    }

    if (categoryInstance.hasErrors()) {
      respond categoryInstance.errors, view: 'create'
      return
    }

    categoryInstance.save flush: true

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.created.message', args: [message(code: 'category.label', default: 'Category'), categoryInstance.id])
        redirect categoryInstance
      }
      '*' { respond categoryInstance, [status: CREATED] }
    }
  }

  def edit(Category categoryInstance) {
    respond categoryInstance
  }

  @Transactional
  def update(Category categoryInstance) {
    if (categoryInstance == null) {
      notFound()
      return
    }

    if (categoryInstance.hasErrors()) {
      respond categoryInstance.errors, view: 'edit'
      return
    }

    categoryInstance.save flush: true

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.updated.message', args: [message(code: 'Category.label', default: 'Category'), categoryInstance.id])
        redirect categoryInstance
      }
      '*' { respond categoryInstance, [status: OK] }
    }
  }

  @Transactional
  def delete(Category categoryInstance) {

    if (categoryInstance == null) {
      notFound()
      return
    }

    categoryInstance.delete flush: true

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.deleted.message', args: [message(code: 'Category.label', default: 'Category'), categoryInstance.id])
        redirect action: "index", method: "GET"
      }
      '*' { render status: NO_CONTENT }
    }
  }

  protected void notFound() {
    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.not.found.message', args: [message(code: 'category.label', default: 'Category'), params.id])
        redirect action: "index", method: "GET"
      }
      '*' { render status: NOT_FOUND }
    }
  }
}
