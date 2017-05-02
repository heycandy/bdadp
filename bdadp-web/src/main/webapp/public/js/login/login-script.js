/**
 * Created by labo on 2016/8/25.
 */

$(function () {
    require(['public/js/login/login'], function (login) {
        var login,
            LoginExtend = {};
        LoginExtend.enterkeyDown = function () {
            $("#formSignin input[name='userName']").keydown(function (e) {
                login.backspaceEvent(e);
            });
            $("#formSignin input[name='userPassword']").keydown(function (e) {
                login.backspaceEvent(e);
            });
        };
        login = new login.Login();
        login.validate();        //validate the inputs of the login page
        LoginExtend.enterkeyDown();
        $("#I_a_login").click(function () {
            login.loginFun();                //login
        });
    });
})

