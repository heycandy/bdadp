/**
 * Created by labo on 2016/10/26.
 */

define(["home/home-bar", "home/home-line", "home/home-cicle", "home/echarts-create"],
    function (barCreate, lineCreate, pieCreate, echartsFactor) {
        'use strict';
        var arrParams = [[lineCreate, "scenarioLine"], [pieCreate, "scenarioPie"],
            [barCreate, "scenarioBar"]];
        var creatEcharts = function (option, id) {
        var sceneStatistics = new echartsFactor.create(option);
        sceneStatistics.init("#" + id);
        };
        return function (startTime, endTime) {
        $.each(arrParams, function (index, item) {
            var lineInstance = new this[0].create(startTime, endTime, this[1]);
            if (this[1] !== "scenarioPie") {
                var myChart = echarts.init(document.getElementById(this[1]));
                myChart.showLoading({                                                     // 过渡---------------------
                    text: $.i18n.prop('d_home_readData'),
                    effect: "bubble"
                });                                                                       //loading
                                                                                          // jiazai
            }
            lineInstance.init(this[1], echartsFactor, creatEcharts);
        })
        }
    })



