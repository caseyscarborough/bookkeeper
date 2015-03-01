package com.caseyscarborough.budget

import grails.transaction.Transactional
import org.springframework.web.multipart.commons.CommonsMultipartFile

@Transactional
class ReceiptService {

  def grailsApplication
  def messageSource

  Receipt createReceipt(CommonsMultipartFile file, Transaction transaction) {
    def fileExtension = file.originalFilename.split("\\.").last()
    def newFilePath = grailsApplication.config.budget.receiptStorageLocation + "/${transaction.filenameDescription}.${fileExtension}"
    def newFile = new File(newFilePath as String)
    file.transferTo(newFile)

    def receipt = new Receipt(
        filename: transaction.filenameDescription, location: grailsApplication.config.budget.receiptStorageLocation, contentType: file.contentType, size: file.size, transaction: transaction)
    if (!receipt.save(flush: true)) {
      log.error("Couldn't save receipt. ${messageSource.getMessage(receipt.errors.fieldError, Locale.default)}")
    }
    receipt
  }
}
