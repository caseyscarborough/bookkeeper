package com.caseyscarborough.budget

import grails.plugin.springsecurity.annotation.Secured

@Secured('ROLE_ADMIN')
class CategoryController {

  def manage() {
    [categories: Category.list(), categoryTypes: CategoryType.findAll()]
  }
}
