<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Account List</title>
  <meta name="layout" content="main">
</head>

<body>
<div id="content">
  <div class="row">
    <div class="col-md-12">
      <h1>Accounts</h1>

      <div id="account-error" class="alert alert-danger" style="display:none">
        <div id="account-error-message"></div>
      </div>

      <g:if test="${flash.message}">
        <div class="alert alert-info">
          ${flash.message}
        </div>
      </g:if>
      <div id="accounts-table" class="table-responsive hidden-xs">
        <form id="new-account-form" onsubmit="return false">
          <table class="table table-hover table-condensed">
            <thead>
            <tr>
              <g:sortableColumn property="description" title="Description" />
              <g:sortableColumn property="balance" title="Balance" />
              <g:sortableColumn property="type" title="Type" />
              <th>Active?</th>
              <th>Options</th>
            </tr>
            </thead>
            <tbody>
            <tr>
              <td><input type="text" class="form-control domain-property" id="description"
                         placeholder="Account Description"></td>
              <td><input type="number" class="form-control domain-property" id="balance" step="0.01"
                         placeholder="Balance"></td>
              <td>
                <select class="form-control domain-property" id="type">
                  <g:each in="${accountTypes}" var="accountType">
                    <option value="${accountType}">${accountType.name}</option>
                  </g:each>
                </select>
              </td>
              <td></td>
              <td><button id="submit" class="btn btn-primary">New</button></td>
            </tr>
            <g:each in="${accountList}" var="account">
              <tr class="account-${account.id} ${!account.active ? 'inactive' : ''}">
                <td>
                  <g:link class="tooltip-link" title="View Transactions for ${account}" controller="transaction" action="index" params="[account: account.id]">
                    <span id="account-${account.id}-description">${account.description}</span>
                  </g:link>
                </td>
                <td>${account.balanceString}<span id="account-${account.id}-balance" data-id="${account.id}"
                                                  data-is-debt="${account.type.isDebt}" class="account-balance"
                                                  style="display:none">${account.balance}</span></td>
                <td id="account-${account.id}-type">${account.type.name}</td>
                <td><span id="account-${account.id}-active" style="display:none">${account.active}</span>${account.active ? "Yes" : "No"}</td>
                <td>
                  <input type="checkbox" class="include-in-total tooltip-link" title="Include in Current Total Balance?" data-id="${account.id}" ${account.active ? "checked" : ""}>
                  <a title="Edit" class="tooltip-link account-edit" data-id="${account.id}"><i class="glyphicon glyphicon-pencil"></i></a>
                  <a title="Delete" class="tooltip-link account-delete" data-id="${account.id}"><i class="glyphicon glyphicon-remove"></i></a>
                </td>
              </tr>
            </g:each>
            </tbody>
          </table>
        </form>
      </div>

      <!-- Mobile Layout -->
      <div id="accounts-list" class="visible-xs">
        <g:each in="${accountList}" var="account">
          <div class="account-${account.id}">
            <div class="mobile-account ${!account.active ? "inactive" : ""}">
              <h3>${account.description}</h3>
              <p>Balance: ${account.balanceString}</p>
              <p>Type: ${account.type.name}</p>
              <p>
                <a class="account-edit cursor" data-id="${account.id}">Edit</a> &middot;
                <a class="account-delete cursor" data-id="${account.id}">Delete</a>
              </p>
            </div>
          <br>
          </div>
        </g:each>
      </div>

      <h3>Current Total Balance: $<span id="total-balance"></span></h3>
    </div>

  </div>
</div>
<g:render template="editAccountModal"/>

<asset:javascript src="account/index.js"/>
</body>
</html>