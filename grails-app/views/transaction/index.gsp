<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main">
  <title>Transactions</title>

  <script>
    $(function () {
      $("#new-transaction-form").on('submit', function () {
        var data = {};
        $(".domain-property").each(function () {
          if ($(this).attr("disabled") === "disabled") {
          } else {
            data[$(this).attr("id")] = $(this).val();
          }
        });

        $.ajax({
          type: "post",
          data: data,
          url: "${createLink(controller: 'transaction', action: 'save')}",
          dataType: 'json',
          success: function (response) {
            window.location.reload()
          },
          error: function (response) {
            $("#transaction-error-message").html(response.responseJSON.message);
            $(".domain-property").parent().removeClass('has-error');
            $("#" + response.responseJSON.field).focus().parent().addClass('has-error');
            $("#transaction-error").show();
          }
        });
      });

      $(".transaction-delete").on('click', function() {
        var id = $(this).attr("data-id");
        $.ajax({
          type: "delete",
          url: "${createLink(controller: 'transaction', action: 'delete')}/" + id,
          success: function() {
            $("#transaction-" + id).fadeOut();
          }
        });
      });

      $("#description").autocomplete({
        serviceUrl: "${createLink(controller: 'transaction', action: 'queryDescription')}",
        minChars: 3,
        onSelect: function(suggestion) {
          $("#subCategory").val(suggestion.data);
          updateToAccount();
        }
      });

      $("#filter").on('change', function() {
        if ($(this).val() === 'ALL') {
          window.location.href = "${createLink()}";
          return;
        }
        window.location.href = "${createLink()}?category=" + $(this).val();
      });


      function updateToAccount() {
        if ($("#subCategory option:selected").attr("data-type") === "Transfer") {
          $("#toAccount").removeAttr("disabled");
        } else {
          $("#toAccount").attr("disabled", "disabled");
        }
      }
      updateToAccount();
      $("#subCategory").on('change', function() {
        updateToAccount();
      });


      function updateEditToAccount() {
        if ($("#edit-subCategory option:selected").attr("data-type") === "Transfer") {
          $("#edit-toAccount").removeAttr("disabled");
        } else {
          $("#edit-toAccount").attr("disabled", "disabled");
        }
      }
      updateEditToAccount();
      $("#edit-subCategory").on('change', function() {
        updateEditToAccount();
      });

      $("#date").datepicker();
      $("#edit-date").datepicker();

      $(".transaction-edit").click(function() {
        var id = $(this).attr("data-id");
        $("#edit-id").val(id);
        $("#edit-date").val($("#transaction-" + id + "-date").html());
        $("#edit-amount").val($("#transaction-" + id + "-amount").html());
        $("#edit-description").val($("#transaction-" + id + "-description").html());
        $("#edit-account option:contains(" + $('#transaction-' + id + '-account').html() + ")").attr('selected', true);
        $("#edit-subCategory option:contains(" + $('#transaction-' + id + '-subCategory').html() + ")").attr('selected', true);
        $("#edit-transaction-modal").modal('show');
      });

      $("#edit-transaction-form").on('submit', function() {
        var data = {};
        $(".modal-domain-property").each(function () {
          if ($(this).attr("disabled") === "disabled") {
          } else {
            data[$(this).attr("name")] = $(this).val();
          }
        });

        $.ajax({
          type: "post",
          data: data,
          url: "${createLink(controller: 'transaction', action: 'update')}",
          dataType: 'json',
          success: function (response) {
            window.location.reload()
          },
          error: function (response) {
            $("#transaction-edit-error-message").html(response.responseJSON.message);
            $(".modal-domain-property").parent().removeClass('has-error');
            $("#edit-" + response.responseJSON.field).focus().parent().addClass('has-error');
            $("#transaction-edit-error").show();
          }
        });
      });

      $("title").html($("#filter option:selected").html() + " Transactions");
    });
  </script>
</head>
<body>
<div id="content">
  <div class="row">
    <div class="col-md-12">
      <div class="pull-left">
        <h1>Transactions</h1>
        <g:paginate total="${transactionInstanceCount}" params="[category: params.category]"/>
      </div>
      <div class="pull-right">
        <div class="form-group">
          <label for="filter">Filter By:</label>
          <select class="form-control domain-property" id="filter">
            <option value="ALL">ALL</option>
            <g:each in="${categories}" var="category">
              <optgroup label="${category.name}">
                <g:each in="${category.subcategories?.sort { it.name }}" var="subcategory">
                  <option data-type="${subcategory.type}" value="${subcategory.id}" <g:if test="${subcategory.id.toString() == params.category}">selected</g:if>>${subcategory.name}</option>
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
      <table class="table table-condensed table-hover">
        <thead>
        <tr>
          <th>Date</th>
          <th>Description</th>
          <th>Amount</th>
          <th>From Account</th>
          <th>Category</th>
          <th>To Account</th>
          <th></th>
        </tr>
        </thead>
        <tbody>
          <form id="new-transaction-form" onsubmit="return false">
          <tr>
            <td><input type="text" class="form-control domain-property" id="date" placeholder="Date" tabindex="1"></td>
            <td><input type="text" class="form-control domain-property" id="description" placeholder="Transaction Description"></td>
            <td><input type="number" class="form-control domain-property" id="amount" step="0.01" placeholder="Amount"></td>
            <td>
              <select class="form-control domain-property" id="fromAccount">
                <g:each in="${accounts}" var="account">
                  <option value="${account.id}">${account.description}</option>
                </g:each>
              </select>
            </td>
            <td>
              <select class="form-control domain-property" id="subCategory">
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
              <select class="form-control domain-property" id="toAccount" disabled="disabled">
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
            <td><span id="transaction-${transaction.id}-subCategory">${transaction.subCategory}</span></td>
            <td><span id="transaction-${transaction.id}-toAccount">${transaction.toAccount}</span></td>
            <td>
              <a href="#" class="transaction-edit" data-id="${transaction.id}"><i class="glyphicon glyphicon-pencil"></i></a>
              <a href="#" class="transaction-delete" data-id="${transaction.id}"><i class="glyphicon glyphicon-remove"></i></a>
            </td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
  </div>
</div>
<g:render template="editTransactionModal"/>
</body>
</html>