package com.caseyscarborough.budget

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Secured('ROLE_USER')
@Transactional(readOnly = true)
class CategoryController {

  static allowedMethods = [index: 'GET']

  def index() {
    def categories = Category.list()
    render ([total: categories.size(), categories: categories] as JSON)
  }
}
