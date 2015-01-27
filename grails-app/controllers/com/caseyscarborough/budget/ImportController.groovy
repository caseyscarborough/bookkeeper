package com.caseyscarborough.budget

import grails.plugin.springsecurity.annotation.Secured

@Secured('IS_AUTHENTICATED_FULLY')
class ImportController {

  def springSecurityService

  def index() {
  }

  def upload() {
    def file = request.getFile('file')
    def allLines = file.inputStream.toCsvReader().readAll()
    def misc = Category.findByName("Miscellaneous")

    if (!misc) {
      misc = new Category(name: "Miscellaneous", type: CategoryType.DEBIT)
      misc.save(flush: true)
    }
    allLines?.each { line ->
      Date date = Date.parse("M/dd/yy", line[0])
      String description = line[1]
      SubCategory category = SubCategory.findByName(line[3])
      if (!category) {
        category = new SubCategory(name: line[3], type: misc.type)
        category.save(flush: true)
        misc.addToSubcategories(category)
        misc.save(flush: true)
      }
      BigDecimal amount = new BigDecimal(line[2])
      Account account = Account.findByDescription(line[4])
      if (account) {
        log.debug("Creating transaction for $description on $date")
        new Transaction(description: description, date: date, fromAccount: account, amount: amount, subCategory: category, user: springSecurityService.currentUser).save(flush: true)
      }
    }
    render "complete"
  }
}
