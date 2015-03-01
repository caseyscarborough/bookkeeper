package com.caseyscarborough.budget

class Receipt {

  String location
  String filename
  Long size
  String contentType

  static belongsTo = [receipt: Receipt]

  static constraints = {
  }
}
