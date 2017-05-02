/**
 * Created by maqingzhe on 2016/9/8 0008.
 */

define(["component/BdaTable", 'js/scene-monitor/monitor-detail', 'js/scene-develop/scene-common'],
    function (table, monitorDetail, SceneCommon) {
        'use strict';
        var ScenarioMonitor = function () {
            this.status = "";
        };

        ScenarioMonitor.prototype.showScenario = function (scenarioMonitor) {
            var dates = $(".scene-monitor-header .date-selete select").val();
            _showScenario($(".scene-monitor-content .all-monitor .tab-pane"), null,
                          "monitorTableId", dates);
            scenarioMonitor.status = "0";
        }

        ScenarioMonitor.prototype.clickScenarioTitle = function (scenarioMonitor) {
        //change date show scene-monitor
            $(".scene-monitor-header .date-selete select").change(function () {
                var dates = $(this).val();
                switch (scenarioMonitor.status) {
                    case "0":
                    {
                        _showScenario($(".scene-monitor-content .all-monitor .tab-pane"), null,
                                      "monitorTableId", dates);
                        break;
                    }
                    case "1":
                    {
                        _showScenario($(".scene-monitor-content .all-monitor .tab-pane"), '1',
                                      "monitorTableId", dates);
                        break;
                    }
                    case "2":
                    {
                        _showScenario($(".scene-monitor-content .all-monitor .tab-pane"), '2',
                                      "monitorTableId", dates);
                        break;
                    }
                    case "3":
                    {
                        _showScenario($(".scene-monitor-content .all-monitor .tab-pane"), '3',
                                      "monitorTableId", dates);
                        break;
                    }
            }

        })

            $(".scene-monitor-header .scene-monitor-status").click(function () {
                var status = $(this).attr("name");
                var dates = $(".scene-monitor-header .date-selete select").val();
                switch (status) {
                    case "run":
                    {
                        _showScenario($(".scene-monitor-content .all-monitor .tab-pane"), "1",
                                      "monitorTableId", dates);
                        scenarioMonitor.status = "1";

                        break;
                    }
                    case "succ":
                    {
                        _showScenario($(".scene-monitor-content .all-monitor .tab-pane"), "2",
                                      "monitorTableId", dates);
                        scenarioMonitor.status = "2";
                        break;
                    }
                    case "fail":
                    {
                        _showScenario($(".scene-monitor-content .all-monitor .tab-pane"), "3",
                                      "monitorTableId", dates);
                        scenarioMonitor.status = "3";
                        break;
                    }
            }
        })

            $(".scene-monitor-content .tab-content-title").find("span").click(function () {
                var spanName = $(this).attr("name");
                switch (spanName) {
                    case 'arrow':
                    {
                        _scenarioList(this);
                        break;

                }
                }
            })
        }

        ScenarioMonitor.prototype.searchScenario = function () {

        $(".scene-monitor .monitor-search").keyup(function (e) {
        })
        }

        var _showScenario = function ($tableContent, flag, monitorTableId, dates) {
        window.operateFormatter = function (value, row, index) { //加操作列
            return [
                '<a class="edit" href="javascript:void(0)" title="detail">',
                '<i class="glyphicon glyphicon-list" style="color:#000"></i>',
                '</a>'
            ].join('');
        };
        window.operateEvents = { //添加操作事件
            'click .edit': function (e, value, row, index) {
                new monitorDetail.monitorDetail(row.executionId, row.scenarioName, row);
            }
        };

            window.progress = function (value, row, index) {
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
                return '<div class="progress ' + striped
                       + '" style=" margin-bottom: 0px;margin-top: 0px;">' +
                       '<div class="progress-bar" role="progressbar" aria-valuenow="' + progressVal
                                                                                        * 100
                       + '" aria-valuemin="0" aria-valuemax="1" style="color:#fff;font-size:12px; width:'
                       + progressVal * 100 + '%; background-color: ' + backgroundColor + '">' +
                       +Math.round(progressVal * 10000) / 100 + '%' +
                       '</div>' +
                       '</div>';
        }

            window.progress2 = function (value, row, index) {
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

                return '<div class="status" style="width:80%; margin-bottom: 0px;margin-left: 10%;">'
                       +
                       '<div style="text-align:center;color:black;background-color: '
                       + backgroundColor + '">' +
                       '<span style="color:#fff;font-size:12px;">' + statusStr + '</span>' +
                       '</div>' +
                       '</div>';
        }

        //创建表格
        //var monitorTables = new table.BdadpTable(monitorTableId, head, dataArr, true);
        //var tableHtml = monitorTables.creatTable();
            var param = "?dayNum=" + dates;
            if (flag) {
                param += "&jobStatus=" + flag;
        }
        $tableContent.empty();
        $tableContent.load("./html/scene-monitor/tableLoad.html", function () {
            var url = settings.globalVariableUrl()+'/service/v1/schedule/pagejobs';
            $tableContent.find("table tr.no-records-found").empty();
            $tableContent.find("table").bootstrapTable('refresh',
                {url: url + param});
            var language = settings.testLangType();
            if (language.substring(0, 2) == "zh") {
                $tableContent.find("table").bootstrapTable("changeLocale", "zh-TW");
                $tableContent.find("table").bootstrapTable("changeTitle", {
                    scenarioName: "场景名称",
                    scenarioId: "场景ID",
                    executionId: "运行ID",
                    taskId: "任务ID",
                    commitTime: "开始时间",
                    status: "状态",
                    progress: "进度",
                    userName: "创建者",
                    runTime: "运行时间",
                    detail: "详情",
                });
            } else if (language.substring(0, 2) == "en") {
                $tableContent.find("table").bootstrapTable("changeLocale", "en_US");
                $tableContent.find("table").bootstrapTable("changeTitle", {
                    scenarioName: "Scenario Name",
                    scenarioId: "Scenario ID",
                    executionId: "Execution ID",
                    taskId: "Task ID",
                    commitTime: "Commit Time",
                    status: "Status",
                    progress: "Progress",
                    userName: "User Name",
                    runTime: "Run Time",
                    detail: "Detail",
                });
            }
            //进度条长度
            $("table th[data-field=progress]").css("width", "20%")
        });
        };

        var _scenarioList = function (arrowObj) {
        if (!settings.containClass(arrowObj, "ion-chevron-down")) {
            $(arrowObj).addClass("ion-chevron-down");
            $(arrowObj).removeClass("ion-chevron-up");
            $(arrowObj).parent().parent().find(".tab-content-body").css("display", "none");  //收起
        } else {
            $(arrowObj).addClass("ion-chevron-up");
            $(arrowObj).removeClass("ion-chevron-down");
            $(arrowObj).parent().parent().find(".tab-content-body").css("display", "block");//列出
        }
        };

        var _monitorInit = function () {

        var scenarioMonitor = new ScenarioMonitor();
        scenarioMonitor.showScenario(scenarioMonitor);
        scenarioMonitor.clickScenarioTitle(scenarioMonitor);
        scenarioMonitor.searchScenario();

        SceneCommon.setLayoutLeft([$('.scene-monitor-content')[0]], 172);

            settings.InterNation(['.d_monitor_seven', '.d_monitor_fifth', '.d_monitor_oneMonth',
                                  '.d_monitor_halfAYear', '.d_all_monitor', '.d_monitor_operation',
                                  '.d_monitor_success', '.d_monitor_fail'],
                ['d_monitor_seven', 'd_monitor_fifth', 'd_monitor_oneMonth', 'd_monitor_halfAYear',
                 'd_all_monitor', 'd_monitor_operation', 'd_monitor_success', 'd_monitor_fail']);
        };

        return {
        monitorInit: _monitorInit
        }
    })








