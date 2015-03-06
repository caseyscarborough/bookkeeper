<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main">
  <title>Graphs</title>
</head>

<body>
<div id="content">
  <g:render template="spendingByDay"/>
  <hr>
  <g:render template="spendingByCategory"/>
  <hr>
  <g:render template="spendingWithSubcategory"/>
  <hr>
  <g:render template="monthlySpendingBySubCategory"/>
  <hr>
  <g:render template="accountBalancesOverTime"/>
  <hr>
  <g:render template="spendingByPayee"/>
</div>

</body>
</html>