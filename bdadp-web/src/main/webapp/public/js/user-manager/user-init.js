/**
 * Created by labo on 2016/9/13.
 */
define(["js/user-manager/add-userInformation"], function (userInformation) {
    'use strict';
    var _userManager = function () {
        this.userName = sessionStorage.userName;
        this.userId = sessionStorage.userId;
    };

    var userIn = new userInformation.addInformation();
    _userManager.prototype.init = function () {
        var _self = this;
        $(".navbar.navbar-static-top").find("span.hidden-xs").text(_self.userName);
        $(".navbar.navbar-static-top").find("ul[id='header-user'] li").click(function () {
            var name = $(this).attr("name");
            switch (name) {
                case "userInfo" :
                {
                    userIn.addInformationDialog(_self.userId);
                    break;
                }
                case "userSet" :
                {
                    //alert("userSet");
                    break;
                }
                case "userHelp":
                {
                    //alert("userHelp");
                    break;
                }
                case "userExit":
                {
                    _userExit();
                    break;
                }
            }
        })
    };
    var _userExit = function () {
        var paramObj = {};
        paramObj.userName = sessionStorage.userName;
        settings.HttpClient("POST", paramObj, "/service/v1/user/logout", function (response) {
            window.location.href = '../index.html';
        });
    }
    return {
        userManager: _userManager
    }
})