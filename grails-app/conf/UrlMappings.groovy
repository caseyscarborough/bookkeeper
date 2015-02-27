class UrlMappings {

  static mappings = {
    "/$controller/$action?/$id?(.$format)?" {
      constraints {
        // apply constraints here
      }
    }

    "/api/categories"(controller: 'category', action: 'index')
    "/api/accounts"(controller: 'account', action: 'index')
    "/api/transactions"(controller: 'transaction', action: 'index')
    "500"(view: '/error')
  }
}
