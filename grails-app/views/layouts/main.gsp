<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Bookkeeper &middot; <g:layoutTitle /></title>
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <link rel="icon" type="image/png" href="${resource(dir: 'images', file: 'favicon.png')}">
  <asset:javascript src="application.js" />
  <asset:stylesheet href="application.css" />
  <g:layoutHead/>
</head>

<body>
<g:render template="../shared/navigation" />
<g:layoutBody/>
</body>
</html>