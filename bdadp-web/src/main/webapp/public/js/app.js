/**
 * Created by Hu on 2016/8/19.
 */
$(function () {
    settings.interFun();
    // var url = window.document.location.pathname.substr(0,window.document.location.pathname.lastIndexOf("/"));
    $(".skin-blue .wrapper, .skin-blue .main-sidebar, .skin-blue .left-side").each(function (index,
                                                                                             dom) {
        $(dom).css("background-color", "#2e4c7a");
    })

    $(".skin-blue .main-header .navbar").each(function (index, dom) {
        $(dom).css("background-color", "#2e4c7a");
    })

    var bodyWidth = document.body.clientWidth;
    var bodyHeight = $("#content-wrapper").height() + 40;
    $("#loadingDiv").css({"width": bodyWidth, "height": bodyHeight});
    $("#loadingDiv").find("img").css({
        "left": (bodyWidth - 100) / 2,
        "top": (bodyHeight - 100) / 2
    });

    settings.testLangType();
    if (settings.testLangType() == 'en' || settings.testLangType() == 'en_US'
        || settings.testLangType() == 'en_GB') {
        $('.nav-self').css('margin-left', '10%');
        $('#I_span_logoName').css('letter-spacing', '0');
    }
    !function initChromTabs() {
        var $chromeTabsShell = $('.chrome-tabs-shell')
        chromeTabs.init({
            $shell: $chromeTabsShell,
            minWidth: 45,
            maxWidth: 100
        });

        chromeTabs.closeTab = function ($shell, $tab) {
            $('section#' + $tab.data('tabData').data['sceneId']).remove(); 		// remove scene dom
            for (var i = 0; i < settings.currentSceneTab.editScenes.length; i++) {
                if ($tab.data('tabData').data['sceneId']
                    == settings.currentSceneTab.editScenes[i]['sceneId']) {
                    settings.currentSceneTab.editScenes.splice(i, 1);
                    break;
                }
            }
            if ($tab.hasClass('chrome-tab-current')) {
                if ($tab.prev().length) {
                    chromeTabs.setCurrentTab($shell, $tab.prev());
                } else if ($tab.next().length) {
                    chromeTabs.setCurrentTab($shell, $tab.next());
                }
            }
            $tab.remove();
            return chromeTabs.render($shell);
        }

        $chromeTabsShell.bind('chromeTabRender', function () {
            $("section.content").each(function (index, dom) {
                $(dom).css("display", "none");
            })
            var $currentTab = $chromeTabsShell.find('.chrome-tab-current');
            if ($currentTab.data('tabData') && $currentTab.data('tabData').data
                && $currentTab.data('tabData').data.sceneId) {
                $('footer.main-footer').css('display', 'none');
                var sce = settings.currentSceneTab.editScenes;
                for (var i = 0; i < sce.length; i++) {
                    if (sce[i].sceneId == $currentTab.data('tabData').data['sceneId']
                        && sce[i].sceneName == $currentTab.data('tabData').data['sceneName']) {
                        $('#' + sce[i].sceneId).css('display', 'block');
                        break;
                    }
                }
                return;
            }
            if ($currentTab.length) {
                closeRefresh();
                switch ($.trim($currentTab.text())) {
                    case $.i18n.prop('d_nav_homepage'):
                        $('footer.main-footer').css('display', 'block');
                        $('#main-content').css('display', 'block');
                        if ($('#scenarioLine').children().length !== 0
                            && $('#scenarioBar').children().length !== 0) {
                            $.each(echartsArray, function (index, value) {
                                for (var attr in value) {
                                    value[attr].resize();
                                }
                            });
                        }
                        if ($('#main-content').children().length == 0) {
                            $('#main-content').load('html/home/home.html', function () {
                                /*require(['./home/test'], function (testPie) {

                                 })*/
                            });
                        }
                        break;
                    case $.i18n.prop('d_nav_develop'):
                        $('footer.main-footer').css('display', 'none');
                        $('#scene-content').css('display', 'block');
                        $("#scene-content .search-underline").val("");
                        if ($('#scene-content').children().length == 0) {
                            $("#scene-content").load('html/scene-develop/scene-develop.html',
                                                     function () {
                                                         require(['./js/ark-develop.js'],
                                                             function (sceneDevelop) {
                                                                 new sceneDevelop().start();
                                                             })
                                                     });
                            if ($(".isotope").children().length > 0) {
                                $(".isotope").isotope('layout');
                            }
                            var bodyHeight = window.innerHeight;
                            var scrollBar = (bodyHeight - 195) + "px";
                            $('.scene-content-main .scene-content-inner').css("height", scrollBar);
                            $('.scene-content-main .isotope').css("overflow-y", "auto");
                            $('.scene-content-main .isotope').css("height", scrollBar);
                        } else {
                            var $container = $('.isotope').isotope({
                                itemSelector: '.element-item',
                                layoutMode: 'fitRows',
                                /*fitRows: {
                                 gutter: 20
                                 },*/
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
                            $container.isotope({filter: ""});
                            var bodyHeight = window.innerHeight;
                            var scrollBar = (bodyHeight - 195) + "px";
                            $('.scene-content-main .scene-content-inner').css("height", scrollBar);
                            $('.scene-content-main .isotope').css("overflow-y", "auto");
                            $('.scene-content-main .isotope').css("height", scrollBar);

                            $(".scene-content-inner").children().not(".scene-add").css("display",
                                                                                       "block");
                        }
                        break;
                    case $.i18n.prop('d_nav_tool'):
                        $('footer.main-footer').css('display', 'none');
                        $('#tool-content').css('display', 'block');
                        if ($('#tool-content').children().length == 0) {
                            $('#tool-content').load("./html/tools/tools.html", function () {
                                var bodyHeight = document.documentElement.clientHeight;
                                var scrollBar = (bodyHeight - 85) + "px";
                                $('#tool-content').css("height", scrollBar);
                                require(["./js/tools/tools"],
                                    function (tools) {
                                        tools().allTools();
                                    })
                                $.ajax({
                                    type: "GET",
                                    url: settings.globalVariableUrl()+"/service/v1/get/HbaseInfo/tables",
                                    success: function (msg) {
                                        //alert( "Data Saved: " + msg );
                                    },
                                    error: function (err) {
                                        $('.hbase').css("display", "none");
                                    }
                                });
                                $(".tools").load("./html/tools/hdfs.html", function () {
                                    require(["./js/tools/hdfs"], function (hdfsTool) {
                                        hdfsTool.hdfs_tools();
                                        settings.interFun();
                                    })
                                });
                            });
                        }
                        break;
                    case $.i18n.prop('d_nav_detail'):
                        $('footer.main-footer').css('display', 'none');
                        $('#monitor-content').css('display', 'block');
                        //$("#monitor-content").load("./html/monitor-detail/monitor-detail.html");
                        $("#monitor-content").load("./html/scene-monitor/scene-monitor.html",
                                                   function () {
                                                       require(['./js/ark-monitor'],
                                                           function (monitor) {
                                                               new monitor().start();
                                                           })
                                                   });
                        break;
                    case '集群':
                        $('#cluster-monitor').css('display', 'block');
                        break;
                    case $.i18n.prop('d_nav_schedule'):
                        $('footer.main-footer').css('display', 'none');
                        $('#dispatch-monitor').css('display', 'block');
                        $("#dispatch-monitor .search-underline").val("");
                        if ($('#dispatch-monitor').children().length == 0) {
                            $('#dispatch-monitor').load("./html/dispatchs/dispatch.html",
                                                        function () {
                                                            $("#loadingDiv").css("display",
                                                                                 "block");
                                                            $("#startTimeinputText").val("");
                                                            $("#overTimeinputText").val("");
                                                            require(['./js/ark-dispatch'],
                                                                function (dispatch) {
                                                                    new dispatch().start();
                                                                })
                                                        });
                        } else {
                            var $containerPacth = $('#dispatch-isotope').isotope({
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
                                //scenePackages.searchScenePackage();

                            });

                            require(["js/scene-develop/scene-common"], function (SceneCommon) {
                                SceneCommon.setLayoutLeft([$(".dispatch_content .sence-content-left .dispatch-isotope")[0]],
                                    185);
                                SceneCommon.setLayoutLeft([$(".dispatch_content .right_content")[0]],
                                    145);
                            })
                        }
                        break;
                    default :
                        $('footer.main-footer').css('display', 'none');
                        var sce = settings.currentSceneTab.editScenes;
                        for (var i = 0; i < sce.length; i++) {
                            if (sce[i].sceneId == $currentTab.data('tabData').data['sceneId']
                                && sce[i].sceneName
                                   == $currentTab.data('tabData').data['sceneName']) {
                                $('#' + sce[i].sceneId).css('display', 'block');
                                break;
                            }
                        }
                        break;
                }

            }
        });

    }();

    function closeRefresh() {
        $(".chrome-tab-close").click(function () {
            if ($(this).prev().text() == $.i18n.prop('d_nav_schedule')) {
                $('#dispatch-monitor').empty();
            } else if ($(this).prev().text() == $.i18n.prop('d_nav_tool')) {
                $('#tool-content').empty();
            } else if ($(this).prev().text() == $.i18n.prop('d_nav_develop')) {
                $('#scene-content').empty();
            } else if ($(this).prev().text() == $.i18n.prop('d_nav_detail')) {
                $('#monitor-content').empty();
            }
        })
    }

    function _navi(id, _title, contentId) {
        var $shell = $('.chrome-tabs-shell');
        $("#" + id).click(function () {
            $("section.content").each(function (index, dom) {
                $(dom).css("display", "none");
            })
            var result = $shell.find('.chrome-tab-title').filter(function (index, dom) {
                // 函数内的this === dom
                var title = $(dom).html();
                return title == _title
            })
            if (result.length == 0) {
                var faviUrl = '';
                if (id == 'navi-main') {
                    faviUrl = 'img/component/homefavicon.png';
                }
                chromeTabs.addNewTab($shell, {
                    favicon: faviUrl,
                    title: _title,
                    data: {
                        timeAdded: +new Date()
                    }
                });

                $('#' + contentId).css('display', 'block');
            } else {
                var $tab = $shell.find('.chrome-tab').filter(function (index, dom) {
                    return $(dom).find('.chrome-tab-title').html() == _title;
                })
                chromeTabs.setCurrentTab($shell, $tab);
                $('#' + contentId).css('display', 'block');
            }
            if ($.trim($('.chrome-tab-current').find('.chrome-tab-title').html())
                == $.i18n.prop('d_nav_homepage')) {
                var lang = (navigator.languages) ? navigator.languages[0] : (navigator.language
                                                                             || navigator.userLanguage /* IE */
                                                                             || 'en');
                if (lang == 'zh' || lang == "zh_CN") {
                    $('a.navbar-brand>span').css('font-family', '微软雅黑').css('letter-spacing',
                                                                            '5px');
                    $('.chrome-tab-current').find('.chrome-tab-title').parent().find('.chrome-tab-close').css('display',
                                                                                                              'none').parent().find('.chrome-tab-title').css('padding-left',
                                                                                                                                                             '30px').css('letter-spacing',
                                                                                                                                                                         '12px')
                } else {
                    $('.chrome-tab-current').find('.chrome-tab-title').parent().find('.chrome-tab-close').css('display',
                                                                                                              'none').parent().find('.chrome-tab-title').css('padding-left',
                                                                                                                                                             '30px')
                }

            }
        })

    }

    !function initNavi() {
        var roleName = sessionStorage.roleName;
        switch (roleName) {
            case "admin":
                break;
            case "dev":
                $("#navbar-collapse ul li").eq(2).css("display", "none");
                $("#navbar-collapse ul li").eq(3).css("display", "none");
                break;
            case "oper":
                $("#navbar-collapse ul li").eq(1).css("display", "none");
                $("#navbar-collapse ul li").eq(4).css("display", "none");
                break;
            default :
                $("#navbar-collapse ul li").eq(1).css("display", "none");
                $("#navbar-collapse ul li").eq(2).css("display", "none");
                $("#navbar-collapse ul li").eq(3).css("display", "none");
                $("#navbar-collapse ul li").eq(4).css("display", "none");
                break;
        }
        _navi('navi-main', $.i18n.prop('d_nav_homepage'), 'main-content');
        _navi('navi-scene', $.i18n.prop('d_nav_develop'), 'scene-content');
        _navi('navi-tool', $.i18n.prop('d_nav_tool'), 'tool-content');
        _navi('navi-monitor', $.i18n.prop('d_nav_detail'), 'monitor-content');
        //   _navi('navi-cluster', '集群', 'cluster-monitor');
        _navi('navi-dispatch', $.i18n.prop('d_nav_schedule'), 'dispatch-monitor');
        $("#navi-main").trigger('click');

    }();

    $("#ajax-test").click(function () {
        var url_customer = settings.globalVariableUrl()+'/service/v1/components/business'
            , url_system = settings.globalVariableUrl()+'/service/v1/components/base'
            , url_scene = settings.globalVariableUrl()+'/service/v1/scenario'
            , ajax_cus = $.ajax({
                type: 'GET',
                async: false,
                url: url_customer,
            })
            , ajax_sce = $.ajax({
                type: 'GET',
                async: false,
                url: url_scene,
            })
            , ajax_sys = $.ajax({
                type: 'GET',
                async: false,
                url: url_system,
            });
        $.when(ajax_cus, ajax_sys, ajax_sce).done(function (cusData, sysData, sceData) {
            var a = cusData;
            var b = sysData;
            var c = sceData;
            console.log(a);
            console.log(b);
            console.log(c);
        }).fail(function (data) {
            alert("data exception!");
        })
    })

    settings.socket.getInstance().on("msg", function (data) {
        var exportId = data.result.id;
        var exportText = data.result.progress;
        var exportState = data.result.state;
        var progressExport = $('#' + exportId).find(".progress-bar");

        $("#" + exportId).find(".reminder-btn .btnCancel").attr("name", exportState);
        if (exportState == 2) {
            var isScenario = data.result.action.split(".")[0];
            var downAddress = settings.globalVariableUrl()+"/service/v1/resources/" + isScenario + "/" + exportId + "/zip?name="
                              + encodeURIComponent(data.result.name);
            $("#" + exportId).find(".exportDownload a").attr("href", downAddress);
            $("#"
              + exportId).find(".reminder-btn .btnCancel").text($.i18n.prop('d_reminder_remove'));
            $("#"
              + exportId).find(".reminder-btn span.statText").text($.i18n.prop('d_reminder_alreadyComplete'));
            $("#" + exportId).find(".exportDownload").css({
                "background": "#446087",
                "opacity": "1"
            });
            $("#" + exportId).find(".exportDownload a").hover(function () {
                $(this).css("background", "#446087");
            }, function () {
                $(this).css({"color": "#fff", "text-decoration": "none"});
            })
        } else if (exportState == 3) {
            // $("#" + exportId).find(".reminder-btn
            // span.statText").text($.i18n.prop('d_reminder_alreadyFail'));
            $("#" + exportId).find(".exportDownload a").attr("href", "#");
        }
        else {
            $("#" + exportId).find(".exportDownload a").attr("href", "#");
        }
        $('#' + exportId).find(".process-num").text(parseInt(exportText * 100) + '%');
        progressExport.css("width", parseInt(exportText * 100) + '%');

    });

    $("#header-reminder .all-remove").click(function () {
        var allId = $(".reminder-scenario li");
        var idArr = [];
        for (var i = 0; i < allId.length; i++) {
            var btnText = $(allId[i]).find(".btn.btn-primary.btnCancel").text();
            if (btnText == $.i18n.prop('d_reminder_remove')) {
                var id = $(allId[i]).attr("id");
                idArr.push(id);
            }
        }
        var removeObject = {};
        removeObject.action = "remove";
        removeObject.iterable = idArr;
        settings.HttpClient("POST", removeObject, "/service/v1/queue/task/action", function (data) {
            for (var i = 0; i < idArr.length; i++) {
                $("#" + idArr[i]).remove();
            }
            var len = idArr.length
            var infoNum = $("#exportNum").text() - len;
            $("#exportNum").text(infoNum);
            if (infoNum <= 0) {
                $("#exportNum").css("display", "none")
            }

        });
    });

    var getAllHostoryInfo = function () {
        settings.HttpClient("GET", null, "/service/v1/queue/task", function (data) {
            var export_history = $("#header-reminder").find(".reminder-scenario");
            var export_history_html = "";
            var historyInfo = data;
            for (var i = 0; i < historyInfo.length; i++) {
                var aa = historyInfo[i].action.split(".");
                var isImport = aa[1] == "export" ? "progress-bar-danger" : "progress-bar-warning";
                //var isImporttext = aa[1] == "export" ? $.i18n.prop('d_reminder_scenarioExport') :
                // $.i18n.prop('d_reminder_scenarioImport');
                var isImporttext;
                if (aa[1] == "export") {
                    if (aa[0] == "tools") {
                        var sceneLengthName = historyInfo[i].name.substring(0, 10) + "...";
                        isImporttext =
                            '<span class="statwidth"><span>' + sceneLengthName
                            + '</span><i class=""></i></span>';
                    } else {
                        isImporttext =
                            '<span class="statwidth">' + $.i18n.prop('d_reminder_scenarioExport')
                            + '<span>' + historyInfo[i].name
                            + '</span><i class="span_reminder_num"></i></span>'
                    }
                } else {
                    isImporttext =
                        '<span class="statwidth">' + $.i18n.prop('d_reminder_scenarioImport')
                        + '<span>' + historyInfo[i].name
                        + '</span><i class="span_reminder_num"></i></span>'
                }
                var isImporthref = aa[1] == "export" ? $.i18n.prop('d_reminder_download')
                    : $.i18n.prop('d_reminder_cancel');
                var changeBtn = historyInfo[i].state == 1 || 0 ? $.i18n.prop('d_reminder_cancel')
                    : $.i18n.prop('d_reminder_remove');
                if (aa[1] == "export") {
                    var href = historyInfo[i].state == 2 ? settings.globalVariableUrl()+"/service/v1/resources/" + aa[0] + "/"
                                                           + historyInfo[i].id + '/zip?name='
                                                           + encodeURIComponent(historyInfo[i].name)
                        : "#";
                } else {
                    var href = "#";
                }
                var isDown = aa[1] == "export"
                    ? '<button type="button" class="btn btn-primary btnCancel" name="'
                      + historyInfo[i].state + '">' + changeBtn + '</button>' +
                      '<a href="' + href + '">' + isImporthref + '</a>'
                    : '<button style="margin-left: 24%" type="button" class="btn btn-primary btnCancel" name="'
                      + historyInfo[i].state + '">' + changeBtn + '</button>';

                var exporting;
                if (historyInfo[i].state == 0 || historyInfo[i].state == 1) {
                    exporting =
                        aa[1] == "export" ? $.i18n.prop('d_reminder_beingExport')
                            : $.i18n.prop('d_reminder_beingImport');
                } else if (historyInfo[i].state == 2) {
                    exporting =
                        aa[1] == "export" ? $.i18n.prop('d_reminder_exportComplete')
                            : $.i18n.prop('d_reminder_importComplete');

                } else if (historyInfo[i].state == 3) {
                    exporting =
                        aa[1] == "export" ? $.i18n.prop('d_reminder_exportFail')
                            : $.i18n.prop('d_reminder_importFail');
                } else {
                    exporting = $.i18n.prop('d_reminder_canceled');
                }

                export_history_html +=
                    '<li class="header-reminder" id=' + historyInfo[i].id + ' name='
                    + historyInfo[i].name + '><i id="export-wrap">' +
                    '<div class="export-wrap" style="padding:10px 0;">' + isImporttext +
                    '<span class="process-num">' + parseInt(historyInfo[i].progress * 100)
                    + '%</span>' +
                    '</div>' +
                    '<div class="progress progress-tiao">' +
                    '<div class="progress-bar ' + isImport
                    + ' progress-bar-striped" role="progressbar" aria-valuenow="80" aria-valuemin="0" aria-valuemax="100" style="width:'
                    + parseInt(historyInfo[i].progress * 100) + '%">' +
                    '</div>' +
                    '</div>' +
                    '<div class="reminder-btn">' +
                    '<span class="statText">' + exporting + '</span>' +
                    '' + isDown + '' +
                    '</div>' +
                    '</i></li>'

            }
            export_history.prepend(export_history_html);
            var isDownArr = export_history.find("li.header-reminder");
            for (var i = 0; i < isDownArr.length; i++) {
                if ($(isDownArr[i]).find(".btnCancel").attr("name") == 2) {
                    $(isDownArr[i]).find(".exportDownload").css({
                        "background": "#446087",
                        "opacity": "1"
                    });
                    $(isDownArr[i]).find(".exportDownload a").hover(function () {
                        $(this).css("background", "#446087");
                    }, function () {
                        $(this).css({"color": "#fff", "text-decoration": "none"});
                    })
                }

            }
            settings.InterNation(['.span_reminder_num'], ['span_reminder_num']);

            $("#header-reminder").on("click",
                                     ".reminder-scenario .header-reminder .reminder-btn button.btn.btn-primary.btnCancel",
                                     function () {
                                         if ($(this).text() === $.i18n.prop('d_reminder_cancel')) {
                                             var _id = $(this).parents(".header-reminder").attr("id");
                                             var cancelObject = {};
                                             var cancelArr = [];
                                             cancelArr.push(_id);
                                             cancelObject.action = "cancel";
                                             cancelObject.iterable = cancelArr;
                                             settings.HttpClient("POST", cancelObject,
                                                                 "/service/v1/queue/task/action",
                                                                 function (data) {
                                                                     $("#"
                                                                       + _id).find(".reminder-btn .btnCancel").text($.i18n.prop('d_reminder_remove'));
                                                                     $("#"
                                                                       + _id).find(".reminder-btn span.statText").text($.i18n.prop('d_reminder_canceled'));
                                                                 })
                                         } else if ($(this).attr("name") == 2) {
                                             var infoNum = $("#exportNum").text() - 1;
                                             $("#exportNum").text(infoNum);
                                             if ($("#exportNum").text() == 0) {
                                                 $("#exportNum").css("display", "none")
                                             }
                                             var _id = $(this).parents(".header-reminder").attr("id");
                                             var _name = $(this).parents(".header-reminder").attr("name");
                                             var removeObject = {};
                                             var removeArr = [];
                                             removeArr.push(_id);
                                             removeObject.action = "remove";
                                             removeObject.iterable = removeArr;
                                             settings.HttpClient("POST", removeObject,
                                                                 "/service/v1/queue/task/action",
                                                                 function (data) {
                                                                     $("#" + _id).remove();
                                                                 });

                                         } else if ($(this).attr("name") == 3) {
                                             var infoNum = $("#exportNum").text() - 1;
                                             $("#exportNum").text(infoNum);
                                             if ($("#exportNum").text() == 0) {
                                                 $("#exportNum").css("display", "none")
                                             }
                                             var _id = $(this).parents(".header-reminder").attr("id");
                                             var _name = $(this).parents(".header-reminder").attr("name");
                                             var removeObject = {};
                                             var removeArr = [];
                                             removeArr.push(_id);
                                             removeObject.action = "remove";
                                             removeObject.iterable = removeArr;
                                             settings.HttpClient("POST", removeObject,
                                                                 "/service/v1/queue/task/action",
                                                                 function (data) {
                                                                     $("#" + _id).remove();
                                                                     $("#"
                                                                       + _id).find(".reminder-btn .btnCancel").text($.i18n.prop('d_reminder_remove'));
                                                                 });

                                         }
                                     });
            var numlength = historyInfo.length;
            if (numlength > 0) {
                $("#exportNum").css("display", "block");
                $("#exportNum").text(numlength);
            } else {
                $("#exportNum").css("display", "none")
            }

        });

        $("#header-reminder").click(function (e) {
            e.stopPropagation();
        });

    };
    getAllHostoryInfo();
});


