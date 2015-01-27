package com.caseyscarborough.budget

class SubCategory {

  String name
  Category category
  CategoryType type

  static constraints = {
    type nullable: true
  }

  CategoryType getType() {
    type ?: category.type
  }

  String toString() {
    name
  }
}
