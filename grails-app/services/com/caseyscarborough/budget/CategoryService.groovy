package com.caseyscarborough.budget

import grails.transaction.Transactional

class CategoryException extends RuntimeException {
  String message
  Category category
}

@Transactional
class CategoryService {

  def messageSource

  Category createCategory(String name, CategoryType type) {
    def category = new Category(name: name, type: type)
    if (!category.save(flush: true)) {
      def error = messageSource.getMessage(category.errors.fieldError, Locale.default)
      throw new CategoryException(message: error, category: category)
    }
    return category
  }

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

  void deleteCategory(Long id) {
    def category = Category.get(id)
    if (!category) {
      throw new CategoryException(message: "Couldn't find category with ID: ${id}")
    }

    try {
      category.delete(flush: true)
    } catch (Exception e) {
      throw new CategoryException(message: "Could not delete category due to relationship with other entities.", category: category)
    }
  }
}
