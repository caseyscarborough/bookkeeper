package com.caseyscarborough.budget

import com.caseyscarborough.budget.security.User

class Budget {

  User user
  Date startDate
  Date endDate

  static hasMany = [budgetItems: BudgetItem]

  static constraints = {
  }

  static mapping = {
    budgetItems cascade: 'all-delete-orphan'
  }

  Boolean hasBudgetItemForCategory(SubCategory subCategory) {
    def budgetItems = BudgetItem.where {
      budget == this && category == subCategory
    }

    budgetItems.size() > 0
  }
}
