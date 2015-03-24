<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main">
  <title>Budget for ${budget.startDate.format("MMMMM yyyy")}</title>
  <script>
    $(function () {
      $("#add-category-button").click(function () {
        var data = {category: $("#category").val(), budget: $("#budget").val()};
        postRequest("${createLink(controller: 'budget', action: 'addCategoryToBudget')}", data, function () {
          window.location.reload();
        });
      });

      $(".edit-budgeted-amount").click(function () {
        var id = $(this).attr("data-id");
        $("#budgeted-amount-" + id).hide();
        $("#edit-budgeted-amount-" + id).show();
      });

      $(".edit-budgeted-amount-input").blur(function() {
        var id = $(this).attr("data-id");
        var amount = $(this).val();
        var data = { id: id, amount: amount };

        postRequest("${createLink(controller: 'budgetItem', action: 'update')}", data, function() {
          window.location.reload();
        });
      });

      $(".delete-budget-item").click(function () {
        var id = $(this).attr("data-id");
        var category = $(this).attr("data-category");

        if (confirm("Are you sure you'd like to delete the " + category + " category from this month's budget? (It can always be added back)")) {
          deleteRequest("${createLink(controller: 'budgetItem', action: 'delete')}/" + id, function() {
            $("#budget-item-" + id).fadeOut();
          });
        }
      });

      $("#sync-budget").click(function() {
        $(this).button('loading');
        var id = $(this).attr("data-id");
        var data = { budget: id };
        postRequest("${createLink(controller: 'budget', action: 'synchronize')}", data, function() {
          window.location.reload();
        });
      });
    });
  </script>
</head>

<body>
<input type="hidden" id="budget" value="${budget.id}">

<div id="content">
  <div class="row">
    <div class="col-md-12">
      <div class="pull-left">
        <h1>${budget.startDate.format("MMMMM yyyy")}</h1>
        <button id="sync-budget" class="btn btn-success" data-id="${budget.id}" data-loading-text="Synchronizing...">Synchronize</button>
      </div>

      <div class="pull-right">
        <div class="form-group">
          <label for="category">Add Category to Budget:</label>
          <select class="form-control" id="category">
            <g:each in="${categories}" var="category">
              <optgroup label="${category.name}">
                <g:each in="${category.subcategories?.sort { it.name }}" var="subcategory">
                  <option data-type="${subcategory.type}" value="${subcategory.id}" <g:if
                      test="${subcategory.id.toString() == params.category}">selected</g:if>>${subcategory.name}</option>
                </g:each>
              </optgroup>
            </g:each>
          </select>
        </div>
        <button id="add-category-button" class="btn btn-primary">Add</button>
      </div>

      <div class="clearfix"></div>

      <g:each in="${budgetItems}" var="budgetItem">
        <div class="row" id="budget-item-${budgetItem.id}">
          <div class="col-md-12">
            <h3 class="pull-left">${budgetItem.category}</h3>

            <div class="pull-right budget-item-amounts">
              $<span data-id="${budgetItem.id}">${budgetItem.actualAmount}</span> of
            $<span id="budgeted-amount-${budgetItem.id}">${budgetItem.budgetedAmount}</span>
              <input id="edit-budgeted-amount-${budgetItem.id}" value="${budgetItem.budgetedAmount}" style="display:none" class="edit-budgeted-amount-input" data-id="${budgetItem.id}">
              <a class="edit-budgeted-amount tooltip-link" data-id="${budgetItem.id}">Edit</a> &middot;
              <a class="delete-budget-item tooltip-link" data-id="${budgetItem.id}" data-category="${budgetItem.category}"}>Delete</a>
            </div>
            <div class="clearfix"></div>

            <div class="progress">
              <div class="progress-bar progress-bar-${budgetItem.cssClass} progress-bar-striped" role="progressbar"
                   aria-valuenow="${budgetItem.percentage}" aria-valuemin="0"
                   aria-valuemax="100" style="width:${budgetItem.percentage > 100 ? 100 : budgetItem.percentage}%">
                $${budgetItem.actualAmount} of $${budgetItem.budgetedAmount} (${budgetItem.percentage}%)
              </div>
            </div>

          </div>
        </div>
      </g:each>
    </div>
  </div>
</div>

</body>
</html>