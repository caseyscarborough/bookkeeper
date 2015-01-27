<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main">
  <title></title>

  <script>
    $(function () {
      $("#new-transaction-form").on('submit', function () {

        var data = {};
        $(".domain-property").each(function () {
          data[$(this).attr("id")] = $(this).val();
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
        }
      });

      $("#date").datepicker();
    });
  </script>
</head>
<body>
<div id="content">
  <div class="row">
    <div class="col-md-10 col-md-offset-1">
      <h1>Transactions</h1>


      <div id="transaction-error" class="alert alert-danger" style="display:none">
        <div id="transaction-error-message"></div>
      </div>
      <g:if test="${transactionInstanceCount > 0}">
        <table class="table table-condensed table-hover">
          <thead>
          <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Amount</th>
            <th>Account</th>
            <th>Category</th>
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
                <select class="form-control domain-property" id="account">
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
                        <option value="${subcategory.id}">${subcategory.name}</option>
                      </g:each>
                    </optgroup>
                  </g:each>
                </select>
              </td>
              <td><button id="submit" class="btn btn-primary">New</button></td>
            </tr>
            </form>
          <g:each in="${transactions}" var="transaction">
            <tr id="transaction-${transaction.id}" class="${transaction.cssClass}">
              <td>${transaction.date.format("MM-dd-yyyy")}</td>
              <td>${transaction.description}</td>
              <td>${transaction.amountString}</td>
              <td>${transaction.account}</td>
              <td>${transaction.subCategory}</td>
              <td><a href="#" class="transaction-delete" data-id="${transaction.id}"><i class="glyphicon glyphicon-remove"></i></a></td>
            </tr>
          </g:each>
          </tbody>
        </table>
        <g:paginate total="${transactionInstanceCount}" />
      </g:if>
      <g:else>
        <div class="alert alert-info">
          You do not have any transactions yet.
        </div>
      </g:else>
    </div>
  </div>
</div>
</body>
</html>