/**
 * Created by maqingzhe on 2016/9/8 0008.
 */

define(['js/scene-develop/scene-common', 'js/scene-monitor/monitor-graph', 'js/modeldialog'],
    function (SceneCommon, Mgraph, dialog) {
        'use strict';
        function Scenario() {

        }

        var _create = function (executionId, sceneName, row) {
            var tabsName = sceneName + "_" + executionId + "_" + row.taskId;
            var tabId = executionId + row.taskId
        var $shell = $('.chrome-tabs-shell'), graph;
        var $currentTab = $shell.find('.chrome-tab-current');
            var sceneEdit = $shell.find('.chrome-tab-title').filter(function (index, dom) {
                return $(dom).html() == tabsName
            })
        if (sceneEdit.length > 0) {
            var $tab = $shell.find('.chrome-tab').filter(function (index, dom) {
                return $(dom).data('tabData').data['sceneName'] == tabsName;
            })
            chromeTabs.setCurrentTab($shell, $tab);
        } else {
            chromeTabs.addNewTab($shell, {
                // favicon: '',
                title: tabsName,
                data: {
                    timeAdded: +new Date(),
                    sceneName: tabsName,
                    sceneId: tabId,
                }
            });
            settings.currentSceneTab.set(tabId, tabsName);  // store edit scene count

            var _html = '<section id="' + tabId + '" class="content monitor-detail"</section>';
          if ($('#' + tabId).length == 0) {
            $('div.content-wrapper').append(_html);
          }

        }
        _loadDetailHtml(tabId, row);
        }

        var _loadDetailHtml = function (_id, row) {
            $('#' + _id).load("./html/scene-monitor/monitor-detail.html", function () {
                _monitorDetailInit(_id, row);

                $("#" + _id
                  + ".monitor-detail .monitor-right-content .diagram-content .refresh").click(function () {
                    new Mgraph().init(row);
                })

                SceneCommon.setLayoutLeft([$("#" + _id
                                             + ".monitor-detail .monitor-left-content")[0],
                                           $("#" + _id
                                             + ".monitor-detail .monitor-right-content")[0]], 162);
                SceneCommon.setLayoutLeft([$("#" + _id
                                             + ".monitor-detail .monitor-right-content .log-content")[0],
                                           $("#" + _id
                                             + ".monitor-detail .monitor-right-content .graph-content")[0]],
                    242);

                //new Mgraph().init('asd', 'ad'); //scenario, excution
            });
        }

        var _monitorDetailInit = function (_id, row) {

        _taskMonitorHistory(_id, row);

            settings.HttpClient("GET", null,
                                "/service/v1/schedule/tasks?executionId=" + row.executionId
                                + "&taskId=" + row.taskId, function (response) {
                    _showScenarioMess(_id, row, response);
                    new Mgraph().init(row);
                })

        }

        var _hrefEval = function (_id) {
            var name = "";
            var names = $("#" + _id + " a[data-toggle='tab']");
            for (var i = 0; i < names.length; i++) {
                name = $(names[i]).attr("name");
                $("#" + _id + " a[name='" + name + "']").attr("href", "#" + _id + " ." + name);
        }
        }

        var _taskMonitorHistory = function (_id, scenarioRow) {
        //任务详情查询参数
            var param = "?executionId=" + scenarioRow.executionId + "&scenarioId="
                        + scenarioRow.scenarioId;
        //任务状态HTML
            window.statusFun = function (value, row, index) {
                var progressVal = row.status;
                var statusStr = "RUNNING"
                var backgroundColor = "#f5b75a";
                if (progressVal == "FAILURE") {
                    backgroundColor = "#eb432d";
                    statusStr = "FAILURE"
                } else if (progressVal == "SUCCESS") {
                    backgroundColor = "#d0e68f";
                    statusStr = "SUCCESS"
                } else if (progressVal == "READY") {
                    backgroundColor = "#B4B4B4";
                    statusStr = "READY"
                }

                return '<div class="status" style="width:80%; margin-bottom: 0px;margin-left: 10%;">'
                       +
                       '<div style="text-align:center;color:black;background-color: '
                       + backgroundColor + '">' +
                       '<span style="color:#fff;font-size:12px;">' + statusStr + '</span>' +
                       '</div>' +
                       '</div>';
        };

        window.taskFormatter = function (value, row, index) { //加操作列
            return [
                '<a class="edit" href="javascript:void(0)" title="logs">',
                '<i class="ion-clipboard" style="color:#000"></i>',
                '</a>'
            ].join('');
        };

        window.taskEvents = { //添加操作事件
            'click .edit': function (e, value, row, index) {
                settings.HttpClient("GET", null, "/service/v1/scenario/" + scenarioRow.scenarioId
                                                 + "/execute?executionId=" + scenarioRow.executionId
                                                 + "&taskId=" + row.taskId, function (response) {
                    var message = '<div class="container-fuild task-dialog">' +
                                  '<textarea class="form-control taskLog " name="userDesc" id="userDes" placeholder="" disabled style="background: transparent;">'

                    for (var i = 0; i < response.length; i++) {
                        message += response[i] + "\n";
                    }
                    message += '</textarea></div>';
                    $(message).dialog({
                        title: "" + row.taskName,
                        onBeforeShow: function () {
                            $(".xdsoft_dialog").css("width", "60%")
                            $(".xdsoft_dialog .taskLog").css("height", "300px")
                            $(".xdsoft_dialog .xdsoft_dialog_buttons button.xdsoft_btn").text($.i18n.prop('d_confirm'));
                        },
                        buttons: {
                            " ": function () {

                            }
                        }
                    });
                })
            }
        };

        //添加表格
            $("#" + _id).find("table").empty();
            $("#" + _id).find("table").bootstrapTable('hideLoading');
            var url = settings.globalVariableUrl()+'/service/v1/schedule/pagetasks';
            $("#" + _id
              + " .monitor-right-content .graph-content").load("./html/scene-monitor/scenarioDetail.html",
                                                               function () {
                                                                   $("#"
                                                                     + _id).find("table").bootstrapTable('refresh',
                                                                       {
                                                                           url: url
                                                                                + param
                                                                       });
                                                                   var language = settings.testLangType();
                                                                   if (language.substring(0, 2)
                                                                       == "zh") {
                                                                       $("#"
                                                                         + _id).find("table").bootstrapTable("changeLocale",
                                                                                                             "zh-TW");
                                                                       $("#"
                                                                         + _id).find("table").bootstrapTable("changeTitle",
                                                                           {
                                                                               taskName: "任务名称",
                                                                               taskId: "任务Id",
                                                                               commitTime: "开始时间",
                                                                               status: "状态",
                                                                               runTime: "运行时间",
                                                                               taskLog: "任务日志"
                                                                           });
                                                                   } else if (language.substring(0,
                                                                                                 2)
                                                                              == "en") {
                                                                       $("#"
                                                                         + _id).find("table").bootstrapTable("changeLocale",
                                                                                                             "en_US");
                                                                       $("#"
                                                                         + _id).find("table").bootstrapTable("changeTitle",
                                                                           {
                                                                               taskName: "Task Name",
                                                                               taskId: "Task Id",
                                                                               commitTime: "Commit Time",
                                                                               status: "Status",
                                                                               runTime: "Run Time",
                                                                               taskLog: "Task Log"
                                                                           });
                                                                   }
                                                               });
        }

        var _showScenarioMess = function (_id, row, scenarioMess) {
            var onlineTime, offlineTime, createTime;

        //中英文翻译
            settings.InterNation(['.d_detail_pic', '.d_detail_digest', '.d_detail_describe',
                                  '.d_detail_userName', '.d_detail_status', '.d_detail_progress',
                                  '.d_detail_id', '.d_detail_graph', '.d_detail_detail',
                                  '.d_detail_journal', '.d_detail_creatTime', '.d_detail_fixTime',
                                  '.d_detail_onlineTime', '.d_detail_startTime',
                                  '.d_detail_endTime'],
                ['d_detail_pic', 'd_detail_digest', 'd_detail_describe', 'd_detail_userName',
                 'd_detail_status', 'd_detail_progress', 'd_detail_id', 'd_detail_graph',
                 'd_detail_detail', 'd_detail_journal', 'd_detail_creatTime', 'd_detail_fixTime',
                 'd_detail_onlineTime', 'd_detail_startTime', 'd_detail_endTime']);

            $("#" + _id + " .monitor-left .monitor-left-header").find("span")[0].innerHTML =
                row.scenarioName;

            var spans = $("#" + _id + " .monitor-left-content .tab-content").find("span");
            for (var i = 0; i < spans.length; i++) {
                switch ($(spans[i]).attr("class")) {
                    case "detail-creator":
                    {
                        $(spans[i]).append(row.userName);
                        break;
                    }
                    case "detail-status":
                    {
                        var progressVal = row.status;
                        var statusStr = "RUNNING"
                        var backgroundColor = "#f5b75a";
                        if (progressVal == "FAILURE") {
                            backgroundColor = "#eb432d";
                            statusStr = "FAILURE"
                        } else if (progressVal == "SUCCESS") {
                            backgroundColor = "#d0e68f";
                            statusStr = "SUCCESS"
                        }
                        $(spans[i]).append('<div class="status" style="width:80%; margin-bottom: 0px;margin-left: 10%;">'
                                           +
                                           '<div style="text-align:center;color:black;background-color: '
                                           + backgroundColor + '">' +
                                           '<span style="color:#fff;font-size:12px;">' + statusStr
                                           + '</span>' +
                                           '</div>' +
                                           '</div>');
                        break;
                    }
                    case "detail-progress":
                    {
                        var status = row.status;
                        var striped = "progress-striped active";
                        var progressVal = row.progress;
                        var backgroundColor = "#f5b75a";
                        if (status == "FAILURE") {
                            backgroundColor = "#eb432d";
                            progressVal = 1;
                            var striped = "";
                        } else if (status == "SUCCESS") {
                            backgroundColor = "#d0e68f";
                            progressVal = 1;
                            var striped = "";
                        }
                        $(spans[i]).append('<div class="progress ' + striped
                                           + '" style="width:80%; margin-bottom: 0px;margin-left: 10%;">'
                                           +
                                           '<div class="progress-bar" role="progressbar" aria-valuenow="'
                                           + progressVal * 100
                                           + '" aria-valuemin="0" aria-valuemax="1" style="color:#fff;font-size:12px; width:'
                                           + progressVal * 100 + '%; background-color: '
                                           + backgroundColor + '">' +
                                           +Math.round(progressVal * 10000) / 100 + '%' +
                                           '</div>' +
                                           '</div>');
                        break;
                    }
                    case "detail-ID":
                    {
                        $(spans[i]).attr("title", row.scenarioId);
                        $(spans[i]).append(row.scenarioId);
                        break;
                    }
            }
        }

            settings.HttpClient("GET", null, "/service/v1/scenario?scenario_id=" + row.scenarioId,
                                function (response) {
                                    $("#" + _id
                                      + " .monitor-left-content .tab-content").find("textarea")[0].innerHTML =
                                        response[0].scenario_desc;
                                    var detailSpans = $("#" + _id
                                                        + " .monitor-right-content .detail").find("span");
                                    for (var i = 0; i < detailSpans.length; i++) {
                                        switch ($(detailSpans[i]).attr("class")) {
                                            case "detail-creatTime":
                                            {
                                                $(detailSpans[i]).append(response[0].create_time);
                                                break;
                                            }
                    //case "detail-modifyTime":
                    //{
                    //    $(detailSpans[i]).append(createTime);
                    //    break;
                    //}
                                            case "detail-onlineTime":
                                            {
                                                $(detailSpans[i]).append(response[0].online_time);
                                                break;
                    }
                                            case "detail-startTime":
                                            {
                                                $(detailSpans[i]).append(settings.transformMilliseconds(scenarioMess["startTime"]));
                                                break;
                                            }
                                            case "detail-endTime":
                                            {
                                                if (row["endTime"] == null) {
                                                    $(detailSpans[i]).append(settings.transformMilliseconds(new Date().getTime()));
                                                } else {
                                                    $(detailSpans[i]).append(settings.transformMilliseconds(row["endTime"]));
                                                }
                                                break;
                                            }
                }
                                    }
                                })

            settings.HttpClient("GET", null,
                                "/service/v1/scenario/" + row.scenarioId + "/execute?executionId="
                                + row.executionId + "&taskId=" + row.taskId, function (response) {
                    $("#" + _id + " .monitor-right-content .log").find("textarea").append("");
                    for (var i = 0; i < response.length; i++) {
                        $("#" + _id
                          + " .monitor-right-content .log").find("textarea").append(response[i]
                                                                                    + "\n")
                    }
                })

        _hrefEval(_id);
        }

        return {
            monitorDetail: _create
        }
    })
