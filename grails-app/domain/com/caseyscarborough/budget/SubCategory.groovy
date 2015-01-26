package com.caseyscarborough.budget

class SubCategory {

  String name
  Category category

  static constraints = {
  }

  CategoryType getType() {
    category.type
  }

  String toString() {
    name
  }
}
