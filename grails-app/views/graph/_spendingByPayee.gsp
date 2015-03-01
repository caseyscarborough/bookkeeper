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
    var target = document.getElementById('spendingByPayee');
    var spinner = new Spinner(opts).spin(target);

    $.getJSON("${createLink(controller: 'graph', action: 'spendingByPayee')}", function (data) {
      $('#spendingByPayee').highcharts({
        chart: {
          type: 'column',
          height: 600
        },
        title: {
          text: 'Spending By Payee'
        },
        xAxis: {
          type: 'category',
          labels: {
            rotation: -60,
            style: {
              fontSize: '10px'
            }
          }
        },
        yAxis: {
          min: 0,
          title: {
            text: 'Cost (USD)'
          }
        },
        legend: {
          enabled: false
        },
        tooltip: {
          pointFormat: '<b>$' + '{point.y:.2f}</b>'
        },
        series: [{
          name: 'Population',
          data: data,
          dataLabels: {
            enabled: true,
            color: '#000',
            align: 'center',
            format: '$' + '{point.y:.2f}', // one decimal
            y: 0, // 10 pixels down from the top
            style: {
              fontSize: '9px'
            }
          }
        }]
      });
    });
  });
</script>
<div class="row">
  <div class="col-md-12">
    <div id="spendingByPayee" class="graph"></div>
  </div>
</div>
