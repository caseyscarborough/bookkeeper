package com.caseyscarborough.budget

class BudgetItem {

  BigDecimal budgetedAmount = 0
  BigDecimal actualAmount = 0
  SubCategory category
  Budget budget

  Date dateCreated
  Date lastUpdated

  private transient List<String> classes = ["success", "warning", "danger"]

  static constraints = {
  }

  static mapping = {
    sort "category"
  }

  BigDecimal getPercentage() {
    if (budgetedAmount == 0) {
      return 0
    }
    (actualAmount / budgetedAmount) * 100
  }

  String getCssClass() {
    def percentage = percentage
    def classes = classes
    if (category.type == CategoryType.CREDIT) {
      classes = classes.reverse()
    }

    if (percentage <= 33.333) {
      return classes[0]
    } else if (percentage <= 66.666) {
      return classes[1]
    } else {
      return classes[2]
    }
  }
}
