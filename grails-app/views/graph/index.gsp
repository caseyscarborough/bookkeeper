<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main">
  <title>Graphs</title>
  <script>

  </script>
</head>
<body>
<div id="content">
  <g:render template="spendingByDay" />
  <hr>
  <g:render template="spendingByCategory" />
  <hr>
  <g:render template="spendingWithSubcategory" />
  <hr>
  <g:render template="monthlySpendingBySubCategory" />
</div>

</body>
</html>