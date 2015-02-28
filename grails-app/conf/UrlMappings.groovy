class UrlMappings {

  static mappings = {
    "/$controller/$action?/$id?(.$format)?" {
      constraints {
        // apply constraints here
      }
    }

    "/api/categories"(controller: 'category', parseRequest: true) {
      action = [GET: 'index', POST: 'save']
    }
    "/api/accounts"(controller: 'account', parseRequest: true) {
      action = [GET: 'index', POST: 'save']
    }
    "/api/transactions"(controller: 'transaction', parseRequest: true) {
      action = [GET: 'index', POST: 'save']
    }
    "500"(view: '/error')
  }
}
