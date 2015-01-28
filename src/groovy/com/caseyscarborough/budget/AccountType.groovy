package com.caseyscarborough.budget

enum AccountType {

  LOAN("Loan", true),
  CREDIT_CARD("Credit Card", true),
  CHECKING_ACCOUNT("Checking Account", false),
  SAVINGS_ACCOUNT("Savings Account", false),
  CASH("Cash", false)

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