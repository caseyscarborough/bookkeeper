package com.caseyscarborough.budget

import com.caseyscarborough.budget.security.User

class Account {

  String description
  AccountType type
  BigDecimal balance
  User user

  static constraints = {
  }

  static mapping = {
    sort description: "asc"
  }

  String getBalanceString() {
    String.format("\$%.2f", balance)
  }

  String toString() {
    description
  }
}
