/**
 * Created by labo on 2016/8/26.
 */
define(['js/scene-develop/add-package', 'js/scene-develop/add-scene-new',
        'js/scene-develop/scene-common', 'js/scene-develop/chart', 'js/scene-develop/scene-import'],
    function (ScenePackages, Scene, SceneCommon, cartMenthod, sceneImportMethod) {
        'use strict';
        var ScenePictrue = function () {
        };
        var addScene = new Scene.Scene();
        var scenePackages = new ScenePackages.ScenePackages();

        ScenePictrue.prototype.clickToolsLeft = function () {                                   //toolbar click
            $(".scene-content-header .header-tools").find(".scene-left-tool").click(function () {
                var spanName = $(this).attr("name");
                switch (spanName) {
                    case "scene-checkall":
                    {
                        _sceneCheck();
                        break;
                }
                    case "scene-inverse":
                    {
                        _sceneInverse();
                        break;
                }
                    case "scene-import":
                    {
                        sceneImportMethod._sceneImport();

                        break;
                }
                    case "scene-export":
                    {
                        cartMenthod._sceneCart();
                        break;
                }
                    case "scene-delete":
                    {
                        _sceneDelete();
                        break;
                }
                    case "scene-refresh":
                    {
                        _sceneRfresh();
                        break;
                }
                }
            });
            var _sceneCheck = function () {
                if (!settings.containClass($("#scene-content .header-tools div span#allCheck")[0],
                                           "ion-android-checkbox-outline-blank")) {
                    $("#scene-content .header-tools div span#allCheck").removeClass("ion-android-checkbox-outline");
                    $("#scene-content .header-tools div span#allCheck").addClass("ion-android-checkbox-outline-blank");
                    $("#scene-content .element-item input[type='checkbox']").prop("checked", false);                                       //取消全选
                    SceneCommon.floatFun.sceneFloatHide($(".isotope")[0],
                        ["scene-cover-black", "scene-check", "img-toolbar"]);
                } else {
                    $("#scene-content .header-tools div span#allCheck").addClass("ion-android-checkbox-outline");
                    $("#scene-content .header-tools div span#allCheck").removeClass("ion-android-checkbox-outline-blank");
                    $("#scene-content .element-item input[type='checkbox']").prop("checked", 'true');//全选
                    SceneCommon.floatFun.sceneFloatShow($(".isotope")[0],
                        ["scene-cover-black", "scene-check", "img-toolbar"]);
                }
        };

        var _sceneInverse = function () {
            var checkboxArr = $(".scene-content-left .element-item input[type='checkbox']");
            var len = checkboxArr.length;
            var flag = false;
            for (var i = 0; i < len; i++) {
                if (checkboxArr[i].checked) {
                    var parentsElement = $(checkboxArr[i]).parents(".element-item")[0];
                    checkboxArr[i].checked = false;
                    SceneCommon.floatFun.sceneFloatHide(parentsElement,
                        ["scene-check", "img-toolbar","scene-cover-black"]);
                    flag = true;
                } else {
                    var parentsElement = $(checkboxArr[i]).parents(".element-item")[0];
                    checkboxArr[i].checked = true;
                    SceneCommon.floatFun.sceneFloatShow(parentsElement,
                        ["scene-check", "img-toolbar","scene-cover-black"]);
                }
            }
            if ( flag == true ) {
                $("div[name=scene-checkall].scene-left-tool span").removeClass("ion-android-checkbox-outline").addClass("ion-android-checkbox-outline-blank")
            } else {
                $("div[name=scene-checkall].scene-left-tool span").removeClass("ion-android-checkbox-outline-blank").addClass("ion-android-checkbox-outline")
            }

        };

            var _sceneDelete = function () {
                var scenario = $("#scene-content .element-item input[type='checkbox']:checked");
                var multiScenarioId = [];
                for (var i = 0; i < scenario.length; i++) {
                    multiScenarioId.push($(scenario[i]).parents(".element-item").attr("name"));
                }
                var scene = new Scene.Scene();
                scene.delMultiScenario(multiScenarioId, scenePackages);
        };

            var _sceneRfresh = function () {
                if (!settings.containClass($("#scene-content .header-tools div span#allCheck")[0],
                                           "ion-android-checkbox-outline-blank")) {
                    $("#scene-content .header-tools div span#allCheck").removeClass("ion-android-checkbox-outline");
                    $("#scene-content .header-tools div span#allCheck").addClass("ion-android-checkbox-outline-blank");
                }
                $(".scene-content-main.scene-content-left .isotope").remove();
                $(".scene-content-main.scene-content-left").append('<div class="isotope"></div>');
                $("#scene-content .header-tools div[name='scene-search'] input").val("");
                addScene.initAllScene();
        };

        };

        ScenePictrue.prototype.otherInit = function () {
        $(".scene-content-inner .scene-add").click(function () {                       //添加应用包
            var appPackage = new ScenePackages.ScenePackages();
            appPackage.addApp();
        })
        };

        ScenePictrue.prototype.clickToolsRight = function () {
            $(".scene-content-header.scene-right-toolbar").find(".pull-right").click(function () {
                var spanName = $(this).children("span").attr("name");
                switch (spanName) {
                    case "scene-refresh-scenePackage":
                    {
                        $(".scene-content-main.scene-content-right .scene-content-inner").children().not(".scene-add").remove();
                        scenePackages.initAllScenePackage();
                        $(".scene-content-header.scene-right-toolbar").find(".search-underline").val("");
                        scenePackages.searchScenePackage();
                        break;
                    }
            }
        })
        };

        var _sceneInit = function () {
        var scenePictrue = new ScenePictrue();
        scenePictrue.clickToolsLeft();
        scenePictrue.clickToolsRight();
        scenePictrue.otherInit();
        };

        return {
        sceneInit: _sceneInit
        }
    })
