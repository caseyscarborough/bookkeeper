package com.caseyscarborough.budget

class Category {

  String name
  CategoryType type

  static hasMany = [subcategories: SubCategory]

  static constraints = {
  }

  static mapping = {
    sort name: "asc"
  }

  String toString() {
    name
  }
}
