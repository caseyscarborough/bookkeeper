<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Account List</title>
  <meta name="layout" content="main">
  <script>
    $(function () {
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
          success: function (response) {
            window.location.reload()
          },
          error: function (response) {
            $("#account-error-message").html(response.responseJSON.message);
            $(".domain-property").parent().removeClass('has-error');
            $("#" + response.responseJSON.field).focus().parent().addClass('has-error');
            $("#account-error").show();
          }
        });
      });

      $(".account-delete").on('click', function() {
        var id = $(this).attr("data-id");
        $.ajax({
          type: "delete",
          url: "${createLink(controller: 'account', action: 'delete')}/" + id,
          success: function() {
            $("#account-" + id).fadeOut();
          }
        });
      });
    });
  </script>
</head>

<body>
<div id="content">
  <div class="row">
    <div class="col-md-3">
      <h1>New Account</h1>

      <form id="new-account-form" onsubmit="return false">
        <div id="account-error" class="alert alert-danger" style="display:none">
          <div id="account-error-message"></div>
        </div>

        <div class="form-group">
          <label for="description">Account Description</label>
          <input type="text" class="form-control domain-property" id="description" placeholder="Account Description">
        </div>

        <div class="form-group">
          <label for="balance">Balance</label>
          <input type="number" class="form-control domain-property" id="balance" step="0.01" placeholder="Balance">
        </div>

        <div class="form-group">
          <label for="type">Account Type</label>
          <select class="form-control domain-property" id="type">
            <g:each in="${accountTypes}" var="accountType">
              <option value="${accountType}">${accountType.name}</option>
            </g:each>
          </select>
        </div>
        <button id="submit" class="btn btn-primary">Submit</button>
      </form>
    </div>

    <div class="col-md-9">
      <h1>Accounts</h1>

      <g:if test="${flash.message}">
        <div class="alert alert-info">
          ${flash.message}
        </div>
      </g:if>
      <g:if test="${accountListCount > 0}">
        <table class="table table-hover">
          <thead>
          <tr>
            <th>Description</th>
            <th>Balance</th>
            <th>Type</th>
            <th>Options</th>
          </tr>
          </thead>
          <tbody>
          <g:each in="${accountList}" var="account">
            <tr id="account-${account.id}">
              <td>${account.description}</td>
              <td>${account.balanceString}</td>
              <td>${account.type.name}</td>
              <td><a href="#" class="account-delete" data-id="${account.id}"><i class="glyphicon glyphicon-remove"></i></a></td>
            </tr>
          </g:each>
          </tbody>
        </table>
      </g:if>
      <g:else>
        <div class="alert alert-info">
          You do not have any accounts. Add one below.
        </div>
      </g:else>
    </div>

  </div>
</div>
</body>
</html>