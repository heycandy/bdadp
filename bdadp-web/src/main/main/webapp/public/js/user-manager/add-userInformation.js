/**
 * Created by Administrator on 2016/9/13.
 */
define(["js/modeldialog", "js/user-manager/user-operate", 'js/settings', 'libs/i18n/international',
        'libs/i18n/jquery.i18n.properties'], function (dialog, userOperate) {
    "use strict";

    var _adduserformation = function () {
    };
    var userformation = new _adduserformation();

    _adduserformation.prototype.addInformationDialog = function (useId) {
        var _self = this;
        var message = '<div class="container-fuild user-dialog">' +
                      '<div class="userTab">' +
                      '<ul class="list" role="tablist">' +
                      '<li role="presentation" class="tab-header"><i class="iconfont" id="user-mang-icon" class="tab-icon">&#xe65c;</i><span id="I_span_user_userInformation"></span></li>'
                      +
                      '<li role="presentation" class="tab-header"><i class="iconfont"  class="tab-icon">&#xe603;</i><span id="I_span_user_userAdministration"></span></li>'
                      +
                      '</ul></div>' +
                      '<div class="userManger"></div>' +
                      '<div class="addUserManger"></div>' +
                      '<div class="userInformation" style="">' +
                      '<div class="container-fuild">' +
                      '<div class="row">' +
                      ' <div class="col-md-4 userIn-left">' +
                      '<div class="head_img"></div>' +
                      '<div class="btn-group btn-file">' +
                      '<button type="file" class="btn dropdown-toggle" data-toggle="dropdown">' +
                      '<span class="fa fa-picture-o user_picture"></span><span id="I_btn_user_selectPic"></span>'
                      +
                      '</button>' +
                      '</div>' +
                      '</div>' +
                      ' <div class="col-md-8 userIn-right">' +
                      '<div class="changeInformation changImg" ><i class="fa fa-pencil-square-o fa-2x"></i><span id="I_span_user_modify"></span></div>'
                      +
                      '<form class="form-horizontal userInfor_form" id="userInfor_form" role="form">'
                      +
                      '<div class="form-group">' +
                      '<input type="text" class="userId" name="userId" style="display: none;"/>' +
                      '<label for="userNameText" class="col-sm-3 control-label" id="I_label_user_userName"></label>'
                      +
                      '<div class="col-sm-6">' +
                      '<input type="text" class="form-control sameInput userName" name="userName" id="userNameText" disabled>'
                      +
                      ' </div>' +
                      '</div>' +
                      '<div class="form-group">' +
                      '<label for="userRole" class="col-sm-3 control-label" id="I_label_user_userRole"></label>'
                      +
                      '<div class="col-sm-6">' +
                      '<input type="text" class="form-control roleName" name="roleName" id="userRole" disabled>'
                      +
                      '</div>' +
                      '</div>' +
                      '<div class="form-group userPwd" style="display:none;">' +
                      '<label for="userPassText" class="col-sm-3 control-label" id="I_label_user_Password"></label>'
                      +
                      '<div class="col-sm-6">' +
                      '<input type="text" class="form-control sameInput userPwd" name="userPwd" id="userPassText" disabled>'
                      +
                      ' </div>' +
                      '</div>' +
                      '<div class="form-group userPwd" style="display:none;" >' +
                      '<label for="newPass" class="col-sm-3 control-label" id="I_label_user_newPassword"></label>'
                      +
                      '<div class="col-sm-6">' +
                      '<input type="text" class="form-control sameInput confirmPwd" name="confirmPwd" id="newPass" disabled>'
                      +
                      ' </div>' +
                      '</div>' +
                      '<div class="form-group">' +
                      '<label for="userDes"class="col-sm-3 control-label" id="I_label_user_describe"></label>'
                      +
                      '<div class="col-sm-6">' +
                      '<textarea class="form-control userDescrib userDesc" name="userDesc" id="userDes" placeholder="描述" disabled style="background: transparent;box-shadow: none;"></textarea>'
                      +
                      '</div>' +
                      '</div>' +
                      '</form>' +
                      '</div>' +
                      '</div>' +
                      '</div>' +
                      '</div>' +
                      '</div>';
        $(message).dialog({
            title: $.i18n.prop('d_user_user'),
            onBeforeShow: function () {
                _self.onBeforeShow();
                _self.showMessage(useId);
            },
            onAfterShow: function () {
                _self.userManage("userInfor_form");
            },
            buttons: {
                " ": function () {

                },
                "": function () {
                    _self.editMineMess();
                    return false;
                }
            }
        });
        settings.interFun();
    }

    _adduserformation.prototype.onBeforeShow = function () {
        $(".xdsoft_dialog").addClass("userDialog");
        //为用户信息和用户管理赋值
        $(".list").children("li").eq(0).click(function () {
            _userWindowsChange(".userInformation", ".userManger");
        });
        $(".list").children("li").eq(1).click(function () {
            _userWindowsChange(".userManger", ".userInformation");
        });

        $(".userIn-right").find(".changeInformation").click(function () {           //点击修改
            $(".sameInput").css({
                "border": "1px solid #ccc",
                "background": "#fff"
            }).removeAttr("disabled");
            $(".userDescrib").css({
                "border": "1px solid #ccc",
                "background": "#fff"
            }).removeAttr("disabled");
            $("#userRole").css({
                "border": "none",
                "background": "#f2f2f2"
            }).attr("disabled", "disabled");
            $(".form-group.userPwd").css({"display": "block"});
            $("input").css("border", "1px solid #ccc");
            $("textarea").css("border", "1px solid #ccc");
            $("input:eq(1)").css("border", "none");
        });

        $(".userDialog").addClass("no-border");
        $(".userDialog").find(".userTab ").css("width", "798px");
        $(".userDialog").find(".xdsoft_dialog_popup_title").addClass("user-dialog-title");
        $(".userDialog").find(".xdsoft_dialog_buttons").addClass("user-dialog-btns");

        $(".list").children("li").eq(0).trigger("click");
        //中英文翻译
        $('.xdsoft_dialog_buttons button.xdsoft_btn:first-child').text($.i18n.prop('d_cancel'));
        $('.xdsoft_dialog_buttons button.xdsoft_btn:last-child').text($.i18n.prop('d_confirm'));
        //角色权限控制
        settings.roleManage(function () {
            $(".userDialog .userTab ul li").eq(1).css("display", "block");
        }, function () {
            $(".userDialog .userTab ul li").eq(1).css("display", "none");
        }, function () {
            $(".userDialog .userTab ul li").eq(1).css("display", "none");
        });
    }

    //用户信息、用户管理窗口转换
    var _userWindowsChange = function (strClass1, strClass2) {
        $(strClass1).css("display", "block");
        $(strClass2).css("display", "none");
        $(this).css("color", "#2E4C7A").siblings().css("color", "#666666");
        $(".container-fuild.user-dialog .addUserManger").css("display", "none");
        $(".xdsoft_dialog_buttons").children(".xdsoft_btn:last-child").unbind("click");
    }
    //用户管理
    _adduserformation.prototype.userManage = function (formId) {
        userOperate.userOperate(formId);
    }
    //编辑自己的信息
    _adduserformation.prototype.editMineMess = function () {
        var mineData = $("#userInfor_form").serializeArray(), user = {};
        //用户对象封装
        for (var i = 0; i < mineData.length; i++) {
            if (mineData[i]["name"] == "userPwd") {
                user[mineData[i]["name"]] = $.base64.encode(mineData[i]["value"]);
            } else if (mineData[i]["name"] != "roleName" && mineData[i]["name"] != "confirmPwd") {
                user[mineData[i]["name"]] = mineData[i]["value"];
            }
        }

        var bootstrapValidator = $("#userInfor_form").data('bootstrapValidator');
        bootstrapValidator.validate();
        var loginValid = bootstrapValidator.isValid();
        if (loginValid) {
            settings.HttpClient("PUT", user, "/service/v1/user/" + user.userId,
                                function (response) {
                                    $(".sameInput").css({
                                        "border": "none"
                                    }).attr("disabled", true);
                                    $(".userDescrib").css({
                                        "border": "none"
                                    }).attr("disabled", true);
                                    $("#userRole").css({
                                        "border": "none",
                                        "background": "transparent"
                                    }).attr("disabled", true);
                                    $(".form-group.userPwd").css({"display": "none"});
                                })
        }
    }

    _adduserformation.prototype.showMessage = function (useId) {
        settings.HttpClient("GET", null, "/service/v1/user?id=" + useId, function (response) {
            $(".userInfor_form .userId").val(response.userId);
            $(".userInfor_form .userName").val(response.userName);
          if (response["roles"].length) {
            $(".userInfor_form .roleName").val(response["roles"][0]["roleName"]);
          }
            $(".userInfor_form .userPwd").val($.base64.decode(response.userPwd));
            $(".userInfor_form .userDesc").append(response.userDesc);
        })
    }

    return {
        addInformation: _adduserformation
    }

})
