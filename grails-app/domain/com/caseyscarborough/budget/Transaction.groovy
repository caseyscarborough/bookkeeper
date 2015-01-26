package com.caseyscarborough.budget

import com.caseyscarborough.budget.security.User

class Transaction {

  String description
  SubCategory subCategory
  Date date
  BigDecimal amount
  Account account
  User user

  static constraints = {
  }

  static mapping = {
    sort date: "desc"
  }

  String getAmountString() {
    String.format("\$%.2f", amount)
  }

  String getCssClass() {
    if (subCategory.type == CategoryType.DEBIT) {
      return "debit"
    } else {
      return "credit"
    }
  }
}
