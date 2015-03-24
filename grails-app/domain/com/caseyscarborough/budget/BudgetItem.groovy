package com.caseyscarborough.budget

class BudgetItem {

  BigDecimal budgetedAmount = 0
  BigDecimal actualAmount = 0
  SubCategory category
  Budget budget

  static constraints = {
  }

  BigDecimal getPercentage() {
    if (budgetedAmount == 0) {
      return 0
    }
    (actualAmount / budgetedAmount) * 100
  }

  String getCssClass() {
    def percentage = percentage
    if (percentage <= 25) {
      return "info"
    } else if (percentage <= 50) {
      return "success"
    } else if (percentage <= 75) {
      return "warning"
    } else {
      return "danger"
    }
  }
}
