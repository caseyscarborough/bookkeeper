Highcharts.theme = {
    colors: ['#1ABC9C', '#2ECC71', '#3498DB', '#9B59B6', '#34495E', '#F1C40F', '#E67E22', '#E74C3C', '#BDC3C7', '#95A5A6'],
    chart: {
        style: {
            fontFamily: 'HelveticaNeue-Light, Helvetica-Neue, Helvetica, Arial, sans-serif'
        },
        height: 500,
        zoomType: 'x'
    },
    credits: { enabled: false }
};

Highcharts.setOptions(Highcharts.theme);

function showSpinner(selector) {
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
    var target = document.getElementById(selector);
    var spinner = new Spinner(opts).spin(target);
}

function getMonthlySpendingBySubCategory(data) {
    $('#monthlySpendingByCategory').highcharts({
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
}

function getSpendingWithSubCategory(data, chart) {
    $('#spendingWithSubcategory' + chart).highcharts({
        chart: {
            type: 'pie'
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
}

function getSpendingByPayee(data) {
    $('#spendingByPayee').highcharts({
        chart: {
            type: 'column'
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
}

function getSpendingByDay(data) {
    var time = new Date(data.time);
    time.setHours(0, 0, 0, 0);
    time = time.getTime();
    $('#spendingByDay').highcharts({
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
                        [0, Highcharts.getOptions().colors[1]],
                        [1, Highcharts.Color(Highcharts.getOptions().colors[1]).setOpacity(0.1).get('rgba')]
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
}

function getSpendingByCategory(data) {
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
}

function getAccountBalancesOverTime(data) {
    $('#accountBalancesOverTime').highcharts({
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
        plotOptions: {
            line: {
                marker: {
                    enabled: false
                }
            }
        },
        series: data.data
    });
}