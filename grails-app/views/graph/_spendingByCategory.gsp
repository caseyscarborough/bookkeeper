<div class="row">
  <script>
    $(function () {
      $.getJSON("${createLink(controller: 'graph', action: 'spendingByCategory')}", function(data) {
        $('#spendingByCategory').highcharts({
          chart: {
            type: 'column'
          },
          title: {
            text: 'Spending By Category'
          },
          xAxis: {
            categories: data.months
          },
          yAxis: {
            min: 0,
            title: {
              text: 'Total (USD)'
            },
            stackLabels: {
              enabled: true,
              style: {
                fontWeight: 'bold',
                color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
              }
            }
          },
          legend: {
            align: 'right',
            x: -30,
            verticalAlign: 'top',
            y: 25,
            floating: true,
            backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || 'white',
            borderColor: '#CCC',
            borderWidth: 1,
            shadow: false
          },
          tooltip: {
            formatter: function () {
              return '<b>' + this.x + '</b><br/>' +
                  this.series.name + ': ' + this.y + '<br/>' +
                  'Total: ' + this.point.stackTotal;
            }
          },
          plotOptions: {
            column: {
              stacking: 'normal',
              dataLabels: {
                enabled: true,
                color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white',
                style: {
                  textShadow: '0 0 3px black'
                }
              }
            }
          },
          series: data.categories
        });
      });
    });
  </script>
  <div class="col-md-12">
    <div id="spendingByCategory"></div>
  </div>
</div>