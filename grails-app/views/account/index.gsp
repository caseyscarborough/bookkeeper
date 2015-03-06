<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Account List</title>
  <meta name="layout" content="main">
  <script>
    function calculateCurrentTotal() {
      var total = 0;
      $(".include-in-total").each(function () {
        if ($(this).prop("checked")) {
          var balance = $("#account-" + $(this).attr("data-id") + "-balance");
          var isDebt = balance.attr("data-is-debt");
          var actualBalance = parseFloat(balance.html());
          total += isDebt === "true" ? -actualBalance : actualBalance;
        }
      });
      $("#total-balance").html(total.toFixed(2));
    }

    $(function () {
      calculateCurrentTotal();
      $(".include-in-total").change(function () {
        calculateCurrentTotal();
      });

      $("#new-account-form").on('submit', function () {
        var data = {};
        $(".domain-property").each(function () {
          data[$(this).attr("id")] = $(this).val();
        });

        $.ajax({
          type: "post",
          data: data,
          url: "${createLink(controller: 'account', action: 'save')}",
          dataType: 'json',
          success: function () {
            window.location.reload()
          },
          error: function (response) {
            showErrorMessage("#account-error", response.responseJSON.message, response.responseJSON.field);
          }
        });
      });

      $(".account-delete").on('click', function () {
        var id = $(this).attr("data-id");
        $.ajax({
          type: "delete",
          url: "${createLink(controller: 'account', action: 'delete')}/" + id,
          success: function () {
            $(".account-" + id).fadeOut();
            $("#account-error").hide();
          },
          error: function (response) {
            showErrorMessage("#account-error", response.responseJSON.message, response.responseJSON.field);
          }
        });
      });

      $(".account-edit").on('click', function () {
        var id = $(this).attr("data-id");
        $("#edit-account-description").val($("#account-" + id + "-description").html());
        $("#edit-account-id").val(id);
        $("#edit-account-balance").val($("#account-" + id + "-balance").html());
        $("#edit-account-modal").modal('show');
      });

      $("#edit-account-form").on('submit', function () {
        var data = getData('.modal-domain-property');
        updateAccount(data, function () {
          window.location.reload();
        }, function (response) {
          showErrorMessage("#account-edit-error", response.responseJSON.message, "edit-account-" + response.responseJSON.field);
        })
      });
    });
  </script>
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
              <td><button id="submit" class="btn btn-primary">New</button></td>
            </tr>
            <g:each in="${accountList}" var="account">
              <tr class="account-${account.id}">
                <td>
                  <g:link class="tooltip-link" title="View Transactions for ${account}" controller="transaction" action="index" params="[account: account.id]">
                    <span id="account-${account.id}-description">${account.description}</span>
                  </g:link>
                </td>
                <td>${account.balanceString}<span id="account-${account.id}-balance" data-id="${account.id}"
                                                  data-is-debt="${account.type.isDebt}" class="account-balance"
                                                  style="display:none">${account.balance}</span></td>
                <td id="account-${account.id}-type">${account.type.name}</td>
                <td>
                  <input type="checkbox" class="include-in-total" data-id="${account.id}" checked>
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
            <div class="mobile-account">
              <h3>${account.description}</h3>
              <p>Balance: ${account.balanceString}</p>
              <p>Type: ${account.type.name}</p>
              <p>
                <a class="account-edit" data-id="${account.id}">Edit</a> &middot;
                <a class="account-delete" data-id="${account.id}">Delete</a>
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
</body>
</html>