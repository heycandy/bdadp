/**
 * Created by labo on 2016/9/8.
 */
define(['js/dispatch-develop/init-dispatch', 'js/dispatch-develop/dispatch-property'],
    function (initDispatch, dispatchProperty) {
        'use strict';
        return function () {
        function _start() {
            var initDispatchScene = new initDispatch.initDispatch();
            initDispatchScene.initDispatchSence();                             //init all the scene
            initDispatchScene.dispatchAllCheck();
            // dispatchProperty.getProperty.getJedate();
        }
        return {
            start: _start
        }
        }
    })
