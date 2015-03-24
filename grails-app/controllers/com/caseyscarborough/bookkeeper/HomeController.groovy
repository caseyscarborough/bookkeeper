package com.caseyscarborough.bookkeeper

import grails.plugin.springsecurity.annotation.Secured

@Secured('permitAll')
class HomeController {

  def index() {
    redirect(controller: "transaction", action: "index")
  }
}
