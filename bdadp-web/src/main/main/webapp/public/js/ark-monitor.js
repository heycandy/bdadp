/**
 * Created by labo on 2016/9/8.
 */
define(["js/scene-monitor/scene-monitor"], function (monitorInit) {
    'use strict';
    return function () {
        function _start() {
            new monitorInit.monitorInit();

        }

        return {
            start: _start
        }
    }
})
