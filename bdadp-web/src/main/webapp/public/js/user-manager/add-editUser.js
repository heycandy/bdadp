/**
 * Created by Administrator on 2016/12/27 0027.
 */
define(["js/user-manager/userCommon", 'js/modeldialog'], function (userPackage, initDialog) {

    var User = function (num, id, name, createTime, role) {
        this.number = num;
        this.userId = id;
        this.userName = name;
        this.createTime = createTime;
        this.roleName = role;
    }

    var _clickEdit = function (userTables, row) {
        userPackage._addDialogShow();
        //加载编辑用户页面
        $(".container-fuild.user-dialog .addUserManger").load("./html/scene-user/scene-userEdit.html",
                                                              function () {
                                                                  _editDialogCallback(row);

                                                                  userPackage._userValidate("editUserForm");
                                                                  //为保存按钮添加事件
                                                                  $(".xdsoft_dialog_buttons").children(".xdsoft_btn:last-child").click(function () {
                var _self = this;
                if (userPackage._loginValid("editUserForm")) {
                    userPackage._addUserFn("editUserForm", function (user) {
                        settings.HttpClient("PUT", user, "/service/v1/user/" + row.userId,
                                            function (response) {
                                                userTables.delete(['userId', user["userId"]]);
                                                var roleName = "";
                                                settings.HttpClient("GET", null,
                                                                    "/service/v1//user/role?id="
                                                                    + response["roles"][0]["roleId"],
                                                                    function (response) {
                                                                        roleName =
                                                                            response.roleName;
                            })
                                                userTables.addRow(row.number, new User(row.number,
                                                                                       response["userId"],
                                                                                       response["userName"],
                                                                                       response["createTime"],
                                                                                       roleName));
                                                userPackage._addDialogHide();
                                            })
                    })
                    $(this).unbind("click");
                    return true;
                }
            });settings.interFun();
                                                              });

    };

    var _clickRemove = function (userTables, row) {
        settings.HttpClient("DELETE", null, "/service/v1/user/" + row.userId, function (response) {
            userTables.delete(['userId', row.userId]);
            initDialog().initAlert($.i18n.prop('d_hdfs_deleteSuccessfully'));
            userPackage._changeNum();
        })
    };

    //用户编辑页面保存展示
    var _editDialogCallback = function (row) {
        settings.HttpClient("GET", null, "/service/v1/user?id=" + row.userId, function (response) {
            $(".editUser .userId").val(response.userId);
            $(".editUser .userName").val(response.userName);
            $(".editUser .userPwd").val($.base64.decode(response.userPwd));
            $(".editUser .userDesc").append(response.userDesc);
        })
        var roleHtml = "";
        settings.HttpClient("GET", null, "/service/v1/user/role", function (response) {
            for (var i = 0; i < response.length; i++) {
                if (row.roleName == response[i]["roleName"]) {
                    roleHtml +=
                        '<input type="radio" value="' + response[i]["roleId"]
                        + '"  name="roleId" checked/><span>' + response[i]["roleName"]
                        + '</span><br/>'
                } else {
                    roleHtml +=
                        '<input type="radio" value="' + response[i]["roleId"]
                        + '"  name="roleId"/><span>' + response[i]["roleName"] + '</span><br/>'
                }
            }
            $(".editUser .roleName").append(roleHtml);
        })
    }

    var _userAddCallback = function (userTables) {
        //隐藏按钮
        userPackage._addDialogShow();
        //加载添加用户页面
        $(".container-fuild.user-dialog .addUserManger").load("./html/scene-user/scene-userAdd.html",
                                                              function () {
                                                                  var roleHtml = "";
                                                                  settings.HttpClient("GET", null,
                                                                                      "/service/v1/user/role",
                                                                                      function (response) {
                                                                                          for (var i = 0;
                                                                                               i
                                                                                               < response.length;
                                                                                               i++) {
                                                                                              roleHtml +=
                                                                                                  '<option value="'
                                                                                                  + response[i]["roleId"]
                                                                                                  + '">'
                                                                                                  + response[i]["roleName"]
                                                                                                  + '</option>'
                                                                                          }
                                                                                          $(".addUser .roleId").append(roleHtml);
                                                                                      })
                                                                  userPackage._userValidate("addUserForm");
                                                                  //为保存按钮添加事件
                                                                  $(".xdsoft_dialog_buttons").children(".xdsoft_btn:last-child").click(function () {
                var _self = this;
                if (userPackage._loginValid("addUserForm")) {
                    userPackage._addUserFn("addUserForm", function (user) {
                        settings.HttpClient("POST", user, "/service/v1/user", function (response) {
                            userTables.addRow(0,
                                              new User(0, response["userId"], response["userName"],
                                                       response["createTime"],
                                                       response["roles"][0]["roleName"]));
                            userPackage._changeNum();
                            userPackage._addDialogHide();
                        })
                    })
                    $(_self).unbind("click");
                    return true;
                }
                                                                  })
                                                              });
    }

    return {
        _clickEdit: _clickEdit,
        _clickRemove: _clickRemove,
        _userAddCallback: _userAddCallback

    }

})
