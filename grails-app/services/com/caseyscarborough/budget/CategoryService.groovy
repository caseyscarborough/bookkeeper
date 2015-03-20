package com.caseyscarborough.budget

import grails.transaction.Transactional

class CategoryException extends RuntimeException {
  String message
  Category category
}

@Transactional
class CategoryService {

  def messageSource

  Category updateCategory(Long id, String name) {
    def category = Category.get(id)
    if (!category) {
      throw new CategoryException(message: "Couldn't find subcategory with ID: ${id}")
    }

    category.name = name
    if (!category.save(flush: true)) {
      def error = messageSource.getMessage(category.errors.fieldError, Locale.default)
      throw new CategoryException(message: error, category: category)
    }
    return category
  }
}
