package com.caseyscarborough.bookkeeper

import com.caseyscarborough.bookkeeper.security.User

class Transaction {

  String description
  SubCategory subCategory
  Date date
  BigDecimal amount
  Account fromAccount
  Account toAccount
  User user
  Receipt receipt
  BigDecimal accountBalance

  static constraints = {
    toAccount nullable: true
    receipt nullable: true
    accountBalance nullable: true
  }

  static mapping = {
    sort([date: "desc", description: "asc", amount: "asc"])
    receipt cascade: 'all-delete-orphan'
  }

  String getAmountString() {
    String.format("%.2f", amount)
  }

  String getAccountBalanceString() {
    if (accountBalance != null) {
      def sb = new StringBuilder()
      if (accountBalance < 0) {
        sb.append("-")
      }
      sb.append(String.format("\$%.2f", Math.abs(accountBalance)))
      return sb.toString()
    }
    return ""
  }

  String getFilenameDescription() {
    def sb = new StringBuilder()
    sb.append(date.format('yyyy-MM-dd'))
    sb.append('-')
    sb.append(description.toLowerCase().replaceAll("[ .]", '-').replaceAll("[&^%\$#@!*']", ''))
    sb.append('-')
    sb.append(new Date().toTimestamp().time)
    sb.toString()
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
