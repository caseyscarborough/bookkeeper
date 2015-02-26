package com.caseyscarborough.budget

import grails.plugin.springsecurity.annotation.Secured

class HomeController {

  def index() {
    redirect(controller: "transaction", action: "index")
  }
}
