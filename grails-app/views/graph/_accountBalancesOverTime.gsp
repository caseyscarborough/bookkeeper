<div class="row">
  <script>
    $(function () {
      var opts = {
        lines: 13, // The number of lines to draw
        length: 29, // The length of each line
        width: 10, // The line thickness
        radius: 27, // The radius of the inner circle
        corners: 1, // Corner roundness (0..1)
        rotate: 0, // The rotation offset
        direction: 1, // 1: clockwise, -1: counterclockwise
        color: '#000', // #rgb or #rrggbb or array of colors
        speed: 1, // Rounds per second
        trail: 60, // Afterglow percentage
        shadow: false, // Whether to render a shadow
        hwaccel: false, // Whether to use hardware acceleration
        className: 'spinner', // The CSS class to assign to the spinner
        zIndex: 2e9, // The z-index (defaults to 2000000000)
        top: '50%', // Top position relative to parent
        left: '50%' // Left position relative to parent
      };
      var target = document.getElementById('accountBalancesOverTime');
      var spinner = new Spinner(opts).spin(target);

      $.getJSON("${createLink(controller: 'graph', action: 'accountBalancesOverTime')}", function(data) {
        $('#accountBalancesOverTime').highcharts({
          <g:render template="../shared/highchartsColors" />
          chart: {
            height: 500,
            zoomType: "x",
            <g:render template="../shared/highchartsFonts" />
          },
          title: {
            text: 'Account Balances Over Time',
            x: -20
          },
          subtitle: {
            x: -20
          },
          xAxis: {
            categories: data.days,
            tickInterval: 20
          },
          yAxis: {
            title: {
              text: 'Total (USD)'
            },
            plotLines: [{
              value: 0,
              width: 1,
              color: '#808080'
            }]
          },
          tooltip: {
            valueSuffix: '',
            valuePrefix: "$"
          },
          legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle',
            borderWidth: 0
          },
          series: data.data
        });
      });
    });
  </script>
  <div class="col-md-12">
    <div id="accountBalancesOverTime" class="graph"></div>
  </div>
</div>