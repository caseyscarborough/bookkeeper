package com.caseyscarborough.bookkeeper

import com.caseyscarborough.bookkeeper.security.User

class Account {

  String description
  AccountType type
  BigDecimal balance
  User user
  Boolean active = true

  static constraints = {
    active nullable: true
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

  String sendPayment(BigDecimal amount) {
    balance += (type.isDebt) ? amount : -amount

  }

  String receivePayment(BigDecimal amount) {
    balance += (type.isDebt) ? -amount : amount
  }
}
