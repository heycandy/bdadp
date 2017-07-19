/**
 * Created by labo on 2016/10/26.
 */

define([], function () {
    'use strict';
    var _ScenePie = function (startTime, endTime, id) {
        var canvas = document.getElementById(id),
            context = canvas.getContext("2d");
        var speed = 0,
            speed1 = 0,
            speed2 = 0;
        var width = $(".info-box.senario-pie").css("width"),
            rad = Math.PI * 2 / 100;
        canvas.width = width.substring(0, width.length - 2);
        this._startTime = startTime;
        this._endTime = endTime;
        this._drawBase = {
            blueCircle: function (n, R, centerX, centerY, color) {                                  //绘制内圈
                context.save();
                context.strokeStyle = color;
                context.lineWidth = 9;
                context.beginPath();
                context.arc(centerX, centerY, R, -Math.PI / 2, -Math.PI / 2 + n * rad, false);
                context.stroke(); //绘制
                context.closePath(); //路径结束
                context.restore();
            },
            whiteCircle: function (R, centerX, centerY) {                                                 //绘制外圈
                context.save();
                context.beginPath();
                context.lineWidth = 10;
                context.strokeStyle = "#f2f2f2";
                context.arc(centerX, centerY, R, 0, Math.PI * 2, false);
                context.stroke();
                context.closePath();
                context.restore();
            },
            text: function (n, centerX, centerY, circleName) {                                            //百分比文字绘制
                context.save();
                //context.fillStyle = "red";
                context.font = "15px Arial";
                //绘制字体，并且指定位置
                context.fillText(n.toFixed(0) + "%", centerX - 11, centerY + 3);
                context.font = "15px 微软雅黑";
                context.fillText(circleName, centerX - 33, centerY + 98);
                //context.stroke(); //执行绘制
                context.restore();
            },
            clearRect: function (x, y, dx, dy) {
                context.clearRect(x, y, dx, dy);
            },
            drawGraph: function (centerX, centerY, R, color, circleName, x, y, dx, dy, score,
                                 timerNum) {
                var self = this;
                return function () {
                    var timer = "timer" + timerNum;
                    timer = setInterval(function () {
                        self.drawFrame(centerX, centerY, R, color, circleName, x, y, dx, dy, score,
                                       timer);
                    }, 1);
                }
            },
            drawFrame: function (centerX, centerY, R, color, circleName, x, y, dx, dy, score,
                                 timer) {
                this.clearRect(x, y, dx, dy);
                this.whiteCircle(R, centerX, centerY);
                if (circleName === $.i18n.prop('d_home_scenarioUtilization')) {
                    if (score < 1) {
                        clearInterval(timer);
                    } else {
                        speed += 1;
                        this.blueCircle(speed, R, centerX, centerY, color);
                        if (parseInt(speed) >= score) {
                            clearInterval(timer);
                        }
                    }
                    this.text(speed, centerX, centerY, circleName);
                } else if (circleName === $.i18n.prop('d_home_scenarioSuccess')) {
                    if (score < 1) {
                        clearInterval(timer);
                    } else {
                        speed1 += 1;
                        this.blueCircle(speed1, R, centerX, centerY, color);
                        if (parseInt(speed1) >= score) {
                            clearInterval(timer);
                        }
                    }
                    this.text(speed1, centerX, centerY, circleName);
                } else {
                    if (score < 1) {
                        clearInterval(timer);
                    } else {
                        speed2 += 1;
                        this.blueCircle(speed2, R, centerX, centerY, color);
                        if (parseInt(speed2) >= score) {
                            clearInterval(timer);
                        }
                    }
                    this.text(speed2, centerX, centerY, circleName);
                }

            }
        };
    };

    _ScenePie.prototype.getOptions = function (id) {
        var _self = this;

        settings.HttpClient("GET", null,
                            "/service/v1/scenario/visual/cycle/?startTime=" + this._startTime
                            + "&endTime=" + this._endTime, function (response) {
                var timer1 = null;
                var timer2 = null;
                var timer3 = null;
                var canvas = document.getElementById(id);
                var centerX = canvas.width / 6,
                    centerY = canvas.height / 2,
                    R = 50,
                    color = "#fcd76a",
                    circleName = $.i18n.prop('d_home_scenarioUtilization'),
                    x = 0,                      // 0, 0, canvas.width / 3, canvas.height
                    y = 0,
                    dx = canvas.width / 3,
                    dy = canvas.height,
                    time = null;
                var centerX1 = canvas.width / 2,
                    color1 = "#a696ce",
                    circleName1 = $.i18n.prop('d_home_scenarioSuccess'),
                    x1 = canvas.width / 3,
                    y1 = 0,               //canvas.width / 3, 0, canvas.width * 1 / 3, canvas.height
                    dx1 = canvas.width * 1 / 3,
                    dy1 = canvas.height,
                    time = null;
                var centerX2 = canvas.width * 5 / 6,
                    color2 = "#db5e8c",
                    circleName2 = $.i18n.prop('d_home_scenarioFail'),
                    x2 = canvas.width * 2 / 3,
                    y2 = 0,               //canvas.width * 2 / 3, 0, canvas.width, canvas.height
                    dx2 = canvas.width,
                    dy2 = canvas.height,
                    time = null;
                _self._drawBase.drawGraph(centerX, centerY, R, color, circleName, x, y, dx, dy,
                                          response.usage, 1)();
                _self._drawBase.drawGraph(centerX1, centerY, R, color1, circleName1, x1, y1, dx1,
                                          dy1, response.success, 2)();
                _self._drawBase.drawGraph(centerX2, centerY, R, color2, circleName2, x2, y2, dx2,
                                          dy2, response.failure, 3)();
        });
    };

    _ScenePie.prototype.init = function (id) {
        this.getOptions(id);
    };
    return {
        create: _ScenePie		//return dialog html
    }
})

/*
 * this
 *
 *
 * */
