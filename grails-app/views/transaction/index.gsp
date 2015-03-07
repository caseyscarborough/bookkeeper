<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main">
  <title>Transactions</title>
  <script>

    function setupAutocomplete(descriptionSelector, subcategorySelector, toAccountSelector) {
      $(descriptionSelector).autocomplete({
        serviceUrl: "${createLink(controller: 'transaction', action: 'queryDescription')}",
        minChars: 3,
        autoSelectFirst: true,
        formatResult: function (suggestion, currentValue) {
          return suggestion.value + " (" + suggestion.data.category.name + ")";
        },
        onSelect: function (suggestion) {
          $(descriptionSelector).val(suggestion.data.description);
          $(subcategorySelector).val(suggestion.data.id);
          if (suggestion.data.toAccount !== null) {
            $(toAccountSelector).val(suggestion.data.toAccount);
          }
          updateToAccount();
        }
      });
    }

    $(function () {
      setupAutocomplete("#description", "#subCategory", "#toAccount");
      setupAutocomplete("#edit-description", "#edit-subCategory", "#edit-toAccount");
      setupAutocomplete("#mobile-description", "#mobile-subCategory", "#mobile-toAccount");

      $("#date").datepicker();
      $("#edit-date").datepicker();
      $("#mobile-date").datepicker();
      $("title").html($("#filter-category option:selected").html() + " Transactions");

      $("#show-new-mobile-transaction").click(function() {
        $("#mobile-new-transaction-form").show();
        $(this).hide();
        $("#hide-new-mobile-transaction").show();
      });

      $("#hide-new-mobile-transaction").click(function() {
        $("#show-new-mobile-transaction").show();
        $("#mobile-new-transaction-form").hide();
        $(this).hide();
      });

      $("#new-transaction-form").on('submit', function () {
        var data = getData(".domain-property");
        var fileData = $("#receipt").prop('files')[0];
        if (fileData) {
          data.receipt = fileData;
        }
        createTransaction(data, function () {
          window.location.reload();
        }, function (response) {
          showErrorMessage("#transaction-error", response.responseJSON.message, response.responseJSON.field);
        });
      });

      $("#mobile-new-transaction-form").on('submit', function() {
        var data = getData(".mobile-domain-property");
        createTransaction(data, function() {
          window.location.reload();
        }, function (response) {
          showErrorMessage("#transaction-error", response.responseJSON.message, "mobile-" + response.responseJSON.field);
        });
      });

      $("#edit-transaction-form").on('submit', function () {
        var data = getData(".modal-domain-property");
        var fileData = $("#edit-receipt").prop('files')[0];
        if (fileData) {
          data.receipt = fileData;
        }
        updateTransaction(data, function () {
          window.location.reload();
        }, function (response) {
          showErrorMessage("#transaction-edit-error", response.responseJSON.message, response.responseJSON.field);
        });
      });

      $(".transaction-delete").on('click', function () {
        var id = $(this).attr("data-id");
        deleteTransaction(id, function () {
          $(".transaction-" + id).fadeOut();
        }, function () {
        });
      });

      updateToAccount();
      $("#subCategory").on('change', function () {
        updateToAccount();
      });

      updateEditToAccount();
      $("#edit-subCategory").on('change', function () {
        updateEditToAccount();
      });

      updateMobileToAccount();
      $("#mobile-subCategory").on('change', function () {
        updateMobileToAccount();
      });

      $("#reset").click(function() {
        window.location.href = location.protocol + '//' + location.host + location.pathname
      });

      $("#search").click(function() {
        var currentUrl = location.protocol + '//' + location.host + location.pathname;
        var queryChar = '?';
        var category = $("#filter-category").val();
        var account = $("#filter-account").val();
        var description = $("#filter-description").val();

        if (category !== null) {
          currentUrl += queryChar + 'category=' + category;
          queryChar = '&';
        }

        if (account !== null) {
          currentUrl += queryChar + 'account=' + account;
          queryChar = '&';
        }

        if (description !== "") {
          currentUrl += queryChar + 'description=' + description;
          queryChar = '&';
        }

        if (queryChar === '&') {
          window.location.href = currentUrl;
        }
      });

      $(".transaction-edit").click(function () {
        var id = $(this).attr("data-id");
        $("#edit-id").val(id);
        $("#edit-date").val($("#transaction-" + id + "-date").html());
        $("#edit-amount").val($("#transaction-" + id + "-amount").html());
        $("#edit-description").val($("#transaction-" + id + "-description").html());
        $("#edit-fromAccount option:contains(" + $('#transaction-' + id + '-fromAccount').html() + ")").attr('selected', true);
        $("#edit-subCategory option:contains(" + $('#transaction-' + id + '-subCategory').html() + ")").attr('selected', true);
        $("#edit-transaction-modal").modal('show');
        updateEditToAccount();
      });

      $(".view-receipt").click(function() {
        var url = $(this).attr("data-url");
        $("#current-receipt").attr("src", url);
        $("#view-receipt-modal").modal('show');
      });

      $("#synchronize").click(function() {
        $(this).button('loading');
        $.ajax({
          url: "${createLink(controller: 'transaction', action: 'synchronize')}",
          success: function () {
            window.location.reload();
          }
        });
      })
    });
  </script>
</head>

<body>
<div id="content">
  <div class="row">
    <div class="col-md-12">
      <div class="pull-left">
        <h1>Transactions</h1>
        <p>There ${transactionInstanceCount == 1 ? 'is' : 'are'} a total of ${transactionInstanceCount} transaction${transactionInstanceCount == 1 ? '' : 's'}<g:if test="${params.category || params.account || params.description}"> for this search</g:if>.</p>
        <div class="hidden-xs">
          <g:paginate total="${transactionInstanceCount}" params="[category: params.category, account: params.account]" maxsteps="20" />
        </div>
        <div class="visible-xs">
          <g:paginate total="${transactionInstanceCount}" params="[category: params.category, account: params.account]" maxsteps="5"/>
        </div>
      </div>

      <div id="search-area" class="hidden-xs pull-right">
        <div class="form-group pull-left" style="margin-right:5px">
          <select class="form-control domain-property" id="filter-account">
            <option value="Account" selected disabled>Account</option>
            <g:each in="${accounts}" var="account">
              <option value="${account.id}" <g:if
                  test="${account.id.toString() == params.account}">selected</g:if>>${account.description}</option>
            </g:each>
          </select>
        </div>

        <div class="form-group pull-left" style="margin-right:5px">
          <select class="form-control domain-property" id="filter-category">
            <option value="Category" selected disabled>Category</option>
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
          <input type="text" class="form-control" id="filter-description" placeholder="Description">
        </div>

        <button id="search" class="btn btn-primary">Search</button>
        <button id="reset" class="btn btn-warning">Reset</button>
      </div>
      <div class="clearfix"></div>

      <div id="transaction-error" class="alert alert-danger" style="display:none">
        <div id="transaction-error-message"></div>
      </div>

      <div class="table-responsive hidden-xs">
        <table class="table table-condensed table-hover">
          <thead>
          <tr>
            <g:sortableColumn property="date" title="Date"
                              params="[category: params.category, account: params.account]" width="7%"/>
            <g:sortableColumn property="description" title="Description"
                              params="[category: params.category, account: params.account]"/>
            <g:sortableColumn property="amount" title="Amount"
                              params="[category: params.category, account: params.account]" width="8%"/>
            <g:sortableColumn property="fromAccount" title="From Account"
                              params="[category: params.category, account: params.account]"/>
            <g:sortableColumn property="subCategory" title="Category"
                              params="[category: params.category, account: params.account]"/>
            <g:sortableColumn property="toAccount" title="To Account"
                              params="[category: params.category, account: params.account]"/>
            <g:sortableColumn property="accountBalance" title="Balance"
                              params="[category: params.category, account: params.account]"/>
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
          <g:each in="${transactions}" var="transaction">
            <tr class="${transaction.cssClass} transaction-${transaction.id}">
              <td><span id="transaction-${transaction.id}-date">${transaction.date.format("MM/dd/yyyy")}</span></td>
              <td><span id="transaction-${transaction.id}-description">${transaction.description}</span></td>
              <td>$<span id="transaction-${transaction.id}-amount">${transaction.amountString}</span></td>
              <td><span id="transaction-${transaction.id}-fromAccount">${transaction.fromAccount}</span></td>
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

      <button class="btn btn-primary tooltip-link" id="synchronize" data-loading-text="Synchronizing... This may take some time." title="This will synchronize the account balances at the time of each transaction. It can take some time to complete">Synchronize Balances</button>
      <br>
    </div>
  </div>
</div>
<g:render template="editTransactionModal"/>
<g:render template="viewReceiptModal"/>
</body>
</html>