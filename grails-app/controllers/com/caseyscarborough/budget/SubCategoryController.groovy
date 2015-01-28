package com.caseyscarborough.budget

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import org.springframework.http.HttpStatus

@Secured('ROLE_ADMIN')
@Transactional(readOnly = true)
class SubCategoryController {

  def messageSource

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

  def index(Integer max) {
    def subCategories = SubCategory.list(params)
    def categories = Category.all
    [subCategories: subCategories, categories: categories, categoryTypes: CategoryType.findAll()]
  }

  @Transactional
  def save() {
    def category = Category.get(params.category)
    def type = CategoryType.valueOf(params.type)
    def subCategory = new SubCategory(name: params.name, category: category, type: type)
    if (!subCategory.save(flush: true)) {
      def error = [message: messageSource.getMessage(subCategory.errors.fieldError, Locale.default), field: subCategory.errors.fieldError.field]
      response.status = HttpStatus.BAD_REQUEST.value()
      render error as JSON
      return
    }
    render subCategory as JSON
  }

  @Transactional
  def delete(Long id) {
    def subCategory = SubCategory.get(id)
    subCategory.delete(flush: true)
    response.status = HttpStatus.NO_CONTENT.value()
    render ""
  }
}
