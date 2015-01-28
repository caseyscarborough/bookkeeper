<div class="row">
<script>
  $(function() {
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
    var target = document.getElementById('spendingByDay');
    var spinner = new Spinner(opts).spin(target);

    $.getJSON("${createLink(controller: 'graph', action: 'spendingByDay')}", function(data) {
      var time = new Date(data.time);
      time.setHours(0, 0, 0, 0);
      time = time.getTime();
      $('#spendingByDay').highcharts({
        credits: { enabled: false },
        chart: { zoomType: 'x', height: 500 },
        title: { text: 'Spending By Day' },
        subtitle: {
          text: document.ontouchstart === undefined ?
              'Click and drag in the plot area to zoom in' :
              'Pinch the chart to zoom in'
        },
        tooltip: {
          headerFormat: '<span style="font-size: 10px">{point.key}</span><br/>',
          pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
          '<td style="padding:0"><b>&dollar;{point.y:.2f}</b></td></tr>',
          footerFormat: '</table>',
          shared: true,
          useHTML: true
        },
        xAxis: {
          type: 'datetime',
          minRange: 14 * 24 * 3600000, // fourteen days
          title: { text: 'Date' }
        },
        yAxis: { title: { text: 'Total (USD)' } },
        legend: { enabled: false },
        plotOptions: {
          area: {
            fillColor: {
              linearGradient: {x1: 0, y1: 0, x2: 0, y2: 1},
              stops: [
                [0, Highcharts.getOptions().colors[0]],
                [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
              ]
            },
            marker: { radius: 2 },
            lineWidth: 1,
            states: {
              hover: { lineWidth: 1 }
            },
            threshold: null
          }
        },
        series: [{
          type: 'area',
          name: 'USD',
          pointInterval: 24 * 3600 * 1000,
          pointStart: time,
          data: data.data
        }]
      });
    });
  });

</script>
<div class="col-md-12">
  <div id="spendingByDay" class="graph"></div>
</div>
</div>