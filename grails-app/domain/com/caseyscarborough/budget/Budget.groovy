package com.caseyscarborough.budget

import com.caseyscarborough.budget.security.User

class Budget {

  User user
  Date startDate
  Date endDate

  static hasMany = [budgetItems: BudgetItem]

  static constraints = {
  }
}
