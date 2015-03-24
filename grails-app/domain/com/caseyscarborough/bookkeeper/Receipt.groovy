package com.caseyscarborough.bookkeeper

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

  File getFile() {
    new File(path)
  }
}
