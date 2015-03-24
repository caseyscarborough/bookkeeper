package com.caseyscarborough.bookkeeper

import grails.plugin.springsecurity.annotation.Secured

@Secured('IS_AUTHENTICATED_ANONYMOUSLY')
class ReceiptController {

  def springSecurityService

  def download(Long id) {
    def receipt = Receipt.get(id)
    if (receipt && receipt.transaction.user == springSecurityService.currentUser) {
      def file = new File(receipt.path)
      log.info("Downloading file '${receipt.filename}'")
      response.contentType = receipt.contentType
      render file: file, contentType: receipt.contentType
    }
  }
}
