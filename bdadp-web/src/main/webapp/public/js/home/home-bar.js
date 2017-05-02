/**
 * Created by labo on 2016/10/31.
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

    var getResults = function (_self, id, echartsFactor, creatEcharts) {
        settings.HttpClient("GET", null,
                            "/service/v1/scenario/visual/column/?startTime=" + _self._startTime
                            + "&endTime=" + _self._endTime, function (response) {
                //console.log(response);
                //response=[];
                var colorList = ['#999', '#fcd76a', '#a696ce', '#b9d37f', '#db5e8c'];//柱形图颜色列表
                var xAxisArr = [], yAxisArr = [[], [], [], [], []];    //获取图形数据
                var boolCycle = false;
                $.each(response, function (index, value) {
                    var radios = response[index]['value'];
                    xAxisArr.push(response[index]['scenarioCategory']['cate_name']);
                    $.each(radios, function (item, val) {
                        yAxisArr[item].push(radios[item]);
                    })
            })
                boolCycle = false;
                var option = {
                    grid: {
                        x: 100,
                        x2: 100,
                        borderWidth: 1
                    },
                    title: {
                        text: $.i18n.prop('d_home_scenarioCategory'),
                        x: 'center',
                        y: '3%',
                        textStyle: {
                            color: '#323232',
                            fontSize: 14.5
                        }
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'shadow'
                        },
                        padding: [5, 15, 6, 5],
                        formatter: function (params) {
                            var res = '<div>';
                            res += '<strong>' + params[0].name + '</strong>';
                            for (var i = 0, l = params.length; i < l; i++) {
                                console.log(params[i]);
                                var isValue = params[i].value === undefined ? "--" : params[i].value
                                                                                     + '%';
                                res +=
                                    '<br/><div style="width:10px;height:10px;border-radius:50%;margin-right:5px;margin-top:5px;float:left;background-color:'
                                    + params[i].color + '"></div><div style="height: 5px;">'
                                    + params[i].seriesName + ' : ' + isValue + '</div>';
                            }
                            res += '</div>';
                            return res;
                        }
                        /* padding: [5,15,6,5]
                         formatter:function(params){
                         var res = '<div>';
                         res += '<strong>' + params[0].name + '</strong></br>'
                         + "场景总数："+150+'</br>'
                         + "执行总数："+50;
                         for (var i = 0, l = params.length; i < l; i++) {
                         console.log(params[i]);
                         res += '<br/><div style="width:10px;height:10px;border-radius:50%;margin-right:5px;margin-top:5px;float:left;background-color:'+params[i].color+'"></div><div style="height: 5px;">' + params[i].seriesName + ' : ' + params[i].value+'%</div>'
                         }
                         res += '</div>';
                         return res;
                         }*/
                    },
                    legend: {
                        data: [$.i18n.prop('d_home_resourcesOccupation'),
                               $.i18n.prop('d_home_scenarioUtilization'),
                               $.i18n.prop('d_home_scenarioSuccess'),
                               $.i18n.prop('d_home_scenarioExecute'),
                               $.i18n.prop('d_home_scenarioFail')],
                        x: '60%',
                        y: '3%',
                        orient: 'horizontal'
                    },
                    calculable: true,
                    xAxis: [
                        {
                            type: 'category',
                            data: xAxisArr
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value'
                        }
                    ],
                    series: [
                        {
                            name: $.i18n.prop('d_home_resourcesOccupation'),
                            type: 'bar',
                            data: yAxisArr[4],
                            itemStyle: {
                                normal: {
                                    color: colorList[0]
                                }
                            }
                        },
                        {
                            name: $.i18n.prop('d_home_scenarioUtilization'),
                            type: 'bar',
                            data: yAxisArr[3],
                            itemStyle: {
                                normal: {
                                    color: colorList[1]
                                }
                            }
                        },
                        {
                            name: $.i18n.prop('d_home_scenarioSuccess'),
                            type: 'bar',
                            data: yAxisArr[0],
                            itemStyle: {
                                normal: {
                                    color: colorList[2]
                                }
                            }
                        },
                        {
                            name: $.i18n.prop('d_home_scenarioExecute'),
                            type: 'bar',
                            data: yAxisArr[2],
                            itemStyle: {
                                normal: {
                                    color: colorList[3]
                                }
                            }
                        },
                        {
                            name: $.i18n.prop('d_home_scenarioFail'),
                            type: 'bar',
                            data: yAxisArr[1],
                            itemStyle: {
                                normal: {
                                    color: colorList[4]
                                }
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
