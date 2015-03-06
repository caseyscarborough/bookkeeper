<script>
  function getChartForMonthlySpending(categoryId) {
    $.getJSON("${createLink(controller: 'graph', action: 'monthlySpendingByCategory')}?id=" + categoryId, function (data) {
      getMonthlySpendingBySubCategory(data);
    });
  }
  $(function () {
    showSpinner('monthlySpendingByCategory');
    getChartForMonthlySpending($("#monthlySpendingCategory").val());
    $("#monthlySpendingCategory").on('change', function () {
      var id = $(this).val();
      getChartForMonthlySpending(id);
    });
  });
</script>

<div class="row">
  <div class="col-md-12">
    <div class="form-group">
      <label for="monthlySpendingCategory">Category</label>
      <select class="form-control domain-property" id="monthlySpendingCategory">
        <g:each in="${categories}" var="category">
          <option data-type="${category.type}" value="${category.id}">${category.name}</option>
        </g:each>
      </select>
    </div>

    <div id="monthlySpendingByCategory" class="graph"></div>
  </div>
</div>