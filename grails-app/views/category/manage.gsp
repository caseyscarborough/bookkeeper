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
            alert(response.responseJSON.message);
          }
        });
      });

      $(".delete-category").click(function () {
        var id = $(this).attr("data-id");
        $.ajax({
          type: "delete",
          url: "${createLink(controller: 'category', action: 'delete')}/" + id,
          success: function () {
            $("#category-" + id).fadeOut();
          }, error: function (response) {
            alert(response.responseJSON.message);
          }
        });
      });

      $(".edit-category").click(function () {
        var id = $(this).attr("data-id");
        var name = $(this).attr("data-name");
        $("#edit-category-id").val(id);
        $("#edit-category-name").val(name);
        $("#edit-category-modal").modal('show');
      });

      $("#edit-category-form").on('submit', function () {
        var data = {
          id: $("#edit-category-id").val(),
          name: $("#edit-category-name").val()
        };

        $.ajax({
          type: "post",
          data: data,
          url: "${createLink(controller: 'category', action: 'update')}",
          dataType: 'json',
          success: function () {
            window.location.reload()
          },
          error: function (response) {
            showErrorMessage("#edit-category-error", response.responseJSON.message, "edit-category-" + response.responseJSON.field);
          }
        });
      });

      $(".new-category").click(function () {
        $("#create-category-modal").modal('show');
      });

      $("#create-category-form").on('submit', function () {
        var data = {
          name: $("#create-category-name").val(),
          type: $("#create-category-type").val()
        };

        $.ajax({
          type: "post",
          data: data,
          url: "${createLink(controller: 'category', action: 'save')}",
          dataType: 'json',
          success: function () {
            window.location.reload()
          },
          error: function (response) {
            showErrorMessage("#create-category-error", response.responseJSON.message, "create-category-" + response.responseJSON.field);
          }
        });
      });

      $(".edit-subcategory").click(function () {
        var id = $(this).attr("data-id");
        var category = $(this).attr("data-category");
        var type = $("#subcategory-" + id + "-type").html();
        console.log(category);
        $("#edit-id").val(id);
        $("#edit-name").val($("#subcategory-" + id + "-name").html());
        $("#edit-category option:contains(" + category + ")").attr('selected', true);
        $("#edit-type option:contains(" + type + ")").attr('selected', true);
        $("#edit-subcategory-modal").modal('show');
      });

      $("#edit-subcategory-form").on('submit', function () {
        var data = {};
        $(".edit.modal-domain-property").each(function () {
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

      $("#create-subcategory-form").on('submit', function () {
        var data = {};
        $(".create.modal-domain-property").each(function () {
          if ($(this).attr("disabled") === "disabled") {
          } else {
            data[$(this).attr("name")] = $(this).val();
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
            showErrorMessage("#subcategory-create-error", response.responseJSON.message, "create-" + response.responseJSON.field);
          }
        });
      });

      $(".new-subcategory").click(function () {
        $("#create-subcategory-modal").modal('show');
      });
    });
  </script>
</head>

<body>
<div id="content">
  <div class="row">
    <div class="col-md-12">
      <h1>Category Management</h1>
      <a class="btn btn-primary new-category"><i class="glyphicon glyphicon-plus"></i> New Category</a>
      <a class="btn btn-primary new-subcategory"><i class="glyphicon glyphicon-plus"></i> New SubCategory</a>
      <br>
      <g:each in="${categories}" var="category">
        <div id="category-${category.id}">
          <h2 class="pull-left" id="category-${category.id}-name">${category}&nbsp;</h2>

          <div class="pull-left heading-options">
            <a class="edit-category tooltip-link" title="Edit the ${category} Category" data-id="${category.id}"
               data-name="${category.name}"><i class="glyphicon glyphicon-pencil"></i></a>
            <a class="delete-category tooltip-link" title="Delete the ${category} Category" data-id="${category.id}"><i
                class="glyphicon glyphicon-trash"></i></a>
          </div>

          <div class="clearfix"></div>

          <table class="table table-hover table-condensed">
            <thead>
            <tr>
              <th width="40%">Name</th>
              <th width="20%">Transactions</th>
              <th width="20%">Type</th>
              <th width="20%">Options</th>
            </tr>
            </thead>
            <tbody>
            <g:each in="${category.subcategories}" var="subcategory">
              <tr id="subcategory-${subcategory.id}">
                <td>
                  <g:link controller="transaction" action="index" params="[category: subcategory.id]"
                          class="tooltip-link"
                          title="View Transactions for ${subcategory}">
                    <span id="subcategory-${subcategory.id}-name">${subcategory.name}</span>
                  </g:link>
                </td>
                <td>${subcategory.transactions.size()}</td>
                <td><span id="subcategory-${subcategory.id}-type">${subcategory.type}</span></td>
                <td>
                  <a class="edit-subcategory tooltip-link" title="Edit" data-id="${subcategory.id}"
                     data-category="${category}"><i
                      class="glyphicon glyphicon-pencil"></i></a>
                  <a class="delete-subcategory tooltip-link" title="Delete" data-id="${subcategory.id}"><i
                      class="glyphicon glyphicon-remove"></i></a>
                </td>
              </tr>
            </g:each>
            </tbody>
          </table>
        </div>
      </g:each>
    </div>
  </div>
</div>
<g:render template="editSubCategoryModal"/>
<g:render template="editCategoryModal"/>
<g:render template="newSubCategoryModal"/>
<g:render template="newCategoryModal"/>
</body>
</html>