<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main">
  <title>Transactions</title>
  <script>
    $(function () {
      $("#date").datepicker();
      $("#edit-date").datepicker();
      $("title").html($("#filter-category option:selected").html() + " Transactions");

      $("#new-transaction-form").on('submit', function () {
        var data = getData(".domain-property");
        createTransaction(data, function () {
          window.location.reload();
        }, function (response) {
          showErrorMessage("#transaction-error", response.responseJSON.message, response.responseJSON.field);
        });
      });

      $("#edit-transaction-form").on('submit', function () {
        var data = getData(".modal-domain-property");
        updateTransaction(data, function () {
          window.location.reload();
        }, function (response) {
          showErrorMessage("#transaction-edit-error", response.responseJSON.message, response.responseJSON.field);
        });
      });

      $(".transaction-delete").on('click', function () {
        var id = $(this).attr("data-id");
        deleteTransaction(id, function () {
          $("#transaction-" + id).fadeOut();
        }, function () {
        });
      });

      $("#description").autocomplete({
        serviceUrl: "${createLink(controller: 'transaction', action: 'queryDescription')}",
        minChars: 3,
        autoSelectFirst: true,
        formatResult: function (suggestion, currentValue) {
          return suggestion.value + " (" + suggestion.data.category.name + ")";
        },
        onSelect: function (suggestion) {
          $("#description").val(suggestion.data.description);
          $("#subCategory").val(suggestion.data.id);
          updateToAccount();
        }
      });

      $("#filter-account").on('change', function () {
        if ($(this).val() === 'ALL') {
          window.location.href = "${createLink()}";
          return;
        }
        window.location.href = "${createLink()}?account=" + $(this).val();
      });

      $("#filter-category").on('change', function () {
        if ($(this).val() === 'ALL') {
          window.location.href = "${createLink()}";
          return;
        }
        window.location.href = "${createLink()}?category=" + $(this).val();
      });

      updateToAccount();
      $("#subCategory").on('change', function () {
        updateToAccount();
      });

      updateEditToAccount();
      $("#edit-subCategory").on('change', function () {
        updateEditToAccount();
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
    });
  </script>
</head>

<body>
<div id="content">
  <div class="row">
    <div class="col-md-12">
      <div class="pull-left">
        <h1>Transactions</h1>
        <g:paginate total="${transactionInstanceCount}" params="[category: params.category, account: params.account]"/>
      </div>

      <div class="pull-right">
        <div class="form-group pull-left" style="margin-right:10px">
          <label for="filter-account">Account:</label>
          <select class="form-control domain-property" id="filter-account">
            <option value="ALL">ALL</option>
            <g:each in="${accounts}" var="account">
              <option value="${account.id}" <g:if
                  test="${account.id.toString() == params.account}">selected</g:if>>${account.description}</option>
            </g:each>
          </select>
        </div>

        <div class="form-group pull-left">
          <label for="filter-category">Category:</label>
          <select class="form-control domain-property" id="filter-category">
            <option value="ALL">ALL</option>
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
      </div>

      <div class="clearfix"></div><br>

      <div id="transaction-error" class="alert alert-danger" style="display:none">
        <div id="transaction-error-message"></div>
      </div>

      <div class="table-responsive">
        <table class="table table-condensed table-hover">
          <thead>
          <tr>
            <g:sortableColumn property="date" title="Date"
                              params="[category: params.category, account: params.account]"/>
            <g:sortableColumn property="description" title="Description"
                              params="[category: params.category, account: params.account]"/>
            <g:sortableColumn property="amount" title="Amount"
                              params="[category: params.category, account: params.account]"/>
            <g:sortableColumn property="fromAccount" title="From Account"
                              params="[category: params.category, account: params.account]"/>
            <g:sortableColumn property="subCategory" title="Category"
                              params="[category: params.category, account: params.account]"/>
            <g:sortableColumn property="toAccount" title="To Account"
                              params="[category: params.category, account: params.account]"/>
            <th></th>
          </tr>
          </thead>
          <tbody>
          <form id="new-transaction-form" onsubmit="return false">
            <tr>
              <td><input type="text" class="form-control domain-property" id="date" name="date" placeholder="Date" tabindex="1">
              </td>
              <td><input type="text" class="form-control domain-property" id="description" name="description"
                         placeholder="Transaction Description"></td>
              <td><input type="number" class="form-control domain-property" id="amount" name="amount" step="0.01"
                         placeholder="Amount"></td>
              <td>
                <select class="form-control domain-property" id="fromAccount" name="fromAccount">
                  <g:each in="${accounts}" var="account">
                    <option value="${account.id}">${account.description}</option>
                  </g:each>
                </select>
              </td>
              <td>
                <select class="form-control domain-property" id="subCategory" name="subCategory">
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
                <select class="form-control domain-property" id="toAccount" name="toAccount" disabled="disabled">
                  <g:each in="${accounts}" var="account">
                    <option value="${account.id}">${account.description}</option>
                  </g:each>
                </select>
              </td>
              <td><button id="submit" class="btn btn-primary">New</button></td>
            </tr>
          </form>
          <g:each in="${transactions}" var="transaction">
            <tr id="transaction-${transaction.id}" class="${transaction.cssClass}">
              <td><span id="transaction-${transaction.id}-date">${transaction.date.format("MM/dd/yyyy")}</span></td>
              <td><span id="transaction-${transaction.id}-description">${transaction.description}</span></td>
              <td>$<span id="transaction-${transaction.id}-amount">${transaction.amountString}</span></td>
              <td><span id="transaction-${transaction.id}-fromAccount">${transaction.fromAccount}</span></td>
              <td>
                <g:link controller="transaction" action="index" params="[category: transaction.subCategory.id]">
                  <span id="transaction-${transaction.id}-subCategory">${transaction.subCategory}</span>
                </g:link>
              </td>
              <td><span id="transaction-${transaction.id}-toAccount">${transaction.toAccount}</span></td>
              <td>
                <a href="#" class="transaction-edit" data-id="${transaction.id}"><i
                    class="glyphicon glyphicon-pencil"></i></a>
                <a href="#" class="transaction-delete" data-id="${transaction.id}"><i
                    class="glyphicon glyphicon-remove"></i></a>
              </td>
            </tr>
          </g:each>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</div>
<g:render template="editTransactionModal"/>
</body>
</html>