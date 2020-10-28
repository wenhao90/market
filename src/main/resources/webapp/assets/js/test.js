//------------echarts2

/*-------------- 11 line chart amchart end ------------*/
function test() {
    $.ajax({
        url: '/test/mixed',
        dataType: 'json',
        data:{"code":"000001.XSHE"}
    }).done(function (results) {
        var chart = AmCharts.makeChart("salesanalytic1", {
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
            // "dataProvider": [ {
            //     "date": "2013-01-16",
            //     "market1": 51,
            //     "sales2": 8
            // },{
            //     "date": "2013-01-17",
            //     "market1": 64,
            //     "sales2": 6
            // }, {
            //     "date": "2013-01-18",
            //     "market1": 65,
            //     "sales2": 12
            // }, {
            //     "date": "2013-01-19",
            //     "market1": 73,
            //     "sales2": 8
            // }, {
            //     "date": "2013-01-20",
            //     "market1": 65,
            //     "sales2": 10
            // }, {
            //     "date": "2013-01-21",
            //     "market1": 65,
            //     "sales2": 12
            // }, {
            //     "date": "2013-01-22",
            //     "market1": 68,
            //     "sales2": 7
            // }, {
            //     "date": "2013-01-23",
            //     "market1": 75,
            //     "sales2": 10
            // }, {
            //     "date": "2013-01-24",
            //     "market1": 75,
            //     "sales2": 9
            // }, {
            //     "date": "2013-01-25",
            //     "market1": 75,
            //     "sales2": 10
            // }, {
            //     "date": "2013-01-26",
            //     "market1": 55,
            //     "sales2": 7
            // }, {
            //     "date": "2013-01-27",
            //     "market1": 67,
            //     "sales2": 4
            // }, {
            //     "date": "2013-01-28",
            //     "market1": 62,
            //     "sales2": 7
            // }, {
            //     "date": "2013-01-29",
            //     "market1": 62,
            //     "sales2": 8
            // }, {
            //     "date": "2013-01-30",
            //     "market1": 71,
            //     "sales2": 7
            // }]
        });
    })

    /*-------------- 11 line chart amchart start ------------*/
    if ($('#salesanalytic1').length) {


    }
}

$(function () {// 初始化内容
    test();
});