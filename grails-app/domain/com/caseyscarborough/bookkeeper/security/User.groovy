package com.caseyscarborough.bookkeeper.security

import com.caseyscarborough.bookkeeper.Account
import com.caseyscarborough.bookkeeper.Budget
import com.caseyscarborough.bookkeeper.Transaction

class User {

  transient springSecurityService

  String username
  String password
  String firstName
  String lastName
  String email
  boolean enabled = true
  boolean accountExpired
  boolean accountLocked
  boolean passwordExpired

  static hasMany = [transactions: Transaction, accounts: Account, budgets: Budget]

  static transients = ['springSecurityService']

  static constraints = {
    username blank: false, unique: true
    password blank: false
  }

  static mapping = {
    password column: '`password`'
  }

  Set<Role> getAuthorities() {
    UserRole.findAllByUser(this).collect { it.role }
  }

  def beforeInsert() {
    encodePassword()
  }

  def beforeUpdate() {
    if (isDirty('password')) {
      encodePassword()
    }
  }

  protected void encodePassword() {
    password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
  }

  String toString() {
    username
  }
}
