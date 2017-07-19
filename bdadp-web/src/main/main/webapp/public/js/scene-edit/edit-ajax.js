/**
 * Created by Hu on 2016/9/26.
 */
define([], function () {
    'use strict';
    return function () {
        var baseUrl = settings.globalVariableUrl();

        /**
         * @param params params.sceneId  params.taskId  params.executionId
         * @param callback
         * @param identify
         * @private
         */
        function _getAjaxData(params, callback, identify) {
            var url_graph = baseUrl + '/service/v1/scenario/' + params.sceneId + '/execute?taskId='
                            + params.taskId +
                            '&executionId=' + params.executionId
                , ajax_com = $.ajax({
                    type: 'GET',
                    async: true,
                    url: url_graph
                });
            $.when(ajax_com).done(function (graphArg) {
                callback(graphArg);
            }).fail(function (data) {
                console.log("data exception!");
            })
        }

        function _get(params, callback, identify) {
            _getAjaxData(params, callback, identify);
        }

        function _superAjaxOne(url, type, callback) {
            var ajax_option = $.ajax({
                type: type,
                async: true,
                url: baseUrl + url
            });

            $.when(ajax_option).done(function (response) {
                callback(response);
            }).fail(function (data) {
                console.log("data exception!");
            })
        }

        function _superAjaxTwo(url, url2, type, callback) {
            var url_scenario = baseUrl + url
                , url_excution = baseUrl + url2
                , ajax_sce = $.ajax({
                    type: type,
                    async: true,
                    url: url_scenario,
                    headers: {
                        "Access-Control-Allow-Origin": "*",
                        "Access-Control-Allow-Methods": "POST, GET, PUT, OPTIONS, DELETE",
                        "Access-Control-Max-Age": "3600",
                        "Access-Control-Allow-Headers": " Origin, X-Requested-With, Content-Type, Accept",
                        "If-Modified-Since":"0"
                    }
                })
                , ajax_excution = $.ajax({
                    type: type,
                    async: true,
                    url: url_excution,
                    headers: {
                        "Access-Control-Allow-Origin": "*",
                        "Access-Control-Allow-Methods": "POST, GET, PUT, OPTIONS, DELETE",
                        "Access-Control-Max-Age": "3600",
                        "Access-Control-Allow-Headers": " Origin, X-Requested-With, Content-Type, Accept",
                        "If-Modified-Since":"0"
                    }
                });
            $.when(ajax_sce, ajax_excution).done(function (responseScenario, responseExecution) {
                callback(responseScenario, responseExecution);
            }).fail(function (data) {
                alert("data exception!");
            })
        }

        return {
            get: _get,
            superAjaxOne: _superAjaxOne,
            superAjaxTwo: _superAjaxTwo
        }
    }

})
