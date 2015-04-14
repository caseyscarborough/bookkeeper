package com.caseyscarborough.bookkeeper

import com.caseyscarborough.bookkeeper.security.User

class Budget {

  User user
  Date startDate
  Date endDate
  String slug

  Date dateCreated
  Date lastUpdated

  static hasMany = [budgetItems: BudgetItem]

  static constraints = {
    slug nullable: true
  }

  static mapping = {
    budgetItems cascade: 'all-delete-orphan', sort: 'category'
  }

  Boolean hasBudgetItemForCategory(SubCategory subCategory) {
    def budgetItems = BudgetItem.where {
      budget == this && category == subCategory
    }

    budgetItems.size() > 0
  }

  BigDecimal getBudgetedAmount() {
    budgetItems*.budgetedAmount.sum()
  }

  BigDecimal getActualAmount() {
    budgetItems*.actualAmount.sum()
  }

  BigDecimal getNetBudgetedIncome() {
    def income = 0
    budgetItems.each { BudgetItem b ->
      income += b.category.type == CategoryType.CREDIT ? b.budgetedAmount : -b.budgetedAmount
    }
    income
  }

  BigDecimal getNetActualIncome() {
    def income = 0
    budgetItems.each { BudgetItem b ->
      income += b.category.type == CategoryType.CREDIT ? b.actualAmount : -b.actualAmount
    }
    income
  }

  String getNextMonthSlug() {
    def cal = startDateCalendar
    cal.add(Calendar.MONTH, 1)
    cal.time.format("yyyyMM")
  }

  String getPrevMonthSlug() {
    def cal = startDateCalendar
    cal.add(Calendar.MONTH, -1)
    cal.time.format("yyyyMM")
  }

  private Calendar getStartDateCalendar() {
    def cal = Calendar.instance
    cal.setTime(startDate)
    cal
  }
}
