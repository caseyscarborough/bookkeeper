<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main">
  <title>SubCategories</title>
  <script>
    $(function() {
      $(".delete-subCategory").click(function() {
        var id = $(this).attr("data-id");
        $.ajax({
          type: "delete",
          url: "${createLink(controller: 'subCategory', action: 'delete')}/" + id,
          success: function() {
            $("#subCategory-" + id).fadeOut();
          }, error: function(response) {
            showErrorMessage("#subCategory-error", response.responseJSON.message, response.responseJSON.field);
          }
        });
      });

      $(".edit-subCategory").click(function() {
        var id = $(this).attr("data-id");
        $("#edit-id").val(id);
        $("#edit-name").val($("#subCategory-" + id + "-name").html());
        $("#edit-category option:contains(" + $('#subCategory-' + id + '-category').html() + ")").attr('selected', true);
        $("#edit-type option:contains(" + $('#subCategory-' + id + '-type').html() + ")").attr('selected', true);
        $("#edit-subCategory-modal").modal('show');
      });

      $("#edit-subCategory-form").on('submit', function() {
        var data = {};
        $(".modal-domain-property").each(function () {
          data[$(this).attr("name")] = $(this).val();
        });

        $.ajax({
          type: "post",
          data: data,
          url: "${createLink(controller: 'subCategory', action: 'update')}",
          dataType: 'json',
          success: function () {
            window.location.reload()
          },
          error: function (response) {
            showErrorMessage("#subCategory-error", response.responseJSON.message, response.responseJSON.field);
          }
        });
      });

      $("#new-subCategory-form").on('submit', function () {
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
          url: "${createLink(controller: 'subCategory', action: 'save')}",
          dataType: 'json',
          success: function (response) {
            window.location.reload()
          },
          error: function (response) {
            showErrorMessage("#subCategory-error", response.responseJSON.message, response.responseJSON.field);
          }
        });
      });
    });
  </script>
</head>
<body>
<div id="content">
  <div class="row">
    <div class="col-md-12">
      <h1>SubCategories</h1>

      <div id="subCategory-error" class="alert alert-danger" style="display:none">
        <div id="subCategory-error-message"></div>
      </div>
      <div class="table-responsive">
        <table class="table table-condensed table-hover">
          <thead>
          <tr>
            <g:sortableColumn property="name" title="Name" />
            <g:sortableColumn property="category" title="Parent Category" />
            <th>Transactions</th>
            <g:sortableColumn property="type" title="Type" />
            <th>Options</th>
          </tr>
          </thead>
          <tbody>
            <tr>
              <form id="new-subCategory-form" onsubmit="return false">
              <td><input type="text" class="form-control domain-property" id="name" placeholder="Name"></td>
              <td>
                <select id="category" class="form-control domain-property">
                  <g:each in="${categories}" var="category">
                    <option value="${category.id}">${category}</option>
                  </g:each>
                </select>
              </td>
              <td><input class="form-control" disabled></td>
              <td>
                <select id="type" class="form-control domain-property">
                  <g:each in="${categoryTypes}" var="type">
                    <option value="${type.name()}">${type.name}</option>
                  </g:each>
                </select>
              </td>
              <td><button type="submit" class="btn btn-primary">New</button></td>
              </form>
            </tr>
          <g:each in="${subCategories}" var="subCategory">
            <tr id="subCategory-${subCategory.id}">
              <td>
                <g:link controller="transaction" action="index" params="[category: subCategory.id]">
                  <span id="subCategory-${subCategory.id}-name">${subCategory.name}</span>
                </g:link>
              </td>
              <td><span id="subCategory-${subCategory.id}-category">${subCategory.category.name}</span></td>
              <td>${subCategory.transactions.size()}</td>
              <td><span id="subCategory-${subCategory.id}-type">${subCategory.type}</span></td>
              <td>
                <a class="edit-subCategory tooltip-link" title="Edit" data-id="${subCategory.id}"><i class="glyphicon glyphicon-pencil"></i></a>
                <a class="delete-subCategory tooltip-link" title="Delete" data-id="${subCategory.id}"><i class="glyphicon glyphicon-remove"></i></a>
              </td>
            </tr>
          </g:each>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</div>
<g:render template="editSubCategoryModal" />
</body>
</html>