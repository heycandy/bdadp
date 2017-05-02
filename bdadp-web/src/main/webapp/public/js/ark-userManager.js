/**
 * Created by labo on 2016/9/13.
 */
define([], function () {
    'use strict';
    return function () {
        function _start() {
            require(["js/user-manager/user-init.js"], function (userManager) {
                var userManager = new userManager.userManager();
                userManager.init();
            })
        }

        return {
            start: _start
        }
    }
})
