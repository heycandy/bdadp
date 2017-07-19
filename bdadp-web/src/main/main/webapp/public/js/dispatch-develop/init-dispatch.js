/**
 * Created by Administrator on 2016/9/8.
 */
define(["js/scene-develop/scene-common", "js/dispatch-develop/dispatch-property", 'js/modeldialog',
        'js/dispatch-develop/dispatch-common'],
    function (SceneCommon, getProperty, initDialog, commonDispatch) {
        "use strict";
        settings.interFun();
        var _initDispatch = function () {
        };
        var dispatch = new _initDispatch();
        _initDispatch.prototype.initDispatchSence = function () {

            var _self = this, seceneAllHtml = "", scenePreHtml = "";
        $(".loading").css("display", "block");
        //var results = settings.HttpClient("GET", null, "/service/v1/scenario");
        // //HttpClient:
            settings.HttpClient("GET", null, "/service/v1/scenario", function (results) {
                if (results) {
                $.each(results, function (index, value) {
                    var sceneEach;
                    scenePreHtml +=
                        "<option value='" + value["scenario_name"] + "'>" + value["scenario_name"]
                        + "</option>";  //前置场景拼串
                    sceneEach = sceneEachPorduce(index, value, true);
                    seceneAllHtml += sceneEach;
                });
                }
                $(".loading").css("display", "none");
                $("#dispatch-isotope").append(seceneAllHtml);
                $("#inputSelsect2").empty().append(scenePreHtml);
                _isotopeInit();
                _self.mouseoverFun();          //mouseover events
                _dispatchValidate("dispatchFormId");
                //默认选中第一个
                $("#dispatch-isotope .dispatch-scene-cover-black:first").click();
                //窗口放大缩小时获取高度
                $(window).resize(function () {
                    SceneCommon.setLayoutLeft([$(".dispatch_content .sence-content-left .dispatch-isotope")[0]],
                        185);
                    SceneCommon.setLayoutLeft([$(".dispatch_content .right_content")[0]], 145);
                });
                SceneCommon.setLayoutLeft([$(".dispatch_content .sence-content-left .dispatch-isotope")[0]],
                    185);
                SceneCommon.setLayoutLeft([$(".dispatch_content .right_content")[0]], 145);

            });            //HttpClient:
            // function


        //scene check
        $("#dispatch-isotope").unbind("click");

        $("#dispatch-isotope").on("click", ".scene-check", function () {
            var scenarioId = $(this).parents(".element-item").attr("name");
            settings.HttpClient("GET", null, "/service/v1/scenario/?scenario_id=" + scenarioId,
                                function (response) {
                console.log(response);
                _self.check(response);
                                });
        });

        //点击图片获取信息
        $("#dispatch-isotope").on("click", ".scene-common", function () {
            var self = this;
            getProperty.getProperty.getNameAndDes(self);
        });


        // 点击保存按钮保存用户输入的信息
        $("#I_btn_disp_save").click(function () {
            var startVal = $("#startTimeinputText").val();
            var endVal = $("#overTimeinputText").val();
            var startValDate = new Date(startVal);
            var startValDateScends = startValDate.getTime();
            var endValDate = new Date(endVal);
            var endValDateScends = endValDate.getTime();
            var bootstrapValidator = $("#dispatchFormId").data('bootstrapValidator');
            var $actionWrap = $(".right-form").find(".actionWrap");
            var $timeWrap = $("#timeWrap").val();
            bootstrapValidator.validate();
            var loginValid = bootstrapValidator.isValid();
            if ($actionWrap.css("display") == "block") {
                if (startVal == "") {
                    initDialog().initAlert($.i18n.prop('d_disp_dialog_noNull'),
                                           $.i18n.prop('d_disp_dialog_prompt'), 2000)

                } else if (endValDateScends < startValDateScends) {
                    initDialog().initAlert($.i18n.prop('d_disp_dialog_endNoLessStart'),
                                           $.i18n.prop('d_disp_dialog_prompt'), 2000)

                } else if ($("#ExecutionTime").val() == 4) {
                    initDialog().initAlert($.i18n.prop('d_disp_dialog_FrequencyUnits'),
                                           $.i18n.prop('d_disp_dialog_prompt'), 2000)
                }
                else if ($timeWrap == "") {
                    initDialog().initAlert($.i18n.prop('d_disp_dialog_actiontime'),
                                           $.i18n.prop('d_disp_dialog_prompt'), 2000)

                } else if (loginValid) {
                    getProperty.getProperty.getAllClusters();
                }

            } else {
                var cornVal = $("#cornInputText").val();
                if (cornVal == "") {
                    initDialog().initAlert($.i18n.prop('d_disp_dialog_cornexpresion'),
                                           $.i18n.prop('d_disp_dialog_prompt'), 2000)
                } else {
                    getProperty.getProperty.getAllClusters();
                }
            }
        });

        // 高级按钮
        var scenarioId;
        $("#advance").click(function () {
            scenarioId = $("#seciorIdText").val();
            var $actionWrap = $(".right-form").find(".actionWrap");
            if ($actionWrap.css("display") == "block") {
                settings.HttpClient("GET", null,
                                    "/service/v1/schedule/ScheduleInfo?scenarioId=" + scenarioId
                                    + "&jobGroup=" + scenarioId, function (response) {
                        if (response) {
                            switch (response.triggerType) {
                                case "SimpleTrigger":
                                    commonDispatch.clearInputVal(['#cornInputText']);
                                    break;
                                case "CronTrigger":
                                    commonDispatch.clearInputVal(['#startTimeinputText',
                                                                  '#overTimeinputText',
                                                                  '#ExecutionCycleinputText',
                                                                  '#timeWrap', '#weeksWrap',
                                                                  '#monthWrap']);
                                    $("#ExecutionTime").val("4");
                                    commonDispatch.showHideBatch(['.actiontimeWrap',
                                                                  '.actionmonthWrap',
                                                                  '.actionweekWrap'], "none");
                                    break;
                            }
                        } else {

                        }
                });

                $("#advance").text($.i18n.prop('d_disp_btn_return'));
                commonDispatch.showHideBatch(['.right-form .actionWrap ',
                                              '.right-form .overtimeWrap',
                                              '.right-form .starttimeWrap',
                                              '.right-form .actionmonthWrap',
                                              '.right-form .actionweekWrap',
                                              '.right-form .actiontimeWrap'], "none");
                commonDispatch.showHideBatch(['.right-form .cornWrap'], "block");

            } else {
                $("#advance").text($.i18n.prop('d_disp_btn_advance'));
                settings.HttpClient("GET", null,
                                    "/service/v1/schedule/ScheduleInfo?scenarioId=" + scenarioId
                                    + "&jobGroup=" + scenarioId, function (response) {
                        if (response) {
                            switch (response.triggerType) {
                                case "SimpleTrigger":
                                    commonDispatch.clearInputVal(['#cornInputText']);
                                    break;
                                case "CronTrigger":
                                    commonDispatch.clearInputVal(['#startTimeinputText',
                                                                  '#overTimeinputText',
                                                                  '#ExecutionCycleinputText',
                                                                  '#timeWrap', '#weeksWrap',
                                                                  '#monthWrap']);
                                    $("#ExecutionTime").val("4");
                                    commonDispatch.showHideBatch(['.actiontimeWrap',
                                                                  'actionmonthWrap',
                                                                  '.actionweekWrap'], "none");
                                    break;
                            }
                        } else {

                        }
                });

                var $ExecutionTime = $("#ExecutionTime").val();
                commonDispatch.showHideBatch(['.right-form .actionWrap',
                                              '.right-form .overtimeWrap',
                                              '.right-form .starttimeWrap'], "block");
                commonDispatch.showHideBatch(['.right-form .cornWrap'], "none");

                if ($ExecutionTime == "hour" || $ExecutionTime == "day") {
                    commonDispatch.showHideBatch(['.right-form .actionmonthWrap',
                                                  '.right-form .actionweekWrap'], "none");
                    commonDispatch.showHideBatch(['.right-form .actiontimeWrap'], "block");

                } else if ($ExecutionTime == "week") {
                    commonDispatch.showHideBatch(['.right-form .actionmonthWrap'], "none");
                    commonDispatch.showHideBatch(['.right-form .actionweekWrap',
                                                  '.right-form .actiontimeWrap'], "block");

                } else if ($ExecutionTime == "month") {
                    commonDispatch.showHideBatch(['.right-form .actionweekWrap'], "none");
                    commonDispatch.showHideBatch(['.right-form .actionmonthWrap',
                                                  '.right-form .actiontimeWrap'], "block");

                } else {
                    $("#ExecutionTime").val("4");
                }

            }

        });

        //  重置用户信息

        $("#I_btn_disp_reset").click(function () {
            scenarioId = $("#seciorIdText").val();
            settings.HttpClient("GET", null,
                                "/service/v1/schedule/ScheduleInfo?scenarioId=" + scenarioId
                                + "&jobGroup=" + scenarioId, function (response) {
                if (response) {
                    switch (response.triggerType) {
                        case "SimpleTrigger":
                            $("#advance").text($.i18n.prop('d_disp_btn_advance'));
                            commonDispatch.showHideBatch(['.right-form .cornWrap'], "none");
                            commonDispatch.showHideBatch(['.right-form .actionWrap',
                                                          '.right-form .overtimeWrap',
                                                          '.right-form .starttimeWrap'], "block");
                            $("#startTimeinputText").val(response.startTimeStr);
                            $("#overTimeinputText").val(response.endTimeStr);
                            $("#ExecutionCycleinputText").val(response.repeatInterval);
                            if (response.executionFrequencyUnit == "hour"
                                || response.executionFrequencyUnit == "day") {
                                commonDispatch.showHideBatch(['.right-form .actionweekWrap',
                                                              '.right-form .actionmonthWrap'],
                                    "none");
                                commonDispatch.showHideBatch(['.right-form .actiontimeWrap'],
                                    "block");
                            } else if (response.executionFrequencyUnit == "week") {
                                commonDispatch.showHideBatch(['.right-form .actionweekWrap',
                                                              '.right-form .actiontimeWrap'],
                                    "block");
                                commonDispatch.showHideBatch(['.right-form .actionmonthWrap'],
                                    "none");

                            } else if (response.executionFrequencyUnit == "month") {
                                commonDispatch.showHideBatch(['.right-form .actiontimeWrap',
                                                              '.right-form .actionmonthWrap'],
                                    "block");
                                commonDispatch.showHideBatch(['.right-form .actionweekWrap'],
                                    "none");

                            }
                            $("#ExecutionTime").val(response.executionFrequencyUnit);
                            $("#timeWrap").val(response.executionTime);
                            $("#weeksWrap").val(commonDispatch.changeNum(response.executionDay));
                            $("#monthWrap").val(response.executionDay);
                            break;
                        case "CronTrigger":
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
                    commonDispatch.showHideBatch(['.right-form .cornWrap',
                                                  '.right-form .actionmonthWrap',
                                                  '.right-form .actiontimeWrap',
                                                  '.right-form .actionweekWrap'], "none");
                    commonDispatch.showHideBatch(['.right-form .actionWrap',
                                                  '.right-form .overtimeWrap',
                                                  '.right-form .starttimeWrap'], "block");
                    $("#advance").text($.i18n.prop('d_disp_btn_advance'));
                    $("#ExecutionTime").val(4);
                    commonDispatch.clearInputVal(['#startTimeinputText', '#overTimeinputText',
                                                  '#cornInputText', '#timeWrap',
                                                  '#ExecutionCycleinputText']);
                    $("#weeksWrap").val($.i18n.prop('d_disp_week_choose'));
                    $("#monthWrap").val($.i18n.prop('d_disp_month_choose'));
                    $("#weeksWrap").next().find("input:checked").prop("checked", false);
                    $("#monthWrap").next().find("input:checked").prop("checked", false);

                }
                })

        });

        // 初始化多选菜单
        $(document).ready(function () {
            var options = {
                1: $.i18n.prop('d_disp_week_Sunday'),
                2: $.i18n.prop('d_disp_week_Monday'),
                3: $.i18n.prop('d_disp_week_Tuesday'),
                4: $.i18n.prop('d_disp_week_Wednesday'),
                5: $.i18n.prop('d_disp_week_Thursday'),
                6: $.i18n.prop('d_disp_week_Friday'),
                7: $.i18n.prop('d_disp_week_Saturday')

            };
            $("#weeksWrap").checks_select(options);

        });

        $(document).ready(function () {
            var options = {};
            for (var i = 1; i < 32; i++) {
                if (i < 10) {
                    options[i] = "0" + i;
                } else {
                    options[i] = i;
                }
            }
            $("#monthWrap").checks_select(options);
        });

        // 切换周月
        $("#ExecutionTime").change(function () {
            var actionUnitVal = $("#ExecutionTime").val();
            if (actionUnitVal == "week") {
                $("#weeksWrap").val($.i18n.prop('d_disp_week_choose'));
                $("#weeksWrap").next().find("input:checked").prop("checked", false);
                commonDispatch.showHideBatch(['.actionweekWrap', '.actiontimeWrap'], "block");
                commonDispatch.showHideBatch(['.actionmonthWrap'], "none");
            } else if (actionUnitVal == "month") {
                $("#monthWrap").val($.i18n.prop('d_disp_month_choose'));
                $("#monthWrap").next().find("input:checked").prop("checked", false);
                commonDispatch.showHideBatch(['.actionmonthWrap', '.actiontimeWrap'], "block");
                commonDispatch.showHideBatch(['.actionweekWrap'], "none");
            } else if (actionUnitVal == "hour" || actionUnitVal == "day") {
                commonDispatch.showHideBatch(['.actionweekWrap', '.actionmonthWrap'], "none");
                commonDispatch.showHideBatch(['.actiontimeWrap'], "block");

            } else {
                commonDispatch.showHideBatch(['.actionweekWrap', '.actionmonthWrap',
                                              '.actiontimeWrap'], "none");
            }


        });


        // 初始化jeDate控件
        $("#startTimeinputText").jeDate({
            ishmsVal: false,
            isTime: false,
            maxDate: '2020-06-16 23:59:59',
            minDate: $.nowDate(0),
            format: "YYYY-MM-DD",
            zIndex: 3000
        });


        $("#overTimeinputText").jeDate({
            ishmsVal: false,
            isTime: false,
            maxDate: '2020-06-16 23:59:59',
            mminDate: $.nowDate(0),
            format: "YYYY-MM-DD",
            zIndex: 3000
        });

        $("#timeWrap").jeDate({
            isinitVal: true,
            festival: true,
            ishmsVal: false,
            format: "hh:mm:ss",
            zIndex: 3000
        });

            $('#startTimeinputText').attr('placeholder', $.i18n.prop('d_disp_choose'));
            $('#overTimeinputText').attr('placeholder', $.i18n.prop('d_disp_choose'));
        $('#cornInputText').attr('title', $.i18n.prop('d_disp_corn'));
            SceneCommon.setLayoutLeft([$(".dispatch_content .sence-content-left")[0],
                                       $(".dispatch_content .right_content")[0]], 147);
            $('.scene-left-tool input.search-underline').attr('placeholder',
                                                              $.i18n.prop('d_input_search'));

        $("#loadingDiv").css("display", "none");
        };

        //initDispatchSence is over

        _initDispatch.prototype.mouseoverFun = function () {                    //pictrue hover
        $(".element-item").hover(function () {
            SceneCommon.floatFun.sceneFloatShow(this, ["scene-check", "img-toolbar"]);
        }, function () {
            if ($(this).find("input[type='checkbox']").is(":checked")) {

            } else {
                SceneCommon.floatFun.sceneFloatHide(this, ["scene-check", "img-toolbar"]);
            }
        })
        };
        //mouseoverFun is over
        var $containerPacth;
        var _isotopeInit = function () {
        $containerPacth = $('#dispatch-isotope').isotope({
            itemSelector: '.element-item-patch',
            layoutMode: 'fitRows',
            getSortData: {
                name: '.name',
                symbol: '.symbol',
                number: '.number parseInt',
                category: '[data-category]',
                weight: function (itemElem) {
                    var weight = $(itemElem).find('.weight').text();
                    return parseFloat(weight.replace(/[\(\)]/g, ''));
                }
            }
        });
        var filterFns = {
            contains: function () {
                var checkInput = $("#dispatch-monitor").find(".search-underline").val();
                var checkName = $(this).find(".scene-name").text();
                return checkName.indexOf(checkInput) >= 0 ? true : false;
            }
        };
        $containerPacth.isotope({filter: ""});
        $("#dispatch-monitor .search-underline").keyup(function (e) {
            var filterValue = $(this).val();
            var filterValue = filterFns["contains"] || "." + filterValue;
            $containerPacth.isotope({filter: filterValue});

            SceneCommon.setLayoutLeft([$(".dispatch_content .sence-content-left .dispatch-isotope")[0]],
                185);
            SceneCommon.setLayoutLeft([$(".dispatch_content .right_content")[0]], 145);
        })
        };
        //_isotopeInit is  over

        var sceneEachPorduce = function (index, value) {
        var time = null;
        var timestamp;
        var sceneId = value["scenario_id"];
        var sceneName = "";
            if (value["scenario_name"])
                sceneName = SceneCommon.getStrLength(value["scenario_name"], 10);
        if (value["create_time"]) {
            var time = value["create_time"].split(" ")[0];
            timestamp = SceneCommon.transFromDateToMilli(value["create_time"]);
        }
        var className = value["scenario_name"]; //+" "+value["scenario_name"].split("").join(" ");
            var sceneEach = '<div class="element-item element-item-patch ' + className
                            + '" data-category="transition" name="' + sceneId + '">'
        var onlineBool = "";
            if (value["scenario_status"] == "3") {
                if (settings.testLangType() == "zh" || settings.testLangType() == "zh_CN") {
                    onlineBool =
                        '<img class="online-img" style="display: block" src="img/scene-develop/online.png"/>';
                } else if (settings.testLangType() == "en" || settings.testLangType() == "en_US"
                           || settings.testLangType() == "en_GB") {
                    onlineBool =
                        '<img class="online-img" style="display: block" src="img/scene-develop/online-en.png"/>';
                }

            } else {
                if (settings.testLangType() == "zh" || settings.testLangType() == "zh_CN") {
                    onlineBool =
                        '<img class="online-img" style="display: none" src="img/scene-develop/online.png"/>';
                } else if (settings.testLangType() == "en" || settings.testLangType() == "en_US"
                           || settings.testLangType() == "en_GB") {
                    onlineBool =
                        '<img class="online-img" style="display: none" src="img/scene-develop/online-en.png"/>';
                }
        }

            sceneEach +=
                '<div class="scene-common" style="background: url(' + value["scenario_extra"]
                + ') no-repeat center center">'
            + '<span class="scene-check">'
            + '<span class="fa fa-eye"></span>'
            + '</span>'
                + onlineBool
            + '<div class="img-toolbar">'
            + '<div class="checkbox-inline">'
            + '<input type="checkbox" value="1">'
            + '<label >Select</label></div>'
                + '</div></div><div><p class="scene-name" title="' + value["scenario_name"] + '">'
                + sceneName + '</p> <p class="scene-time pull-right">' + time
                + '</p><p class="number" style="display:none">' + (0 - timestamp)
                + '</p></div></div>'
        return sceneEach;

        };

        _initDispatch.prototype.check = function (thisObj) {
        var message = '<div class="container-fluid" style="height:100%;">' +
                      '<form  id="modifySceneForm" name="biaodan" class="form-horizontal" action="javascript:void(0);">'
                      +
            '<div class="form-group">' +
            '<div class="col-sm-12">' +
                      '<textarea id="" class="form-control" placeholder="场景描述" rows="6" name="description" disabled>'
                      + thisObj[0]["scenario_desc"] + '</textarea>' +
            '</div>' +
            '</div>' +
            '</form>' +
            '</div>';

        $(message).dialog({
            title: $.i18n.prop('d_add_sceneDescribe'),
            onBeforeShow: function () {
                $('.xdsoft_dialog_buttons button.xdsoft_btn').text($.i18n.prop('d_cancel'));
                $('#modifySceneForm .form-group .col-sm-12 textarea').attr('placeholder',
                                                                           $.i18n.prop('d_add_sceneDescribe'));
            },
            buttons: {
                "": function () {
                }
            }
        });
        };

        _initDispatch.prototype.dispatchAllCheck = function () {
            $(".dispatch-left-toolbar").find(".dispatch-left-tool").click(function () {
                var spanName = $(this).attr("name");
                switch (spanName) {
                    case "dispatch-checkall":
                    {
                        _dispatchCheck();
                        break;
                }
                    case "dispatch-inverse" :
                    {
                        _dispatchInverse();
                        break;
                }
                    case "dispatch-refresh":
                    {
                        _dispatchRefresh();
                        break;
                }
                    case "dispatch-online":
                    {
                        _dispatchLine("on");
                        break;
                }
                    case "dispatch-downline":
                    {
                        _dispatchLine("off");
                        break;
                }
                }
            });

        };
        var _dispatchCheck = function () {
            if (!settings.containClass($(".dispatch .header-tools div span#allCheck")[0],
                                       "ion-android-checkbox-outline-blank")) {
                $(".dispatch .header-tools div span#allCheck").removeClass("ion-android-checkbox-outline");
                $(".dispatch .header-tools div span#allCheck").addClass("ion-android-checkbox-outline-blank");
                $(".dispatch .element-item input[type='checkbox']").prop("checked", false);                                       //取消全选
                SceneCommon.floatFun.sceneFloatHide($("#dispatch-isotope")[0],
                    ["scene-check", "img-toolbar","scene-cover-black"]);
        } else {
                $(".dispatch .header-tools div span#allCheck").addClass("ion-android-checkbox-outline");
                $(".dispatch .header-tools div span#allCheck").removeClass("ion-android-checkbox-outline-blank");
                $(".dispatch .element-item input[type='checkbox']").prop("checked", 'true');//全选
                SceneCommon.floatFun.sceneFloatShow($("#dispatch-isotope")[0],
                    ["scene-check", "img-toolbar","scene-cover-black"]);
        }
        };

        var _dispatchInverse = function () {
        var checkboxArr = $(".dispatch .element-item input[type='checkbox']");
        var len = checkboxArr.length;
        var flag = false;
        for (var i = 0; i < len; i++) {
            if (checkboxArr[i].checked) {
                var parentsElement = $(checkboxArr[i]).parents(".element-item")[0];
                checkboxArr[i].checked = false;
                SceneCommon.floatFun.sceneFloatHide(parentsElement, ["scene-check", "img-toolbar","scene-cover-black"]);
                flag = true;
            } else {
                var parentsElement = $(checkboxArr[i]).parents(".element-item")[0];
                checkboxArr[i].checked = true;
                SceneCommon.floatFun.sceneFloatShow(parentsElement, ["scene-check", "img-toolbar","scene-cover-black"]);

            }
        }
        if ( flag == true ) {
            $("div[name=dispatch-checkall].dispatch-left-tool span").removeClass("ion-android-checkbox-outline").addClass("ion-android-checkbox-outline-blank")

        } else {
            $("div[name=dispatch-checkall].dispatch-left-tool span").removeClass("ion-android-checkbox-outline-blank").addClass("ion-android-checkbox-outline")

        }

        };

        var _dispatchRefresh = function () {
            if (!settings.containClass($(".dispatch .header-tools div span#allCheck")[0],
                                       "ion-android-checkbox-outline-blank")) {
                $(".dispatch .header-tools div span#allCheck").removeClass("ion-android-checkbox-outline");
                $(".dispatch .header-tools div span#allCheck").addClass("ion-android-checkbox-outline-blank");
        }
        $(".sence-content-main.sence-content-left div#dispatch-isotope").remove();
            $(".sence-content-main.sence-content-left").append('<div id="dispatch-isotope" class="dispatch-isotope"></div>');
        $("#dispatch-monitor .search-underline").val("");
        dispatch.initDispatchSence();
        };

        var _dispatchLine = function (val) {
        var dialogTip, url, method;
        if (val === "on") {
            dialogTip = $.i18n.prop('d_disp_goonline');
            url = "/service/v1/schedule/run";
            method = "POST";
        } else {
            dialogTip = $.i18n.prop('d_disp_offline');
            ;
            url = "/service/v1/schedule/delete";
            method = "DELETE";
        }
        var checkboxArr = $("#dispatch-isotope").find("input[type=checkbox]");
            var checkboxArrlen = $("#dispatch-isotope").find("input[type=checkbox]").length;
        var scenarioIdArr = [];
        for (var i = 0; i < checkboxArrlen; i++) {
            if (checkboxArr[i].checked) {
                var obj = {};
                var scenarioIds = $(checkboxArr[i]).parents(".element-item").attr("name");
                obj.scenarioId = scenarioIds;
                obj.jobGroup = scenarioIds;
                obj.userId = sessionStorage.getItem("userId");
                scenarioIdArr.push(obj);
            }
        }
        if (scenarioIdArr.length < 1) {
            initDialog().initAlert($.i18n.prop('d_disp_selectThis') + ' ' + dialogTip + ' '
                                   + $.i18n.prop('d_disp_thisScene'),
                                   $.i18n.prop('d_disp_dialog_prompt'));
        } else {
            settings.HttpClient(method, scenarioIdArr, url, function (response) {
                if (response.length > 0) {
                    initDialog().initAlert($.i18n.prop('d_disp_dialog_noFull'),
                                           $.i18n.prop('d_disp_dialog_prompt'), 2000);
                    //initDialog().initAlert("调度信息不完整或填写信息有误", "提示", 2000);

                    for (var i = 0; i < response.length; i++) {
                        for (var j = 0; j < scenarioIdArr.length; j++) {
                            if (scenarioIdArr[j]["scenarioId"] == response[i]["scenarioId"]) {
                                scenarioIdArr.splice(j, 1);
                                break;
                            }
                        }
                    }
                    for (var i = 0; i < scenarioIdArr.length; i++) {

                        $("#dispatch-isotope").find("div[name='" + scenarioIdArr[i]["scenarioId"]
                                                    + "'] .online-img").css("display", "block")
                    }
                } else {
                    if (method == "DELETE") {
                        for (var i = 0; i < scenarioIdArr.length; i++) {

                            $("#dispatch-isotope").find("div[name='"
                                                        + scenarioIdArr[i]["scenarioId"]
                                                        + "'] .online-img").css("display", "none")

                        }

                        initDialog().initAlert($.i18n.prop('d_disp_offlines'),
                                               $.i18n.prop('d_disp_dialog_prompt'), 2000);
                    } else {
                        for (var i = 0; i < scenarioIdArr.length; i++) {

                            $("#dispatch-isotope").find("div[name='"
                                                        + scenarioIdArr[i]["scenarioId"]
                                                        + "'] .online-img").css("display", "block")
                        }
                        initDialog().initAlert($.i18n.prop('d_disp_goonlines'),
                                               $.i18n.prop('d_disp_dialog_prompt'));
                    }
                }

            });
        }
        }

        var _dispatchValidate = function (formId) {
            $("#" + formId).bootstrapValidator({
                message: $.i18n.prop('d_sacne_notValue'),
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                fields: {
                    executionCycle: {
                        container: 'tooltip',
                        validators: {
                            notEmpty: {
                                message: $.i18n.prop('d_executionCycle_notEmpty')
                            },
                            regexp: {
                                regexp: /^(0|[1-9][0-9]*)$/,
                                message: $.i18n.prop('d_executionCycle_regexp')
                            }
                        }
                }
            }
        });
        }

        return {
        initDispatch: _initDispatch
        }

    });
