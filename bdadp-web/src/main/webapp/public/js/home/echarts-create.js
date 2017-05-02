/**
 * Created by labo on 2016/10/19.
 */
define([], function () {
    'use strict';
    window.echartsArray = [];
    var _EchatsFactory = function (option) {
        this._option = option;
        this._myChart = null;
    };

    _EchatsFactory.prototype.init = function (id) {
        var _self = this;
        this._myChart = echarts.init($(id)[0]);
        for (var i = 0, len = echartsArray.length; i < len; i++) {
            for (var attr in echartsArray[i]) {
                if (echartsArray[i][attr] === id) {
                    return;
                }
            }
        }
        if (i === echartsArray.length) {
            var obj = {};
            obj[id] = this._myChart;
            echartsArray.push(obj);
        }
        this._myChart.hideLoading();
        this._myChart.setOption(_self._option);
    };

    _EchatsFactory.prototype.update = function (data) {

    };

    _EchatsFactory.prototype.remove = function (data) {
        this._myChart.clear();     // 图表清空-------------------
        this._myChart.dispose();   // 图表释放-------------------
    };

    _EchatsFactory.prototype.refresh = function (data) {
        this._myChart.refresh();
    }
    return {
        create: _EchatsFactory		//return dialog html
    }
})
