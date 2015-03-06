<script>
  $(function () {
    showSpinner('spendingByPayee');
    $.getJSON("${createLink(controller: 'graph', action: 'spendingByPayee')}", function (data) {
      getSpendingByPayee(data);
    });
  });
</script>

<div class="row">
  <div class="col-md-12">
    <div id="spendingByPayee" class="graph"></div>
  </div>
</div>
