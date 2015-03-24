package com.caseyscarborough.budget

class BudgetItem {

  BigDecimal budgetedAmount = 0
  BigDecimal actualAmount = 0
  SubCategory category
  Budget budget

  Date dateCreated
  Date lastUpdated

  private transient List<String> classes = ["info", "success", "warning", "danger"]

  static constraints = {
  }

  static mapping = {
    sort "category"
  }

  BigDecimal getPercentage() {
    if (budgetedAmount == 0) {
      return 0
    }
    new BigDecimal((actualAmount / budgetedAmount) * 100).setScale(2, BigDecimal.ROUND_HALF_UP)
  }

  String getCssClass() {
    def percentage = percentage
    def classes = classes
    if (category.type == CategoryType.CREDIT) {
      classes = classes.reverse()
    }

    if (percentage <= 33.3333) {
      return classes[0]
    } else if (percentage <= 66.6666) {
      return classes[1]
    } else if (percentage <= 100) {
      return classes[2]
    } else {
      return classes[3]
    }
  }
}
