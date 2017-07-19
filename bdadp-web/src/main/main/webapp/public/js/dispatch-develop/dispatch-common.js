/**
 * Created by Administrator on 2016/11/9.
 */
define([], function () {
    var commonDispatch = {};
    commonDispatch.changeNum = function () {
        var interObj = {
            "1": 'd_disp_week_Sunday',
            "2": 'd_disp_week_Monday',
            "3": 'd_disp_week_Tuesday',
            "4": 'd_disp_week_Wednesday',
            "5": 'd_disp_week_Thursday',
            "6": 'd_disp_week_Friday',
            "7": 'd_disp_week_Saturday'
        };
        return function (numstr) {
            var weekArray = [];
            var weekArr = numstr.split(",");
            for (var i = 0, len = weekArr.length; i < len; i++) {
                if (interObj[weekArr[i]]) {
                    weekArray.push($.i18n.prop(interObj[weekArr[i]]));
                }
            }
            return weekArray.join(",");
        }

    }();

    commonDispatch.showHideBatch = function (arr, display) {
        $.each(arr, function (index, value) {
            $(value).css("display", display);
        })
    };

    commonDispatch.clearInputVal = function (eleArr) {
        $.each(eleArr, function (index, value) {
            $(value).val("");

        })
    };
    return commonDispatch;

});


