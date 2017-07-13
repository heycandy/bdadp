/**
 * Created by labo on 2016/8/31.
 */
define(['js/scene-develop/add-package', 'js/modeldialog', 'js/scene-develop/scene-common',
        'js/scene-edit/edit-init', 'js/settings', 'libs/i18n/international',
        'libs/i18n/jquery.i18n.properties'],
    function (ScenePackage, initDialog, SceneCommon, editInit) {
        "use strict"
        settings.interFun();
        var _Scene = function () {
        this.imgSrc = "";
        };
        _Scene.prototype.addScene = function (scenePictrue) {
        var self = this;
        var message = '<div class="container-fluid" style="height:100%;">' +
                      '<form  id="addSceneForm" name="biaodan" class="form-horizontal" action="javascript:void(0);">'
                      +
            '<div class="form-group">' +
            '<div class="row" style="margin:1%;margin-bottom: 15px;">' +
            '<div class="col-sm-5 picbox">' +
            '<div class="thumbBox"></div>' +
            '</div>' +
            '<div class="col-sm-7" style="margin-top: 10%;">' +
                      '<input id="" type="text" placeholder="场景名称" class="form-control sceneName" name="sceneName" style="font-size: 15px;margin-bottom: 23%;"  value="">'
                      +
                      //'<input type="file" class="form-control upload-file" name="defined"
                      // style="margin:10% 0%;background-color: #EFEFEF;width: 55%;" value="自定义" >'
                      // +
                      '<select class="fileLoad" style="background:#f2f2f2; width: 111px; height:36px; font-size: 14px;display: inline;-webkit-padding-right: 6px;">'
                      +
            '<option value="2" id="I_xiugai_localupload"></option>' +
            '<option value="1" id="I_xiugai_custom"></option>' +
            '</select>' +
                      '<button class="btn choiceImg" style="height:36px; background-color: #F1F1F1;color: #525252;border: 1px solid #C7C7C7;margin-bottom: 4px;margin-left: 8px;width: 81px;" value="选择图片"><span id="I_add_selectpicture"></span></button>'
                      +
                      '<input type="file" class="upload-file fileInput" style="display: none;">' +
                      '<div class="customWrap" style="width: 193px; height: 158px; border:1px solid #ccc; display: none; position: absolute; left: 6%; top:107%; background: #f2f2f2; z-index: 99;overflow-y: scroll;">'
                      +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '<div class="form-group">' +
            '<div class="col-sm-12">' +
                      '<textarea  class="form-control" placeholder="场景描述" rows="6" name="description"></textarea>'
                      +
            '</div>' +
            '</div>' +
            '</form>' +
            '</div>';

        var valiTrueFunc = function () {
            $(".xdsoft_dialog button").attr("disabled", "disabled");
            self.addSceneRegin(scenePictrue);
        };

        var valiFalseFunc = function () {
            return false;
        };

        $(message).dialog({
            title: $.i18n.prop('d_add_addscene'),
            onBeforeShow: function () {
                $(".xdsoft_btn")
                $("#addSceneForm").parents(".xdsoft_dialog").css("width", "440px");
                $('#I_add_selectpicture').parent('.choiceImg').css("padding", "6px 6px");
                $('#addSceneForm .form-group .row .col-sm-7 input').attr('placeholder',
                                                                         $.i18n.prop('d_input_scenarioName'));
                $('#addSceneForm  .form-group .col-sm-12 textarea').attr('placeholder',
                                                                         $.i18n.prop('d_add_sceneDescribe'));
                $('.xdsoft_dialog_buttons button.xdsoft_btn:first-child').text($.i18n.prop('d_save'));
                $('.xdsoft_dialog_buttons button.xdsoft_btn:last-child').text($.i18n.prop('d_cancel'));
                $("#addSceneForm .choiceImg").click(function (e) {
                    var temp = $("#addSceneForm .fileLoad").val();
                    if (temp == 1) {
                        e.stopPropagation();
                        if ($("#addSceneForm .customWrap").css("display") == "none") {
                            $("#addSceneForm .customWrap").css("display", "block");
                            //_getAllImg(self);
                        } else if ($("#addSceneForm .customWrap").css("display") == "block") {
                            $("#addSceneForm .customWrap").css("display", "none");
                        }
                    } else if (temp == 2) {
                        $("#addSceneForm .customWrap").css("display", "none");
                        $("#addSceneForm .fileInput").trigger("click");
                    }
                });
                $(".xdsoft_dialog_overlay").find(".xdsoft_dialog_shadow_effect").click(function () {
                    $("#addSceneForm .customWrap").css("display", "none");
                })
                $(".xdsoft_dialog_shadow_effect").find(".xdsoft_dialog_buttons").css("text-align",
                                                                                     "right");
                $("#addSceneForm").find(".fileLoad").addClass("change-fileload");
            },
            onAfterShow: function () {
                _userDefined('img/scene-develop/img1.png');
            },
            buttons: {
                " ": function () {
                    console.log(self.imgSrc)
                    return SceneCommon.validate("addSceneForm", valiTrueFunc, valiFalseFunc);
                },
                "": function () {

                }

            }
        });
        this.validate("addSceneForm");
        settings.interFun();
        };

        _Scene.prototype.addSceneRegin = function (scenePictrue) {
        var paramObj = {};
            var flag = true;
            var sceneName = $("#addSceneForm").find("input[ name='sceneName']").val();
        var sceneScripe = $("#addSceneForm").find("textarea").val();

            var unScenarioName = ["主页", "场景", "调度", "监控", "工具", "Home", "Scenario", "Schedule",
                                  "Monitor", "Tools"];
            for (var i = 0; i < unScenarioName.length; i++) {
                if (sceneName == unScenarioName[i]) {
                    flag = false;
                    break;
                }
        }
            if (flag) {
                paramObj.scenario_name = sceneName;
                paramObj.scenario_desc = sceneScripe;
                paramObj.create_user = sessionStorage.userName;
                var imgVal = cropper.getDataURL();
                paramObj.scenario_extra = imgVal[0];
                paramObj.scenario_col = imgVal[1];
                settings.HttpClient("POST", paramObj, "/service/v1/scenario", function (response) {
                console.log(response);
                return _sceneEachPictrue(scenePictrue, response, imgVal[0]);
                });
            } else {
                initDialog().initAlert($.i18n.prop('d_scene_disable'), $.i18n.prop('d_hbase_warn'));
        }
        }

        _Scene.prototype.modifyScene = function (modifyObj, scenarioData) {
        var paramObj = {};
            var flag = true;
            var sceneName = $("#editSceneForm").find("input[ name='sceneName']").val();
        var sceneScripe = $("#editSceneForm").find("textarea").val();

            var unScenarioName = ["主页", "场景", "调度", "监控", "工具", "Home", "Scenario", "Schedule",
                                  "Monitor", "Tools"];
            for (var i = 0; i < unScenarioName.length; i++) {
                if (sceneName == unScenarioName[i]) {
                    flag = false;
                    break;
                }
        }
            if (flag) {
                paramObj.scenario_id = scenarioData[0]["scenario_id"];
                paramObj.scenario_name = sceneName;
                paramObj.scenario_desc = sceneScripe;
                var imgVal = cropper.getDataURL();
                paramObj.scenario_extra = imgVal[0];
                paramObj.scenario_col = imgVal[1];
                settings.HttpClient("PUT", paramObj,
                                    "/service/v1/scenario/" + scenarioData[0]["scenario_id"],
                                    function (data) {
                var scenario = $(modifyObj).parents(".element-item");
                scenario.className = "element-item sceneName";
                                        $(scenario).find(".scene-common").css("background-image",
                                                                              "url("
                                                                              + paramObj.scenario_extra
                                                                              + ")");
                sceneName = SceneCommon.getStrLength(sceneName, 10);
                $(scenario).find("p.scene-name").html(sceneName);
                                        if (data["create_time"])
                                            $(scenario).find("p.scene-time").html(data["create_time"].split(" ")[0]);
                                        initDialog().initAlert($.i18n.prop('d_modific_sccess'),
                                                               $.i18n.prop('d_modific_modific'));
                                    });
            } else {
                initDialog().initAlert($.i18n.prop('d_scene_disable'), $.i18n.prop('d_hbase_warn'));
        }
        }

        //init all the scene
        _Scene.prototype.initAllScene = function () {
            var _self = this, seceneAllHtml = "";
        $("#loadingDiv").css("display", "block");
        //var results = settings.HttpClient("GET", null, "/service/v1/scenario");
        // //HttpClient: function (method,inputData,url);
            settings.HttpClient("GET", null, "/service/v1/scenario", function (results) {
                if (!results || !results.length) {
                seceneAllHtml = sceneEachPorduce("", "", false);
                } else {
                $.each(results, function (index, value) {
                    var sceneEach = "";
                    if (value.create_user == sessionStorage.userName) {
                        if (!index) {
                            sceneEach =
                                sceneEachPorduce(index, value, false) + sceneEachPorduce(index,
                                                                                         value,
                                                                                         true);
                        } else {
                            sceneEach = sceneEachPorduce(index, value, true);
                        }
                    } else if (seceneAllHtml == "") {
                        seceneAllHtml += sceneEachPorduce(index, value, false);
                    }
                    seceneAllHtml += sceneEach;
                });
                }
                $("#loadingDiv").css("display", "none");
                $(".isotope").append(seceneAllHtml);
                /* var $sceneNew = $(".element-item");
                 if (value['scenario_extra']) {
                 $sceneNew.children(".scene-common").removeClass("");
                 $sceneNew.children(".scene-common").css("background-image", "url(" + value['scenario_extra'] + ")");
                 $sceneNew.children(".scene-common").css("background-repeat", "no-repeat");
                 }*/
                settings.interFun();
                var $li = $(".dropdown-menu li ");

                _isotopeInit();
                _self.mouseoverFun();          //mouseover events

                //添加场景
                $(".add-img.scene-common").click(function () {
                var scenePictrue = new _Scene();
                scenePictrue.addScene(scenePictrue);
                });

                //scene check
                $(".isotope").unbind("click");
                $(".isotope").on("click", ".scene-check", function () {
                var scenarioId = $(this).parents(".element-item").attr("name");
                    settings.HttpClient("GET", null,
                                        "/service/v1/scenario/?scenario_id=" + scenarioId,
                                        function (response) {
                                            console.log(response);
                                            _self.check(response);
                                        });
                });

                //箭头上下切换
                $(".isotope").on("click", ".triangle", function () {
                var scenePictrue = new _Scene();
                if ($(this).hasClass("fa-sort-down")) {
                    $(this).removeClass("fa-sort-down").addClass("fa-sort-up");
                } else if ($(this).hasClass("fa-sort-up")) {
                    $(this).removeClass("fa-sort-up").addClass("fa-sort-down");
                }
                });
                // 鼠标离开时，箭头恢复，且dropdrown-meanu消失
                $(".element-item:gt(0)").mouseleave(function () {
                    if ($(this).find("ul.dropdown-menu").css("display") == "block") {
                        $(this).find(".triangle").trigger("click");
                }

                });
                // 点击修改，删除或编辑时，箭头恢复，且dropdrown-meanu消失
                $(".element-item:gt(0)").find("ul.dropdown-menu li").click(function () {
                    $(this).parents(".scene-up-down").find(".triangle").trigger("click");
                });

                //scene edit,cancel,delete
                $(".isotope").on("click", "ul.dropdown-menu li", function () {
                var thisObj = this;
                var scenarioId = $(this).parents(".element-item").attr("name");
                    var scenerioName = $(this).parents(".element-item").find(".scene-name").text();
                    // var operateText = $(this).text();
                var operateName = $(this).attr("name");
                    if (operateName == "edit_name") {
                        _self.edit(scenarioId, scenerioName);
                    } else if (operateName == "delete_name") {      //delete scenario
                        // $(this).unbind("",this.deleteScenario);
                        var scenePackage = new ScenePackage.ScenePackages();
                        _self.deleteScenario(scenarioId, scenePackage);
                    } else if (operateName == "modify_name") {
                        settings.HttpClient("GET", null,
                                            "/service/v1/scenario/?scenario_id=" + scenarioId,
                                            function (data) {
                        _self.modify(thisObj, data);
                                            });
                }
            });
                //窗口放大缩小时获取高度
                $(window).resize(function () {
                    SceneCommon.setLayoutLeft([$('.scene-content-main .scene-content-inner')[0],
                                               $('.scene-content-main .isotope')[0]], 195);
                })
                SceneCommon.setLayoutLeft([$('.scene-content-main .scene-content-inner')[0],
                                           $('.scene-content-main .isotope')[0]], 195);
                $('.scene-left-tool input.search-underline').attr('placeholder',
                                                                  $.i18n.prop('d_input_search'));
                $('.scene-right-toolbar input.search-underline').attr('placeholder',
                                                                      $.i18n.prop('d_input_search'));
            });

        };

        _Scene.prototype.edit = function (scenarioId, scenerioName) {
        editInit().create(scenarioId, scenerioName);
        }

        _Scene.prototype.delMultiScenario = function (multiScenarioId, scenePackage) {
        if (multiScenarioId.length < 1) {
            initDialog().initAlert($.i18n.prop('d_scene_deleteScene'),
                                   $.i18n.prop('d_delete_delete'));
        } else {
            var paramObj = {};
            paramObj.action = "delete";
            paramObj.scenario_id = multiScenarioId;
            initDialog().initConfirm($.i18n.prop('d_delete_yesno'), function () {
                settings.HttpClient("POST", paramObj, "/service/v1/scenario/action", function () {
                    for (var i = 0; i < multiScenarioId.length; i++) {
                        $container.isotope('remove', $(".element-item[name=" + multiScenarioId[i]
                                                       + "]")).isotope('layout');
                    }
                    $(".scene-content-main.scene-content-right .scene-content-inner").children().not(".scene-add").remove();
                    scenePackage.initAllScenePackage();
                    $(".scene-content-header.scene-right-toolbar").find(".search-underline").val("");
                    scenePackage.searchScenePackage();
                    SceneCommon.setLayoutLeft([$('.scene-content-main .scene-content-inner')[0],
                                               $('.scene-content-main .isotope')[0]], 195);
                }, function () {
                    initDialog().initAlert($.i18n.prop('d_mulScene_deleteScene'),
                                           $.i18n.prop('d_delete_delete'));
                });
            }, $.i18n.prop('d_delete_delete'))
        }
        }

        _Scene.prototype.deleteScenario = function (scenarioId, scenePackage) {
        initDialog().initConfirm($.i18n.prop('d_delete_yesno'), function () {
            settings.HttpClient("DELETE", null, "/service/v1/scenario/" + scenarioId, function () {
                $container.isotope('remove',
                                   $(".element-item[name=" + scenarioId + "]")).isotope('layout');

                $(".scene-content-main.scene-content-right .scene-content-inner").children().not(".scene-add").remove();
                scenePackage.initAllScenePackage();
                $(".scene-content-header.scene-right-toolbar").find(".search-underline").val("");
                scenePackage.searchScenePackage();
                SceneCommon.setLayoutLeft([$('.scene-content-main .scene-content-inner')[0],
                                           $('.scene-content-main .isotope')[0]], 195);
            }, function () {
                initDialog().initAlert($.i18n.prop('d_scene_alreadyLine'),
                                       $.i18n.prop('d_hbase_warn'));
            });
        }, $.i18n.prop('d_delete_delete'))
        }

        _Scene.prototype.modify = function (modifyObj, data) {
        var self = this;
        var descInfo = !!data[0]["scenario_desc"]?data[0]["scenario_desc"]:'';
        var message = '<div class="container-fluid" style="height:100%;">' +
                      '<form  id="editSceneForm" name="biaodan" class="form-horizontal" action="javascript:void(0);">'
                      +
            '<div class="form-group">' +
            '<div class="row" style="margin:1%;">' +
            '<div class="col-sm-5 picbox">' +
            '<div class="thumbBox"></div>' +
            '</div>' +
            '<div class="col-sm-7" style="margin-top: 10%;">' +
                      '<input id="" type="text" placeholder="场景名称" class="form-control sceneName" name="sceneName"  value="'
                      + data[0]["scenario_name"] + '">' +
            '<select class="fileLoad">' +
            '<option value="2" id="I_xiugai_localupload"></option>' +
            '<option value="1" id="I_xiugai_custom"></option>' +
            '</select>' +

                      '<button class="btn choiceImg" id="I_xiugai_selectpicture"></button>' +

                      '<input type="file" class="upload-file fileInput" style="display: none;">' +
            '<div class="customWrap">' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '<div class="form-group">' +
            '<div class="col-sm-12">' +
                      '<textarea id="" class="form-control sceneDesc" placeholder="" rows="6" style="resize: none;width:100%;margin:0px -10px;" name="sceneDesc">'
                      + descInfo + '</textarea>' +
            '</div>' +
            '</div>' +
            '</form>' +
            '</div>';

        var valiTrueFunc = function () {
            $(".xdsoft_dialog button").attr("disabled", "disabled");
            self.modifyScene(modifyObj, data);
        };

        var valiFalseFunc = function () {
            return false;
        };
        $(message).dialog({
            title: $.i18n.prop('d_modific_scene'),
            onBeforeShow: function () {
                $("#editSceneForm").parents(".xdsoft_dialog").css("width", "440px");
                $('#editSceneForm .form-group .row .col-sm-7 input').attr('placeholder',
                                                                          $.i18n.prop('d_input_scenarioName'));
                $('#editSceneForm .form-group .col-sm-12 textarea').attr('placeholder',
                                                                         $.i18n.prop('d_add_sceneDescribe'));
                $("#editSceneForm").find(".choiceImg").click(function (e) {
                    var temp = $("#editSceneForm .fileLoad").val();
                    if (temp == 1) {
                        e.stopPropagation();
                        if ($("#editSceneForm .customWrap").css("display") == "none") {
                            $("#editSceneForm .customWrap").css("display", "block");
                            // _getAllImg();
                        } else if ($("#editSceneForm .customWrap").css("display") == "block") {
                            $("#editSceneForm .customWrap").css("display", "none");
                        }
                    } else if (temp == 2) {
                        $("#editSceneForm .customWrap").css("display", "none");
                        $("#editSceneForm .fileInput").trigger("click");
                    }
                });
                $(".xdsoft_dialog_overlay").find(".xdsoft_dialog_shadow_effect").click(function () {
                    $("#editSceneForm .customWrap").css("display", "none");
                })
                $(".xdsoft_dialog_shadow_effect").find(".xdsoft_dialog_buttons").css("text-align",
                                                                                     "right");
                $('.xdsoft_dialog_buttons button.xdsoft_btn:first-child').text($.i18n.prop('d_save'));
                $('.xdsoft_dialog_buttons button.xdsoft_btn:last-child').text($.i18n.prop('d_cancel'));
                $("#editSceneForm").find(".fileLoad").addClass("change-fileload");
            },
            onAfterShow: function () {
                _userDefined(data[0].scenario_extra);
            },
            buttons: {
                " ": function () {
                    return SceneCommon.validate("editSceneForm", valiTrueFunc, valiFalseFunc);
                },
                "": function () {

                }

            }
        });

        this.validate("editSceneForm");
        settings.interFun();
        };

        //scene check
        _Scene.prototype.check = function (thisObj) {
        var message = '<div class="container-fluid" style="height:100%;">' +
                      '<form  id="modifySceneForm" name="biaodan" class="form-horizontal" action="javascript:void(0);">'
                      +
            '<div class="form-group">' +
            '<div class="col-sm-12">' +
                      '<textarea id="" class="form-control" placeholder="" rows="6" name="description" disabled>'
                      + thisObj[0]["scenario_desc"] + '</textarea>' +
            '</div>' +
            '</div>' +
            '</form>' +
            '</div>';

//        var dialog = new Dialog.DialogArk();
//        dialog.createDialog("场景展示", message, "","");
        $(message).dialog({
            title: $.i18n.prop('d_add_sceneDescribe'),

            onBeforeShow: function () {
                $('#modifySceneForm .form-group .col-sm-12 textarea').attr('placeholder',
                                                                           $.i18n.prop('d_add_sceneDescribe'));
                $('.xdsoft_dialog_buttons button.xdsoft_btn').text($.i18n.prop('d_cancel'));
            },
            buttons: {
                "": function () {
                }
            }
        });
        }

        //pictrue hover
        _Scene.prototype.mouseoverFun = function () {                    //pictrue hover
        $(".element-item").hover(function () {
            SceneCommon.floatFun.sceneFloatShow(this,
                ["scene-cover-black", "scene-check", "img-toolbar"]);
        }, function () {
            if (!$(this).find("input[type='checkbox']").is(":checked")) {
                SceneCommon.floatFun.sceneFloatHide(this,
                    ["scene-cover-black", "scene-check", "img-toolbar"]);
            }


        })
        };

        //produce each scene pictrue
        var sceneEachPorduce = function (index, value, bool) {
        var time = null;
        var timestamp;
        var sceneId = value["scenario_id"];
        var sceneName = "";
            if (value["scenario_name"])
                sceneName = SceneCommon.getStrLength(value["scenario_name"], 10);
        if (value["create_time"]) {
            var time = value["create_time"].split(" ")[0];
            timestamp = parseInt(new Date().valueOf()/10000);
        }

        var className = value["scenario_name"];             //+"
                                                            // "+value["scenario_name"].split("").join("
                                                            // ");
        if (bool) {
            if (!value["scenario_extra"]) {
                value["scenario_extra"] = "img/scene-develop/img1.png";
            }
            var sceneEach = '<div class="element-item ' + className
                            + '" data-category="transition" name="' + sceneId + '">'
                            + '<div class="scene-cover-black" draggable="true"></div>'
                            + '<div class="scene-common" style="background: url('
                            + value["scenario_extra"] + ') no-repeat center center">'
                            + '<span class="scene-check">'
                            + '<span class="fa fa-eye"></span>'
                            + '</span>'
                            + '<div class="img-toolbar">'
                            + '<div class="checkbox-inline">'
                            + '<input type="checkbox" value="1">'
                            + '<label>Select</label></div>'
                            + '<div class="mg-group pull-right scene-up-down">'
                            + '<span class="dropdown-toggle fa fa-sort-down triangle" data-toggle="dropdown" style="margin-top: 6px;"></span>'
                            + ' <ul class="dropdown-menu" role="menu">'
                            + '<li name="edit_name"><a href="#"><i class="glyphicon glyphicon-pencil" style="font-size: 10px;"></i>'
                            + $.i18n.prop("d_span_edit") + '</a></li>'
                            + '<li name="delete_name"><a href="#"><i class="fa fa-trash-o"></i>'
                            + $.i18n.prop("d_span_delete") + '</a></li>'
                            + '<li name="modify_name"><a href="#"><i class="fa fa-edit"></i>'
                            + $.i18n.prop("d_span_modify") + '</a></li>'
                            + ' </ul></div> </div></div><div><p class="scene-name" title="'
                            + value["scenario_name"] + '">' + sceneName
                            + '</p> <p class="scene-time pull-right">' + time
                            + '</p><p class="number" style="display:none">' + (0 - timestamp)
                            + '</p></div></div>'
        } else {
            //var sceneEach = '<div class="element-item ' + value["scenario_name"] + '"
            // data-category="transition" draggable="true">'
            var sceneEach = '<div class="element-item">'
                            + '<div class="add-img  scene-common" style="margin-top: 21px;">'
                            + '</div><div><p class="scene-name" style="display: none;">场景名称</p> <p class="scene-time pull-right" style="display: none;">创建时间</p><p class="number" style="display:none">'
                            + (0 - 9999999999999) + '</p></div></div>'
        }

        return sceneEach;

        };
        var $container;
        var _isotopeInit = function () {
        $container = $('.isotope').isotope({
            itemSelector: '.element-item',
            layoutMode: 'fitRows',
            /*fitRows: {
             gutter: 20
             },*/
            getSortData: {
                name: '.name',
                symbol: '.symbol',
                number: '.number parseFloat',
                category: '[data-category]',
                weight: function (itemElem) {
                    var weight = $(itemElem).find('.weight').text();
                    return parseFloat(weight.replace(/[\(\)]/g, ''));
                }
            }
        });
        var filterFns = {
            contains: function () {
                var checkInput = $(".scene-content-header.scene-left-toolbar").find(".search-underline").val();
                var checkName = $(this).find(".scene-name").text();
                return checkName.indexOf(checkInput) >= 0 ? true : false;
            }
        };
        $container.isotope({sortBy: "number"});
        $(".header-tools .search-underline").keyup(function (e) {
            var filterValue = $(this).val();
            var filterValue = filterFns["contains"] || "." + filterValue;
            $container.isotope({filter: filterValue});
            SceneCommon.setLayoutLeft([$('.scene-content-main .scene-content-inner')[0],
                                       $('.scene-content-main .isotope')[0]], 195);
        })
        };
        var _sceneEachPictrue = function (scenePictrue, response, imgBase64) {
        var createTime = null;
        var timestamp;
        var sceneName = SceneCommon.getStrLength(response["scenario_name"], 10);
        if (response["create_time"]) {
            createTime = response["create_time"].split(" ")[0];
            timestamp = parseInt(new Date().valueOf()/10000);
        }
        var sceneId = response["scenario_id"];
            var sceneNew = '<div class="element-item ' + response["scenario_name"]
                           + '" data-category="transition" name="' + sceneId + '">'
            + '<div class="scene-cover-black" draggable="true"></div>'
            + '<div class="scene-img  scene-common">'
            + '<span class="scene-check">'
            + '<span class="fa fa-eye"></span>'
            + '</span>'
            + '<div class="img-toolbar">'
            + '<div class="checkbox-inline">'
            + '<input type="checkbox" value="1">'
            + '<label >Select</label></div>'
            + '<div class="mg-group pull-right scene-up-down">'
            + '<span class="dropdown-toggle fa fa-sort-down" data-toggle="dropdown" style="margin-top: 6px;"></span>'
            + ' <ul class="dropdown-menu" role="menu">'
                           + '<li name="edit_name"><a href="#"><i class="glyphicon glyphicon-pencil" style="font-size: 10px;"></i>'
                           + $.i18n.prop("d_span_edit") + '</a></li>'
                           + '<li name="delete_name"><a href="#"><i class="fa fa-trash-o"></i>'
                           + $.i18n.prop("d_span_delete") + '</a></li>'
                           + '<li name="modify_name"><a href="#"><i class="fa fa-edit"></i>'
                           + $.i18n.prop("d_span_modify") + '</a></li>'
                           + ' </ul></div> </div></div><div><p class="scene-name" title="'
                           + response["scenario_name"] + '">' + sceneName
                           + '</p> <p class="scene-time pull-right">' + createTime
                           + '</p><p class="number" style="display:none">' + (0 - timestamp)
                           + '</p></div></div>'
        var $sceneNew = $(sceneNew);
        $sceneNew.children(".scene-common").removeClass("");
            $sceneNew.children(".scene-common").css("background-image", "url(" + imgBase64 + ")");
            $sceneNew.children(".scene-common").css("background-repeat", "no-repeat");
            $container.prepend($sceneNew).isotope('reloadItems').isotope({sortBy: 'number'});
            SceneCommon.setLayoutLeft([$('.scene-content-main .scene-content-inner')[0],
                                       $('.scene-content-main .isotope')[0]], 195);
        scenePictrue.mouseoverFun();
        };

        //用户自定义上传图片
        var cropper;
        var _userDefined = function (imgSrc) {
        var options =
        {
            thumbBox: '.thumbBox',
            imgSrc: imgSrc
        };
        cropper = $('.picbox').cropbox(options);
        $('.upload-file').on('change', function () {
            var reader = new FileReader();
            reader.onload = function (e) {
                options.imgSrc = e.target.result;
                cropper = $('.picbox').cropbox(options);
            }
            reader.readAsDataURL(this.files[0]);
        })
        }

        //获取所有已上传图片
        var _getAllImg = function () {

        settings.HttpClient("GET", null, "", function (response) {
            var imgName = "";
            var containHtml = "<table class='table table-bordered table-condensed'><tr>";
            $.each(response, function (index, val) {
                imgName = getIconsName(val);
                val = getRealPath(val);
                index = index + 1;
                if (index % 5) {
                    containHtml +=
                        "<td style='width:20px'><img src='" + val + "' title='" + imgName
                        + "' style='width:40px;height:40px;' onclick='imgClick(this)'/></td>";
                } else {
                    containHtml +=
                        "<td style='width:20px'><img src='" + val + "' title='" + imgName
                        + "' style='width:40px;height:40px;' onclick='imgClick(this)'/></td></tr><tr>";
                }
            })
            containHtml += "</tr></table>";
            $(".customWrap").append(containHtml);
        });
        };

        var imgClick = function (imgObj) {
        var imgSrc = $(imgObj).attr("src");
        $(".customWrap").css("display", "none");
        _userDefined(imgSrc);
        };

        //场景验证
        _Scene.prototype.validate = function (id) {
        switch (id) {
            case "addSceneForm":
            {
                $('#' + id).bootstrapValidator({
                    message: $.i18n.prop('d_sacne_notValue'),
                    feedbackIcons: {
                        valid: 'glyphicon glyphicon-ok',
                        invalid: 'glyphicon glyphicon-remove',
                        validating: 'glyphicon glyphicon-refresh'
                    },
                    fields: {
                        sceneName: {
                            container: 'tooltip',
                            validators: {
                                notEmpty: {
                                    message: $.i18n.prop('d_scene_userName_notEmpty')                    //$.i18n.prop('d_userName_notEmpty')    //用户名不能为空
                                },
                                stringLength: {
                                    max: 20,
                                    message: $.i18n.prop('d_scene_scenarioLong')
                                }
                            }
                        }
                    }
                });
            }
            case "editSceneForm":
            {
                $('#' + id).bootstrapValidator({
                    message: $.i18n.prop('d_sacne_notValue'),
                    feedbackIcons: {
                        valid: 'glyphicon glyphicon-ok',
                        invalid: 'glyphicon glyphicon-remove',
                        validating: 'glyphicon glyphicon-refresh'
                    },
                    fields: {
                        sceneName: {
                            container: 'tooltip',
                            validators: {
                                notEmpty: {
                                    message: $.i18n.prop('d_scene_userName_notEmpty')                   //$.i18n.prop('d_userName_notEmpty')    //用户名不能为空
                                },
                                stringLength: {
                                    max: 20,
                                    message: $.i18n.prop('d_scene_scenarioLong')
                                }
                            }
                        }
                    }
                });
            }
        }
        }

        return {
        Scene: _Scene
        }
    })






