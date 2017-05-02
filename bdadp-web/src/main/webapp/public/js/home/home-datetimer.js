/**
 * Created by Hu on 2016/10/19.
 */
$(function () {
    var start = moment().subtract(29, 'days');
    var end = moment();

    function callback(start, end, label) {
        //时间插件
        $('#reportrange span').html(start.format('YYYY-MM-DD HH:mm:ss') + '  -  '
                                    + end.format('YYYY-MM-DD HH:mm:ss'));
        require(['scene-edit/edit-ajax', 'home/home-radar', 'home/home-intro', 'home/home-onresize', "./home/home-bar", "./home/home-line", "./home/home-cicle",
                 "./home/echarts-create"],
            function (Ajax, Radar, Intro, Winresize, barCreate, lineCreate, pieCreate, echartsFactor) {

                console.log("---------------");
                laboPraph(barCreate, lineCreate, pieCreate, echartsFactor);
                /*new Home().create(start.format('YYYY-MM-DD HH:mm:ss'),
                    end.format('YYYY-MM-DD HH:mm:ss'));*/
                function circleMenu() {
                }

                circleMenu.prototype.load = function (data) {
                    if (data == 'undefinded' || data.length == 0)return;
                    $('.senario-circle-menu > .trigger').trigger('click');
                    $('div.senario-circle-menu').empty();
                    var _html = '<ul class="cicle-menu"></ul>';
                    for (var i = 0; i < data.length; i++) {
                        var _src = data[i].scenario.scenario_extra,
                            _scenarioid = data[i].scenario.scenario_id,
                            _scenarioname = data[i].scenario.scenario_name,
                            _delta = data[i].weight * 50, _width = parseInt(_delta + 25) + 'px',
                            _li = '<li style="background: transparent"><img style="border:1px solid darkgray; border-radius: 50%;max-width: 61px; '
                                +
                                'min-width: 15px; width:' + _width
                                + '; background-color: transparent"' +
                                'data-scenarioid="' + _scenarioid + '" src="' + _src + '" title="'
                                + _scenarioname + '"/></li>';
                        _html = $(_html).append(_li)[0];
                    }
                    $('.senario-circle-menu').append(_html);
                    $('.senario-circle-menu').mobilyblocks();
                    $('.senario-circle-menu > .trigger').trigger('click');

                    $('ul.cicle-menu li img').each(function (index, element) {
                        $(element).click(function () {
                            var scenario_id = $(this).data('scenarioid'),
                                scenario_data = $.grep(data, function (obj, i) {
                                    return scenario_id == obj.scenario.scenario_id;
                                })
                            new Radar().initRadar('radarChart',
                                [scenario_data[0].scenario.scenario_name], scenario_data[0].value);
                        })
                    })
                    if (data.length > 0 && data.sort(function (a, b) {
                            return b.weight - a.weight;
                        })) {
                        var _max = data[0];
                        $('ul.cicle-menu li img').each(function (index, element) {
                            if ($(this).data('scenarioid') == _max.scenario.scenario_id) {
                                $(element).trigger('click');
                            }
                        })
                    }
                    // bindMouseEvent();  /*transform scale 方法取代*/
                    getTotal();
                    new Intro().init();
                    new Winresize().init();
                }

                function bindMouseEvent() {
                    $('ul.cicle-menu li img').each(function (index, element) {
                        $(element).on({
                                'mouseover': function () {
                                    var _width = $(this).width() + 15;
                                    $(this).css('width', _width);
                                },
                                'mouseout': function () {
                                    var _width = $(this).width() - 15;
                                    $(this).css('width', _width);
                                }
                            }
                        )
                    })
                }

                var _create = function (startTime, endTime) {
                    var url = '/service/v1/scenario/visual/radar?startTime=' + startTime + '&endTime='
                        + endTime;
                    new Ajax().superAjaxOne(url, 'GET', function (response) {
                        if (response.resultCode == 0) {
                            var _result = response.result;
                            new circleMenu().load(_result);
                        }
                    })

                }

                function getTotal() {
                    new Ajax().superAjaxTwo('/service/v1/scenario/visual/totalcount',
                        '/service/v1/scenario/visual/totalexecnt', 'GET',
                        function (scenarioCount, executionCountD) {
                            if (scenarioCount[1] == 'success') {
                                var sTotal = scenarioCount[0].result;
                                var count = 0;
                                var sInterval = setInterval(function () {
                                    var c = fmoney(count, 0).substring(0,
                                        fmoney(count,
                                            0).indexOf('.'));
                                    $('span.senario-count').html(c);
                                    count++;
                                    if (count == sTotal) {
                                        var _c = fmoney(sTotal, 0).substring(0,
                                            fmoney(sTotal,
                                                0).indexOf('.'));
                                        $('span.senario-count').html(_c);
                                        clearInterval(sInterval);
                                    }
                                }, 1)
                            }
                            if (executionCountD[1] == 'success') {
                                var ecount = 0;
                                var eTotal = executionCountD[0].result;
                                var eInterval = setInterval(function () {
                                    var ec = fmoney(ecount, 0).substring(0,
                                        fmoney(ecount,
                                            0).indexOf('.'));
                                    $('span.execution-count').html(ec);
                                    if (eTotal < 200) {
                                        ecount++;
                                    } else {
                                        ecount =
                                            ecount + parseInt(eTotal / 500 + 10);
                                    }
                                    if (ecount >= eTotal) {
                                        var _ecount = fmoney(eTotal, 0).substring(0,
                                            fmoney(eTotal,
                                                0).indexOf('.'));
                                        $('span.execution-count').html(_ecount);
                                        clearInterval(eInterval);
                                    }
                                }, 1)
                            }
                        })
                }

                function fmoney(s, n) {
                    n = n > 0 && n <= 20 ? n : 2;
                    s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
                    var l = s.split(".")[0].split("").reverse(),
                        r = s.split(".")[1],
                        t = "";
                    for (var i = 0; i < l.length; i++) {
                        t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
                    }
                    return t.split("").reverse().join("") + "." + r;
                }
                _create(start.format('YYYY-MM-DD HH:mm:ss'),
                    end.format('YYYY-MM-DD HH:mm:ss'));




                //HomeLabo(start.format('YYYY-MM-DD HH:mm:ss'), end.format('YYYY-MM-DD HH:mm:ss'));
        })

        /* $('.daterangepicker>.ranges>ul>li').each(function(index,element){
         if($(element).attr('class')=='active'){
         $(element).removeAttr('class')
         }
         })*/
    }

    var laboPraph = function (barCreate, lineCreate, pieCreate, echartsFactor) {
        var arrParams = [[lineCreate, "scenarioLine"], [pieCreate, "scenarioPie"],
            [barCreate, "scenarioBar"]];
        var creatEcharts = function (option, id) {
            var sceneStatistics = new echartsFactor.create(option);
            sceneStatistics.init("#" + id);
        };
        $.each(arrParams, function (index, item) {
            var lineInstance = new this[0].create(start.format('YYYY-MM-DD HH:mm:ss'),
                                                  end.format('YYYY-MM-DD HH:mm:ss'), this[1]);
            if (this[1] !== "scenarioPie") {
                var myChart = echarts.init(document.getElementById(this[1]));
                myChart.showLoading({                                                     // 过渡---------------------
                    text: $.i18n.prop('d_home_readData'),
                    effect: "bubble"
                });                                                                       //loading
                                                                                          // jiazai
            }
            lineInstance.init(this[1], echartsFactor, creatEcharts);
        })
    }

    var lang = (navigator.languages) ? navigator.languages[0] : (navigator.language
                                                                 || navigator.userLanguage /* IE */
                                                                 || 'en');
    if (lang == 'zh' || lang == "zh-CN") {
        $('#reportrange').daterangepicker({
            startDate: start,
            endDate: end,
            ranges: {
                '今天': [moment(), moment()],
                '昨天': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                '一周': [moment().subtract(6, 'days'), moment()],
                '一个月': [moment().subtract(29, 'days'), moment()]
            },
            opens: 'right', //日期选择框的弹出位置
            buttonClasses: ['btn btn-default'],
            applyClass: 'btn-small btn-primary blue',
            cancelClass: 'btn-small',
            format: 'YYYY-MM-DD HH:mm:ss', //控件中from和to 显示的日期格式
            separator: ' to ',
            locale: {
                applyLabel: '确定',
                cancelLabel: '取消',
                fromLabel: '起始时间',
                toLabel: '结束时间',
                customRangeLabel: '自定义',
                daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
                monthNames: ['一月', '二月', '三月', '四月', '五月', '六月',
                             '七月', '八月', '九月', '十月', '十一月', '十二月'],
                firstDay: 1
            }

        }, callback);
    } else {
        $('#reportrange').daterangepicker({
            startDate: start,
            endDate: end,
            ranges: {
                'Today': [moment(), moment()],
                'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                'Last 7 Days': [moment().subtract(6, 'days'), moment()],
                'Last 30 Days': [moment().subtract(29, 'days'), moment()],
            },
            opens: 'right', //日期选择框的弹出位置
            buttonClasses: ['btn btn-default'],
            applyClass: 'btn-small btn-primary blue',
            cancelClass: 'btn-small',
            format: 'YYYY-MM-DD HH:mm:ss' //控件中from和to 显示的日期格式
        }, callback);
    }

    callback(start, end);
});
