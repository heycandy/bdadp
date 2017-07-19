/**
 * Created by labo on 2016/10/19.
 */
define([], function () {
    'use strict';
    var _ScenePie = function (option) {
        this._option = option;
        this._myChart = null;
    }
    _ScenePie.prototype.init = function (id) {
        var _self = this;
        this._myChart = echarts.init($(id)[0]);
        this._myChart.setOption(_self._option);
    };
    _ScenePie.prototype.update = function (data) {

    };
    _ScenePie.prototype.remove = function (data) {
        this._myChart.clear();     // 图表清空-------------------
        this._myChart.dispose();   // 图表释放-------------------
    };
    _ScenePie.prototype.refresh = function (data) {

    }
    return {
        create: _ScenePie		//return dialog html
    }
})
