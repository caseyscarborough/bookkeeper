<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main">
  <title></title>
  <script>
    $(function() {
      $("#add-category-button").click(function() {
        $.ajax({
          type: 'post',
          url: "${createLink(controller: 'budget', action: 'addCategoryToBudget')}",
          data: { id: $("#category").val() },
          dataType: 'json',
          success: function() {
            window.location.reload();
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
      <div class="pull-left">
        <h1>${budget.startDate.format("MMMMM yyyy")}</h1>
      </div>
      <div class="pull-right">
        <div class="form-group">
          <label for="category">Add Category to Budget:</label>
          <select class="form-control" id="category">
            <g:each in="${categories}" var="category">
              <optgroup label="${category.name}">
                <g:each in="${category.subcategories?.sort { it.name }}" var="subcategory">
                  <option data-type="${subcategory.type}" value="${subcategory.id}" <g:if test="${subcategory.id.toString() == params.category}">selected</g:if>>${subcategory.name}</option>
                </g:each>
              </optgroup>
            </g:each>
          </select>
        </div>
        <button id="add-category-button" class="btn btn-primary">Add</button>
      </div>
      <div class="clearfix"></div>

      <g:each in="${budget.budgetItems}" var="budgetItem">
        <h4>${budgetItem.category.name}</h4>
        <table class="table table-condensed">
          <thead>
          <tr>
            <th>Category</th>
            <th>Budgeted Amount</th>
            <th>Actual Amount</th>
            <th>Net</th>
          </tr>
          </thead>
          <tbody>
          <g:each in="${budgetItem.items}" var="item">
            <tr id="item-${item.id}">
              <td>${item.category}</td>
              <td>${item.budgetedAmount}</td>
              <td>${item.actualAmount}</td>
              <td>${item.budgetedAmount - item.actualAmount}</td>
            </tr>
          </g:each>
          </tbody>
        </table>
      </g:each>
    </div>
  </div>
</div>

</body>
</html>