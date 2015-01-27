<div class="row">
<script>
  $.getJSON("${createLink(controller: 'graph', action: 'spendingByDay')}", function(data) {
    var now = new Date()
    now.setHours(0, 0, 0, 0);
    var timeInPast = now.getTime() - (364 * 24 * 3600 * 1000);
    $('#spendingByDay').highcharts({
      chart: { zoomType: 'x' },
      title: { text: 'Spending By Day' },
      subtitle: {
        text: document.ontouchstart === undefined ?
            'Click and drag in the plot area to zoom in' :
            'Pinch the chart to zoom in'
      },
      tooltip: {
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
        pointStart: timeInPast,
        data: data
      }]
    });
  });
</script>
<div class="col-md-12">
  <div id="spendingByDay"></div>
</div>
</div>