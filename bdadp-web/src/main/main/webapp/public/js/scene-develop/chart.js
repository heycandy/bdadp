/**
 * Created by Administrator on 2016/11/18.
 */
define(["js/modeldialog", "js/scene-develop/scene-common"], function (initDialog, sceneCommon) {
    settings.interFun();
    var parentsElementGlobal, positionGlobal;
    var cartMenthod = {};

    cartMenthod._sceneCart = function () {
        var exportCount = 0;
        scenarioIdArr = [];
        sceneNameArr = [];
        exportObject = {};
        var checkboxArr = $(".scene-content-left .element-item input[type='checkbox']");
        var checkboxArrCheckedLen = $(".scene-content-left .element-item input:checked").length;
        if (checkboxArrCheckedLen < 1) {
            initDialog().initAlert($.i18n.prop('d_scene_dialog_export'),
                                   $.i18n.prop('d_disp_dialog_prompt'), 2000)

        } else {
            if ($("#exportNum").text() == 0) {
                exportCount++;
                $("#exportNum").text(exportCount);
                $("#exportNum").css("display", "block")
            } else {
                var exportNum = parseInt($("#exportNum").text()) + 1;
                $("#exportNum").text(exportNum);

            }

            var len = checkboxArr.length;
            for (var i = 0; i < len; i++) {
                if (checkboxArr[i].checked) {
                    var parentsElement = $(checkboxArr[i]).parents(".element-item").find(".scene-common");
                    scenarioIdArr.push($(checkboxArr[i]).parents(".element-item").attr("name"));
                    sceneNameArr.push($(checkboxArr[i]).parents(".element-item").find(".scene-name").attr("title"));
                    parentsElementGlobal = parentsElement;
                    var position = parentsElement.offset();
                    positionGlobal = position;
                    flyToCart();
                }
            }
            var sceneLengthName = sceneCommon.getStrLength(sceneNameArr[0], 10);
            var isOnlyScene = sceneNameArr.length == 1 ? sceneLengthName + sceneNameArr.length
                : sceneLengthName + $.i18n.prop('d_reminder_total') + sceneNameArr.length;
            exportObject.action = "export";
            exportObject.cate_id = "*";
            exportObject.scenario_id = scenarioIdArr;
            //exportObject.name = _changeArrayTOstr(sceneNameArr);
            exportObject.name = isOnlyScene;
            exportObject.resType = "zip";
            exportObject.userId = sessionStorage.getItem("userId");
            settings.HttpClient("POST", exportObject, "/service/v1/scenario/action",
                                function (data) {
                console.log(data);
                                    var scene_export = $("#header-reminder").find(".reminder-scenario");
                                    var url=settings.globalVariableUrl()+'/service/v1/resources/scenario/';
                var scene_export_html = "";
                                    scene_export_html +=
                                        '<li class="header-reminder" id=' + data + ' name='
                                        + exportObject.name + '><i id="export-wrap">' +
                                        '<div class="export-wrap" style="padding:10px 0;">' +
                                        '<span class="statwidth"><i class="span_reminder_export"></i><span>'
                                        + exportObject.name
                                        + '</span><i class="span_reminder_num"></i></span>' +
                                        '<span class="process-num"></span>' +
                                        '</div>' +
                                        '<div class="progress progress-tiao">' +
                                        '<div class="progress-bar progress-bar-danger progress-bar-striped" role="progressbar" aria-valuenow="80" aria-valuemin="0" aria-valuemax="100" style="width: 0">'
                                        +
                                        '</div>' +
                                        '</div>' +
                                        '<div class="reminder-btn">' +
                                        '<span class="statText"><span class="span_reminder_nowExport"></span></span>'
                                        +
                                        '<button type="button" class="btn btn-primary reminder-cancel btnCancel"><span class="span_reminder_cancel"></span></button>'
                                        +
                                        '<a href='+url + data
                                        + '/zip?name=' + exportObject.name
                                        + '><span class="span_reminder_download"></span></a>' +
                                        '</div>' +
                                        '</i></li>';
                scene_export.prepend(scene_export_html);

                                    settings.InterNation(['.span_reminder_export',
                                                          '.span_reminder_num',
                                                          '.span_reminder_nowExport',
                                                          '.span_reminder_cancel',
                                                          '.span_reminder_download'],
                                        ['d_reminder_scenarioExport', 'span_reminder_num',
                                         'd_reminder_beingExport', 'd_reminder_cancel',
                                         'd_reminder_download']);
                                })
        }
    };

    var flyToCart = function () {
        $('body').append('<div class="floating-cart"></div>');
        var cart = $('div.floating-cart');
        var parentsElementClone = parentsElementGlobal.clone();

        parentsElementClone.appendTo(cart);
        parentsElementClone.find("input[type='checkbox']").css("opacity", "0");
        parentsElementClone.find("label").css("opacity", "0");
        parentsElementClone.find(".scene-check").css("opacity", "0");
        $(cart).css({
            'top': positionGlobal.top + 'px',
            'left': positionGlobal.left + 'px'
        }).fadeIn('slow').addClass('moveToCart');
        setTimeout(function () {
            $('body').addClass('MakeFloatingCart');
            $(".dropdown-toggle .fa-bell-o").trigger("click");
        }, 800);
        setTimeout(function () {
            $('div.floating-cart').remove();
            $('body').removeClass('MakeFloatingCart');
            $(cart).removeClass('moveToCart');
        }, 1000);

    };
    var _changeArrayTOstr = function (newArray) {
        var newstr = newArray.join("_");
        return newstr;

    };
    $('.li a').click(function (e) {
        var li = $(".lia").parent().parent();
        li.slideUp();
        e.stopPropagation();
    });
    return cartMenthod;

});
