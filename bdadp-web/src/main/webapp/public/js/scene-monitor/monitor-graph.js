/**
 * Created by Hu on 2016/10/13.
 */
define(["js/scene-edit/edit-monitor"], function (Emonitor) {
    'use strict';
    return function () {
        var baseUrl = settings.globalVariableUrl();

        /**
         * return 'taskid' , 'key' , 'Monitor'
         * @type {{data: Array, get: _map.get, set: _map.set}}
         * @private
         */
        var _map = {
            data: [],
            get: function () {
                return this.data;
            },
            set: function (_data) {
                this.data = _data;
            }
        };
        var _monitorData = {
            excuteId: null,
        }

        var socket = settings.socket.getInstance();
        socket.on('monitor', function (data) {
            if (data.code == 0 && data.result && data.result.executionId == _monitorData.excuteId) {
                var _node = $.grep(_map.get(), function (dd, index) {
                    return data.result.id == dd.task_id
                })
                if (_node.length == 1) {
                    _node[0].monitor.start(_node[0].key, data.result.state);
                }
            }
        });

        function Monitor(sceneId, myDiagram) {
            if (sceneId && myDiagram) {
                this.sceneId = sceneId;
                this.myDiagram = myDiagram;
            } else {
                throw 'some parameters is undefined!'
            }
        }

        Monitor.prototype.start = function (key, status) {
            if (typeof(key) == "number") {
                this.key = key;
            }
            if (this.int) {
                clearInterval(this.int);
            }
            var _model = this.myDiagram.model;
            // this.status = status;
            var _key = this.key;
            var _nodeDataArr = _model.nodeDataArray;
            var _data = $.grep(_nodeDataArr, function (element, index) {
                return (element.key == _key)
            })

            if (_data instanceof Array && _data.length == 1) {
                var data = _data[0], flag = true;
                if (status == 1) {   // running ---yellow
                    this.int = setInterval(function () {
                        if (flag) {
                            data.status = status;
                            _model.updateTargetBindings(data);
                            flag = false;
                        } else {
                            data.status = -1;   // transparent
                            _model.updateTargetBindings(data);
                            flag = true;
                        }

                    }, 300);
                } else {
                    data.status = status;
                    _model.updateTargetBindings(data);
                }

            }
        }

        var Diagram = function () {

        }

        Diagram.prototype.load = function (sceneId, executionId) {
            var _graphId = "graphMonitor_" + sceneId + '_exe_' + executionId;
            var _html = '<div class="graph-content" style="float: left; height:'
                        + ($(document).height() - 220) + 'px" id="' + _graphId
                        + '"></div><div class="monitorLegend"></div>';
            //$('.diagram-content').append(_html);
            $('.diagram-content').each(function (index, element) {
                if ($(element).find('.graph-content').length == 0) {
                    $(element).append(_html)
                }
            })
            var _graphContentArr = $('.monitor-right-content > .graph').find('.graph-content');
            for (var i = 0; i < _graphContentArr.length; i++) {
                if ($(_graphContentArr[i]).attr('id') == undefined) {
                    $(_graphContentArr[i]).attr('id',
                                                _graphId)
                }
            }
            //$('.monitor-right-content > .graph').find('.graph-content').attr('id', _graphId);
            var diagram = new DFlow().monitorFlow(_graphId);
            var params = "?executionId=" + executionId + "&scenarioId=" + sceneId;
            _getAjaxGraphArg(params, function (response) {
                var _taskidMapStatusArr = [];
                if (response.resultCode == 0) {
                    var _graphData = response.result.graphRaw;
                    diagram.model = go.Model.fromJson($.base64.atob(_graphData, true));
                    if (response.result.rows.length > 0) {
                        for (var i = 0, rows = response.result.rows; i < rows.length; i++) {
                            _taskidMapStatusArr.push({
                                'taskid': rows[i].taskId,
                                'status': rows[i].intStatus
                            });
                        }
                    }

                    _getSceneData(sceneId, function (_response) {
                        if (_response.resultCode == 0) {
                            _map.set($.map(_response.result.graph_vertexs, function (element) {
                                var _monitor = new Monitor(sceneId, diagram);
                                return {
                                    'key': element.key,
                                    'task_id': element.task.task_id,
                                    'monitor': _monitor
                                }
                            }))
                            console.log(_map.get());
                            for (var j = 0; j < _taskidMapStatusArr.length; j++) {
                                var _m = _map.get();
                                var _md = $.grep(_m, function (element, index) {
                                    return _taskidMapStatusArr[j].taskid == element.task_id;
                                })
                                _md[0].monitor.start(_md[0].key, _taskidMapStatusArr[j].status);
                            }
                            //debugger;
                            addLengend();
                        }

                    })
                }
            });

        }

        function addLengend() {
            $('.monitorLegend').css({
                'position': 'absolute',
                'background': 'transparent',
                'height': '50px',
                'width': '33%',
                'margin-top': $(document).height() - 250 + 'px',
                'margin-left': '1%'
            })
            var language = settings.testLangType();
            if (language.substring(0, 2) == "zh") {
                $('.monitorLegend').each(function (index, element) {
                    if ($(element).find('img').length
                        == 0) {
                        $(element).append('<img src="img/component/legend.png"/>')
                    }
                })
            } else if (language.substring(0, 2) == "en") {
                $('.monitorLegend').each(function (index, element) {
                    if ($(element).find('img').length
                        == 0) {
                        $(element).append('<img src="img/component/enlegend.png"/>')
                    }
                })
            }
        }

        function _getSceneData(sceneId, callback) {
            var emonitor = new Emonitor();
            emonitor.graphArg(sceneId, callback);
        }

        function _startIO(sceneId, callback) {
            var emonitor = new Emonitor();
            emonitor.startIO(sceneId, callback);
        }

        function _getAjaxGraphArg(params, callback) {

            var url_graph = baseUrl + '/service/v1/schedule/pagetasks' + params
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

        var _init = function (row) {
            //var param="?executionId="+row.executionId+"&scenarioId="+row.scenarioId;
            _monitorData.excuteId = row.executionId;
            new Diagram().load(row.scenarioId, row.executionId);
        }

        return {
            init: _init
        }
    }

})
