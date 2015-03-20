package com.caseyscarborough.budget

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import org.springframework.http.HttpStatus

@Secured('ROLE_ADMIN')
class CategoryController {

  static allowedMethods = [update: 'POST']

  def categoryService

  def manage() {
    [categories: Category.list(), categoryTypes: CategoryType.findAll()]
  }

  def update() {
    try {
      def category = categoryService.updateCategory(params.id as Long, params.name)
      render category as JSON
    } catch (CategoryException e) {
      response.status = HttpStatus.BAD_REQUEST.value()
      render([message: e.message, field: e.category.errors.fieldError.field] as JSON)
    }
  }
}
