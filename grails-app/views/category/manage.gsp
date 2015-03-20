<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Categories</title>
  <meta name="layout" content="main">
  <script>
    $(function () {
      $(".delete-subcategory").click(function () {
        var id = $(this).attr("data-id");
        $.ajax({
          type: "delete",
          url: "${createLink(controller: 'subCategory', action: 'delete')}/" + id,
          success: function () {
            $("#subcategory-" + id).fadeOut();
          }, error: function (response) {
            showErrorMessage("#subcategory-error", response.responseJSON.message, response.responseJSON.field);
          }
        });
      });

      $(".edit-subcategory").click(function () {
        var id = $(this).attr("data-id");
        var category = $(this).attr("data-category");
        var type = $("#subcategory-" + id + "-type");
        console.log(category);
        $("#edit-id").val(id);
        $("#edit-name").val($("#subcategory-" + id + "-name").html());
        $("#edit-category option:contains(" + category + ")").attr('selected', true);
        $("#edit-type option:contains(" + type + ")").attr('selected', true);
        $("#edit-subcategory-modal").modal('show');
      });

      $("#edit-subcategory-form").on('submit', function () {
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
            showErrorMessage("#subcategory-error", response.responseJSON.message, response.responseJSON.field);
          }
        });
      });

      $("#new-subcategory-form").on('submit', function () {
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
          url: "${createLink(controller: 'subcategory', action: 'save')}",
          dataType: 'json',
          success: function (response) {
            window.location.reload()
          },
          error: function (response) {
            showErrorMessage("#subcategory-error", response.responseJSON.message, response.responseJSON.field);
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
      <h1>Category Management</h1>

      <g:each in="${categories}" var="category">
        <h3>${category}</h3>

        <table class="table table-hover table-condensed">
          <thead>
          <tr>
            <th>Name</th>
            <th>Transactions</th>
            <th>Type</th>
            <th>Options</th>
          </tr>
          </thead>
          <tbody>
          <g:each in="${category.subcategories}" var="subcategory">
            <tr id="subcategory-${subcategory.id}">
              <td>
                <g:link controller="transaction" action="index" params="[category: subcategory.id]" class="tooltip-link"
                        title="View Transactions for ${subcategory}">
                  <span id="subcategory-${subcategory.id}-name">${subcategory.name}</span>
                </g:link>
              </td>
              <td>${subcategory.transactions.size()}</td>
              <td><span id="subcategory-${subcategory.id}-type">${subcategory.type}</span></td>
              <td>
                <a class="edit-subcategory tooltip-link" title="Edit" data-id="${subcategory.id}" data-category="${category}"><i
                    class="glyphicon glyphicon-pencil"></i></a>
                <a class="delete-subcategory tooltip-link" title="Delete" data-id="${subcategory.id}"><i
                    class="glyphicon glyphicon-remove"></i></a>
              </td>
            </tr>
          </g:each>
          </tbody>
        </table>
        <hr>
      </g:each>
    </div>
  </div>
</div>
<g:render template="editSubCategoryModal"/>

</body>
</html>