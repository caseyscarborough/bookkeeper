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

  String getBudgetedAmount() {
    String.format("%.2f", budgetItems*.budgetedAmount.sum())
  }

  String getActualAmount() {
    String.format("%.2f", budgetItems*.actualAmount.sum())
  }

  String getNetBudgetedIncome() {
    def income = 0
    budgetItems.each { BudgetItem b ->
      income += b.category.type == CategoryType.CREDIT ? b.budgetedAmount : -b.budgetedAmount
    }
    String.format("%.2f", income)
  }

  String getNetActualIncome() {
    def income = 0
    budgetItems.each { BudgetItem b ->
      income += b.category.type == CategoryType.CREDIT ? b.actualAmount : -b.actualAmount
    }
    String.format("%.2f", income)
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
