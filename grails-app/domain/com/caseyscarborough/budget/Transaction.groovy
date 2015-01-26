package com.caseyscarborough.budget

import com.caseyscarborough.budget.security.User

class Transaction {

  String description
  SubCategory subCategory
  Date date
  Double amount
  Account account
  User user

  static constraints = {
  }
}
