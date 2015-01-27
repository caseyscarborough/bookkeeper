package com.caseyscarborough.budget

class BudgetItem {

  BigDecimal budgetedAmount = 0
  BigDecimal actualAmount = 0
  Budget budget
  SubCategory category

  static constraints = {
  }
}
