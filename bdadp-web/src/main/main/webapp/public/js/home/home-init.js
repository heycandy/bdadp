/**
 * Created by Hu on 2016/10/19.
 */
define(['scene-edit/edit-ajax', 'home/home-radar', 'home/home-intro', 'home/home-onresize'],
    function (Ajax, Radar, Intro, Winresize) {
        'use strict';
        return function () {

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

            return {
                create: _create
            }
    }

})
