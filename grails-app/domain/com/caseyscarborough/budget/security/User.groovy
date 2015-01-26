package com.caseyscarborough.budget.security

import com.caseyscarborough.budget.Account
import com.caseyscarborough.budget.BudgetItem
import com.caseyscarborough.budget.Transaction

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

	static hasMany = [transactions: Transaction, accounts: Account, budgets: BudgetItem]

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
