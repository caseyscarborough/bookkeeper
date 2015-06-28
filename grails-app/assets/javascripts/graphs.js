Highcharts.theme = {
    colors: ['#1ABC9C', '#2ECC71', '#3498DB', '#9B59B6', '#34495E', '#F1C40F', '#E67E22', '#E74C3C', '#BDC3C7', '#95A5A6'],
    chart: {
        style: {
            fontFamily: 'HelveticaNeue-Light, Helvetica-Neue, Helvetica, Arial, sans-serif'
        },
        height: 500,
        zoomType: 'x'
    },
    legend: {
        align: 'center',
        verticalAlign: 'bottom',
        backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || 'white',
        borderColor: '#CCC',
        borderWidth: 1,
        shadow: false
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
        },
        subtitle: {
            text: document.ontouchstart === undefined ?
                'Click and drag in the plot area to zoom in' :
                'Pinch the chart to zoom in'
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
        subtitle: {
            text: document.ontouchstart === undefined ?
                'Click and drag in the plot area to zoom in' :
                'Pinch the chart to zoom in'
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
    $('#spendingByDay').highcharts({
        title: {
            text: data.title
        },
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
        series: [data]
    });
}

function getSpendingByCategory(data) {
    $('#spendingByCategory').highcharts({
        chart: {
            type: 'column'
        },
        subtitle: {
            text: document.ontouchstart === undefined ?
                'Click and drag in the plot area to zoom in' :
                'Pinch the chart to zoom in'
        },
        title: {
            text: data.title
        },
        xAxis: {
            categories: data.xAxisCategories
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
                },
                format: '${total}'
            }
        },
        tooltip: {
            formatter: function () {
                return '<b>' + this.x + '</b><br/>' +
                    this.series.name + ': $' + this.y + '<br/>' +
                    'Total: $' + this.point.stackTotal;
            }
        },
        plotOptions: {
            column: {
                stacking: 'normal',
                dataLabels: {
                    enabled: true,
                    formatter: function () {
                        if (this.y != 0) {
                            return '$' + this.y;
                        }
                        return '';
                    },
                    color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white',
                    style: {
                        textShadow: '0 0 3px black'
                    }
                }
            }
        },
        series: data.series
    });
}

function getAccountBalancesOverTime(data) {
    $('#accountBalancesOverTime').highcharts({
        title: {
            text: 'Account Balances Over Time'
        },
        subtitle: {
            text: document.ontouchstart === undefined ?
                'Click and drag in the plot area to zoom in' :
                'Pinch the chart to zoom in'
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