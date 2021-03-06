class UrlMappings {

  static mappings = {
    "/$controller/$action?/$id?(.$format)?" {
      constraints {
        // apply constraints here
      }
    }

    "/accounts"(controller: "account", action: "index")
    "/transactions"(controller: "transaction", action: "index")
    "/graphs"(controller: "graph", action: "index")
    "/import"(controller: "import", action: "index")
    "/categories"(controller: "category", action: "manage")
    "/budget/show/$slug"(controller: "budget", action: "show")
    "/"(controller: "home", action: "index")
    "500"(view: '/error')
  }
}
