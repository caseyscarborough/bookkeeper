<script>
  $(function () {
    showSpinner('accountBalancesOverTime');
    $.getJSON("${createLink(controller: 'graph', action: 'accountBalancesOverTime')}", function (data) {
      getAccountBalancesOverTime(data);
    });
  });
</script>

<div class="row">
  <div class="col-md-12">
    <div id="accountBalancesOverTime" class="graph"></div>
  </div>
</div>