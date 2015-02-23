<!DOCTYPE html>
<html>
<head>
  <title><g:if env="development">Grails Runtime Exception</g:if><g:else>Error</g:else></title>
  <meta name="layout" content="main">
  <g:if env="development"><asset:stylesheet src="errors.css"/></g:if>
</head>

<body>
<ul class="errors">
  <li>An error has occurred</li>
</ul>
<g:renderException exception="${exception}"/>
</body>
</html>
