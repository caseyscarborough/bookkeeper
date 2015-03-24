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
      <g:link controller="transaction" action="index" class="navbar-brand">Budget</g:link>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
        <li><g:link controller="account" action="index">Accounts</g:link></li>
        <li><g:link controller="transaction" action="index">Transactions</g:link></li>
        <li><g:link controller="budget" action="index">Monthly Budget</g:link></li>
        <li><g:link controller="graph" action="index">Graphs</g:link></li>
        <li><g:link controller="data" action="index">Import/Export</g:link></li>
        <sec:ifAnyGranted roles="ROLE_ADMIN">
          <li><g:link controller="category" action="manage">Categories</g:link></li>
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