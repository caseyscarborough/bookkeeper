<div class="row">
  <script>
    function getChartForMonthlySpending(categoryId) {
      $.getJSON("${createLink(controller: 'graph', action: 'monthlySpendingByCategory')}?id=" + categoryId, function(data) {
        $('#monthlySpendingByCategory').highcharts({
          <g:render template="../shared/highchartsColors" />
          chart: {
            height: 500,
            <g:render template="../shared/highchartsFonts" />
          },
          title: {
            text: 'Monthly Transactions Over Time for ' + data.category,
            x: -20 //center
          },
          subtitle: {
            x: -20
          },
          xAxis: {
            categories: data.months
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
    }
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
      var target = document.getElementById('monthlySpendingByCategory');
      var spinner = new Spinner(opts).spin(target);

      getChartForMonthlySpending($("#monthlySpendingCategory").val());
      $("#monthlySpendingCategory").on('change', function() {
        var id = $(this).val();
        getChartForMonthlySpending(id);
      });
    });
  </script>
  <div class="col-md-12">
    <div class="form-group">
      <label for="monthlySpendingCategory">Category</label>
      <select class="form-control domain-property" id="monthlySpendingCategory">
        <g:each in="${categories}" var="category">
          <option data-type="${category.type}" value="${category.id}">${category.name}</option>
        </g:each>
      </select>
    </div>
    <div id="monthlySpendingByCategory" class="graph"></div>
  </div>
</div>