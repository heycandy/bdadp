/**
 * Created by Administrator on 2016/11/24.
 */

define(["component/BdaTableDefined", "js/modeldialog", "js/scene-develop/scene-common"],
    function (table, initDialog, sceneCommon) {
        settings.interFun();

        var sceneImportMethod = {};
        sceneImportMethod._sceneImport = function () {
            var that = this;
            $("#scene-import").unbind("change");
            $("#scene-import").on('change',function(e){
                var options = {
                    url: settings.globalVariableUrl()+'/service/v1/resources/scenario/*/zip',
                    type: 'post',
                    success: function (data) {
                        var scenario_ids = data.result.name;
                        //var scenariosArr = scenario_ids.split("_");
                        var previewObject = {};
                        previewObject.action = "preview";
                        previewObject.cate_id = data.result.id;
                        previewObject.scenario_id = [];
                        previewObject.name = data.result.name;
                        previewObject.resType = "zip";
                        previewObject.userId = "";

                        $("#scene-import").val("");

                        settings.HttpClient("POST", previewObject, "/service/v1/scenario/action",
                                            function (data) {

                                                that._scenePreview(data, previewObject);

                                            })

                    },
                    error: function (data) {
                        alert(data);

                        $("#scene-import").val("");
                    }
                };
                $("#importForm").ajaxSubmit(options);
            })

            $("#scene-import").trigger("click");
        };


        sceneImportMethod._scenePreview = function (data, previewObject) {

            var message = '<div class="container-fuild importScenario-dialog">' +
                          '<div class="scene-preview">' +

                          '</div>' +
                          '</div>';

            $(message).dialog({
                title: $.i18n.prop('d_tabs_preview'),
                onBeforeShow: function () {
                    //创建表格结构
                    var tableData = _importOnBeforeShow(data["previewRows"]);
                    //表格中展示自场景方法
                    _tableFunction();
                    //创建表格
                    var ImportTables = new table.BdadpTable("importTableId",
                                                            tableData["tableProps"],
                                                            tableData["head"], tableData["dataArr"],
                                                            true);
                    var tableHtml = ImportTables.creatTable();
                    $(".importScenario-dialog .scene-preview").append(tableHtml);
                    var $table = $('#importTableId');
                    //添加数据
                    $table.bootstrapTable({
                        data: tableData["dataArr"]
                    });
                    //隐藏表格userId列
                    $table.bootstrapTable('hideColumn', 'isChildScenario');
                    $table.bootstrapTable('hideColumn', 'scenarioId');

                    var scenarios = $("#importTableId").bootstrapTable("getData");
                    while (true) {
                        //隐藏子场景
                        for (var i = 0; i < scenarios.length; i++) {
                            if (scenarios[i]["isChildScenario"] != "false") {
                                $("#importTableId").bootstrapTable('hideRow', {index: i})
                            }
                        }
                        break;
                    }
                    $table.bootstrapTable("checkAll");

                    $('#importTableId').parents(".bootstrap-table").find("table").css({
                        "width": "95%",
                        "margin-left": "20px"
                    });
                    $('#importTableId').parents(".bootstrap-table").find("search").css({"right": "3%"});

                    $(".xdsoft_dialog").addClass("userDialog");
                    $(".userDialog").addClass("no-border");
                    $(".userDialog").find(".userTab ").css("width", "798px");
                    $(".userDialog").find(".xdsoft_dialog_popup_title").addClass("user-dialog-title");
                    $(".userDialog").find(".xdsoft_dialog_buttons").addClass("user-dialog-btns");

                    $('.xdsoft_dialog_buttons button.xdsoft_btn:first-child').text($.i18n.prop('d_confirm'));
                    $('.xdsoft_dialog_buttons button.xdsoft_btn:last-child').text($.i18n.prop('d_cancel'));

                },
                onAfterShow: function () {

                },
                buttons: {
                    "s": function () {
                        var scenarioIds = _getSelections($('#importTableId')[0]);
                        //alert(JSON.stringify(scenarioIds));
                        var scenarioIdslength = scenarioIds.length;
                        var sceneInportantOblect = {};
                        sceneInportantOblect.action = "import";
                        sceneInportantOblect.resType = "zip";
                        sceneInportantOblect.name = previewObject.name;
                        sceneInportantOblect.userId = sessionStorage.getItem("userName");
                        sceneInportantOblect.cate_id = previewObject.cate_id;
                        sceneInportantOblect.scenario_id = scenarioIds;

                        settings.HttpClient("POST", sceneInportantOblect,
                                            "/service/v1/scenario/action", function (data) {
                                var exportCounts = 0;
                                if ($("#exportNum").text() == 0) {
                                    exportCounts++;
                                    $("#exportNum").text(exportCounts);
                                    $("#exportNum").css("display", "block")
                                } else {
                                    var exportNum = parseInt($("#exportNum").text()) + 1;
                                    $("#exportNum").text(exportNum);

                                }
                                $(".dropdown-toggle .fa-bell-o").trigger("click");
                                var scene_export = $("#header-reminder").find(".reminder-scenario");
                                var sceneimporthtml = '<li class="header-reminder" id=' + data
                                                      + '><i id="export-wrap">' +
                                                      '<div class="export-wrap" style="padding:10px 0;">'
                                                      +
                                                      '<span class="statwidth"><i class="reminder_import_scenario"></i><span>'
                                                      + sceneInportantOblect.name
                                                      + '</span><i class="span_reminder_num"></i></span>'
                                                      +
                                                      '<span class="process-num"></span>' +
                                                      '</div>' +
                                                      '<div class="progress progress-tiao">' +
                                                      '<div class="progress-bar progress-bar-warning progress-bar-striped" role="progressbar" aria-valuenow="80" aria-valuemin="0" aria-valuemax="100" style="width: 0;">'
                                                      +
                                                      '</div>' +
                                                      '</div>' +
                                                      '<div class="reminder-btn">' +
                                                      '<span class="statText"><i class="reminder_import_ing"></i></span>'
                                                      +
                                                      '<button type="button" class="btn btn-primary reminder-cancel btnCancel" style="margin-left: 24%;"><span class="span_reminder_cancel"></span></button>'
                                                      +
                                                      //'<button type="button" class="btn
                                                      // btn-primary reminder-download
                                                      // exportDownload">取消 ' + '</button>' +
                                                      '</div>' +
                                                      '</i></li>';

                                scene_export.prepend(sceneimporthtml);

                                settings.InterNation(['.reminder_import_scenario',
                                                      '.span_reminder_cancel', '.span_reminder_num',
                                                      '.reminder_import_ing'],
                                    ['d_reminder_scenarioImport', 'd_reminder_cancel',
                                     'span_reminder_num', 'd_reminder_beingImport']);

                            });

                        // return false;
                    },
                    "c": function () {
                        settings.HttpClient("DELETE", null, "/service/v1/resources/scenario/"
                                                            + previewObject.cate_id + "/zip?name="
                                                            + previewObject.name, function (data) {

                        });

                    }

                }
            });


    };

        var _importOnBeforeShow = function (data) {
            var head = {}, dataArr = [],
                tableProps = {
                    "data-pagination": "false",
                    "data-classes": "table table-no-bordered table-hover",
                    "data-toolbar": "#toolbar",
                    "data-search": "true",
                    "data-height": "300",
                    "data-row-style": "childStyle"
                };
            head = {
                "dataField": ["isChildScenario", "scenarioId", "scenarioName", "createUser",
                              "createTime", "scenarioDesc", "operate"],
                "headText": ["", "", $.i18n.prop('d_tabs_userName'), $.i18n.prop('d_tabs_userRole'),
                             $.i18n.prop('d_tabs_creationTime'), $.i18n.prop('d_tabs_descripion'),
                             ""],
                //"headText": ["子场景", "场景Id", "场景名称", "创建人员", "创建时间", "场景描述", " "],
                "events": [{}, {}, {"data-formatter": "nameFormatter"},
                    {"data-formatter": "userFormatter"}, {"data-formatter": "timeFormatter"},
                    {"data-formatter": "descFormatter"}, {
                        "data-formatter": "operateFormatter",
                        "data-events": "operateEvents"
                    }]
            };
            for (var i = 0; i < data.length; i++) {
                dataArr.push({
                    "isChildScenario": "false",
                    "scenarioId": data[i]["scenarioId"],
                    "scenarioName": data[i]["scenarioName"],
                    "createUser": data[i]["createUser"],
                    "createTime": data[i]["createTime"],
                    "scenarioDesc": data[i]["scenarioDesc"]
                });
                if (data[i]["scenarioDeps"].length > 0) {
                    //标志子场景
                    for (var j = 0; j < data[i]["scenarioDeps"].length; j++) {
                        dataArr.push({
                            "isChildScenario": data[i]["scenarioId"],
                            "scenarioId": data[i]["scenarioDeps"][j]["scenarioId"],
                            "scenarioName": data[i]["scenarioDeps"][j]["scenarioName"],
                            "createUser": data[i]["scenarioDeps"][j]["createUser"],
                            "createTime": data[i]["scenarioDeps"][j]["createTime"],
                            "scenarioDesc": data[i]["scenarioDeps"][j]["scenarioDesc"]
                        })
                    }
            }

        }

            return {head: head, dataArr: dataArr, tableProps: tableProps};
    };

        var _tableFunction = function () {
            window.operateFormatter = function (value, row, index) { //加操作列
                if (row.isChildScenario == "false") {
                    return [
                        '<a class="list" href="javascript:void(0)" title="list">',
                        '<i class="ion-chevron-down" style="color:#000"></i>',
                        '</a>'
                    ].join('');
                }
                return "";
        };
            window.operateEvents = { //添加操作事件
                'click .list': function (e, value, row, index) {
                    //收起方法
                    var arrowObj = $(this).find("i")[0];
                    var scenarioId = row.scenarioId;
                    var scenarios = $("#importTableId").bootstrapTable("getData");
                    if (!settings.containClass(arrowObj, "ion-chevron-down")) {
                        $(arrowObj).addClass("ion-chevron-down");
                        $(arrowObj).removeClass("ion-chevron-up");
                        while (true) {
                            //隐藏子场景
                            for (var i = 0; i < scenarios.length; i++) {
                                if (scenarios[i]["isChildScenario"] == scenarioId) {
                                    $("#importTableId").bootstrapTable('hideRow', {index: i})
                                }
                            }
                            break;
                        }
                    } else {
                        $(arrowObj).addClass("ion-chevron-up");
                        $(arrowObj).removeClass("ion-chevron-down");
                        while (true) {
                            //隐藏子场景
                            for (var i = 0; i < scenarios.length; i++) {
                                if (scenarios[i]["isChildScenario"] == scenarioId) {
                                    $("#importTableId").bootstrapTable('showRow', {index: i})
                                }
                            }
                            break;
                        }
                    }

                }
        };
            window.nameFormatter = function (value, row, index) { //加操作列
                var nameSpace = "";
                if (row.isChildScenario != "false") {
                    nameSpace += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                }
                return "<div>" + nameSpace + value + "</div>";
            };
            window.userFormatter = function (value, row, index) { //加操作列
                var nameSpace = "";
                if (row.isChildScenario != "false") {
                    nameSpace += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                }
                return "<div>" + nameSpace + value + "</div>";
            };
            window.timeFormatter = function (value, row, index) { //加操作列
                var nameSpace = "";
                if (row.isChildScenario != "false") {
                    nameSpace += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                }
                return "<div>" + nameSpace + value + "</div>";
            };
            window.descFormatter = function (value, row, index) { //加操作列
                var nameSpace = "";
                if (row.isChildScenario != "false") {
                    nameSpace += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                }
                return "<div style='width:155px;'>" + nameSpace + value + "</div>";
            };
            window.checkFormatter = function (value, row, index) { //加操作列
                if (row.isChildScenario != "false") {
                    return {
                        disabled: true
                    };
                }
                return value;
            };
            window.childStyle = function (row, index) { //加操作列
                if (row.isChildScenario != "false") {
                    return {
                        classes: "active"
                    };
                }
                return {};
            };
    };

        var _getSelections = function (table) {
            var selectedScenario = $(table).bootstrapTable('getSelections'), selectedScenarioId = [];
            if (selectedScenario.length)
                for (var i = 0; i < selectedScenario.length; i++) {
                    selectedScenarioId.push(selectedScenario[i]["scenarioId"]);
                }
            return selectedScenarioId;
        };

        return sceneImportMethod;

});


