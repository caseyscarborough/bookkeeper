<nav class="navbar navbar-default navbar-inverse">
  <div class="container-fluid">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="#">Budget</a>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
        <li><g:link controller="account" action="index">Accounts</g:link></li>
        <li><g:link controller="transaction" action="index">Transactions</g:link></li>
        <li><g:link controller="graph" action="index">Graphs</g:link></li>
        <li><g:link controller="import" action="index">Import</g:link></li>
        <sec:ifAnyGranted roles="ROLE_ADMIN">
          <li><g:link controller="subCategory" action="index">SubCategory Management</g:link></li>
        </sec:ifAnyGranted>
      </ul>
      <ul class="nav navbar-nav navbar-right">
        <sec:ifNotLoggedIn>
          <li><g:link controller="login" action="auth">Login</g:link></li>
        </sec:ifNotLoggedIn>
        <sec:ifLoggedIn>
          <li><g:link controller="logout" action="">Logout</g:link></li>
        </sec:ifLoggedIn>
      </ul>
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>