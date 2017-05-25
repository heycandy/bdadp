/**
 * Created by Hu on 2016/9/13.
 */
define(["js/scene-edit/edit-dialog"], function (Dialog) {
    'use strict';
    return function () {
        var baseUrl = settings.globalVariableUrl();

        var socket = settings.socket.getInstance();
        socket.on("connect", function (data) {
            console.log("connect");
        });
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
            map: [],
            get: function () {
                return this.map;
            },
            set: function (_map, data) {
                this.map = $.map(_map, function (element) {
                    var sceneId = element.monitor['sceneId'],
                        taskId = element['task_id'], key = element['key'];
                    return {
                        'executeId': data.result,
                        'sceneId': sceneId,
                        'taskId': taskId,
                        'key': key
                    }
                })
            }
        }

        socket.on('test', function (data) {
            if (data.code == 0 && data.result && data.result.executionId == _monitorData.excuteId) {
                var _node = $.grep(_map.get(), function (dd, index) {
                    return data.result.id == dd.task_id
                })
                if (_node.length == 1) {
                    _node[0].monitor.start(_node[0].key, data.result.state);
                }
            }
        });

        /**
         * status now is unknown
         * @param sceneId
         * @param myDiagram
         * @param key  number type
         * @param taskId  map your key
         * @param status
         * @constructor
         */
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

        function _init(sceneId, myDiagram, key, taskId) {
            return new Monitor(sceneId, myDiagram);
        }

        function getAjaxComponentArg(scene_id,keyArr,callback) {
            var dataStr ={selected:keyArr}
            var dataJson = JSON.stringify(dataStr)
            console.log(dataStr)
            var url_component = baseUrl + '/service/v1/scenario/' + scene_id + '/execute'
                , ajax_com = $.ajax({
                    type: 'POST',
                    async: false,
                    cache:false,
                    data:dataJson,
                    contentType:"application/json;charset=utf-8",
                    dataType:'json',
                    url: url_component
                });
            $.when(ajax_com).done(function (componentArg) {
                callback(componentArg);
            }).fail(function (data) {
                console.log("data exception!");
            })
        }

        function getAjaxGraphArg(sceneId, callback) {
            var url_graph = baseUrl + '/service/v1/scenario/graph?scenario_id=' + sceneId
                , ajax_com = $.ajax({
                    type: 'GET',
                    async: false,
                    cache:false,
                    url: url_graph
                });
            $.when(ajax_com).done(function (graphArg) {
                callback(graphArg);
            }).fail(function (data) {
                console.log("data exception!");
            })
        }

        function _socketIO(sceneId, myDiagram,keyArr) {
            //clear
            $("#" + sceneId + " .dropzone-top li.ion-trash-a").trigger('click');
            if (settings.testLangType() == 'zh' || settings.testLangType() == 'zh-CN'
                || settings.testLangType() == 'zh_CN') {
                $('#' + sceneId
                  + ' .myLegend').append('<img src="./img/component/legend.png" ondragstart="return false"/>');
            } else {
                $('#' + sceneId
                  + ' .myLegend').append('<img src="./img/component/enlegend.png" ondragstart="return false"/>');
            }
            getAjaxGraphArg(sceneId, function (response) {
                if (response.resultCode == 0) {
                    _map.set($.map(response.result.graph_vertexs, function (element) {
                        var _monitor = new Monitor(sceneId, myDiagram);
                        return {
                            'key': element.key,
                            'task_id': element.task.task_id,
                            'monitor': _monitor
                        }
                    }))
                    console.log(_map.get());
                    getAjaxComponentArg(sceneId, keyArr,function (data) {
                        if (data.resultCode == 1) {  //exception
                            new Dialog().initConfirmDialog(function () {
                                                           }, function () {   //cancel
                                                               // diagram.commandHandler.undo();
                                                           }
                                , {"message": $.i18n.prop('d_edit_executionException')},
                                                           function () {
                                                               $('#cancelBtn').remove();
                                                               // $('.modal-dialog .modal-body
                                                               // .messContent
                                                               // span:eq(0)').attr('class',
                                                               // 'glyphicon glyphicon-ok-sign')
                                                           });
                        } else if (data.resultCode == 0) {
                            var m = _map.get();
                            _monitorData.set(m, data);
                            _monitorData.excuteId = data.result;

                        }
                    })

                }

            })


        }

        return {
            init: _init,
            monitorMap: _map,
            monitorData: _monitorData,
            socketIO: _socketIO,
            graphArg: getAjaxGraphArg
        }
    }

})
