package com.caseyscarborough.budget


enum CategoryType {

  CREDIT("Credit"),
  DEBIT("Debit")

  private final String name

  private CategoryType(String name) {
    this.name = name
  }

  public String getName() {
    name
  }

  public String toString() {
    name
  }

}