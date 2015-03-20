<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Categories</title>
  <meta name="layout" content="main">
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

<asset:javascript src="category/manage.js" />
</body>
</html>