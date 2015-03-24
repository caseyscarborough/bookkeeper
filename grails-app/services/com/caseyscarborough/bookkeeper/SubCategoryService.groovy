package com.caseyscarborough.bookkeeper

import grails.transaction.Transactional

class SubCategoryException extends RuntimeException {
  String message
  SubCategory subCategory
}

@Transactional
class SubCategoryService {

  def messageSource

  SubCategory createSubCategory(String name, Category category, CategoryType type) {
    def subCategory = new SubCategory(name: name, category: category, type: type)
    if (!subCategory.save(flush: true)) {
      def error = messageSource.getMessage(subCategory.errors.fieldError, Locale.default)
      throw new SubCategoryException(message: error, subCategory: subCategory)
    }
    return subCategory
  }

  SubCategory updateSubCategory(Long id, String name, Category category, CategoryType type) {
    def subCategory = SubCategory.get(id)
    if (!subCategory) {
      throw new SubCategoryException(message: "Couldn't find subcategory with ID: ${id}")
    }

    subCategory.name = name
    subCategory.category = category
    subCategory.type = type
    if (!subCategory.save(flush: true)) {
      def error = messageSource.getMessage(subCategory.errors.fieldError, Locale.default)
      throw new SubCategoryException(message: error, subCategory: subCategory)
    }
    return subCategory
  }

  void deleteSubCategory(Long id) {
    def subCategory = SubCategory.get(id)
    if (!subCategory) {
      throw new SubCategoryException(message: "Couldn't find subcategory with ID: ${id}")
    }

    try {
      subCategory.delete(flush: true)
    } catch (Exception e) {
      throw new SubCategoryException(message: "Oops! Couldn't delete the ${subCategory} subcategory due to relationship with other entities. Try deleting related transactions first.", subCategory: subCategory)
    }
  }
}
