package com.caseyscarborough.budget

import grails.plugin.springsecurity.annotation.Secured

@Secured('IS_AUTHENTICATED_ANONYMOUSLY')
class ReceiptController {

  def springSecurityService

  def download(Long id) {
    def receipt = Receipt.get(id)
    if (receipt.transaction.user == springSecurityService.currentUser) {
      def file = new File(receipt.path)
      log.info("Downloading file '${receipt.filename}'")
      response.contentType = receipt.contentType
      response.setHeader("Content-disposition", "attachment;filename=${receipt.filename}")
      response.outputStream << file.bytes
      response.outputStream.flush()
    }
  }
}
