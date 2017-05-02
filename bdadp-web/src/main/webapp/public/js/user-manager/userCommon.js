define(["component/BdaTable", "js/scene-develop/scene-common"], function (table, common) {

    return {
        _loginValid: function (formId) {
            var bootstrapValidator = $("#" + formId).data('bootstrapValidator');
            bootstrapValidator.validate();
            return bootstrapValidator.isValid();
        },
        //改变用户序号
        _changeNum: function () {
            var trs = $("#userTableId").find("tr[data-index]");
            if (trs.length) {
                for (var i = 0; i < trs.length; i++) {
                    $(trs[i]).children("td")[0].innerHTML = i;
                }
            }
        },
        //隐藏用户添加页面，展示用户列表
        _addDialogHide: function () {
            common.floatFun.sceneFloatHide($(".user-dialog")[0], ["addUserManger"])
            common.floatFun.sceneFloatShow($(".xdsoft_dialog")[0], ["userManger"])
        },
        //展示用户添加页面，隐藏用户列表
        _addDialogShow: function () {
            common.floatFun.sceneFloatHide($(".xdsoft_dialog")[0], ["userManger"])
            //获取dialog高度
            var dialogHeight = $(".userDialog")[0].clientHeight;
            $(".container-fuild.user-dialog .addUserManger").css({
                "height": (dialogHeight - 100) + "px",
                "display": "block",
                "background-color": "#fff"
            });
        },
        //添加新用户
        _addUserFn: function (formId, callbacks) {
            var addUserData = $("#" + formId).serializeArray(), user = {}, roles = [], role = {}

            for (var i = 0; i < addUserData.length; i++) {
                if (addUserData[i]["name"] == "roleId") {
                    role["roleId"] = addUserData[i]["value"];
                    roles.push(role);
                } else if (addUserData[i]["name"] == "userPwd") {
                    user[addUserData[i]["name"]] = $.base64.encode(addUserData[i]["value"]);
                } else if (addUserData[i]["name"] != "number" && addUserData[i]["name"]
                                                                 != "confirmPwd") {
                    user[addUserData[i]["name"]] = addUserData[i]["value"];
                }
            }
            user["roles"] = roles;
            callbacks(user);
        },
        _userValidate: function (formId) {
            $("#" + formId).bootstrapValidator({
                message: 'This value is not valid',
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
                                message: "The userName is required and cannot be empty"                    //$.i18n.prop('d_userName_notEmpty')    //用户名不能为空
                            },
                            stringLength: {
                                max: 20,
                                message: "The user name must be less than 20 characters long"
                            },
                            callback: {
                                message: "This user name has been used",
                                callback: function (value, validator) {
                                    var userNames = [], flag = true;
                                    settings.HttpClient("GET", null, "/service/v1/user",
                                                        function (response) {
                                                            for (var i = 0; i < response.length;
                                                                 i++) {
                                                                userNames.push(response[i]["userName"]);
                                                            }
                                                        })

                                    for (var userName in userNames) {
                                        if (userNames[userName] == value) {
                                            flag = false;
                                        }
                                    }

                                    return flag;
                                }
                            }
                        }
                    },
                    userPwd: {
                        container: 'tooltip',
                        validators: {
                            notEmpty: {
                                message: "The password is required and cannot be empty"                    //$.i18n.prop('d_userName_notEmpty')    //用户名不能为空
                            },
                            stringLength: {
                                min: 3,
                                max: 20,
                                message: "The password must be more than 3 and less than 20 characters long"
                            }
                        }
                    },
                    confirmPwd: {
                        container: 'tooltip',
                        validators: {
                            notEmpty: {
                                message: "The confirmPwd is required and cannot be empty"                    //$.i18n.prop('d_userName_notEmpty')    //用户名不能为空
                            },
                            identical: {
                                field: "userPwd",
                                message: "The password and its confirm are not the same"
                            },
                            stringLength: {
                                min: 3,
                                max: 20,
                                message: "The password must be more than 3 and less than 20 characters long"
                            }
                        }
                    }
                }
            });
        }
    }

})
