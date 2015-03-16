<script>
  function getChart(time, chart) {
    $.getJSON("${createLink(controller: 'graph', action: 'spendingWithSubcategory')}?time=" + time, function (data) {
      getSpendingWithSubCategory(data, chart);
    });
  }

  $(function () {
    showSpinner('spendingWithSubcategory1');
    showSpinner('spendingWithSubcategory2');
    getChart($("#spendingWithSubcategory-month1").val(), "1");
    getChart($("#spendingWithSubcategory-month2").val(), "2");
    $("#spendingWithSubcategory-month1").on('change', function () {
      getChart($(this).val(), "1");
    });
    $("#spendingWithSubcategory-month2").on('change', function () {
      getChart($(this).val(), "2");
    });
  });
</script>

<div class="row">
  <div class="col-md-6">
    <div class="form-group">
      <label for="spendingWithSubcategory-month1">Select Month</label>
      <select class="form-control" id="spendingWithSubcategory-month1">
        <g:each in="${months}" var="month" status="i">
          <option value="${month.value}" <g:if test="${i == 1}">selected</g:if>>${month.name}</option>
        </g:each>
      </select>
    </div>

    <div id="spendingWithSubcategory1" class="graph"></div>
  </div>

  <div class="col-md-6">
    <div class="form-group">
      <label for="spendingWithSubcategory-month2">Select Month</label>
      <select class="form-control" id="spendingWithSubcategory-month2">
        <g:each in="${months}" var="month" status="i">
          <option value="${month.value}" <g:if test="${i == 0}">selected</g:if>>${month.name}</option>
        </g:each>
      </select>
    </div>

    <div id="spendingWithSubcategory2" class="graph"></div>
  </div>
</div>