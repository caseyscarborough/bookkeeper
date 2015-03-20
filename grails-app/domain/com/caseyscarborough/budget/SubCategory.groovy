package com.caseyscarborough.budget

class SubCategory {

  String name
  Category category
  CategoryType type

  static hasMany = [transactions: Transaction]

  static constraints = {
    type nullable: true
  }

  static mapping = {
    sort "name"
  }

  CategoryType getType() {
    type ?: category.type
  }

  String toString() {
    name
  }
}
