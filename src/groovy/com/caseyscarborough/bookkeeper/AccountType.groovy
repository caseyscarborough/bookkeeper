package com.caseyscarborough.bookkeeper

enum AccountType {

  LOAN("Loan", true),
  CREDIT_CARD("Credit Card", true),
  CHECKING_ACCOUNT("Checking Account", false),
  SAVINGS_ACCOUNT("Savings Account", false),
  CASH("Cash", false),
  RETIREMENT_ACCOUNT("Retirement Account", false)

  private final String name
  private final Boolean isDebt

  private AccountType(String name, Boolean isDebt) {
    this.name = name
    this.isDebt = isDebt
  }

  public String getName() {
    name
  }

  public Boolean getIsDebt() {
    isDebt
  }
}