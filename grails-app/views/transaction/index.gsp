<%@ page import="com.caseyscarborough.bookkeeper.Transaction" contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main">
  <title>Transactions</title>

</head>

<body>
<div id="content">
  <div class="row">
    <div class="col-md-12">
      <div class="pull-left">
        <h1>Transactions</h1>
        <p>There ${transactionInstanceCount == 1 ? 'is' : 'are'} a total of ${transactionInstanceCount} transaction${transactionInstanceCount == 1 ? '' : 's'}<g:if test="${params.category || params.account || params.description}"> for this search</g:if>.</p>
        <div class="hidden-xs">
          <g:paginate total="${transactionInstanceCount}" params="[category: params.category, account: params.account, description: params.description]" maxsteps="20" />
        </div>
        <div class="visible-xs">
          <g:paginate total="${transactionInstanceCount}" params="[category: params.category, account: params.account, description: params.description]" maxsteps="5"/>
        </div>
      </div>

      <div id="search-area" class="hidden-xs pull-right">
        <form id="search-form" onsubmit="return false">
          <div class="form-group pull-left" style="margin-right:5px">
            <select class="form-control domain-property" id="filter-account">
              <option selected disabled>Select an Account</option>
              <g:each in="${accounts}" var="account">
                <option value="${account.id}" <g:if
                    test="${account.id.toString() == params.account}">selected</g:if>>${account.description}</option>
              </g:each>
            </select>
          </div>

          <div class="form-group pull-left" style="margin-right:5px">
            <select class="form-control domain-property" id="filter-category">
              <option selected disabled>Select a Category</option>
              <g:each in="${categories}" var="category">
                <optgroup label="${category.name}">
                  <g:each in="${category.subcategories?.sort { it.name }}" var="subcategory">
                    <option data-type="${subcategory.type}" value="${subcategory.id}" <g:if
                        test="${subcategory.id.toString() == params.category}">selected</g:if>>${subcategory.name}</option>
                  </g:each>
                </optgroup>
              </g:each>
            </select>
          </div>

          <div class="form-group pull-left" style="margin-right:5px;">
            <input type="text" class="form-control" id="filter-description" placeholder="Description" value="${params.description}">
          </div>

          <div class="form-group pull-left" style="margin-right:5px">
            <select class="form-control domain-property" id="filter-max">
              <option value="Per Page" selected disabled>Per Page</option>
              <g:each in="[10, 30, 50, 100]" var="number">
                <option value="${number}" <g:if test="${number == params.max}">selected</g:if>>${number}</option>
              </g:each>
            </select>
          </div>

          <button id="search" type="submit" class="btn btn-primary">Search</button>
          <button id="reset" class="btn btn-warning">Reset</button>
        </form>
      </div>
      <div class="clearfix"></div>

      <br>
      <div id="transaction-error" class="alert alert-danger" style="display:none">
        <div id="transaction-error-message"></div>
      </div>

      <div class="table-responsive hidden-xs">
        <table class="table table-condensed table-hover">
          <thead>
          <tr>
            <g:sortableColumn property="date" title="Date"
                              params="[category: params.category, account: params.account, description: params.description]" width="7%"/>
            <g:sortableColumn property="description" title="Description"
                              params="[category: params.category, account: params.account, description: params.description]" width="23%"/>
            <g:sortableColumn property="amount" title="Amount"
                              params="[category: params.category, account: params.account, description: params.description]" width="8%"/>
            <g:sortableColumn property="fromAccount" title="From Account"
                              params="[category: params.category, account: params.account, description: params.description]" width="16%"/>
            <g:sortableColumn property="subCategory" title="Category"
                              params="[category: params.category, account: params.account, description: params.description]" width="10%"/>
            <g:sortableColumn property="toAccount" title="To Account"
                              params="[category: params.category, account: params.account, description: params.description]" width="16%"/>
            <g:sortableColumn property="accountBalance" title="Balance"
                              params="[category: params.category, account: params.account, description: params.description]" width="8%"/>
            <th>Receipt</th>
            <th></th>
          </tr>
          </thead>
          <tbody>
          <form id="new-transaction-form" onsubmit="return false">
            <tr>
              <td><input type="text" class="form-control domain-property" id="date" name="date" placeholder="mm/dd/yyyy" value="${new Date().format('MM/dd/yyyy')}" tabindex="1">
              </td>
              <td><input type="text" class="form-control domain-property" id="description" name="description"
                         placeholder="Transaction Description" tabindex="2"></td>
              <td><input type="number" class="form-control domain-property" id="amount" name="amount" step="0.01"
                         placeholder="Amount" tabindex="3"></td>
              <td>
                <select class="form-control domain-property" id="fromAccount" name="fromAccount" tabindex="4">
                  <g:each in="${accounts}" var="account">
                    <option value="${account.id}">${account.description}</option>
                  </g:each>
                </select>
              </td>
              <td>
                <select class="form-control domain-property" id="subCategory" name="subCategory" tabindex="5">
                  <g:each in="${categories}" var="category">
                    <optgroup label="${category.name}">
                      <g:each in="${category.subcategories?.sort { it.name }}" var="subcategory">
                        <option data-type="${subcategory.type}" value="${subcategory.id}">${subcategory.name}</option>
                      </g:each>
                    </optgroup>
                  </g:each>
                </select>
              </td>
              <td>
                <select class="form-control domain-property" id="toAccount" name="toAccount" disabled="disabled" tabindex="6">
                  <g:each in="${accounts}" var="account">
                    <option value="${account.id}">${account.description}</option>
                  </g:each>
                </select>
              </td>
              <td></td>
              <td style="vertical-align: middle; width: 95px"><input type="file" id="receipt" style="width: 100%" tabindex="7"></td>
              <td><button id="submit" class="btn btn-primary" tabindex="8">New</button></td>
            </tr>
          </form>
          <g:each in="${transactions as List<com.caseyscarborough.bookkeeper.Transaction>}" var="transaction">
            <tr class="${transaction.cssClass} transaction-${transaction.id}">
              <td><span id="transaction-${transaction.id}-date">${transaction.date.format("MM/dd/yyyy")}</span></td>
              <td>
                <a href="${createLink()}?description=${transaction.description}" class="tooltip-link" title="View Transactions for ${transaction.description}">
                  <span id="transaction-${transaction.id}-description">${transaction.description}</span>
                </a>
              </td>
              <td>$<span id="transaction-${transaction.id}-amount">${transaction.amountString}</span></td>
              <td>
                <a href="${createLink()}?account=${transaction.fromAccount.id}" class="tooltip-link" title="View Transactions for ${transaction.fromAccount}">
                  <span id="transaction-${transaction.id}-fromAccount">${transaction.fromAccount}</span>
                </a>
              </td>
              <td>
                <g:link controller="transaction" action="index" params="[category: transaction.subCategory.id]" class="tooltip-link" title="View Transactions for ${transaction.subCategory}">
                  <span id="transaction-${transaction.id}-subCategory">${transaction.subCategory}</span>
                </g:link>
              </td>
              <td><span id="transaction-${transaction.id}-toAccount">${transaction.toAccount}</span></td>
              <td>${transaction.accountBalanceString}</td>
              <td>
                <g:if test="${transaction.receipt}">
                  <a class="view-receipt cursor tooltip-link" title="View Receipt" data-url="${createLink(controller: 'receipt', action: 'download', id: transaction.receipt.id)}">View</a>
                </g:if>
              </td>
              <td>
                <a title="Edit" class="tooltip-link transaction-edit" data-id="${transaction.id}"><i
                    class="glyphicon glyphicon-pencil"></i></a>
                <a title="Delete" class="tooltip-link transaction-delete" data-id="${transaction.id}"><i
                    class="glyphicon glyphicon-remove"></i></a>
              </td>
            </tr>
          </g:each>
          </tbody>
        </table>
      </div>

      <div class="visible-xs">

        <button id="show-new-mobile-transaction" class="btn btn-primary">New Transaction</button>
        <button id="hide-new-mobile-transaction" class="btn btn-primary" style="display:none">Hide Form</button>
        <br><br>

        <form id="mobile-new-transaction-form" onsubmit="return false" style="display:none">
          <div class="form-group">
            <label for="mobile-date">Date</label>
            <input type="text" class="form-control mobile-domain-property" id="mobile-date" name="date" placeholder="mm/dd/yyyy">
          </div>
          <div class="form-group">
            <label for="mobile-description">Description</label>
            <input type="text" class="form-control mobile-domain-property" id="mobile-description" name="description" placeholder="Description">
          </div>
          <div class="form-group">
            <label for="mobile-amount">Amount</label>
            <input type="number" class="form-control mobile-domain-property" id="mobile-amount" name="amount" step="0.01" placeholder="Amount">
          </div>

          <div class="form-group">
            <label for="mobile-fromAccount">From Account</label>
            <select class="form-control mobile-domain-property" id="mobile-fromAccount" name="fromAccount">
              <g:each in="${accounts}" var="account">
                <option value="${account.id}">${account.description}</option>
              </g:each>
            </select>
          </div>

          <div class="form-group">
            <label for="mobile-subCategory">Category</label>
            <select class="form-control mobile-domain-property" id="mobile-subCategory" name="subCategory">
              <g:each in="${categories}" var="category">
                <optgroup label="${category.name}">
                  <g:each in="${category.subcategories?.sort { it.name }}" var="subcategory">
                    <option data-type="${subcategory.type}" value="${subcategory.id}">${subcategory.name}</option>
                  </g:each>
                </optgroup>
              </g:each>
            </select>
          </div>

          <div class="form-group">
            <label for="mobile-toAccount">To Account</label>
            <select class="form-control mobile-domain-property" id="mobile-toAccount" name="toAccount" disabled="disabled">
              <g:each in="${accounts}" var="account">
                <option value="${account.id}">${account.description}</option>
              </g:each>
            </select>
          </div>

          <button type="submit" class="btn btn-primary">New</button>
        </form>

        <g:each in="${transactions}" var="transaction">
          <div class="transaction-${transaction.id}">
            <div class="mobile-transaction ${transaction.cssClass}">
              <strong>${transaction.description}</strong> - ${transaction.date.format("MM/dd/yyyy")}<br>
              Amount: $${transaction.amountString}<br>
              From Account: ${transaction.fromAccount}<br>
              Category: ${transaction.subCategory}<br>
              Account Balance: ${transaction.accountBalanceString}<br>
              <a href="#" class="transaction-edit" data-id="${transaction.id}">Edit</a> &middot;
              <a href="#" class="transaction-delete" data-id="${transaction.id}">Delete</a>
            </div><br>
          </div>
        </g:each>
      </div>

      <button class="btn btn-primary tooltip-link" id="synchronize" data-loading-text="Synchronizing... This may take some time." title="This will synchronize the account balances at the time of each transaction. It can take some time to complete">Synchronize Balances</button>&nbsp;
      <g:link action="exportCurrentResults" params="[account: params.account, category: params.category, description: params.description]" class="btn btn-primary">Export Current Results to Excel</g:link>
      <br><br>
    </div>
  </div>
</div>
<g:render template="editTransactionModal"/>
<g:render template="viewReceiptModal"/>

<script>var params_max = ${params.max};</script>
<asset:javascript src="transaction/index.js"/>
</body>
</html>