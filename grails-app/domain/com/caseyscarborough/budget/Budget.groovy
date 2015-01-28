package com.caseyscarborough.budget

import com.caseyscarborough.budget.security.User

class Budget {

  User user
  Date startDate
  Date endDate

  static hasMany = [budgetItems: BudgetItem]

  static constraints = {
  }

  List getBudgetItems() {
    def data = []
    Category.all.each { Category c ->
      def items = BudgetItem.where {
        category.category == c && budget == this
      }.get()
      if (items?.count() > 0) {
        data << [category: c, items: items]
      }
    }
    return data
  }
}
