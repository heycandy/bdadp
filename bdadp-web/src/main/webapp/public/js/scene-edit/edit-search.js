/**
 * Created by Hu on 2016/10/10.
 */
define([], function () {
    'use strict';
    return function () {

        function _init(sceneId) {
            $('#' + sceneId + " input.search-2").on({
                input: function (event) {
                    if ($(this).val() == "") {
                        _back(sceneId, ' .collapseOne .scenario-assembly .panel');
                        _back(sceneId, ' .collapseTwo .data-assembly .panel');
                        _back(sceneId, ' .collapseThree .customer-assembly .panel');
                    } else {
                        _search(sceneId, $(this).val());
                    }
                },
                keydown: function (event) {
                    if (event.keyCode == "13") {
                        _search(sceneId, $(this).val());
                    }
                }
            })

            $('#' + sceneId + ' li.ion-android-search').click(function () {
                _search(sceneId, $('#' + sceneId + " input.search-2").val());
            })
        }

        function _search(sceneId, searchValue) {
            _filterLi(sceneId, ' .collapseOne .scenario-assembly .panel', searchValue);
            _filterLi(sceneId, ' .collapseTwo .data-assembly .panel', searchValue);
            _filterLi(sceneId, ' .collapseThree .customer-assembly .panel');
        }

        function _filterLi(sceneId, panelClass, searchValue) {
            $('#' + sceneId + panelClass).each(function (index, item) {
                $(item).hide();
                $(item).find('ul li[draggable="true"]').each(function () {
                    $(this).hide();
                });
                $(item).find('ul li span:contains("' + searchValue + '")').each(function () {
                    $(item).show();
                    var _that = this;
                    $(item).find('ul li[draggable="true"]').each(function (i, li) {
                        if ($(_that).html() == $(li).find('span').html()) {
                            $(li).show();
                        }
                    });
                });
                if ($(item).css('display') == "block"
                    && $(item).find('div[class*=panel-collapse]').attr("aria-expanded")
                       == undefined) {
                    $(item).find('ul li[class*=arrow]').trigger('click');
                }
            })
        }

        function _back(sceneId, panelClass) {
            $('#' + sceneId + panelClass).each(function (index, item) {
                $(item).show();
                $(item).find('ul li[draggable="true"]').each(function (i, li) {
                    $(li).show();
                });
            })
        }

        ///为字符串添加模糊比较的方法
        String.prototype.isLike = function (exp/*类似于SQL中的模糊查询字符串*/, i/*是否区分大小写*/) {
            var str = this;
            i = i == null ? false : i;
            if (exp.constructor == String) {

                /*首先将表达式中的‘_’替换成‘.’，但是‘[_]’表示对‘_’的转义，所以做特殊处理*/
                var s = exp.replace(/_/g, function (m, i) {
                    if (i == 0 || i == exp.length - 1) {
                        return ".";
                    }
                    else {
                        if (exp.charAt(i - 1) == "[" && exp.charAt(i + 1) == "]") {
                            return m;
                        }
                        return ".";
                    }
                });
                /*将表达式中的‘%’替换成‘.’，但是‘[%]’表示对‘%’的转义，所以做特殊处理*/
                s = s.replace(/%/g, function (m, i) {
                    if (i == 0 || i == s.length - 1) {
                        return ".*";
                    }
                    else {
                        if (s.charAt(i - 1) == "[" && s.charAt(i + 1) == "]") {
                            return m;
                        }
                        return ".*";
                    }
                });

                /*将表达式中的‘[_]’、‘[%]’分别替换为‘_’、‘%’*/

                s = s.replace(/\[_\]/g, "_").replace(/\[%\]/g, "%");

                /*对表达式处理完后构造一个新的正则表达式，用以判断当前字符串是否和给定的表达式相似*/

                var regex = new RegExp("^" + s, i ? "" : "i");
                return regex.test(this);
            }
            return false;
        };

        ///为数组添加模糊查询方法
        Array.prototype.selectLike = function (exp/*类似于SQL中的模糊查询字符串*/, fun) {
            var arr = [];
            if (fun && fun.constructor == Function) {
                for (var i = 0; i < this.length; i++) {
                    if (fun(this[i], exp)) {
                        arr.push(i);
                    }
                }
            }
            else {
                for (var i = 0; i < this.length; i++) {
                    if (this[i].isLike(exp, false)) {
                        arr.push(i);
                    }
                }
            }
            return arr;
        };

        return {
            init: _init
        }
    }

})
