/**
 * Created by labo on 2016/8/25.
 */
define(['public/js/settings', 'public/libs/i18n/international',
        'public/libs/i18n/jquery.i18n.properties'], function () {
    'use strict';
    settings.interFun('public/I18n/');
    var _Login = function () {
    };
    _Login.prototype.login = function () {
        alert("denglu");
    };

    _Login.prototype.validate = function () {
        $('#formSignin').bootstrapValidator({
            message: $.i18n.prop('d_sacne_notValue'),
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                userName: {
                    container: 'tooltip',
                    validators: {
                        notEmpty: {
                            message: $.i18n.prop('d_userName_notEmpty')     //$.i18n.prop('d_userName_notEmpty')
                                                                            // //用户名不能为空
                        }
                    }
                },
                userPassword: {
                    container: 'tooltip',
                    validators: {
                        notEmpty: {
                            message: $.i18n.prop('d_password_notEmpty')       // "密码不能为空"
                                                                              // $.i18n.prop('d_password_notEmpty')
                                                                              // //'密码不能为空'
                        }
                    }
                }
            }
        });
    };

    //login function
    _Login.prototype.loginFun = function () {
        var userName = $("#formSignin input[name='userName']").val().trim();
        var passWorld = $("#formSignin input[name='userPassword']").val();
        var bootstrapValidator = $("#formSignin").data('bootstrapValidator');
        bootstrapValidator.validate();
        var loginValid = bootstrapValidator.isValid();
        if (loginValid) {
            var paramObj = {};
            paramObj.userName =userName;
            paramObj.userPwd = $.base64.encode(passWorld);
            settings.HttpClient("POST", paramObj, "/service/v1/user/login", function (response) {
                //sessionStorage.setItem("ARK_PLATFORM_TOKEN", response);
                sessionStorage.setItem("userId", response);
                sessionStorage.setItem("userName", userName);
                settings.HttpClient("GET", null, "/service/v1/user?id=" + response,
                                    function (response) {
                                        sessionStorage.setItem("roleName",
                                                               response["roles"][0]["roleName"]);
                                        window.location.href = 'public/main.html';
                                    })
                /* $(".navbar.navbar-static-top").find("span.hidden-xs").text(userName);*/
            }, function () {
                alert($.i18n.prop('d_alert_userPsw'));
            });
        }
    }
    $('#formSignin .content-wrap .form-group input[name="userName"]').attr('placeholder',
                                                                           $.i18n.prop('d_label_userName'));
    $('#formSignin .content-wrap .form-group input[name="userPassword"]').attr('placeholder',
                                                                               $.i18n.prop('d_label_psw'));
    _Login.prototype.backspaceEvent = function (e) {
        var keyPressed;
        if (window.event) {
            keyPressed = window.event.keyCode;
        } else {
            keyPressed = e.which;
        }
        if (keyPressed == 13) {
            e.preventDefault();
            this.loginFun();
        }
    }

    return {
        Login: _Login
    }
})
