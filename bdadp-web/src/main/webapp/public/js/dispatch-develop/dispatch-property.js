/**
 * Created by labo on 2016/9/19.
 */
define(["js/scene-develop/scene-common", 'js/modeldialog', 'js/dispatch-develop/dispatch-common'],
    function (sceneCommon, initDialog, commonDispatch) {
        settings.interFun();

        var getNameAndDes = function (self) {
        var scenarioId = $(self).parents(".element-item").attr("name");
        $("#seciorIdText").val(scenarioId);
            settings.HttpClient("GET", null, "/service/v1/scenario/?scenario_id=" + scenarioId,
                                function (response) {
                                    var scenarioName = response[0]["scenario_name"];
                                    var scenarioDescribe = response[0]["scenario_desc"];
                                    var creator = response[0]["create_user"];
                                    $(".dispatch_right label[name='sceneNameLabel']").text(sceneCommon.getStrLength(scenarioName,
                                                                                                                    15)).attr("title",
                                                                                                                              scenarioName);
                                    $("#sceneNameInputtestarea").text(scenarioDescribe);
                                    $(".creatorWrap .creator").text(creator);
                                });
            settings.HttpClient("GET", null,
                                "/service/v1/schedule/ScheduleInfo?scenarioId=" + scenarioId
                                + "&jobGroup=" + scenarioId, function (response) {
                    if (response) {
                switch (response.triggerType) {
                    case "SimpleTrigger":
                        commonDispatch.clearInputVal(['#cornInputText']);
                        $("#advance").text($.i18n.prop('d_disp_btn_advance'));
                        commonDispatch.showHideBatch(['.right-form .actionWrap',
                                                      '.right-form .overtimeWrap',
                                                      '.right-form .starttimeWrap'], "block");
                        commonDispatch.showHideBatch(['.right-form .cornWrap'], "none");
                        $("#startTimeinputText").val(response.startTimeStr);
                        $("#overTimeinputText").val(response.endTimeStr);
                        $("#ExecutionCycleinputText").val(response.repeatInterval);
                        if (response.executionFrequencyUnit == "hour"
                            || response.executionFrequencyUnit == "day") {
                            commonDispatch.showHideBatch(['.right-form .actionweekWrap',
                                                          '.right-form .actionmonthWrap'], "none");
                            commonDispatch.showHideBatch(['.right-form .actiontimeWrap'], "block");
                        } else if (response.executionFrequencyUnit == "week") {
                            commonDispatch.showHideBatch(['.right-form .actionweekWrap',
                                                          '.right-form .actiontimeWrap'], "block");
                            commonDispatch.showHideBatch(['.right-form .actionmonthWrap'], "none");
                            $("#weeksWrap").val(commonDispatch.changeNum(response.executionDay));

                        } else if (response.executionFrequencyUnit == "month") {
                            commonDispatch.showHideBatch(['.right-form .actiontimeWrap',
                                                          '.right-form .actionmonthWrap'], "block");
                            commonDispatch.showHideBatch(['.right-form .actionweekWrap'], "none");

                        }
                        $("#ExecutionTime").val(response.executionFrequencyUnit);
                        $("#timeWrap").val(response.executionTime);
                        $("#monthWrap").val(response.executionDay);

                        break;
                    case "CronTrigger":
                        commonDispatch.clearInputVal(['#ExecutionCycleinputText',
                                                      '#startTimeinputText', '#overTimeinputText']);
                        $("#cornInputText").val(response.cronExpression);
                        commonDispatch.showHideBatch(['.right-form .actionWrap',
                                                      '.right-form .overtimeWrap',
                                                      '.right-form .starttimeWrap',
                                                      '.right-form .actionmonthWrap',
                                                      '.right-form .actionweekWrap',
                                                      '.right-form .actiontimeWrap'], "none");
                        commonDispatch.showHideBatch(['.right-form .cornWrap'], "block");
                        $("#advance").text($.i18n.prop('d_disp_btn_return'));
                        break;
                }

                    } else {
                $("#I_btn_disp_reset").trigger("click");
                    }
                });
        };

        var saveScenarioProperty = function () {
        var $actionWrap = $(".right-form").find(".actionWrap");
        if ($actionWrap.css("display") == "block") {
            var dispatchInformationObj = {};
            var seciorIdText = $("#seciorIdText").val();
            var startTimeInput = $("#startTimeinputText").val() + " " + "00:00:00";
            var endTimeInput = $("#overTimeinputText").val() + " " + "23:59:59";
            startTimeInput=startTimeInput.replace(/-/g,"/");
            endTimeInput=endTimeInput.replace(/-/g,"/");
            var startTimeDate = new Date(startTimeInput);
            var endTimeDate = new Date(endTimeInput);
            var startTimeZ = startTimeDate.toJSON();
            var endTimeZ = endTimeDate.toJSON();
            var actionCicle = $("#ExecutionCycleinputText").val();
            var actionCicleUnit = $("#ExecutionTime").val();
            var exetimehour = $("#timeWrap").val();
            var exehour = $("#timeWrap").val();
            var exetimeweek = $("#weeksWrap").val();
            var exetimemonth = $("#monthWrap").val();
            dispatchInformationObj.userId = sessionStorage.getItem("userId");
            dispatchInformationObj.scenarioId = seciorIdText;
            dispatchInformationObj.jobGroup = seciorIdText;
            dispatchInformationObj.triggerType = "SimpleTrigger";
            dispatchInformationObj.repeatInterval = actionCicle;
            dispatchInformationObj.executionFrequencyUnit = actionCicleUnit;
            dispatchInformationObj.startTime = startTimeZ;
            dispatchInformationObj.endTime = endTimeZ;
            switch (actionCicleUnit) {
                case "hour":
                {
                    dispatchInformationObj.executionTime = exehour;
                    break;
                }

                case "day":
                {
                    dispatchInformationObj.executionTime = exehour;
                    break;
                }

                case "week":
                {
                    dispatchInformationObj.executionDay = changeweektonum(exetimeweek);
                    dispatchInformationObj.executionTime = exehour;
                    break;
                }

                case "month":
                {
                    dispatchInformationObj.executionDay = exetimemonth;
                    dispatchInformationObj.executionTime = exehour;
                    break;
                }

            }

            settings.HttpClient("POST", dispatchInformationObj, "/service/v1/schedule/add",
                                function (response) {
                                    initDialog().initAlert($.i18n.prop('d_disp_dialog_saveSuccess'),
                                                           $.i18n.prop('d_disp_dialog_prompt'))
                                }, function () {
                //initDialog().initAlert($.i18n.prop('d_disp_dialog_saveFail'),
                // $.i18n.prop('d_disp_dialog_prompt'))
                });
        } else {
            var dispatchInformationObj = {};
            var seciorIdText = $("#seciorIdText").val();
            var cornInput = $("#cornInputText").val();
            dispatchInformationObj.scenarioId = seciorIdText;
            dispatchInformationObj.jobGroup = seciorIdText;
            dispatchInformationObj.triggerType = "CronTrigger";
            dispatchInformationObj.cronExpression = cornInput;
            dispatchInformationObj.userId = sessionStorage.getItem("userId");

            settings.HttpClient("POST", dispatchInformationObj, "/service/v1/schedule/add",
                                function (response) {
                                    initDialog().initAlert($.i18n.prop('d_disp_dialog_saveSuccess'),
                                                           $.i18n.prop('d_disp_dialog_prompt'))
                                }, function () {

                    initDialog().initAlert($.i18n.prop('d_disp_dialog_saveFail'),
                                           $.i18n.prop('d_disp_dialog_prompt'))

                })
        }
        };

        function changeweektonum(weekstr) {
        var numstr = "";
        var weekArr = weekstr.split(",");
        var weekArrlen = weekArr.length;
        for (var i = 0; i < weekArrlen; i++) {
            if (weekArr[i] == $.i18n.prop('d_disp_week_Sunday')) {
                weekArr[i] = 1
            } else if (weekArr[i] == $.i18n.prop('d_disp_week_Monday')) {
                weekArr[i] = 2
            } else if (weekArr[i] == $.i18n.prop('d_disp_week_Tuesday')) {
                weekArr[i] = 3
            } else if (weekArr[i] == $.i18n.prop('d_disp_week_Wednesday')) {
                weekArr[i] = 4
            } else if (weekArr[i] == $.i18n.prop('d_disp_week_Thursday')) {
                weekArr[i] = 5
            } else if (weekArr[i] == $.i18n.prop('d_disp_week_Friday')) {
                weekArr[i] = 6
            } else if (weekArr[i] == $.i18n.prop('d_disp_week_Saturday')) {
                weekArr[i] = 7
            }

        }

        numstr = weekArr.join(",");
        return numstr;

        }

        return {
        getProperty: {
            getNameAndDes: getNameAndDes,
            getAllClusters: saveScenarioProperty
        }
        }
    });
