package com.caseyscarborough.budget

import com.caseyscarborough.budget.security.User

class Account {

  String description
  AccountType type
  Double balance
  User user

  static constraints = {
  }

  String getBalanceString() {
    "\$${balance}"
  }

  String toString() {
    description
  }
}
