/**
 * Created by Hu on 2016/10/20.
 */
define([], function () {
    'use strict';
    return function () {

        function _initRadar(elementId, scenarioArr, data) {
            // 基于准备好的dom，初始化echarts图表
            $('#' + elementId).empty();
            var myChart = echarts.init(document.getElementById(elementId));
            var option = {
                /* title: {
                 x: 'left', // 'center' | 'left' | {number},
                 y: 'top', // 'cent
                 text: $.i18n.prop('d_home_hotScenario'),
                 textStyle: {
                 color: '#323232',
                 fontSize: 14.5
                 }
                 },*/
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    orient: 'horizontal', // 'vertical'
                    x: 'right', // 'center' | 'left' | {number},
                    y: 'top', // 'cent
                    data: scenarioArr
                },
                radar: [
                    {
                        indicator: [
                            {text: $.i18n.prop('d_home_resourcesOccupation'), max: 100},
                            {text: $.i18n.prop('d_home_failRate'), max: 100},
                            {text: $.i18n.prop('d_home_utilizationRate'), max: 100},
                            {text: $.i18n.prop('d_home_executeRate'), max: 100},
                            {text: $.i18n.prop('d_home_successRate'), max: 100},
                        ],
                        startAngle: 90,
                        splitNumber: 5,
                        shape: 'circle',
                        name: {
                            // formatter:'【{value}】',
                            textStyle: {
                                color: 'black'
                            }
                        }
                    }

                ],
                series: [
                    {
                        type: 'radar',
                        tooltip: {
                            trigger: 'item',
                            formatter: function (params) {
                                var res = '<div>';
                                res += '<strong>' + params.name + '</strong>';
                                res +=
                                    '<br/><div style="height:10px;border-radius:50%;margin-top:5px;float:left;"></div><div style="height: 5px;">'
                                    + $.i18n.prop('d_home_resourcesOccupation') + ': '
                                    + params.value[0] + '%</div>'
                                res +=
                                    '<br/><div style="height:10px;border-radius:50%;margin-top:5px;float:left;"></div><div style="height: 5px;">'
                                    + $.i18n.prop('d_home_failRate') + ' : ' + params.value[1]
                                    + '%</div>'
                                res +=
                                    '<br/><div style="height:10px;border-radius:50%;margin-top:5px;float:left;"></div><div style="height: 5px;">'
                                    + $.i18n.prop('d_home_utilizationRate') + ' : '
                                    + params.value[2] + '%</div>'
                                res +=
                                    '<br/><div style="height:10px;border-radius:50%;margin-top:5px;float:left;"></div><div style="height: 5px;">'
                                    + $.i18n.prop('d_home_executeRate') + ' : ' + params.value[3]
                                    + '%</div>'
                                res +=
                                    '<br/><div style="height:10px;border-radius:50%;margin-top:5px;float:left;"></div><div style="height: 5px;margin-bottom: 10px">'
                                    + $.i18n.prop('d_home_successRate') + ': ' + params.value[4]
                                    + '%</div>'
                                res += '</div>';
                                return res;
                            }
                        },
                        itemStyle: {
                            normal: {areaStyle: {type: 'default'}}
                        },
                        data: [
                            {
                                value: data,
                                name: scenarioArr[0],
                                lineStyle: {
                                    normal: {
                                        color: 'rgba(222,119,119, 1)'
                                    }
                                },
                                areaStyle: {
                                    normal: {
                                        color: 'rgba(244, 91, 91, 0.6)'
                                    }
                                }
                            }
                        ]
                    }
                ]
            };

            myChart.setOption(option);
        }

        return {
            initRadar: _initRadar		//return dialog html
        }
    }

})
