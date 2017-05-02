/**
 * Created by labo on 2016/8/31.
 */

define(["js/modeldialog", 'js/scene-develop/scene-common', 'js/settings', 'libs/i18n/international',
        'libs/i18n/jquery.i18n.properties'], function (initDialog, SceneCommon) {
    "use strict"
    settings.interFun();
    var _ScenePackages = function () {
        this.appScene = [];
    };

    //初始化场景包
    _ScenePackages.prototype.initAllScenePackage = function () {
        var _self = this;
        //this.results = settings.HttpClient("GET", null, "/service/v1/scenario/category");
        settings.HttpClient("GET", null, "/service/v1/scenario/category", function (results) {
            //var resultsScene = settings.HttpClient("GET", null, "/service/v1/scenario");
            settings.HttpClient("GET", null, "/service/v1/scenario", function (resultsScene) {
                $.each(results, function (index, value) {
                    _addPackageStrConcat(this, value["cate_name"], resultsScene);
                })
                //Package hover
                $(".scene-category-main").hover(function () {
                    SceneCommon.floatFun.sceneFloatShow(this,
                        ["scene-addcategory-mask", "fa-eye", "ion-close"]);
                    var packageHeight = $(this).height();
                    var packageWidth = $(this).width();
                    $(".scene-category-main .ion-close").css("margin-right",
                                                             (packageWidth - 100) / 2 + "px");
                    $(".scene-category-main .scene-addcategory-mask span").css("margin-top",
                                                                               (packageHeight - 40)
                                                                               / 2 + "px");
                    $(".scene-category-main .fa-eye").css("margin-left",
                                                          (packageWidth - 100) / 2 + "px");
                }, function () {
                    SceneCommon.floatFun.sceneFloatHide(this,
                        ["scene-addcategory-mask", "fa-eye", "ion-close"]);
                })
            });
        });

    }

    //添加应用包
    _ScenePackages.prototype.addApp = function () {
        var _self = this;
        var message = '<div class="container-fluid" style="height:100%;">' +
                      '<form  id="appPackageForm" name="biaodan" class="form-horizontal" name="cateApp" action="javascript:void(0);">'
                      +
            '<div class="form-group">' +
            '<div class="col-sm-12">' +
                      '<input id="" type="text" placeholder="分类名称" class="form-control" name="packageName" autofocus="autofocus">'
                      +
            '</div>' +
            '</div>' +
            '<div class="form-group">' +
            '<div class="col-sm-12">' +
                      '<textarea id="" class="form-control" placeholder="分类描述" rows="6" name="description"></textarea>'
                      +
            '</div>' +
            '</div>' +
            '</form>' +
            '</div>';

        var valiTrueFunc = function () {
            $(".xdsoft_dialog button").attr("disabled", "disabled");
            var obj = {};
            var nameTemp = $("#appPackageForm").find("input[ name='packageName']").val();
            var desTemp = $("#appPackageForm").find("textarea[ name='description' ]").val();

            obj.cate_name = nameTemp;
            obj.cate_desc = desTemp;
            _self.addAppRegin(obj);
        };

        var valiFalseFunc = function () {
            return false;
        }

        $(message).dialog({
            title: $.i18n.prop("d_add_applicationpackage"),
            onBeforeShow: function () {
                $('.xdsoft_dialog_buttons button.xdsoft_btn:first-child').text($.i18n.prop('d_save'));
                $('.xdsoft_dialog_buttons button.xdsoft_btn:last-child').text($.i18n.prop('d_cancel'));
                $('#appPackageForm .form-group .col-sm-12 input').attr('placeholder',
                                                                       $.i18n.prop('d_input_categoryName'));
                $('#appPackageForm .form-group .col-sm-12 textarea').attr('placeholder',
                                                                          $.i18n.prop('d_input_classificationDescription'));
            },
            buttons: {
                " ": function () {
                    return SceneCommon.validate("appPackageForm", valiTrueFunc, valiFalseFunc);
                },
                "": function () {

                }

            }
        });
        this.validate();
    };

    // add scene string concat
    var _addPackageStrConcat = function (response, name, resultsScene) {
        var containItem = "";
        var packageId = response["cate_id"];
        if (response["details"] && response["details"].length > 0) {
            var num = 0;
            $.each(response["details"], function (index, value) {
                var html = "", isVisible;
                var sceneId = response["details"][index]["scenario_id"];
                if (num < 8) {
                    isVisible = "block";
                } else {
                    isVisible = "none";
                }
                if (resultsScene || resultsScene.length > 0) {
                    $.each(resultsScene, function (item, val) {
                        if (resultsScene[item]["scenario_id"]
                            === response["details"][index]["scenario_id"]) {
                            if (val.create_user == sessionStorage.userName) {
                                var html = '<div class="col-md-3 col-lg-3 row-no-padding" style="display:'
                                           + isVisible + '" name="' + sceneId + '">'
                                           + '<img class="category-img" src="'
                                           + resultsScene[item]["scenario_extra"] + '"/>'
                                           + '</div>';
                                containItem += html;
                                num++;
                            }
                        }
                    })
                }
            });
        }

        if (typeof name == "object" && name) {
            name = name.cate_name;
        }
        var appNews = '<div class="row row-no-margin scene-category-main" id="' + packageId + '">' +
            '<div class="scene-addcategory-mask">' +
            '<span class="pull-left fa fa-eye"></span>' +
            '<span class="pull-right ion-close"></span>' +
            '</div>' +
            '<p class="category-title">' + name + '</p>' +
            '<div class="category-contain">' +
            containItem +
            '</div></div>';
        $(".scene-content-inner .scene-add").after(appNews);

        //关闭按钮触发事件
        $(".scene-category-main .scene-addcategory-mask span").unbind("click");
        $(".scene-category-main .ion-close").click(function () {
            var cateId = $(this).parents(".scene-category-main").attr("id");
            var _self = this;
            initDialog().initConfirm($.i18n.prop("d_delete_yesno"), function () {
                $(_self).parents(".scene-category-main").remove();
                var result = settings.HttpClient("DELETE", null,
                                                 "/service/v1/scenario/category/" + cateId,
                                                 function () {
                });
            }, $.i18n.prop('d_delete_delete'))
        });
        //详细信息触发事件
        $(".scene-category-main .fa-eye").click(function () {
            var cateId = $(this).parents(".scene-category-main").attr("id");
            var _self = $(this).parents(".scene-category-main")[0];
            var scenePackage = new _ScenePackages();
            scenePackage.packageCheck(cateId, _self, scenePackage);
        });
        //鼠标悬停
        $(".scene-category-main").hover(function () {
            SceneCommon.floatFun.sceneFloatShow(this,
                ["scene-addcategory-mask", "fa-eye", "ion-close"]);
        }, function () {
            SceneCommon.floatFun.sceneFloatHide(this,
                ["scene-addcategory-mask", "fa-eye", "ion-close"]);
        })
    }

    //添加场景应用包区域
    _ScenePackages.prototype.addAppRegin = function (obj) {
        settings.HttpClient("POST", obj, "/service/v1/scenario/category", function (response) {
            return _addPackageStrConcat(response, obj);
        });
    };

    //应用包详细信息展示
    _ScenePackages.prototype.packageCheck = function (packageId, packageObj, scenePackage) {
        var scenarios = $(packageObj).find(".category-contain").children();
        var message = "<div>";
        var imgSrc = "";
        var scenarioId = "";
        for (var i = 0; i < scenarios.length; i++) {
            imgSrc = $(scenarios[i]).find("img").attr("src");
            scenarioId = $(scenarios[i]).attr("name");
            message +=
                '<div class="col-md-3 col-lg-3 row-no-padding scenarios" name="' + scenarioId
                + '" style="margin-bottom: 10px;">' +
                '<span class="ion-close"></span>' +
                '<img class="category-img" src="' + imgSrc + '"/>' +
                '</div>';
        }
        message += "</div>"
        //dialog
        $(message).dialog({
            title: $.i18n.prop("d_add_applicationpackagedetails"),
            onBeforeShow: function () {
                $(".xdsoft_modal .xdsoft_dialog").css({
                    'width': '500px',
                    'height': '329px'
                });
                $(".xdsoft_modal .xdsoft_dialog .xdsoft_dialog_content").css({
                    'overflow-y': 'auto',
                    'height': '215px'
                });
            },
            onAfterShow: function () {
                $(".xdsoft_dialog_content .ion-close").click(function () {
                    var scenario = $(this).parents("div.scenarios")[0];
                    scenePackage.delPackageScenario(packageId, scenario, scenePackage)
                })
                $('.xdsoft_dialog_buttons button.xdsoft_btn').text($.i18n.prop('d_cancel'));
            },
            buttons: {
                "": function () {

                }
            }
        })
    }

    //应用包中场景删除
    _ScenePackages.prototype.delPackageScenario = function (packageId, scenario, scenePackage) {
        var paramObj = {};
        paramObj.action = "cate_remove";
        paramObj.cate_id = packageId;
        paramObj.scenario_id = [$(scenario).attr("name")];
        initDialog().initConfirm($.i18n.prop('d_delete_yesno'), function () {
            $(scenario).remove();
            settings.HttpClient("POST", paramObj, "/service/v1/scenario/action", function () {
                $(".scene-content-main.scene-content-right .scene-content-inner").children().not(".scene-add").remove();
                scenePackage.initAllScenePackage();
                $(".scene-content-header.scene-right-toolbar").find(".search-underline").val("");
                scenePackage.searchScenePackage();
            });
        }, $.i18n.prop('d_delete_delete'))

    }

    var _searchPackage = function (filterValue, scenePackages) {
        //var packageTitless = scenePackages.find(".category-title");
        var packageTitless = $(".scene-content-inner").children().not(".scene-add").css("display",
                                                                                        "none").find(".category-title");
        $.each(packageTitless, function (index, value) {
            if ($(value).text().indexOf(filterValue) >= 0) {
                $(value).parents(".scene-category-main").css("display", "block");
            }
        })
    };

    //搜索scene package
    _ScenePackages.prototype.searchScenePackage = function (obj) {
        var scenePackages = $(".scene-content-inner").children().not(".scene-add");
        $(".scene-content-header.scene-right-toolbar").find(".search-underline").keyup(function () {
            var filterValue = $(this).val();
            if (filterValue) {
                // scenePackages.css("display", "none");
                $(".scene-content-inner").children().not(".scene-add").css("display", "none");
                _searchPackage(filterValue, scenePackages);
            } else {
                //scenePackages.css("display", "block");
                $(".scene-content-inner").children().not(".scene-add").css("display", "block");
            }
        });
    };

    _ScenePackages.prototype.validate = function () {
        settings.HttpClient("GET", null, "/service/v1/scenario/category", function (results) {
            $('#appPackageForm').bootstrapValidator({
                message: $.i18n.prop('d_sacne_notValue'),
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                fields: {
                    packageName: {
                        container: 'tooltip',
                        validators: {
                            callback: {
                                callback: function (value, validator) {
                                    var bool = _isPackageNameEqual(value, results);
                                    if (!value) {
                                        return {
                                            valid: false,
                                            message: $.i18n.prop('d_add_ification_no')
                                        }
                                    } else if (bool) {
                                        return {
                                            valid: false,
                                            message: $.i18n.prop('d_add_sceneName_already')
                                        }
                                    }
                                    return true;
                                }
                            }
                        }
                    }
                }
            });

        });
    }

    var _isPackageNameEqual = function (targetId, results) {
        var boolName = false;
        $.each(results, function (index, value) {
            if (results[index]["cate_name"] == targetId) {
                boolName = true;
            }
        })
        return boolName;
    }

    return {
        ScenePackages: _ScenePackages
    }
})
