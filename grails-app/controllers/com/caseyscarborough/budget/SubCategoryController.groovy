package com.caseyscarborough.budget

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import org.springframework.http.HttpStatus

@Secured('ROLE_ADMIN')
@Transactional(readOnly = true)
class SubCategoryController {

  def subCategoryService

  static allowedMethods = [save: "POST", update: "POST", delete: "DELETE"]

  def index() {
    params.sort = params.sort ?: "name"
    def subCategories = SubCategory.list(params)
    def categories = Category.all
    [subCategories: subCategories, categories: categories, categoryTypes: CategoryType.findAll()]
  }

  @Transactional
  def save() {
    def category = Category.get(params.category)
    def type = CategoryType.valueOf(params.type)
    try {
      def subCategory = subCategoryService.createSubCategory(params.name, category, type)
      render subCategory as JSON
    } catch (SubCategoryException e) {
      response.status = HttpStatus.BAD_REQUEST.value()
      render([message: e.message, field: e.subCategory.errors.fieldError.field] as JSON)
    }
  }

  @Transactional
  def update() {
    def category = Category.get(params.category)
    def type = CategoryType.valueOf(params.type)
    try {
      def subCategory = subCategoryService.updateSubCategory(params.id as Long, params.name, category, type)
      render subCategory as JSON
    } catch (SubCategoryException e) {
      response.status = HttpStatus.BAD_REQUEST.value()
      render([message: e.message, field: e.subCategory.errors.fieldError.field] as JSON)
    }
  }

  @Transactional
  def delete(Long id) {
    try {
      subCategoryService.deleteSubCategory(id)
      response.status = HttpStatus.NO_CONTENT.value()
      render ""
    } catch (SubCategoryException e) {
      response.status = HttpStatus.BAD_REQUEST.value()
      render([message: e.message] as JSON)
    }
  }
}
