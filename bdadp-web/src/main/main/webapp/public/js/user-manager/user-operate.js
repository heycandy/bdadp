/**
 * Created by labo on 2016/9/18.
 */
define(["component/BdaTable", "js/scene-develop/scene-common", "js/user-manager/userCommon",
        "js/user-manager/add-editUser"], function (table, common, userPackage, addUserPackage) {
    settings.interFun();

    var User = function (num, id, name, createTime, role) {
        this.number = num;
        this.userId = id;
        this.userName = name;
        this.createTime = createTime;
        this.roleName = role;
    }

    var _userOperate = function (formId) {
        if (formId != null) {
            userPackage._userValidate(formId);
        }
        var head = {};
        var dataArr = [];

        settings.HttpClient("GET", null, "/service/v1/user", function (response) {
            head = {
                "dataField": ["number", "userId", "userName", "createTime", "roleName", "operate"],
                "headText": [$.i18n.prop('d_tabs_userCode'), $.i18n.prop('d_tabs_userID'),
                             $.i18n.prop('d_tabs_userName'), $.i18n.prop('d_tabs_creationTime'),
                             $.i18n.prop('d_tabs_userRole'), $.i18n.prop('d_tabs_operation')],
                "events": [{}, {}, {}, {}, {},
                    {"data-formatter": "operateFormatter", "data-events": "operateEvents"}]
            };
            for (var i = 0; i < response.length; i++) {
                if (response[i]["roles"].length) {
                    dataArr.push(new User(i, response[i]["userId"], response[i]["userName"],
                                          response[i]["createTime"],
                                          response[i]["roles"][0]["roleName"]));
                } else {
                    dataArr.push(new User(i, response[i]["userId"], response[i]["userName"],
                                          response[i]["createTime"], null));
                }
            }
            _userManage("userTableId", head, dataArr, response)

        });
    }
    //生成用户管理列表
    var _userManage = function (tableId, head, dataArr, usersArr) {
        //创建表格
        var userTables = new table.BdadpTable(tableId, head, dataArr);
        var tableHtml = userTables.creatTable();
        _userTableInit(userTables);
        $(".user-dialog .userManger").append(tableHtml);
        //添加数据
        $("#" + tableId).bootstrapTable({
            data: dataArr
        });
        //隐藏表格userId列
        $("#" + tableId).bootstrapTable('hideColumn', 'userId');
        $(".fixed-table-body .fixed-table-loading").empty();
        $(".fixed-table-toolbar .bars.pull-left").append('<div id="toolbar"><button class="ion-plus add" style="width:40px;height:30px;background-color:white;"></button></div>');
        $(".fixed-table-toolbar .ion-plus.add").click(function () {
            addUserPackage._userAddCallback(userTables);
        });
    }

    var _userTableInit = function (userTables) {
        window.operateFormatter = function (value, row, index) { //加操作列
            return [
                '<a class="edit" href="javascript:void(0)" title="eidt">',
                '<i class="glyphicon glyphicon-pencil" style="color:#000"></i>',
                '</a>&nbsp;&nbsp;&nbsp;&nbsp;',
                '<a class="remove" href="javascript:void(0)" title="Remove">',
                '<i class="glyphicon glyphicon-remove" style="color:#000"></i>',
                '</a>'
            ].join('');
        };
        window.operateEvents = { //添加操作事件
            'click .edit': function (e, value, row, index) {
                addUserPackage._clickEdit(userTables, row);
            },
            'click .remove': function (e, value, row, index) {
                addUserPackage._clickRemove(userTables, row);
            }
        };
    };

    return {
        userOperate: _userOperate,
    }
})
