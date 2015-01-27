package com.caseyscarborough.budget

import com.caseyscarborough.budget.security.User

class Budget {

  User user
  SubCategory subCategory
  BigDecimal budgetedAmount

  static constraints = {
  }
}
