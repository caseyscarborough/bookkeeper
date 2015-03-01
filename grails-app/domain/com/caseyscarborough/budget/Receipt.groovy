package com.caseyscarborough.budget

class Receipt {

  String location
  String filename
  Long size
  String contentType

  static belongsTo = [transaction: Transaction]

  static constraints = {
  }

  String getPath() {
    "${location}/${filename}"
  }
}
