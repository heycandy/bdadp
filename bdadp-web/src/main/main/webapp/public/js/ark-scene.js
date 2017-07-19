/**
 * Created by labo on 2017/4/25.
 */
define(['js/ark-develop'], function (arkDevelop) {
    'use strict';
    return function () {
        function _start() {
            $("#scene-content").load('html/scene-develop/scene-develop.html', function () {
                $("#scene-content").css("display", "block");
                new arkDevelop().start();
            })
        }

        return {
            start: _start
        }
    }

})