/**
 * Created by labo on 2016/10/20.
 */


define([], function () {
    'use strict';
    var _ScenePie = function (startTime, endTime) {
        this._startTime = startTime;
        this._endTime = endTime;
        this._options = null;
    };

    _ScenePie.prototype.getOptions = function (id, echartsFactor, creatEcharts) {
        var _self = this;
        getResults(_self, id, echartsFactor, creatEcharts);
    };

    _ScenePie.prototype.init = function (id, echartsFactor, creatEcharts) {
        this.getOptions(id, echartsFactor, creatEcharts);
    };

    var getResults = function (_selft, id, echartsFactor, creatEcharts) {
        var _self = this;
        settings.HttpClient("GET", null,
                            "/service/v1/scenario/visual/linear/?startTime=" + _selft._startTime
                            + "&endTime=" + _selft._endTime, function (response) {
                console.log(response);
                var xAxisArr = [], yAxisArr1 = [], yAxisArr2 = [];    //获取图形数据
                var boolCycle = false;
                $.each(response, function (index, value) {
                    $.each(response[index], function (index1, value1) {
                        if (!false) {
                            xAxisArr.push(response[index][index1][0]);
                        }
                        eval(("yAxisArr" + (index + 1))).push(response[index][index1][1]);
                    });
                    boolCycle = true;
            });
                boolCycle = false;
                var option = {
                    grid: {
                        x: 100,
                        y: 72,
                        x2: 100,
                        borderWidth: 1
                },
                    title: {
                        text: $.i18n.prop('d_home_scenarioStatistics'),
                        x: 'center',
                        y: '3%',
                        textStyle: {
                            color: '#323232',
                            fontSize: 14.5
                        }
                },
                    tooltip: {
                        trigger: 'axis'
                },
                    legend: {
                        data: [$.i18n.prop('d_home_executeSuccessfully'),
                               $.i18n.prop('d_home_executeFail')],
                        x: '80%',
                        y: '3%',
                        orient: 'horizontal'
                    },
                    /*toolbox: {
                     show: true,
                     feature: {
                     mark: {show: true},
                     dataView: {show: true, readOnly: false},
                     magicType: {show: true, type: ['line', 'bar']},
                     restore: {show: true},
                     saveAsImage: {show: true}
                     }
                     },*/
                    calculable: true,
                    xAxis: [
                    {
                        type: 'category',
                        boundaryGap: false,
                        data: xAxisArr
                    }
                    ],
                    yAxis: [
                        {
                            type: 'value',
                            axisLabel: {
                                formatter: '{value}'
                            }
                        }
                    ],
                    series: [
                        {
                            name: $.i18n.prop('d_home_executeSuccessfully'),
                            type: 'line',
                            itemStyle: {
                                normal: {
                                    color: '#a696ce'
                                }
                            },
                            data: yAxisArr1,
                            markPoint: {
                                data: [
                                    {type: 'max', name: $.i18n.prop('d_home_mix')},
                                    {type: 'min', name: $.i18n.prop('d_home_min')}
                                ]
                            },
                            markLine: {
                                data: [
                                    {type: 'average', name: $.i18n.prop('d_home_mean')}
                                ]
                            }
                    },
                        {
                            name: $.i18n.prop('d_home_executeError'),
                            type: 'line',
                            itemStyle: {
                                normal: {
                                    color: '#db5e8c'
                                }
                            },
                            data: yAxisArr2,
                            markPoint: {
                                data: [
                                    {
                                        name: $.i18n.prop('d_home_weekMin'),
                                        value: -2,
                                        xAxis: 1,
                                        yAxis: -1.5
                                    },
                                    {type: 'max', name: $.i18n.prop('d_home_mix')},
                                    {type: 'min', name: $.i18n.prop('d_home_min')}
                                ]
                            },
                            markLine: {
                                data: [
                                    {type: 'average', name: $.i18n.prop('d_home_mean')}
                                ]
                            }
                        }
                    ]
                };
                creatEcharts(option, id);
        });
    };

    return {
        create: _ScenePie		//return dialog html
    }
})
