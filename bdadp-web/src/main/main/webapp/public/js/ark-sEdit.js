/**
 * Created by Hu on 2016/8/26.
 */
define(['js/scene-edit/edit-init'], function (Edit) {
    'use strict';
    return function () {
        function _start() {
            new Edit().init();
        }

        return {
            start: _start
        }
    }

})
