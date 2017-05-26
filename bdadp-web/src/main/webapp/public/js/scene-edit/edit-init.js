/**
 * Created by Hu on 2016/8/26.
 */
define(["js/scene-edit/eidt-attributeTabs", "js/scene-edit/edit-form", "js/scene-edit/edit-edges",
        "js/scene-edit/edit-monitor", "js/scene-edit/edit-dialog", "js/scene-edit/edit-ajax",
        "js/scene-edit/edit-search", "js/scene-edit/edit-copynode"],
    function (ETab, EForm, Edges, Emonitor, Dialog, Ajax, Search, Copy) {

        'use strict';
        return function () {
            var monitor;
            $.base64.utf8encode = true;
            var baseUrl = settings.globalVariableUrl();
            var _options = {
                isDiagramModified: false
            }

            function GraphId(graph_id) {
                this.graph_id = graph_id;
            }

            GraphId.prototype.getGraphId = function () {
                return this.graph_id;
            }

            GraphId.prototype.setGraphId = function (graph_id) {
                this.graph_id = graph_id;
            }

            function GraphResponse(response) {
                this.response = response;
            }

            function TabPanelArr(tabPanelArr) {
                this.tabPanelArr = tabPanelArr;
            }

            TabPanelArr.prototype.getTabPanelArr = function () {
                return this.tabPanelArr;
            }

            TabPanelArr.prototype.setTabPanelArr = function (tabPanelArr, componentType) {
                this.componentType = componentType;
                this.tabPanelArr = tabPanelArr;
            }
            var _tabPanelArr = new TabPanelArr();

            GraphResponse.prototype.getResponse = function () {
                return this.response;
            }

            GraphResponse.prototype.setResponse = function (response) {
                this.response = response;
            }

            var graphResponse = new GraphResponse();

            var _create = function (sceneId, sceneName) {
                var $shell = $('.chrome-tabs-shell'), graph;
                var $currentTab = $shell.find('.chrome-tab-current');
                var sceneEdit = $shell.find('.chrome-tab-title').filter(function (index, dom) {
                    return $(dom).html() == sceneName
                })
                if (sceneEdit.length > 0 && settings.currentSceneTab.exist(sceneId)) {
                    var $tab = $shell.find('.chrome-tab').filter(function (index, dom) {
                        return $(dom).data('tabData').data['sceneId'] == sceneId;
                    })
                    chromeTabs.setCurrentTab($shell, $tab);
                } else {
                    chromeTabs.addNewTab($shell, {
                        // favicon: '',
                        title: sceneName,
                        data: {
                            timeAdded: +new Date(),
                            sceneName: sceneName,
                            sceneId: sceneId
                        }
                    });

                    settings.currentSceneTab.set(sceneId, sceneName);  // store edit scene count
                    var _id = sceneId, _diagramId = "myDiagram_" + sceneId, _myOverview = "myOverviewId_"
                        + sceneId;
                    var _height = document.body.clientHeight - 45;
                    var _html = '<section id="' + _id + '" class="content sceneEdit-sec"' +
                        ' style="display: block;height:' + _height + 'px">' +
                        '<div class="col-md-12" style="padding: 0; visibility: hidden;height: auto">'
                        +
                        '<div class="col-md-2 edit-platte" style="padding-left: 0;"></div>' +
                        '<div class="col-md-7" style="padding-left: 0;"><div class="dropzone-top">'
                        +
                        '<div><ul>' +
                        '<li class="ion-social-youtube"><span>'
                        + $.i18n.prop("d_span_edit_execution") + '</span></li>'
                        + '<li class="selectexec"><span>'                  // restore execution
                        + $.i18n.prop("d_span_edit_selectexec") + '</span><span class = "glyphicon glyphicon-triangle-bottom"></span></li>'
                        /*+'<li><input class="excute-isCheck" type="checkbox" data-size = "mini"></li>'*/
                        +'<li class="ion-archive"><span>' + $.i18n.prop("d_span_edit_save")
                        + '</span></li>' +
                        '<li class="glyphicon glyphicon-export" style="display: none"><span>'
                        + $.i18n.prop("d_span_edit_export") + '</span></li>' +
                        '<li class="ion-help-circled"><span>' + $.i18n.prop("d_span_edit_help")
                        + '</span></li><li class="ion-trash-a" style="display: none" title="清除状态"><span>'
                        + $.i18n.prop("d_span_edit_eliminate") + '</span></li>' +
                        '</ul>' +
                        '</div></div>' +
                        '<div><ul class="select-dropdown dropdown-menu" style="display: none"><div class="user-down"></div><ul class="checkedData" style="list-style: none"></ul><ul style="padding: 0px;background-color: #e6e6e6;"><li class = "clear-li" style="text-align: center;list-style: none;padding: 0px"><a class="clearSelected">全部清除</a></li></ul></ul></div>'+
                        '<div style="position:relative;">' +
                        '<div class="dropzone" id="' + _diagramId
                        + '" style="position: absolute;border: solid 1px #b3b3b3; width:100%;background-color:white"></div>'
                        +
                        '<div id="' + _myOverview
                        + '" class="myOverview"></div><div class="myLegend"></div><div class="myZoom" ondragstart="return false"></div>'
                        +
                        '</div></div>' +
                        '<div class="col-md-3 edit-attribute" style="padding:0;">' +
                        '<div class="dropzone-top" style="text-align:center"><span>'
                        + $.i18n.prop("d_span_edit_attributePanel") + '</span></div>' +
                        '<div class="close-panel"></div><div class="attribute-panel"></div>' +
                        '</div></div>' +
                        '<div class="modal fade in transitionGifDiv" style="z-index: 10000;opacity: 0.6;background: #808080; position: absolute; width: 100%; height: 100%; overflow: auto; display:none; text-align: center;"> '
                        +
                        '<img src="img/component/transit.gif" style="position: relative; top: 30%; overflow: auto; text-align: center;"/> </div>'
                        +
                        '</section>';
                    if ($('#' + _id).length == 0) {
                        $('div.content-wrapper>.main-footer').before(_html);
                    }
                    var graph_id = new GraphId();
                    var tabPanelArr = [];   //save &handle the show and hide of the tabPanel
                    loadPlatte(sceneId, "html/scene-edit/dndPlatte.html", function () {
                        var diagram = new DFlow().dflowInit(_diagramId, _myOverview, "contextMenu",
                            function (e, node) {
                                var n = node;
                            }, function (e, node) {     //the dbclick callbck of node
                                var isShow = $(".select-dropdown").css("display")
                                if(isShow != "none" && e.Hq.type != "mousemove" && e.targetObject.Qn != "Circle") {
                                    // $("#"+sceneId+" li.empty-li").hide()
                                    var selectedComponents = $("#" + sceneId + " li.selectedComponents")
                                    if(selectedComponents.length != 0){
                                        var KeyData = []
                                        for (var k = 0; k< selectedComponents.length ;k++){
                                            var key = $(selectedComponents[k]).attr("key")
                                            KeyData.push(parseInt(key))
                                        }
                                        if(KeyData.indexOf(node.data.key) != -1){
                                            alert("该组件已被选择！")
                                        }else{
                                            $("#"+sceneId+" ul.checkedData").append('<li class = "selectedComponents"  style="list-style: none" key='+node.data.key+'><img width = 25 src= '+node.data.img+'>'+node.data.text+'<span class="glyphicon glyphicon-remove pull-right" title="删除" style="color: #e66864;padding: 9px;cursor: pointer"></span></li>')
                                        }
                                    }else {
                                        $("#"+sceneId+" ul.checkedData").append('<li class = "selectedComponents"  style="list-style: none" key='+node.data.key+'><img width = 25 src= '+node.data.img+'>'+node.data.text+'<span class="glyphicon glyphicon-remove pull-right" title="删除" style="color: #e66864;padding: 9px;cursor: pointer"></span></li>')
                                    }
                                    //$("#"+sceneId+" ul.checkedData").append('<li class = "selectedComponents"  style="list-style: none" key='+node.data.key+'><img width = 25 src= '+node.data.img+'>'+node.data.text+'<span class="glyphicon glyphicon-remove pull-right" title="删除" style="color: #e66864;padding: 9px;cursor: pointer"></span></li>')
                                    $("#" + sceneId + " .glyphicon-remove").click(function () {
                                        $(this).parent().remove()
                                    })

                                    $("#" + sceneId + " .clearSelected").click(function () {
                                        $("#" + sceneId + " .selectedComponents").remove()
                                    })
                                }
                                var thatId = sceneId;
                                var componentId = node.data.pid, componentType = "component";    // judge
                                                                                                 // system,
                                                                                                 // scene,eg
                                if (componentId.indexOf('_') != -1) {
                                    var _type = componentId.substring(componentId.indexOf('_') + 1);
                                    if (_type == "scenario") {
                                        componentId = node.data.pid.substring(0, node.data.pid.indexOf('_'));
                                        componentType = "scenario";
                                    } else {
                                        componentType = "component";
                                    }
                                }
                                var thatKey = node.data.key;
                                var _tPanelClass = thatId + "_tabPanel_" + thatKey;
                                var isExist = (function () {
                                    var flag = false;
                                    if (tabPanelArr.length == 0) {
                                        return false;
                                    }
                                    for (var i = 0; i < tabPanelArr.length; i++) {
                                        if (_tPanelClass == tabPanelArr[i].tabelPanel) {
                                            return true;
                                        } else {
                                            flag = false;
                                        }
                                    }
                                    return flag;
                                })();
                                if (isExist) {    // change display for the  block attribute
                                    $('#' + sceneId + " .attribute-panel .tabPanel").each(function (index,
                                                                                                    element) {
                                        $(element).hide();
                                    })
                                    $('#' + thatId + ' .tabPanel.' + _tPanelClass).show();
                                } else {
                                    getAjaxComponentArg(componentId, function (response) {
                                        if (response.resultCode == 0) {
                                            var res = response;
                                            var formId = thatId + "_component_" + componentId + "_form_" + thatKey
                                                , attriTabId = thatId + "_attri_" + thatKey
                                                , descriTabId = thatId + "_descri_" + thatKey
                                                , tabPanelClass = thatId + "_tabPanel_" + thatKey;
                                            $('#' + sceneId + " .attribute-panel .tabPanel").each(function (index,
                                                                                                            element) {
                                                $(element).hide();
                                            })
                                            tabPanelArr.push({
                                                "tabelPanel": tabPanelClass,
                                                "conponentType": componentType
                                            });   // tabPanelArr add the current show tabPanelClass
                                            _tabPanelArr.setTabPanelArr(tabPanelArr);
                                            var eForm = new EForm();
                                            $('#' + sceneId + " .attribute-panel").append(eForm.create(tabPanelClass,
                                                formId,
                                                attriTabId,
                                                descriTabId,
                                                res.result,
                                                node.data.text).toString());
                                            eForm.isChange(formId);  //bind change event
                                            $('#' + sceneId + ' input[type="text"]').each(function () {
                                                var _mess = $(this).val();
                                                $(this).tinytooltip({message: _mess});
                                            })
                                            addExpression(sceneId,res.result);
                                            return;
                                        }
                                    });
                                }
                            }, function (data) {   // the delete callback of event
                                /**
                                 * delete dom of panel by key
                                 */
                                var thatId = sceneId, key = data.key;
                                var _tabPanelClass = thatId + "_tabPanel_" + key;
                                $('#' + thatId + ' .tabPanel.' + _tabPanelClass).remove();
                                tabPanelArr = $.grep(tabPanelArr, function (obj, index) {   //filter
                                    if (obj.tabelPanel != _tabPanelClass) {
                                        return true;
                                    }
                                })
                                _tabPanelArr.setTabPanelArr(tabPanelArr);  //save tabPanel
                            }
                            , function (textBlock, beforeStr, afterStr) {    // the textBlock change callback
                                textBlock.diagram.nodes.each(function (d) {
                                    var node = d;
                                    var thatId = sceneId;
                                    var componentId = node.data.pid;
                                    if (componentId.indexOf('_') != -1) {
                                        var _type = componentId.substring(componentId.indexOf('_') + 1);
                                        if (_type == "scenario") {
                                            componentId = node.data.pid.substring(0, node.data.pid.indexOf('_'));
                                        }
                                    }
                                    var thatKey = node.data.key;
                                    var formId = thatId + "_component_" + componentId + "_form_" + thatKey;
                                    var tabDescFromId = 'tabDesc_' + formId;
                                    if (thatKey == textBlock.key) {
                                        if ($('#' + tabDescFromId).length > 0) {
                                            $('#'
                                                + tabDescFromId).find('label:eq(0)').html($.trim(afterStr));
                                        }
                                    }
                                });
                                return true;
                            }, function (monitorKey) {   // monitor
                                  var md = monitor.monitorData.get();
                                  for (var i = 0; i < md.length; i++) {
                                    if (monitorKey == md[i]['key']) {  //params.sceneId  params.taskId  params.executionId
                                        var params = {};
                                        params['sceneId'] = md[i]['sceneId'], params.taskId = md[i]['taskId']
                                            , params.executionId = md[i]['executeId'];
                                        var _http = new Ajax();
                                        _http.get(params, function (data) {
                                            if (data.resultCode == 0) {
                                                new Dialog().initConfirmDialog(function () {
                                                    }, function () { //cancel
                                                    }
                                                    , null, function () {
                                                        $('#cancelBtn').remove();
                                                        $('#myModalLabel').html($.i18n.prop('d_edit_Journal'));
                                                        $('.modal-footer').remove();
                                                        $('.modal-dialog>.modal-content>.modal-header').prepend("<span class='glyphicon glyphicon-fullscreen' style='margin-left: 90%;font-size: 13px;cursor: pointer'></span>")
                                                        $('.modal-dialog>.modal-content>.modal-header .modal-title').css({
                                                            float:'left'
                                                        })
                                                        $('.modal-dialog>.modal-content>.modal-header span').click(function(){

                                                            if($(this).attr('class') == "glyphicon glyphicon-fullscreen"){
                                                                $('.modal-dialog').css({
                                                                    'margin-top':'10%',
                                                                    'margin-left':'6%',
                                                                    'width':'70%'
                                                                })
                                                                $('.modal-content').css({
                                                                    'width':'118%',
                                                                    'height' : $('body').height()-200 + 'px'
                                                                })
                                                                $('span.glyphicon-fullscreen').css({
                                                                    'margin-left': '94%'
                                                                })
                                                                $(this).attr('class','ion-arrow-shrink').css('font-size','18px')
                                                                $('.task_log').css({
                                                                    'height':$('.modal-dialog>.modal-content').height() -30 + 'px'
                                                                })
                                                                $('.task_log textarea').css({
                                                                    'height':'92%'
                                                                })
                                                            }else{
                                                                $(this).attr('class','glyphicon glyphicon-fullscreen').css({
                                                                    'font-size':'13px',
                                                                    'margin-left':'90%',
                                                                    'cursor':'pointer'
                                                                })
                                                                $('.modal-dialog').css({
                                                                    'margin-top':'10%',
                                                                    'margin-left':'20%',
                                                                })
                                                                if(typeof(data.result) != "string"){
                                                                    $('.modal-content').css({
                                                                        'width':'76%',
                                                                        'height':'331px'
                                                                    })
                                                                }else{
                                                                    $('.modal-content').css({
                                                                        'width':'76%',
                                                                        'height':'451px'
                                                                    })
                                                                }

                                                                $('.modal-content > .modal-body').css({
                                                                    'width':'100%',
                                                                    'height':'301px'
                                                                })
                                                                $('.task_log').css({
                                                                    'height':'271px'
                                                                })
                                                                $('.task_log > textarea').css({
                                                                    'height':271 + 'px'
                                                                })
                                                            }
                                                        })



                                                        if(data.result instanceof Array){
                                                            for (var i = 0; i < data.result.length; i++) {
                                                                $('.task_log textarea').append(data.result[i]).append('\n');
                                                            }
                                                        }else if(typeof(data.result) == "string"){
                                                            var opsArr = eval("["+data.result+"]");
                                                            $('.task_log').append('<div id =log_graph_temp style="width: 450px;height: 400px;margin: 0 auto;text-align: center;">');
                                                            var myChart = echarts.init(document.getElementById("log_graph_temp"));
                                                            myChart.setOption(opsArr[0]);

                                                            $(".task_log textarea").remove();
                                                        }else{

                                                        }


                                                    }, null, null, 'textarea');
                                            } else {
                                                new Dialog().initConfirmDialog(function () {
                                                        diagram[0].diagram.commandHandler.undo();
                                                    }, function () { //cancel
                                                        // diagram[0].diagram.commandHandler.undo();
                                                    }
                                                    , {"message": $.i18n.prop('d_edit_pleaseCheck')}
                                                    , function () {
                                                        $('#cancelBtn').remove();
                                                    });
                                            }

                                        })
                                        break;
                                    }
                                }
                            },function(data){
                                //预览-------点击获取当前控件的key & task_id

                                /*getAjaxGraphArg(sceneId, function (response) {
                                    var res = response.result.graph_vertexs;
                                    for(var i = 0; i < res.length; i++){
                                        if(res[i].key == data.key){
                                            var taskId = res[i].task.task_id;
                                            var key = res[i].key;

                                            //alert(key+"------"+taskId+"---"+res[i].task.task_name);
                                            // new Dialog().initPreShowDialog(Id);

                                        }
                                    }
                                })*/
                            });

                        var drag = new DFlow(diagram[0].diagram).platteInit(sceneId, "edit-platte", diagram);
                        var diagramN = drag.workFlowInit(_diagramId);
                        $('#' + sceneId + " .contextMenu ul li").each(function (index, dom) {
                            $(dom).click(function () {
                                var txtContent = this.textContent;
                                diagramN.cxcommand(this.textContent);
                            })
                        })

                        diagram[0].diagram.addDiagramListener("LinkDrawn", function (e) {
                            var edge = new Edges();
                            var isDAG = edge.isDAG(e.diagram.model.toJSON());
                            if (!isDAG) {
                                new Dialog().initConfirmDialog(function () {
                                        diagram[0].diagram.commandHandler.undo();
                                    }, function () { //cancel
                                        // diagram[0].diagram.commandHandler.undo();
                                    }
                                    , {"message": $.i18n.prop('d_edit_err')}
                                    , function () {
                                        $('#cancelBtn').remove();
                                    });
                                // alert('this is not the DAG, please check your diagram or workflow!')
                            }
                        })

                        new Copy().init(sceneId, diagram[0].diagram, function (data) {
                            data.forEach(function (_d) {
                                var srcSceneId = _d.node.formId.split('_')[0],
                                    componentId = _d.node.formId.split('_')[2],
                                    thatKey = _d.pasternode.key, componentType = "component", nodeText = _d.node.text;
                                if (componentId.indexOf('_') != -1) {
                                    var _type = componentId.substring(componentId.indexOf('_') + 1);
                                    if (_type == "scenario") {
                                        componentId = componentId.substring(0, componentId.indexOf('_'));
                                        componentType = "scenario";
                                    } else {
                                        componentType = "component";
                                    }
                                }
                                getAjaxComponentArg(componentId, function (response) {
                                    if (response.resultCode == 0) {
                                        var res = response;
                                        var formId = sceneId + "_component_" + componentId + "_form_" + thatKey
                                            , attriTabId = sceneId + "_attri_" + thatKey
                                            , descriTabId = sceneId + "_descri_" + thatKey
                                            , tabPanelClass = sceneId + "_tabPanel_" + thatKey;
                                        $('#' + sceneId + " .attribute-panel .tabPanel").each(function (index,
                                                                                                        element) {
                                            $(element).hide();
                                        })
                                        tabPanelArr.push({
                                            "tabelPanel": tabPanelClass,
                                            "conponentType": componentType
                                        });   // tabPanelArr add the current show tabPanelClass
                                        _tabPanelArr.setTabPanelArr(tabPanelArr);
                                        var eForm = new EForm();
                                        $('#' + sceneId + " .attribute-panel").append(eForm.create(tabPanelClass,
                                            formId, attriTabId,
                                            descriTabId,
                                            res.result,
                                            nodeText).toString());
                                        eForm.isChange(formId);  //bind change event
                                        $('#' + sceneId + ' input[type="text"]').each(function () {
                                            var _mess = $(this).val();
                                            $(this).tinytooltip({message: _mess});
                                        })

                                        var configArr = response.result;
                                        for (var j = 0; j < configArr.length; j++) {   //traverse each element of form
                                            var param_id = configArr[j].param_id;
                                            var param_type = configArr[j].param_type;
                                            if (param_type == "text") {
                                                $('#' + formId + ' #' + param_id).val($.grep(_d.data, function (n) {
                                                    return (n.name == param_id)
                                                })[0].value);
                                            } else if (param_type == "textarea") {
                                                $('#' + formId + ' #' + param_id).val($.grep(_d.data, function (n) {
                                                    return (n.name == param_id)
                                                })[0].value);
                                            } else if (param_type == "number") {
                                                $('#' + formId + ' #' + param_id).val($.grep(_d.data, function (n) {
                                                    return (n.name == param_id)
                                                })[0].value);
                                            } else if (param_type == "password") {
                                                $('#' + formId + ' #' + param_id).val($.grep(_d.data, function (n) {
                                                    return (n.name == param_id)
                                                })[0].value);
                                            } else if (param_type == "date") {
                                                $('#' + formId + ' #' + param_id).val($.grep(_d.data, function (n) {
                                                    return (n.name == param_id)
                                                })[0].value);
                                            } else if (param_type == "options") {
                                                $('#' + formId + ' #' + param_id).val($.grep(_d.data, function (n) {
                                                    return (n.name == param_id)
                                                })[0].value);
                                            } else if (param_type == "fileupload") {
                                                $('#' + formId + ' #' + param_id).val($.grep(_d.data, function (n) {
                                                    return (n.name == param_id)
                                                })[0].value);
                                            } else if (param_type == "expr") {
                                                $('#' + formId + ' #' + param_id).val($.grep(_d.data, function (n) {
                                                    return (n.name == param_id)
                                                })[0].value);
                                            }
                                            else if (param_type == "radio") {
                                                $('#' + formId + ' #' + param_id
                                                    + ' input[type="radio"]').each(function (index, element) {
                                                    if (param_type == "radio" && $(element).attr('value') == $.grep(_d.data,
                                                            function (n) {
                                                                return (n.name
                                                                == param_id)
                                                            })[0].value) {
                                                        $(element).attr('checked', 'checked').click();
                                                    }
                                                });
                                            } else {

                                            }
                                        }
                                        $('#tabDesc_' + formId).find('label[name*=' + thatKey
                                            + ']').html(_d.pasternode.text);

                                        $('#tabDesc_' + formId).find('textarea[name*=' + thatKey
                                            + ']').val(_d.description.length > 0
                                            ? _d.description[0].value : "");
                                        return;
                                    }
                                })

                            })

                        });

                        setDropZone(diagram[0].diagram, sceneId);
                        // var diagramJson = myDiagram.model.toJSON();
                        //------update workflow----------
                        getAjaxGraphArg(sceneId, function (response) {
                            if (response.resultCode == 0) {
                                var result = response.result;
                                if (result == null) {         //create the new scene when edit a new one
                                    $("#" + sceneId + " .transitionGifDiv").css("display", "none");
                                    $("#" + sceneId + " .col-md-12").css("visibility", "visible");
                                } else {
                                    //20170502-- fix the bug for baseUrl change
                                    var modelJson = JSON.parse($.base64.atob(result.graph_raw, true));
                                    if(modelJson && modelJson.nodeDataArray && modelJson.nodeDataArray.length > 0){
                                        modelJson.nodeDataArray.forEach(function(val, index, arr){
                                            if(val.pid.indexOf("_scenario") == -1){
                                                var requestXUrl =val.img.substring(val.img.indexOf("/service"));
                                                val.img = settings.globalVariableUrl() + requestXUrl;
                                            }

                                        })
                                    }
                                    diagram[0].diagram.model =
                                        go.Model.fromJson(modelJson);  //1,load diagram
                                    graph = new Graph(result);
                                    graph.drawPanels(sceneId, tabPanelArr); //return tabPanelArr
                                    graph_id.setGraphId(response.result.graph_id);
                                    $("#" + sceneId + " .transitionGifDiv").css("display", "none");
                                    $("#" + sceneId + " .col-md-12").css("visibility", "visible");
                                }
                            }
                            _tabPanelArr.setTabPanelArr(tabPanelArr);  //save
                            graphResponse.setResponse(response);
                            initTopTool(sceneId, diagram[0].diagram, graph_id);
                            addZoom(sceneId, diagram[0].diagram);

                        })
                        settings.InterNation(['.edit_sceneComponent', '.edit_dataExtraction',
                                '.edit_dataImport', '.edit_dataConversion',
                                '.edit_generalControl', '.edit_dataResource','.edit_dataHandling','.edit_dataOutput','.edit_customerTarget',
                                '.d_span_dnd_cut', '.d_span_dnd_copy', '.d_span_dnd_paste',
                                '.d_span_dnd_delete'],
                            ['d_span_sceneComponent', 'd_span_dataExtraction', 'd_span_dataImport',
                                'd_span_dataConversion', 'd_span_generalControl','d_span_dataResource','d_span_dataHandling','d_span_dataOutput','d_span_customerTarget',
                                'd_span_dnd_cut', 'd_span_dnd_copy', 'd_span_dnd_paste', 'd_span_dnd_delete']);
                        $('.panel-heading .panel-title  .input-sm').attr('placeholder',
                            $.i18n.prop('d_input_search'));
                    });

                }
            }

            function addExpression(sceneId, componentsRes){
                if(componentsRes instanceof Array){
                    var optionArray = [];
                    for (var i = 0;i<componentsRes.length; i++) {
                        var type = componentsRes[i].param_type;
                        if(type == 'expr') {
                            var option =eval("["+ componentsRes[i].default_options +"]")
                            console.log(option);
                            for (var j=0 ;j<option.length;j++){
                                if(option[j].args.length == 1){
                                    optionArray.push({name: option[j].name+"("+option[j].args[0].dataType+" "+option[j].args[0].name+ ")"});
                                }else if (option[j].args.length == 2){
                                    optionArray.push({name: option[j].name+"("+option[j].args[0].dataType+" "+option[j].args[0].name+ "," + option[j].args[1].dataType+" "+option[j].args[1].name+ ")"});
                                }
                            }
                        }
                    }
                    $('#' + sceneId + ' textarea[expr-type="expr"]').typeahead({
                        source:optionArray,
                        menu: '<ul class="typeahead dropdown-menu"></ul>',
                        item: '<li style="border: 1px solid #C8C8C8;border-bottom: 0;"><a href="#"></a></li>',
                    })

                }
            }
            function addZoom(sceneId, myDiagram) {
                $('#' + sceneId
                    + " .myZoom").append('<table class="table table-no-bordered" ondragstart="return false">'
                    +
                    '<tbody><tr><td style="border:0"><div class="">' +
                    '<button type="button" class="btn btn-default zoomRecover" title="比例尺还原"><span class="size-21"><i class="ion-qr-scanner"></i></span></button>'
                    +
                    '</div></td></tr>' +
                    '<tr><td style="border: 0;"><div class="btn-group-vertical"><button type="button" class="btn btn-default zoomOut">'
                    +
                    '<span class="size-21"><i class="ion-plus"></i></span></button> ' +
                    '<button type="button" class="btn btn-default zoomIn"><span class="size-21"><i class="ion-minus-round"></i></span></button></div></td></tr></tbody></table>')

                var scale = 1, step = 0.2;
                $('#' + sceneId + " button.zoomOut").click(function () {
                    scale = scale + step;
                    scale = parseFloat(scale);
                    if (scale > 0) {
                        myDiagram.startTransaction("");
                        myDiagram.scale = scale;
                        myDiagram.commitTransaction("");
                    }
                })
                $('#' + sceneId + " button.zoomIn").click(function () {
                    scale = scale - step;
                    scale = parseFloat(scale);
                    if (scale > 0) {
                        myDiagram.startTransaction("");
                        myDiagram.scale = scale;
                        myDiagram.commitTransaction("");
                    }
                })
                $('#' + sceneId + " button.zoomRecover").click(function () {
                    scale = 1;
                    scale = parseFloat(scale);
                    if (scale > 0) {
                        myDiagram.startTransaction("");
                        myDiagram.scale = scale;
                        myDiagram.commitTransaction("");
                    }
                })
                $('.table-no-bordered tbody tr td button.zoomRecover').attr('title',
                    $.i18n.prop('d_button_scaleReduction'));
            }

            function Graph(result) {
                this.result = result;
            }

            Graph.prototype.getResult = function () {
                return this.result;
            }

            Graph.prototype.drawPanels = function (sceneId, tabPanelArr) {
                var graphData = this.result;
                for (var i = 0; i < graphData.graph_vertexs.length; i++) {    // traverse each node ,then draw the panel
                    var _task_configs = graphData.graph_vertexs[i].task.task_configs;
                    var thatKey = graphData.graph_vertexs[i].key;
                    var componentId = graphData.graph_vertexs[i].task.relation_id;
                    var componentType = graphData.graph_vertexs[i].task.task_type;
                    var _componentResultArr = [];
                    for (var j = 0; j < _task_configs.length; j++) {
                        var _componentResult = _task_configs[j].component_config;
                        _componentResultArr.push(_componentResult);
                    }
                    var eForm = new EForm();
                    var formId = sceneId + "_component_" + componentId + "_form_" + thatKey
                        , attriTabId = sceneId + "_attri_" + thatKey
                        , descriTabId = sceneId + "_descri_" + thatKey
                        , tabPanelClass = sceneId + "_tabPanel_" + thatKey;
                    $('#' + sceneId + " .attribute-panel").append(eForm.create(tabPanelClass, formId,
                        attriTabId, descriTabId,
                        _componentResultArr).toString());

                    addExpression(sceneId,_componentResultArr);

                    $('#' + sceneId + " .attribute-panel .tabPanel").each(function (index, element) {
                        $(element).hide();
                    })
                    tabPanelArr.push({"tabelPanel": tabPanelClass, "conponentType": componentType});   // tabPanelArr
                                                                                                       // add
                                                                                                       // the
                                                                                                       // current
                                                                                                       // show
                                                                                                       // tabPanelClass
                    eForm.isChange(formId);

                }

                //----------set value for form-------------
                for (var i = 0; i < graphData.graph_vertexs.length; i++) {
                    var configArr = graphData.graph_vertexs[i].task.task_configs;
                    var componentId = graphData.graph_vertexs[i].task.relation_id;
                    var thatKey = graphData.graph_vertexs[i].key;
                    var formId = sceneId + "_component_" + componentId + "_form_" + thatKey
                    for (var j = 0; j < configArr.length; j++) {
                        var param_id = configArr[j].param_id;
                        var param_type = configArr[j].component_config.param_type;
                        if (param_type == "text") {
                            $('#' + formId + ' #' + param_id).val(configArr[j].param_value);
                        } else if (param_type == "textarea") {
                            $('#' + formId + ' #' + param_id).val(configArr[j].param_value);
                        } else if (param_type == "number") {
                            $('#' + formId + ' #' + param_id).val(configArr[j].param_value);
                        } else if (param_type == "password") {
                            $('#' + formId + ' #' + param_id).val(configArr[j].param_value);
                        } else if (param_type == "date") {
                            $('#' + formId + ' #' + param_id).val(configArr[j].param_value);
                        } else if (param_type == "options") {
                            $('#' + formId + ' #' + param_id).val(configArr[j].param_value);
                        } else if (param_type == "fileupload") {
                            $('#' + formId + ' #' + param_id).val(configArr[j].param_value);
                        } else if (param_type == "expr") {
                            $('#' + formId + ' #' + param_id).val(configArr[j].param_value);
                        } else if (param_type == "radio") {
                            $('#' + formId + ' #' + param_id + ' input[type="radio"]').each(function (index,
                                                                                                      element) {
                                if (param_type == "radio" && $(element).attr('value')
                                    == configArr[j].param_value) {
                                    $(element).attr('checked', 'checked').click();
                                }
                            });
                        } else if (param_type == "keyvalue") {
                            $('#' + formId + ' #' + param_id).find('input[name="' + param_id + '-key'
                                + '"]').attr('value',
                                configArr[j].param_name);
                            $('#' + formId + ' #' + param_id).find('input[name="' + param_id + '-value'
                                + '"]').attr('value',
                                configArr[j].param_value);

                        } else {

                        }
                    }
                    var _desc = graphData.graph_vertexs[i].task.task_desc;
                    var _name = graphData.graph_vertexs[i].task.task_name;
                    $('#tabDesc_' + formId).find('label[name*=' + thatKey + ']').html(_name);
                    $('#tabDesc_' + formId).find('textarea[name*=' + thatKey + ']').val(_desc);
                    //----input tips--------
                    $('#' + sceneId + ' input[type="text"]').each(function () {
                        var _mess = $(this).val();
                        $(this).tinytooltip({message: _mess});
                    })
                }
                return tabPanelArr;
            }

            function initTopTool(sceneId, diagram, graph_id) {
                execute(sceneId, diagram, graphResponse.getResponse());
                save(sceneId, diagram, graph_id);
                clear(sceneId, diagram);
                selectExcute(sceneId)
            }

            function selectExcute(sceneId) {
                $("#" + sceneId + " .dropzone-top span.glyphicon-triangle-bottom").click(function () {
                    $("#"+sceneId+" ul.select-dropdown").toggle()
                })
                // $("#"+sceneId+" li.empty-li").hide()

                // $("#" + sceneId + " .excute-isCheck").bootstrapSwitch();
            }
            function clear(sceneId, diagram) {
                $("#" + sceneId + " .dropzone-top li.ion-trash-a").click(function () {
                    var _model = diagram.model;
                    var _nodeDataArr = _model.nodeDataArray;
                    $.each(_nodeDataArr, function (index, data) {
                        console.log(data);
                        data.status = -1;
                        _model.updateTargetBindings(data);
                    })
                    $('#' + sceneId + ' .myLegend').find('img').remove();

                    monitor.monitorData.excuteId = 0;
                    for (var i = 0; i < monitor.monitorMap.get().length; i++) {
                        var _monitor = monitor.monitorMap.get()[i].monitor;
                        var _key = monitor.monitorMap.get()[i].key;
                        _monitor.start(_key, -1);
                    }
                })
            }

            function execute(sceneId, diagram, response) {
               // selectedComponents
                var diagram = diagram;
                monitor = new Emonitor();
                $("#" + sceneId + " .dropzone-top li.ion-social-youtube").click(function () {
                    var selectedComponents = $("#" + sceneId + " li.selectedComponents")
                    var keyArr = []
                    for (var i = 0;i<selectedComponents.length;i++){
                        var selectedKey = $(selectedComponents[i]).attr("key")
                        keyArr.push(parseInt(selectedKey))
                    }
                    if (_options.isDiagramModified || !diagram.isModified) {
                        monitor.socketIO(sceneId, diagram,keyArr);
                    } else {
                        // alert('please save your DAG,firstly!')
                        new Dialog().initConfirmDialog(function () {
                                diagram.isModified =
                                    false, _options.isDiagramModified = true;
                                $("#" + sceneId
                                    + " .dropzone-top li.ion-archive").trigger('click');
                                //clear
                                $("#" + sceneId
                                    + " .dropzone-top li.ion-trash-a").trigger('click');
                            }, function () { //cancel
                                diagram.commandHandler.undo()
                            }
                            , {"message": $.i18n.prop('d_edit_saveTask')});
                    }
                })

            }

            function save(sceneId, diagram, graph_id) {
                var myDiagram = diagram, data = {};
                var _graph_id = graph_id.getGraphId();
                $("#" + sceneId + " .dropzone-top li.ion-archive").click(function () {
                    //clear
                    $("#" + sceneId + " .dropzone-top li.ion-trash-a").trigger('click');
                    diagram.isModified = false, _options.isDiagramModified = true;
                    _graph_id = graph_id.getGraphId();
                    var _map = componentMapkey(sceneId);
                    var tabPanelArr = _tabPanelArr.getTabPanelArr();
                    if (tabPanelArr instanceof Array) {
                        var _graph_raw = myDiagram.model.toJSON();
                        if (_graph_id) {
                            data.graph_id = _graph_id;
                        }
                        data.graph_raw = $.base64.btoa(_graph_raw, true);
                        var _graph_vertexs = [];

                        var edges = $.parseJSON(myDiagram.model.toJSON()).linkDataArray.map(function (ele) {
                            return {'from': ele.from, 'to': ele.to}
                        });
                        var vertices = $.parseJSON(myDiagram.model.toJSON()).nodeDataArray.map(function (ele) {
                            return ele.key
                        });
                        for (var i = 0; i < tabPanelArr.length; i++) {
                            var _graph_vertexs_node = {};

                            var key = tabPanelArr[i].tabelPanel.substr(tabPanelArr[i].tabelPanel.lastIndexOf("_")
                                + 1, tabPanelArr[i].tabelPanel.length);
                            var componentId = (function (key, _map) {
                                for (var i = 0; i < _map.length; i++) {
                                    if (_map[i].key == key) {
                                        return _map[i].componentId;
                                    }
                                }
                            })(key, _map);
                            var formId = sceneId + "_component_" + componentId + "_form_" + key;
                            var _formData = $("#" + formId).serializeArray();
                            var _newData = restructFormData(sceneId, formId, _formData);

                            _graph_vertexs_node.key = key;
                            var _edge = new Edges();
                            var _verdict = _edge.isTT(vertices, edges, ~~key);
                            _graph_vertexs_node.is_top = _verdict.is_top;
                            _graph_vertexs_node.is_terminal = _verdict.is_terminal;
                            _graph_vertexs_node.task = {};
                            _graph_vertexs_node.task.task_name = $('#tabDesc_' + formId).find('label').html();
                            _graph_vertexs_node.task.task_desc = $('#tabDesc_' + formId).find('textarea').val();
                            if (tabPanelArr[i]["conponentType"] == "scenario") {
                                _graph_vertexs_node.task.task_type = "scenario";
                            } else {
                                _graph_vertexs_node.task.task_type = "component";
                            }
                            _graph_vertexs_node.task.task_configs = _newData;
                            _graph_vertexs_node.task.relation_id = componentId;
                            _graph_vertexs.push(_graph_vertexs_node);
                        }
                        // data.graph_edges = [{"from_key": -1, "to_key": -2,}];
                        data.graph_edges = edges;
                        data.graph_vertexs = _graph_vertexs;
                        data.vertex_num = vertices.length;
                        data.edge_num = edges.length;
                        data.scenario_id = sceneId;
                        saveAjaxGraph(data, graph_id, function (response) {
                            diagram.isModified = false, _options.isDiagramModified = true;

                            if (response.resultCode == 0) {
                                graphResponse.setResponse(response);
                                graph_id.setGraphId(response.result.graph_id);
                                if ($('#defaultModel').length > 0) {
                                    $('#defaultModel').remove();
                                }
                                new Dialog().initConfirmDialog(function () {

                                    }, function () {   //cancel
                                        // diagram.commandHandler.undo();
                                    }
                                    , {"message": $.i18n.prop('d_edit_saveSuccess')}, function () {
                                        $('#cancelBtn').remove();
                                        $('.modal-dialog .modal-body .messContent span:eq(0)').attr('class',
                                            'glyphicon glyphicon-ok-sign')
                                    });
                            } else {
                                graphResponse.setResponse(response);
                                if ($('#defaultModel').length > 0) {
                                    $('#defaultModel').remove();
                                }
                                showSaveFailMess(response.resultCode, response.resultArgs);
                            }

                        });
                    }
                })
            }

            function showSaveFailMess(resultCode, resultArgs) {
                var messId, _message = '';
                switch (resultCode) {
                    case 22004:
                        messId = 'd_edit_save_22004';
                        break;
                    case 22007:
                        messId = 'd_edit_save_22007';
                        break;
                    case 22008:
                        messId = 'd_edit_save_22008';
                        break;
                    case 22005:
                        messId = 'd_edit_save_22005';
                        break;
                    case 22009:
                        messId = 'd_edit_save_22009';
                        break;
                    case 22999:
                        messId = 'd_edit_save_22999';
                        break;
                    case 23003:
                        messId = 'd_edit_save_23003';
                        break;
                    case 23999:
                        messId = 'd_edit_save_23999';
                        break;
                    default:
                        messId = 'd_edit_save_22999';
                }

                if (resultArgs && resultArgs instanceof Array) {
                    for (var i = 0; i < resultArgs.length; i++) {
                        if (i >= 1) {
                            _message += " , ";
                        }
                        _message += "{" + resultArgs[i] + "}"
                    }
                    _message = $.i18n.prop(messId) + _message;
                } else {
                    _message = $.i18n.prop(messId);
                }

                new Dialog().initConfirmDialog(function () {

                    }, function () {   //cancel
                        // diagram.commandHandler.undo();
                    }
                    , {"message": _message}, function () {
                        $('#cancelBtn').remove();
                        //  $('.modal-dialog .modal-body .messContent span:eq(0)').attr('class', 'glyphicon
                        // glyphicon-ok-sign')
                    });
            }

            function restructFormData(sceneId, formId, _formData) {
                var _newData = $.map(_formData, function (data, i) {
                    var newObj = {};
                    var _id = data.name;
                    var keyOrValue = _id.substring(_id.indexOf('-') + 1);
                    if (_id == "group") {
                        $("#" + formId).find('input[type=radio]').each(function (index, dom) {
                            if (dom.value == data.value) {
                                newObj.param_id = $(dom).parent().parent().attr('id');  //note:controls id
                                newObj.param_name = $(dom).attr('data-name');
                                newObj.param_value = data.value;
                            }
                        })
                    } else if (keyOrValue == "value" || keyOrValue == "key") {
                        newObj.param_id = _id.substring(0, _id.indexOf('-'));
                        if (keyOrValue == "key") {
                            newObj.param_name = data.value;
                        } else if (keyOrValue == "value") {
                            newObj.param_value = data.value;
                        } else {

                        }

                    } else {
                        newObj.param_id = data.name;
                        newObj.param_name = $("#" + sceneId + " #" + _id).attr("data-name");
                        newObj.param_value = data.value;
                    }
                    return newObj;
                })

                var DD = [];
                for (var i = 0; i < _newData.length; i++) {
                    var start_id = _newData[i].param_id, flag = false;
                    for (var j = i + 1; j < _newData.length; j++) {
                        if (start_id == _newData[j].param_id) {
                            var obj = $.extend({}, _newData[i], _newData[j]);
                            DD.push(obj);
                            flag = true;
                            _newData.splice(j, 1);
                        }
                    }
                    //if no repeat
                    if (flag == false) {
                        DD.push({
                            'param_id': _newData[i].param_id,
                            'param_name': _newData[i].param_name,
                            'param_value': _newData[i].param_value
                        })
                    }

                }
                return DD;
            }

            function componentMapkey(sceneId) {
                var map = [];
                $('#' + sceneId + " .attribute-panel .tabPanel").each(function (index, element) {
                    $(element).find('div[id*="_attri_"]').find("form").each(function (index, innerEle) {
                        var str = $(innerEle).attr('id'), obj = {};
                        obj.key = str.split("_")[4];
                        obj.componentId = str.split("_")[2];
                        map.push(obj)
                    })
                })
                return map;
            }

            /**
             * attributesTabId{sceneId + attri + key}
             * @param sceneId
             * @param attributesTabId
             * @param descTabId
             */
            function createEachAttriPanel(sceneId, attributesTabId, descTabId) {
                //***********load attribute panel************
                var etab = new ETab();
                $('#' + sceneId + " .attribute-panel").append(etab.create(attributesTabId,
                    descTabId).toString());
            }

            function setDropZone(myDiagram, sceneId) {
                var bodyHeight = document.body.clientHeight - 160;
                $('#' + sceneId + " .dropzone").each(function (index, dom) {
                    $(dom).css("height", bodyHeight);
                })
                $('#' + sceneId + " .myLegend").each(function (index, dom) {
                    $(dom).css("top", bodyHeight - 37).css('margin-left', '5px');
                })
                /* $('#' + sceneId + " .myZoom").each(function (index, dom) {
                 $(dom).css("top", bodyHeight - 130).css('margin-left', '5px');
                 })*/
                $('#' + sceneId + " .edit-attribute .attribute-panel").each(function (index, dom) {
                    $(dom).css("height", bodyHeight);
                }).slimScroll({
                    height: bodyHeight
                });
                $('#' + sceneId + " .close-panel").each(function (index, dom) {
                    $(dom).css("height", bodyHeight).css('float', 'left').css('width', '0px');
                    $(dom).append('<img src="img/component/side-slider/r-right.png" style="cursor: pointer" />');
                    $(dom).find('img').css('margin-left', '-10px').css('margin-top',
                        (bodyHeight / 2 - 80)).on({
                        mouseout: function () {
                            if ($(this).attr('src') == "img/component/side-slider/r-right-focus.png") {
                                $(this).attr('src', "img/component/side-slider/r-right.png");
                            } else {
                                if ($(this).attr('src') != 'img/component/side-slider/r-right.png') {
                                    $(this).attr('src', "img/component/side-slider/r-left.png");
                                }
                            }
                        },
                        mouseover: function () {
                            if ($(this).attr('src') == "img/component/side-slider/r-right.png") {
                                $(this).attr('src', "img/component/side-slider/r-right-focus.png");
                            } else {
                                $(this).attr('src', "img/component/side-slider/r-left-focus.png");
                            }
                        }
                    });
                    $(dom).find('img').click(function () {
                        if ($(this).attr('src') == "img/component/side-slider/r-left-focus.png") {  //show panel
                            $(this).attr('src', 'img/component/side-slider/r-right.png');
                            $('#' + sceneId + " .col-md-10").attr('class',
                                'col-md-7').find('canvas').css('width',
                                '100%');
                            $('#' + sceneId + " .myOverview").css("margin-left", "75%");
                            $('#' + sceneId + " .col-md-3.edit-attribute").css('width',
                                '25%').find('.dropzone-top span').css('display',
                                'block').css('padding-top',
                                '8px');
                        } else {      //hide panel
                            $(this).attr('src', 'img/component/side-slider/r-left.png');
                            $('#' + sceneId + " .col-md-3.edit-attribute").css('width',
                                '1px').css('margin-right',
                                '-5px').css('float',
                                'right').find('.dropzone-top span').css('display',
                                'none');
                            $('#' + sceneId + " .col-md-7").attr('class',
                                'col-md-10').find('canvas').css('width',
                                '100%');
                            $('#' + sceneId + " .myOverview").css("margin-left", "82%")
                        }

                        myDiagram.commandHandler.zoomToFit();
                        myDiagram.startTransaction("");
                        myDiagram.scale = 1;
                        myDiagram.commitTransaction("");
                    })

                })
                $(".myOverview").each(function (index, dom) {
                    /*  var bodyWidth =  document.body.clientWidth -440;
                     $(dom).css("left",bodyWidth);*/
                })
            }

            function loadPlatte(sceneId, src, callback) {
                $("#" + sceneId + " .transitionGifDiv").css("display", "block");
                $('#' + sceneId + ' .edit-platte').load(src, function (response, status) {
                    if (status == "success") {
                        platteOuterCss(sceneId); // platte three-panel css setting
                        platteTopCss(sceneId);
                        platteMiddleCss(sceneId);	// component middle-panel css setting
                        platteBottomCss(sceneId)
                        getAjaxData(function (sysComponent) {
                            if (sysComponent[1] == "success") {
                                drawSystemComponent(sceneId, sysComponent[0].result);
                            } else {
                                alert("request wrong!")
                            }
                        }, function (scenComponent) {
                            if (scenComponent[1] == "success") {
                                drawSceneConponent(sceneId, scenComponent[0].result)
                            }
                        });
                        new Search().init(sceneId);
                        callback();
                    }
                })
            }

            function drawSceneConponent(sceneId, tJson) {
                var _li = '<li class=draggable" draggable="true" style="display:block;overflow: hidden;text-overflow:ellipsis;white-space: nowrap;width:140px;"></li>';
                for (var i = 0; i < tJson.length; i++) {
                    if (sceneId != tJson[i].scenario_id) {
                        var _src = tJson[i].scenario_col,
                            _img = '<img draggable="false" data-pid="' + tJson[i].scenario_id
                                + '_scenario" src="' + _src + '"' +
                                '" style="width:20px;height: 20px;margin-right: 5px;vertical-align: bottom"/>'
                                +
                                '<span style="vertical-align: 15%;">' + tJson[i].scenario_name + '</span>';
                        var li = $(_li).append(_img).get(0);
                        $('#' + sceneId + ' .scenario-extraction ul:first').append(li);
                    }
                }
            }

            function drawSystemComponent(sceneId, tJson) {
                var _li = '<li class=draggable" draggable="true"></li>';
                for (var i = 0; i < tJson.length; i++) {
                    if (tJson[i].component_pid == "component_pid_data_extract") {   //---extraction----
                        var _src =baseUrl + '/service/v1/resources/components/' + tJson[i].component_id
                                + '/icon?param=xs',
                            _img = '<img draggable="false" data-pid="' + tJson[i].component_id + '" src="'
                                + _src + '"' +
                                '" style="width:20px;height: 20px;margin-right: 5px;vertical-align: bottom"/>'
                                +
                                '<span>' + tJson[i].component_desc + '</span>';
                        var li = $(_li).append(_img).get(0);
                        $('#' + sceneId + ' .data-extraction ul:first').append(li);
                    } else if (tJson[i].component_pid == "component_pid_data_import") {		//-----import-------
                        var _src =baseUrl + '/service/v1/resources/components/' + tJson[i].component_id
                                + '/icon?param=xs',
                            _img = '<img draggable="false" data-pid="' + tJson[i].component_id + '" src="'
                                + _src + '" ' +
                                '"  style="width:20px;height: 20px;margin-right: 5px;vertical-align: bottom"/>'
                                +
                                '<span>' + tJson[i].component_desc + '</span>';
                        var li = $(_li).append(_img).get(0);
                        $('#' + sceneId + ' .data-import ul:first').append(li);
                    } else if (tJson[i].component_pid == "component_pid_common") {      //-----common-----
                        var _src =baseUrl + '/service/v1/resources/components/' + tJson[i].component_id
                                + '/icon?param=xs',
                            _img = '<img draggable="false" data-pid="' + tJson[i].component_id + '" src="'
                                + _src + '" ' +
                                '"  style="width:20px;height: 20px;margin-right: 5px;vertical-align: bottom"/>'
                                +
                                '<span>' + tJson[i].component_desc + '</span>';
                        var li = $(_li).append(_img).get(0);
                        $('#' + sceneId + ' .data-common ul:first').append(li);
                    } else if(tJson[i].component_pid == "component_pid_source"){    //--data-resource
                        var _src =baseUrl + '/service/v1/resources/components/' + tJson[i].component_id + '/icon?param=xs',
                            _img = '<img draggable="false" data-pid="' + tJson[i].component_id + '" src="' + _src + '" ' +
                                '"  style="width:20px;height: 20px;margin-right: 5px;vertical-align: bottom"/>' +
                                '<span>' + tJson[i].component_desc + '</span>';
                        var li = $(_li).append(_img).get(0);
                        $('#' + sceneId + ' .data-resource ul:first').append(li);
                    } else if(tJson[i].component_pid == "component_pid_apply"){   //data-handling
                        var _src =baseUrl + '/service/v1/resources/components/' + tJson[i].component_id + '/icon?param=xs',
                            _img = '<img draggable="false" data-pid="' + tJson[i].component_id + '" src="' + _src + '" ' +
                                '"  style="width:20px;height: 20px;margin-right: 5px;vertical-align: bottom"/>' +
                                '<span>' + tJson[i].component_desc + '</span>';
                        var li = $(_li).append(_img).get(0);
                        $('#' + sceneId + ' .data-handling ul:first').append(li);
                    } else if(tJson[i].component_pid == "component_pid_sink"){  //data-output
                        var _src =baseUrl + '/service/v1/resources/components/' + tJson[i].component_id + '/icon?param=xs',
                            _img = '<img draggable="false" data-pid="' + tJson[i].component_id + '" src="' + _src + '" ' +
                                '"  style="width:20px;height: 20px;margin-right: 5px;vertical-align: bottom"/>' +
                                '<span>' + tJson[i].component_desc + '</span>';
                        var li = $(_li).append(_img).get(0);
                        $('#' + sceneId + ' .data-output ul:first').append(li);
                    }
                    else {
                        var _src =baseUrl + '/service/v1/resources/components/' + tJson[i].component_id
                                + '/icon?param=xs',
                            _img = '<img draggable="false" data-pid="' + tJson[i].component_id + '" src="'
                                + _src + '" ' +
                                '"  style="width:20px;height: 20px;margin-right: 5px;vertical-align: bottom"/>'
                                +
                                '<span>' + tJson[i].component_desc + '</span>';
                        var li = $(_li).append(_img).get(0);
                        $('#' + sceneId + ' .data-transfer ul:first').append(li);
                    }
                }
            }

            // may the constructure of this
            function getAjaxData(callback, sceneCallback) {
                var url_customer = baseUrl + '/service/v1/components/business'
                    , url_system = baseUrl + '/service/v1/components/base'
                    , url_scene = baseUrl + '/service/v1/scenario'
                    , ajax_cus = $.ajax({
                    type: 'GET',
                    async: true,
                    cache: false,
                    url: url_customer
                })
                    , ajax_sce = $.ajax({
                    type: 'GET',
                    async: true,
                    cache: false,
                    url: url_scene
                })
                    , ajax_sys = $.ajax({
                    type: 'GET',
                    async: true,
                    cache: false,
                    url: url_system
                });
                $.when(ajax_cus, ajax_sys, ajax_sce).done(function (cusData, sysData, sceData) {
                    var a = cusData;
                    var b = sysData;
                    var c = sceData;
                    callback(sysData);
                    sceneCallback(sceData);
                }).fail(function (data) {
                    console.log("data exception!");
                })
            }

            function getAjaxComponentArg(component_id, callback) {
                var url_component = baseUrl + '/service/v1/components/' + component_id + '/configs'
                    , ajax_com = $.ajax({
                    type: 'GET',
                    async: true,
                    cache: false,
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
                    async: true,
                    cache: false,
                    url: url_graph
                });
                $.when(ajax_com).done(function (graphArg) {
                    callback(graphArg);
                }).fail(function (data) {
                    console.log("data exception!");
                })
            }

            function saveAjaxGraph(data, graph_id, callback) {
                var _graph_id = graph_id.getGraphId();
                if (_graph_id) {           // update
                    var url_graph = baseUrl + '/service/v1/scenario/graph/' + _graph_id
                        , ajax_com = $.ajax({
                        type: 'PUT',
                        contentType: "application/json; charset=UTF-8",
                        // dataType: "json",
                        async: false,
                        data: JSON.stringify(data),
                        cache:false,
                        url: url_graph
                    });
                    $.when(ajax_com).done(function (graphArg) {
                        callback(graphArg);
                    }).fail(function (data) {
                        console.log("data exception!");
                    })

                } else {          //create
                    var url_graph = baseUrl + '/service/v1/scenario/graph'
                        , ajax_com = $.ajax({
                        type: 'POST',
                        contentType: "application/json",
                        // dataType: "json",
                        async: false,
                        data: JSON.stringify(data),
                        cache: false,
                        url: url_graph
                    });
                    $.when(ajax_com).done(function (graphArg) {
                        callback(graphArg);
                    }).fail(function (data) {
                        console.log("data exception!");
                    })

                }

            }

            function platteOuterCss(sceneId) {
                /*$('.collapse.in').prev('.panel-heading').addClass('active');
                $('.collapse.in').siblings().find('a').attr("class","ion-chevron-down")
                $('#' + sceneId + ' .collapseOne').on('show.bs.collapse', function(a) {
                        $(a.target).prev('.panel-heading').addClass('active');
                        $(a.target).siblings().find('a').attr("class","ion-chevron-down")
                    })
                $('#' + sceneId + ' .collapseOne').on('hide.bs.collapse', function(a) {
                        $(a.target).prev('.panel-heading').removeClass('active');
                        $(a.target).siblings().find('a').attr("class","ion-chevron-up")
                    });
                $('#' + sceneId + ' .collapseTwo').on('show.bs.collapse', function(a) {
                        $(a.target).prev('.panel-heading').addClass('active');
                        $(a.target).siblings().find('a').attr("class","ion-chevron-down")
                    })
                $('#' + sceneId + ' .collapseTwo').on('hide.bs.collapse', function(a) {
                        $(a.target).prev('.panel-heading').removeClass('active');
                        $(a.target).siblings().find('a').attr("class","ion-chevron-up")
                    });
                $('#' + sceneId + ' .collapseThree').on('show.bs.collapse', function(a) {
                        $(a.target).prev('.panel-heading').addClass('active');
                        $(a.target).siblings().find('a').attr("class","ion-chevron-down")
                    })
                $('#' + sceneId + ' .collapseThree').on('hide.bs.collapse', function(a) {
                        $(a.target).prev('.panel-heading').removeClass('active');
                        $(a.target).siblings().find('a').attr("class","ion-chevron-up")
                    });*/

                $('#' + sceneId + ' .panel-group .panel+.panel').each(function (index, dom) {
                    $(dom).css("margin-top", "-3px");
                })
                $('#' + sceneId + ' .panel-title .ion-chevron-up').each(function (index, dom) {
                    $(dom).on("click", function () {
                        if (0 == index) {
                            $('#' + sceneId + ' .collapseOne').collapse('toggle');
                        } else if (1 == index) {
                            $('#' + sceneId + ' .collapseTwo').collapse('toggle')
                        } else {
                            $('#' + sceneId + ' .collapseThree').collapse('toggle');
                        }

                    })
                })
                $('#' + sceneId + ' .collapseOne').on('show.bs.collapse', function () {
                    if (event.target.className == "ion-chevron-down") {
                        $('#' + sceneId
                            + " .panel-heading:eq(0) .panel-title li[class*='ion-chevron']:first").each(function (index,
                                                                                                                  dom) {
                            if (0 == index) {
                                $(dom).attr("class", "ion-chevron-up");
                            }
                        })
                    }
                })
                $('#' + sceneId + ' .collapseOne').on('hide.bs.collapse', function () {
                    if (event.target.className == "ion-chevron-up") {
                        $('#' + sceneId
                            + " .panel-heading:eq(0) .panel-title li[class*='ion-chevron']:first").each(function (index,
                                                                                                                  dom) {
                            if (0 == index) {
                                $(dom).attr("class", "ion-chevron-down");
                            }
                        })
                    }
                })
                $('#' + sceneId + ' .collapseTwo').on('show.bs.collapse', function () {
                    if (event.target.className == "ion-chevron-down") {
                        $('#' + sceneId
                            + " .panel-group>.panel>.panel-heading:eq(2) .panel-title li[class*='ion-chevron']:first").each(function (index,
                                                                                                                                      dom) {
                            if (0 == index) {
                                $(dom).attr("class", "ion-chevron-up");
                            }
                        })
                    }
                })
                $('#' + sceneId + ' .collapseTwo').on('hide.bs.collapse', function () {
                    if (event.target.className == "ion-chevron-up") {
                        $('#' + sceneId
                            + " .panel-group>.panel>.panel-heading:eq(2) .panel-title li[class*='ion-chevron']:first").each(function (index,
                                                                                                                                      dom) {
                            if (0 == index) {
                                $(dom).attr("class", "ion-chevron-down");
                            }
                        })
                    }
                })
                $('#' + sceneId + ' .collapseThree').on('show.bs.collapse', function () {
                    if (event.target.className == "ion-chevron-down") {
                        $('#' + sceneId
                            + " .panel-heading.customer .panel-title li[class*='ion-chevron']:first").each(function (index,
                                                                                                                     dom) {
                            if (0 == index) {
                                $(dom).attr("class", "ion-chevron-up");
                            }
                        })
                    }
                })
                $('#' + sceneId + ' .collapseThree').on('hide.bs.collapse', function () {
                    if (event.target.className == "ion-chevron-up") {
                        $('#' + sceneId
                            + " .panel-heading.customer .panel-title li[class*='ion-chevron']:first").each(function (index,
                                                                                                                     dom) {
                            if (0 == index) {
                                $(dom).attr("class", "ion-chevron-down");
                            }
                        })
                    }
                })
            }

            function platteTopCss(sceneId) {
                // scenario panel --click
                $('#' + sceneId + ' .scenario-extr-title li.ion-arrow-right-b').each(function (index,
                                                                                               dom) {
                    $(dom).on('click', function (e) {
                        $('#' + sceneId + ' .scenario-extraction').collapse('toggle');
                    })
                })

                // scenario-extraction of bind
                $('#' + sceneId + ' .scenario-extraction').on('show.bs.collapse', function () {
                    $('#' + sceneId
                        + " .scenario-assembly .scenario-extr-title li[class*='ion-arrow']:first").each(function (index,
                                                                                                                  dom) {
                        if (0 == index) {
                            $(this).attr("class", "ion-arrow-down-b");
                        }
                    })
                })
                $('#' + sceneId + ' .scenario-extraction').on('hide.bs.collapse', function () {
                    $('#' + sceneId
                        + " .scenario-assembly .scenario-extr-title li[class*='ion-arrow']:first").each(function (index,
                                                                                                                  dom) {
                        if (0 == index) {
                            $(this).attr("class", "ion-arrow-right-b");
                        }
                    })
                })
            }

            function platteMiddleCss(sceneId) {
                // scroll
                var bodyHeight = document.body.clientHeight - 160;
                $('#' + sceneId + ' .collapseOne>.panel-body:first').slimScroll({
                    height: $('.sceneEdit-sec').height()/3-40
                });
                $('#' + sceneId + ' .collapseTwo>.panel-body:first').slimScroll({
                    height: $('.sceneEdit-sec').height()/3-40
                });
                $('#' + sceneId + ' .collapseThree>.panel-body:first').slimScroll({
                    height: $(".content-wrapper").height() - 110 - ($('#' + sceneId
                        + " .edit-platte>.panel-group>.panel:first").height())
                    - ($('#' + sceneId
                        + " .edit-platte>.panel-group .panel.panel-default:nth(1)").height())
                  //  height: $('.sceneEdit-sec').height()/3-40
                });
                //1 data-extraction panel --click
                $('#' + sceneId + ' .data-extr-title li.ion-arrow-right-b').each(function (index, dom) {
                    $(dom).on('click', function (e) {
                        $('#' + sceneId + ' .data-extraction').collapse('toggle');
                    })
                })
                //2 import --click
                $('#' + sceneId + ' .data-import-title li.ion-arrow-right-b').each(function (index, dom) {
                    $(dom).on('click', function (e) {
                        $('#' + sceneId + ' .data-import').collapse('toggle');
                    })
                })
                //3 transfer --click
                $('#' + sceneId + ' .data-transfer-title li.ion-arrow-right-b').each(function (index,
                                                                                               dom) {
                    $(dom).on('click', function (e) {
                        $('#' + sceneId + ' .data-transfer').collapse('toggle');
                    })
                })
                //4 common --click
                $('#' + sceneId + ' .data-common-title li.ion-arrow-right-b').each(function (index, dom) {
                    $(dom).on('click', function (e) {
                        $('#' + sceneId + ' .data-common').collapse('toggle');
                    })
                })

                // 1 data-extraction of bind
                $('#' + sceneId + ' .data-extraction').on('show.bs.collapse', function () {
                    $('#' + sceneId
                        + " .data-extr-title li[class*='ion-arrow']:first").each(function (index, dom) {
                        if (0 == index) {
                            $(this).attr("class", "ion-arrow-down-b");
                        }
                    })
                })
                $('#' + sceneId + ' .data-extraction').on('hide.bs.collapse', function () {
                    $('#' + sceneId
                        + " .data-extr-title li[class*='ion-arrow']:first").each(function (index, dom) {
                        if (0 == index) {
                            $(this).attr("class", "ion-arrow-right-b");
                        }
                    })
                })
                // 2 data-import of bind
                $('#' + sceneId + ' .data-import').on('show.bs.collapse', function () {
                    $('#' + sceneId
                        + " .data-import-title li[class*='ion-arrow']:first").each(function (index, dom) {
                        if (0 == index) {
                            $(this).attr("class", "ion-arrow-down-b");
                        }
                    })
                })
                $('#' + sceneId + ' .data-import').on('hide.bs.collapse', function () {
                    $('#' + sceneId
                        + " .data-import-title li[class*='ion-arrow']:first").each(function (index, dom) {
                        if (0 == index) {
                            $(this).attr("class", "ion-arrow-right-b");
                        }
                    })
                })
                // 3 data-transfer of bind
                $('#' + sceneId + ' .data-transfer').on('show.bs.collapse', function () {
                    $('#' + sceneId
                        + " .data-transfer-title li[class*='ion-arrow']:first").each(function (index, dom) {
                        if (0 == index) {
                            $(this).attr("class", "ion-arrow-down-b");
                        }
                    })
                })
                $('#' + sceneId + ' .data-transfer').on('hide.bs.collapse', function () {
                    $('#' + sceneId
                        + " .data-transfer-title li[class*='ion-arrow']:first").each(function (index, dom) {
                        if (0 == index) {
                            $(this).attr("class", "ion-arrow-right-b");
                        }
                    })
                })
                // 4 data-common of bind
                $('#' + sceneId + ' .data-common').on('show.bs.collapse', function () {
                    $('#' + sceneId
                        + " .data-common-title li[class*='ion-arrow']:first").each(function (index, dom) {
                        if (0 == index) {
                            $(this).attr("class", "ion-arrow-down-b");
                        }
                    })
                })
                $('#' + sceneId + ' .data-common').on('hide.bs.collapse', function () {
                    $('#' + sceneId
                        + " .data-common-title li[class*='ion-arrow']:first").each(function (index, dom) {
                        if (0 == index) {
                            $(this).attr("class", "ion-arrow-right-b");
                        }
                    })
                })
            }

            function platteBottomCss(sceneId) {
                // data-resource --click
                $('#' + sceneId + ' .data-resource-title li.ion-arrow-right-b').each(function (index, dom) {
                    $(dom).on('click', function (e) {
                        $('#' + sceneId + ' .data-resource').collapse('toggle');
                    })
                })

                // data-handling --click
                $('#' + sceneId + ' .data-handling-title li.ion-arrow-right-b').each(function (index, dom) {
                    $(dom).on('click', function (e) {
                        $('#' + sceneId + ' .data-handling').collapse('toggle');
                    })
                })

                // data-output --click
                $('#' + sceneId + ' .data-output-title li.ion-arrow-right-b').each(function (index, dom) {
                    $(dom).on('click', function (e) {
                        $('#' + sceneId + ' .data-output').collapse('toggle');
                    })
                })
                // data-resource of bind
                $('#' + sceneId + ' .data-resource').on('show.bs.collapse', function () {
                    $('#' + sceneId + " .customer-assembly .data-resource-title li[class*='ion-arrow']:first").each(function (index, dom) {
                        if (0 == index) {
                            $(this).attr("class", "ion-arrow-down-b");
                        }
                    })
                })
                $('#' + sceneId + ' .data-resource').on('hide.bs.collapse', function () {
                    $('#' + sceneId + " .customer-assembly .data-resource-title li[class*='ion-arrow']:first").each(function (index, dom) {
                        if (0 == index) {
                            $(this).attr("class", "ion-arrow-right-b");
                        }
                    })
                })
                // data-handling of bind
                $('#' + sceneId + ' .data-handling').on('show.bs.collapse', function () {
                    $('#' + sceneId + " .customer-assembly .data-handling-title li[class*='ion-arrow']:first").each(function (index, dom) {
                        if (0 == index) {
                            $(this).attr("class", "ion-arrow-down-b");
                        }
                    })
                })
                $('#' + sceneId + ' .data-handling').on('hide.bs.collapse', function () {
                    $('#' + sceneId + " .customer-assembly .data-handling-title li[class*='ion-arrow']:first").each(function (index, dom) {
                        if (0 == index) {
                            $(this).attr("class", "ion-arrow-right-b");
                        }
                    })
                })
                // data-output of bind
                $('#' + sceneId + ' .data-output').on('show.bs.collapse', function () {
                    $('#' + sceneId + " .customer-assembly .data-output-title li[class*='ion-arrow']:first").each(function (index, dom) {
                        if (0 == index) {
                            $(this).attr("class", "ion-arrow-down-b");
                        }
                    })
                })
                $('#' + sceneId + ' .data-output').on('hide.bs.collapse', function () {
                    $('#' + sceneId + " .customer-assembly .data-output-title li[class*='ion-arrow']:first").each(function (index, dom) {
                        if (0 == index) {
                            $(this).attr("class", "ion-arrow-right-b");
                        }
                    })
                })

            }

            function _init() {
                $("#test").click(function () {
                    _create("123", "alex");
                })
                $("#test2").click(function () {
                    _create("23232", "Jefffy");
                })
            }

            return {
                init: _init,
                create: _create		//return dialog html
            }
        }

    }
)
