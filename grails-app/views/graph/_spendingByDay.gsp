<script>
  $(function () {
    showSpinner('spendingByDay');
    $.getJSON("${createLink(controller: 'graph', action: 'spendingByDay')}", function (data) {
      getSpendingByDay(data);
    });
  });
</script>

<div class="row">
  <div class="col-md-12">
    <div id="spendingByDay" class="graph"></div>
  </div>
</div>