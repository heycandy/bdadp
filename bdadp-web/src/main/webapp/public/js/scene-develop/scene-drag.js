/**
 * Created by labo on 2016/8/30.
 */

var SceneDarg = (function () {
    settings.interFun();
    return {
        dragInit: function (dialog) {
            $(".scene-content-main.scene-content-left").on("dragstart", ".scene-cover-black",
                                                           function (ev) {
                                                               SceneDarg.dragStart(ev);
                                                           });
            $(".scene-content-inner").on("dragover", ".row.row-no-margin.scene-category-main",
                                         function (ev) {
                                             SceneDarg.dragover(ev);
                                         });
            $(".scene-content-inner").on("drop", ".row.row-no-margin.scene-category-main",
                                         function (ev) {
                                             SceneDarg.drop(ev, dialog);
                                         });
        },
        dragStart: function (ev) {
            var obj = {};
            var sceneName = $(ev.target).parents(".element-item").find(".scene-name").text();
            var sceneId = $(ev.target).parents(".element-item").attr("name");
            var sceneImg = $(ev.target).parents(".element-item").find(".scene-common").css("background-image").replace("url(\"",
                                                                                                                       "").replace("\")",
                                                                                                                                   "");
            obj.sceneName = sceneName;
            obj.sceneId = sceneId;
            obj.sceneImg = sceneImg;
            ev.originalEvent.dataTransfer.effectAllowed = 'move';
            ev.originalEvent.dataTransfer.setData("Text",JSON.stringify(obj),"sceneInfo");    //transfer
                                                                                        // data
            ev.originalEvent.dataTransfer.setDragImage($("<img src='" + sceneImg + "'>")[0], 80,
                                                       80);
        },
        dragover: function (ev) {
            SceneDarg.cancel(ev);
        },
        dragenter: function (ev) {
            SceneDarg.cancel(ev);
        },
        drop: function (ev, dialog) {
            SceneDarg.cancel(ev);
            var sceneName, sceneId, sceneImg, paramObj = {};
            var sceneInfo = JSON.parse(ev.originalEvent.dataTransfer.getData("Text","sceneInfo"));
            var targetId = $(ev.target).hasClass("scene-category-main") ? $(ev.target).attr("id")
                : $(ev.target).parents(".scene-category-main").attr("id");
            sceneName = sceneInfo["sceneName"];
            sceneId = sceneInfo["sceneId"];
            sceneImg = sceneInfo["sceneImg"];
            paramObj["action"] = "cate_add";
            paramObj["cate_id"] = targetId;
            paramObj["scenario_id"] = [sceneId];
            settings.HttpClient("GET", null, "/service/v1/scenario/category", function (results) {
                $.each(results, function (index, value) {
                    if (results[index]["cate_id"] == targetId) {
                        if (results[index]["details"].length == 0) {
                            settings.HttpClient("POST", paramObj, "/service/v1/scenario/action",
                                                function () {
                                                    SceneDarg.append(sceneImg, targetId, "block",
                                                                     sceneId);
                                                });
                        } else {
                            var boolDrag = false;
                            var len = results[index]["details"].length;
                            $.each(results[index]["details"], function (item, value) {
                                if (results[index]["details"][item]["scenario_id"] === sceneId) {
                                    boolDrag = true;
                                }
                            })
                            if (!boolDrag) {
                                settings.HttpClient("POST", paramObj, "/service/v1/scenario/action",
                                                    function () {
                                                        if (len >= 8) {
                                                            SceneDarg.append(sceneImg, targetId,
                                                                             "none", sceneId);
                                                        } else {
                                                            SceneDarg.append(sceneImg, targetId,
                                                                             "block", sceneId);
                                                        }
                                                    });
                            } else {
                                dialog().initAlert($.i18n.prop('d_scene_alreadyExists'));
                            }
                        }
                    }
                });
            });
        },
        append: function (sceneImg, targetId, isvisible, sceneId) {
            var imgSrc = sceneImg;
            var html = '<div class="col-md-3 col-lg-3 row-no-padding" style="display: ' + isvisible
                       + '" name="' + sceneId + '">'
                       + '<img class="category-img" src="' + imgSrc + '"/>'
                       + '</div>';
            $("#" + targetId).find(".category-contain").append(html);
        },
        cancel: function (e) {
            if (e.preventDefault) {
                e.preventDefault();
            }
            return false;
        }
    }
})();

