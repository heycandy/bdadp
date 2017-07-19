/**
 * Created by Hu on 2016/11/8.
 */
define([], function () {
    'use strict';
    return function () {

        function _init() {

            var cur_val = 1;
            if ($.cookie("intro_cookie_index") == cur_val) {
                return;
            }
            var intro = introJs();
            intro.setOptions({
                //对应的按钮
                prevLabel: $.i18n.prop('d_home_guide_lastStep'),
                nextLabel: $.i18n.prop('d_home_guide_nextStep'),
                skipLabel: $.i18n.prop('d_home_guide_skip'),
                doneLabel: $.i18n.prop('d_home_guide_end'),
                overlayOpacity: 0.8,
                exitOnOverlayClick: false,
                showStepNumbers: false,
                //对应的数组，顺序出现每一步引导提示
                steps: [
                    {
                        //第一步引导
                        //这个属性类似于jquery的选择器， 可以通过jquery选择器的方式来选择你需要选中的对象进行指引
                        element: '#navi-main',
                        //这里是每个引导框具体的文字内容，中间可以编写HTML代码
                        intro: '<span style="font-size: 12px">'
                               + $.i18n.prop('d_home_guide_homeGuide') + '</span>',
                        //这里可以规定引导框相对于选中对象出现的位置 top,bottom,left,right
                        position: 'bottom'
                    },
                    {
                        //第二步引导
                        element: '#navi-scene',
                        intro: '<span style="font-size: 12px">'
                               + $.i18n.prop('d_home_guide_scenarioGuide') + '</span>',
                        position: 'bottom'
                    },
                    {
                        //第三步引导
                        element: '#navi-dispatch',
                        intro: '<span style="font-size: 12px">'
                               + $.i18n.prop('d_home_guide_scheduleGuide') + '</span>',
                        position: 'bottom'
                    },
                    {
                        //第四步引导
                        element: '#navi-monitor',
                        intro: '<span style="font-size: 12px">'
                               + $.i18n.prop('d_home_guide_monitorGuide') + '</span>',
                        position: 'bottom'
                    },
                    {
                        //第五步引导
                        element: '#navi-tool',
                        intro: '<span style="font-size: 12px">'
                               + $.i18n.prop('d_home_guide_toorsGuide')
                               + '</span><div id="intro-detail" style="cursor: ' +
                               'pointer;color:#418ea7;font-style: oblique;font-weight: 600;display: block;margin-top: 5px; "><span>'
                               + $.i18n.prop('d_home_guide_mvGuide') + '</span></div>',
                        position: 'bottom'
                    }
                ]

            }).oncomplete(function () {
                //点击跳过按钮后执行的事件(这里保存对应的版本号到cookie,并且设置有效期为30天）
                $.cookie("intro_cookie_index", cur_val, {expires: 30});
            }).onexit(function () {
                //点击结束按钮后， 执行的事件
                $.cookie("intro_cookie_index", cur_val, {expires: 30});
            }).start();

            intro.onchange(function (targetElement) {
                if (targetElement.id == 'navi-scene') {
                    //$('#' + targetElement.id).trigger('click');
                }
            })

            intro.onafterchange(function (targetElement) {
                if (targetElement.id == 'navi-tool') {
                    setTimeout(function () {
                        _video();
                    }, 1000)
                }

            })
        }

        function _video() {
            $('#intro-detail').click(function () {
                var language = settings.testLangType();
                if ($('.introjs-overlay > div').length == 0) {
                    var _html = '<div style="margin: 15% 20% 15% 20%"><span class="ion-close-circled" style="visibility:hidden;font-size: 28px;float:right"></span>'
                                +
                                '<video id="example_video_1" class="video-js vjs-default-skin" controls preload="none" style="width: 100%;height: 413px;opacity: 1 !important;"'
                                +
                                'poster="img/poster.gif"' +
                                'data-setup="{}"> ' +
                                '<source src="" type="video/mp4" /> ' +
                                '</video></div>';
                    $('.introjs-overlay').append(_html);
                    $('.introjs-overlay').css('opacity', '1');
                    bind();
                }
                if (language.substring(0, 2) == "zh") {
                    $("#example_video_1 source").attr("src", "img/ark-zh.mp4");
                } else if (language.substring(0, 2) == "en") {
                    $("#example_video_1 source").attr("src", "img/ark-en.mp4");
                }
            })
        }

        function bind() {

            $('.introjs-overlay > div').on({
                mouseover: function () {
                    $('.introjs-overlay > div >span.ion-close-circled').css('visibility',
                                                                            'visible');
                },
                mouseout: function () {
                    $('.introjs-overlay > div >span.ion-close-circled').css('visibility', 'hidden');
                }
            })

            $('span.ion-close-circled').on({
                click: function () {
                    $('.introjs-overlay').empty();
                    $('.introjs-overlay').css('opacity', '0.8');
                },
                mouseover: function () {
                    this.style.color = 'rgb(197, 213, 218)';
                    this.style.cursor = 'pointer';
                },
                mouseout: function () {
                    this.style.color = '#333333'
                }
            })

        }

        return {
            init: _init
        }
    }

})
