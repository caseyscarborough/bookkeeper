package com.caseyscarborough.budget

import com.caseyscarborough.budget.security.User

class Budget {

  Date startDate
  Date endDate
  User user

  static hasMany = [budgetItems: BudgetItem]

  static constraints = {
  }
}
