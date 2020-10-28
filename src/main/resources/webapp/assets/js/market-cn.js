function init_market_width() {
    $.ajax({
        url: '/test/mixed',
        dataType: 'json',
        data:{"code":"000001.XSHE"}
    }).done(function (results) {
        var chart = AmCharts.makeChart("market_width", {
            "type": "serial",
            "theme": "light",
            "dataDateFormat": "YYYY-MM-DD",
            "precision": 2,
            // Y轴
            "valueAxes": [{
                "id": "v1",
                "title": "800",
                "position": "left",
                "autoGridCount": false
            }, {
                "id": "v2",
                "title": "宽度",
                "gridAlpha": 0,
                "position": "right",
                "autoGridCount": false
            }],
            // 图
            "graphs": [{
                "id": "g3",
                "valueAxis": "v1",
                "lineColor": "#F3F8FB",
                "fillColors": "#F3F8FB",
                "fillAlphas": 1,
                "type": "column",
                "title": "Actual Sales",
                "valueField": "sales2",
                "clustered": false,
                "columnWidth": 0.5,
                // "legendValueText": "$[[value]]M",
                // "balloonText": "[[title]]<br /><small style='font-size: 130%'>$[[value]]M</small>"
            }, {
                "id": "g1",
                "valueAxis": "v2",
                "bullet": "round",
                "bulletBorderAlpha": 1,
                "bulletColor": "#FFFFFF",
                "bulletSize": 5,
                "hideBulletsCount": 50,
                "lineThickness": 2,
                "lineColor": "#815FF6",
                "type": "smoothedLine",
                "title": "Duration",
                "useLineColorForBulletBorder": true,
                "valueField": "market1",
                "balloonText": "[[title]]<br /><small style='font-size: 130%'>[[value]]</small>"
            }],
            "chartCursor": {
                "pan": true,
                "valueLineEnabled": true,
                "valueLineBalloonEnabled": true,
                "cursorAlpha": 0,
                "valueLineAlpha": 0.2
            },
            "categoryField": "date",
            "categoryAxis": {
                "parseDates": true,
                "dashLength": 1,
                "minorGridEnabled": true,
                "color": "#5C6DF4"
            },
            "legend": {
                "useGraphSettings": true,
                "position": "top"
            },
            "balloon": {
                "borderThickness": 1,
                "shadowAlpha": 0
            },
            // "export": {
            //     "enabled": false
            // },
            dataProvider:results

        });
    })
}

$(function () {// 初始化内容
    init_market_width();
});

/*-------------- 10 line chart amchart start ------------*/
if ($('#user-statistics').length) {
    var chart = AmCharts.makeChart("user-statistics", {
        "type": "serial",
        "theme": "light",
        "marginRight": 0,
        "marginLeft": 40,
        "autoMarginOffset": 20,
        "dataDateFormat": "YYYY-MM-DD",
        "valueAxes": [{
            "id": "v1",
            "axisAlpha": 0,
            "position": "left",
            "ignoreAxisWidth": true
        }],
        "balloon": {
            "borderThickness": 1,
            "shadowAlpha": 0
        },
        "graphs": [{
            "id": "g1",
            "balloon": {
                "drop": true,
                "adjustBorderColor": false,
                "color": "#ffffff",
                "type": "smoothedLine"
            },
            "fillAlphas": 0.2,
            "bullet": "round",
            "bulletBorderAlpha": 1,
            "bulletColor": "#FFFFFF",
            "bulletSize": 5,
            "hideBulletsCount": 50,
            "lineThickness": 2,
            "title": "red line",
            "useLineColorForBulletBorder": true,
            "valueField": "value",
            "balloonText": "<span style='font-size:18px;'>[[value]]</span>"
        }],
        "chartCursor": {
            "valueLineEnabled": true,
            "valueLineBalloonEnabled": true,
            "cursorAlpha": 0,
            "zoomable": false,
            "valueZoomable": true,
            "valueLineAlpha": 0.5
        },
        "valueScrollbar": {
            "autoGridCount": true,
            "color": "#5E72F3",
            "scrollbarHeight": 30
        },
        "categoryField": "date",
        "categoryAxis": {
            "parseDates": true,
            "dashLength": 1,
            "minorGridEnabled": true
        },
        "export": {
            "enabled": false
        },
        "dataProvider": [{
            "date": "2012-07-27",
            "value": 13
        }]
    });
}

/*-------------- 10 line chart amchart end ------------*/
