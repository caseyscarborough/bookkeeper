<script>
  $(function () {
    showSpinner('spendingByCategory');
    $.getJSON("${createLink(controller: 'graph', action: 'spendingByCategory')}", function (data) {
      getSpendingByCategory(data);
    });
  });
</script>

<div class="row">
  <div class="col-md-12">
    <div id="spendingByCategory" class="graph"></div>
  </div>
</div>