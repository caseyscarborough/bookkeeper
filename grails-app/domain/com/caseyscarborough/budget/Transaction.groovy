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
  Receipt receipt

  static constraints = {
    toAccount nullable: true
    receipt nullable: true
  }

  static mapping = {
    sort([date: "desc", description: "asc", amount: "asc"])
    receipt cascade: 'all-delete-orphan'
  }

  String getAmountString() {
    String.format("%.2f", amount)
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
