<div class="row">
  <script>
    function getChart(time, chart) {
      $.getJSON("${createLink(controller: 'graph', action: 'spendingWithSubcategory')}?time=" + time, function(data) {
        $('#spendingWithSubcategory' + chart).highcharts({
          chart: {
            type: 'pie',
            height: 600
          },
          title: {
            text: 'Spending with SubCategory for ' + data.month
          },
          subtitle: {
            text: 'Click the slice to view subcategories.'
          },
          plotOptions: {
            series: {
              dataLabels: {
                enabled: true,
                format: '{point.name}: {point.categoryTotal}'
              }
            }
          },

          tooltip: {
            headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
            pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.categoryTotal}</b> of {point.grandTotal} ({point.y:.1f}%)<br/>'
          },

          series: [{
            name: 'Categories',
            colorByPoint: true,
            data: data.categories
          }],
          drilldown: {
            series: data.drilldown
          }
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
      var target = document.getElementById('spendingWithSubcategory');
      var spinner = new Spinner(opts).spin(target);

      getChart($("#spendingWithSubcategory-month1").val(), "1");
      getChart($("#spendingWithSubcategory-month2").val(), "2");

      $("#spendingWithSubcategory-month1").on('change', function() {
        var time = $(this).val();
        getChart(time, "1");
      });

      $("#spendingWithSubcategory-month2").on('change', function() {
        var time = $(this).val();
        getChart(time, "2");
      });
    });
  </script>

  <div class="col-md-6">
    <div class="form-group">
      <label for="spendingWithSubcategory-month1">Select Month</label>
      <select class="form-control" id="spendingWithSubcategory-month1">
        <g:each in="${months}" var="month" status="i">
          <option value="${month.value}" <g:if test="${i == 1}">selected</g:if>>${month.name}</option>
        </g:each>
      </select>
    </div>
    <div id="spendingWithSubcategory1" class="graph"></div>
  </div>

  <div class="col-md-6">
    <div class="form-group">
      <label for="spendingWithSubcategory-month2">Select Month</label>
      <select class="form-control" id="spendingWithSubcategory-month2">
        <g:each in="${months}" var="month" status="i">
          <option value="${month.value}" <g:if test="${i == 0}">selected</g:if>>${month.name}</option>
        </g:each>
      </select>
    </div>
    <div id="spendingWithSubcategory2" class="graph"></div>
  </div>
</div>