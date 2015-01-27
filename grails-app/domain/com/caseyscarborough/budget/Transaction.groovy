package com.caseyscarborough.budget

import com.caseyscarborough.budget.security.User

class Transaction {

  String description
  SubCategory subCategory
  Date date
  BigDecimal amount
  Account fromAccount
  Account toAccount
  User user

  static constraints = {
    toAccount nullable: true
  }

  static mapping = {
    sort date: "desc"
  }

  String getAmountString() {
    String.format("%.2f", amount)
  }

  String getCssClass() {
    if (subCategory.type == CategoryType.DEBIT) {
      return "debit"
    } else if (subCategory.type == CategoryType.CREDIT) {
      return "credit"
    } else {
      return "transfer"
    }
  }
}
