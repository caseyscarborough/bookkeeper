<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Categories</title>
  <meta name="layout" content="main">
  <script>
    $(function () {
      $(".delete-subcategory").click(function () {
        var id = $(this).attr("data-id");
        deleteSubCategory(id, function () {
          $("#subcategory-" + id).fadeOut();
        }, function (response) {
          alert(response.responseJSON.message);
        });
      });

      $(".delete-category").click(function () {
        var id = $(this).attr("data-id");
        deleteCategory(id, function () {
          window.location.reload();
        }, function (response) {
          alert(response.responseJSON.message);
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
        var data = {id: $("#edit-category-id").val(), name: $("#edit-category-name").val()};
        updateCategory(data, function () {
          window.location.reload()
        }, function (response) {
          showErrorMessage("#edit-category-error", response.responseJSON.message, "edit-category-" + response.responseJSON.field);
        });
      });

      $(".new-category").click(function () {
        $("#create-category-modal").modal('show');
      });

      $("#create-category-form").on('submit', function () {
        var data = {name: $("#create-category-name").val(), type: $("#create-category-type").val()};
        createCategory(data, function () {
          window.location.reload()
        }, function (response) {
          showErrorMessage("#create-category-error", response.responseJSON.message, "create-category-" + response.responseJSON.field);
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
        updateSubCategory(data, function () {
          window.location.reload();
        }, function (response) {
          showErrorMessage("#subcategory-error", response.responseJSON.message, response.responseJSON.field);
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

        createSubCategory(data, function () {
          window.location.reload();
        }, function (response) {
          showErrorMessage("#subcategory-create-error", response.responseJSON.message, "create-" + response.responseJSON.field);
        });
      });

      $(".new-subcategory").click(function () {
        $("#create-subcategory-modal").modal('show');
      });

      $(".show-subcategories").click(function () {
        var id = $(this).attr("data-id");
        $("#subcategories-" + id).show();
        $(this).hide();
        $("#hide-" + id).show();
      });

      $(".hide-subcategories").click(function () {
        var id = $(this).attr("data-id");
        $("#subcategories-" + id).hide();
        $(this).hide();
        $("#show-" + id).show();
      });
    });
  </script>
</head>

<body>
<div id="content" class="container">
  <div class="row">
    <div class="col-md-12">
      <h1 class="pull-left">Category Management</h1>

      <div class="pull-right">
        <br>
        <a class="btn btn-primary new-category"><i class="glyphicon glyphicon-plus"></i> New Category</a>
        <a class="btn btn-info new-subcategory"><i class="glyphicon glyphicon-plus"></i> New SubCategory</a>
      </div>

      <div class="clearfix"></div>
      <g:each in="${categories}" var="category">
        <div id="category-${category.id}">
          <h3 class="pull-left" id="category-${category.id}-name">${category}&nbsp;</h3>

          <div class="pull-left heading-options">
            <a id="show-${category.id}" class="show-subcategories tooltip-link"
               title="Show SubCategories for this Category" data-id="${category.id}"><i
                class="glyphicon glyphicon-plus"></i></a>
            <a id="hide-${category.id}" class="hide-subcategories tooltip-link" style="display:none"
               data-id="${category.id}" title="Hide SubCategories for this Category"><i
                class="glyphicon glyphicon-minus"></i></a>
            <a class="edit-category tooltip-link" title="Edit the ${category} Category" data-id="${category.id}"
               data-name="${category.name}"><i class="glyphicon glyphicon-pencil"></i></a>
            <a class="delete-category tooltip-link" title="Delete the ${category} Category" data-id="${category.id}"><i
                class="glyphicon glyphicon-trash"></i></a>
          </div>

          <div class="clearfix"></div>

          <div id="subcategories-${category.id}" style="display:none" class="subcategory-table table-responsive">
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