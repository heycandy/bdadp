/**
 * Created by msc on 2016/9/14.
 */
define(["component/BdaTable", "js/modeldialog"], function (table, initDialog) {
    settings.interFun();
    function init() {
        $("#operate").attr("title", $.i18n.prop('d_tool_operate'));
        var number = 0;
        $("#sql").val("");
        $("#loadingDiv").css("display", "block");
        $("#startTable").height($("#content1").height() - 35 + "px");
        setTimeout(getdbName, 100);
        function getdbName() {
            $("#dbName").empty();
            $("#dataTable").empty();
            settings.HttpClient("GET", null,
                                "/service/v1/get/dbNameList", function (response) {
                    console.log(response);
                    showdbName(response);
                });
        }

        function showdbName(arrayData) {
            if (arrayData == null) {

            } else {
                for (var i = 0; i < arrayData.length; i++) {
                    if (i == 0) {
                        $("#dbName").append("<option value='" + arrayData[i] + "' selected>"
                                            + arrayData[i] + "</option>");
                    } else {
                        $("#dbName").append("<option value='" + arrayData[i] + "'>" + arrayData[i]
                                            + "</option>");
                    }
                }
                getTable();
            }
        }

        $("#dbName").change(function () {
            getTable();
        })

        function getTable(val) {
            var dbName = $("#dbName").val();
            console.log(dbName);
            settings.HttpClient("GET", null, "/service/v1/get/tableNameList?&dbName=" + dbName,
                                function (response) {
                                    console.log(response);
                                    $("#dataTable").empty();
                                    for (var i = 0; i < response.length; i++) {
                                        showTableList(response[i])
                                        number++;
                                    }
                                    removecloud();
                                });
        }

        function showTableList(param) {
            var value;
            if (window.screen.width > 1366) {
                if (param.length > 35) {
                    value = param.substring(0, 35) + "...";
                } else {
                    value = param;
                }
            } else {
                if (param.length > 20) {
                    value = param.substring(0, 20) + "...";
                } else {
                    value = param;
                }
            }

            $("#dataTable").append("<li style='margin-top: 0px;'><span name='" + param
                                   + "' title = '" + param + "' class='hiveTables' id='" + number
                                   + "' " +
                                   " style='width:100%'><h>"
                                   + value + "</h>"
                                   + "<i class='ion-chevron-right' style='float: right;padding-right: 10%'></i></span>"
                                   +
                                   "<ul  class='' style='padding-left:25%;background-color:#fafafa' id='dataTable"
                                   + number + "'></ul></li>");

        }

        function writeTable(str) {
            var obj = document.getElementById("sql");
            var o1 = $(str).attr("name").match(/\w+/g);//.match(/\w+/g)
            obj.value += "  " + o1[0];
        }

        function getHiveTables() {
            var sql = $("#sql").val();
            if (sql == null || sql == "" || sql == "undefined") {
                initDialog().initAlert($.i18n.prop('d_hive_inputNotNull'),
                                       $.i18n.prop('d_hbase_warn'), 2000);
                $("#executeQuery").prop("disabled", false);
                return;
            }
            var list = sql.split(";");
            for (var i = 0; i < list.length; i++) {
                var sqlList = list[i].split(" ");
                for (var j = 0; j < sqlList.length; j++) {
                    if (sqlList[j] == "from" && sqlList[j + 1].indexOf("(") < 0) {
                        $("#tableHeader").find("> li > a").each(function (index, element) {
                            if ($(element).attr("title") == sqlList[j + 1]) {
                                //hiveTabClick(this, index, param);
                                deletehiveTabAndTable($(this).find("i"));
                                return false;
                            }
                        });
                        break;
                    } else {
                        continue;
                    }
                }
            }
            getHiveTable(sql);
        }

        var startTime;
        var isExcuteTrue = true;

        function getHiveTable(sql, e, val) {
            var mydate = new Date();
            startTime = mydate.getTime();
            var serializeObj = {};
            var list = sql.split(";");
            console.log(list);
            var dbName = $("#dbName").val();
            $("#dataMsg").empty();
            var maxRows = $("#maxRows").val();
            if (maxRows < 0) {
                initDialog().initAlert($.i18n.prop('d_hive_queryNegative'));
            } else if (maxRows == "") {
                serializeObj.dbName = dbName;
                serializeObj.hqlStats = sql;
            } else {
                serializeObj.dbName = dbName;
                serializeObj.hqlStats = sql;
                serializeObj.maxRows = maxRows;
            }
            console.log(serializeObj);
            settings.HttpClient("post", serializeObj, "/service/v1/execHql", function (response) {
                console.log(response);
                showTableMessage(response, list);
                if (isExcuteTrue) {
                    $(e).find("span > h").text(val).parent().attr("name", val).attr("title", val);
                } else {
                    initDialog().initAlert("tableName  already exists!",
                                           $.i18n.prop('d_hbase_warn'), 2000);
                }
                ;
            }, function (response) {
                //$("#startTable").html(response);
                executeResult(list, response)
                $("#executeQuery").prop("disabled", false);
            });
        }

        function excuteHive(param, e, val) {
            $("#tableHeader").css("display", "block");
            if (param == null || param == "" || param == "undefined") {
                getHiveTables();
                return;
            }
            getHiveTable(param, e, val);
        }

        $("#executeQuery").click(function () {
            $("#executeQuery").prop("disabled", "true");
            setTimeout(excuteHive, 100);
        });

        function showTableMessage(arrayList, list) {
            if (arrayList == null) {
                $("#startTable").html($.i18n.prop('d_hive_operationFailure'))
                return false;
            }
            for (var j = 0; j < arrayList.length; j++) {
                $.each(arrayList[j], function (key, value) {
                    if (isNaN(value[0][0])) {
                        if (value[0][0] == "ERROR") {
                            executeResult(list[j], value[0][1]);
                            isExcuteTrue = false;
                            return false;
                        }
                        executeResult(list[j], "");
                        appendTable(value, key);
                        isExcuteTrue = true;
                    } else {
                        //appendHiveTables({},[],list[j]);
                        executeResult(list[j], value[0][0])
                        isExcuteTrue = true;
                    }
                })
            }
        }

        /* var aa = {};
         aa[param[m][n]] = "";*/
        function appendTable(param, key) {
            var head = [];
            var dataArr = [];
            var tableList = [];
            for (var m = 0; m < param.length; m++) {
                var paramList = '{';
                for (var n = 0; n < param[m].length; n++) {
                    if (m == 0) {
                        dataArr.push(param[m][n]);
                        head.push({
                            field: param[m][n],
                            title: param[m][n]
                        })
                    } else {
                        var params = dataArr[n];
                        paramList += '"' + params + '":"' + param[m][n] + '",';
                    }
                }
                paramList += '}';
                paramList = eval("(" + paramList + ")");
                if (!$.isEmptyObject(paramList)) {
                    tableList.push(paramList)
                }
            }
            appendHiveTables(head, tableList, key);
        }

        function executeResult(val, param) {
            if (param == -1) {
                param = "completed in ";
            } else {
                param = param + " completed in  ";
            }
            val = val.substr(0, 60);
            var mydate = new Date();
            var entTime = mydate.getTime() - startTime;
            if ($("#tabOutput").css("display") == "block") {
                $("#showOutput").append("<p>sql>" + val + "</p><p>[" + mydate.toLocaleString() + "]"
                                        + param + entTime + "ms</p>");
            } else {
                hiveTab();
                $(".hiveTabStyle").removeClass("hiveTabClickStyle");
                $("#tabOutput").css("display", "block").addClass("hiveTabClickStyle");
                $("#showOutput").css("display", "block").append("<p>sql>" + val + "</p><p>["
                                                                + mydate.toLocaleString() + "]"
                                                                + param + entTime + "ms</p>");
                hiveTabShowOrHide();
            }
            $("#executeQuery").prop("disabled", false);
        }

        var tabNum = 0;

        function appendHiveTables(head, dataArr, key) {
            hiveTab();
            //var userTables = new table.BdadpTable("userTab" + tabNum, head, dataArr);
            //data-show-columns="true"' + '
            var tableHtml = ' <table id="userTab' + tabNum + '"' + ' data-toolbar="#toolbar"'
                            + ' data-search="true"' + ' data-show-export="true"'
                            + ' data-detail-formatter="detailFormatter"'
                            + ' data-minimum-count-columns="1"' + ' data-pagination="true"' +
                            ' data-id-field="id"' + ' data-page-size="10"'
                            + ' data-page-list="[10,15,20]"' + ' data-show-footer="false"'
                            + ' data-response-handler="responseHandler">';
            $("#startTable").append("<div style='display:block' class='hiveTab' id='tab" + tabNum
                                    + "'></div>")
            $("#tab" + tabNum).append(tableHtml);
            var $table = $('#userTab' + tabNum);
            //添加数据
            $table.bootstrapTable('destroy').bootstrapTable({
                columns: head,
                data: dataArr
            });
            $("div .fixed-table-body .fixed-table-loading").empty();
            $("#executeQuery").prop("disabled", false);
            tab(key);
        }

        function hiveTab() {
            $(".hiveTab").css("display", "none");
        }

        function tab(title) {
            $(".hiveTabStyle").removeClass("hiveTabClickStyle");
            $("#tabOutput").removeClass("hiveTabClickStyle");
            var val = title;
            if (title.length > 10) {
                val = title.substring(0, 10) + "...";
            }
            $("#tableHeader").append('<li id="hiveTab' + tabNum + '" name="' + title
                                     + '" style="display:block">'
                                     + '<a value="' + tabNum + '" name="tab' + tabNum + '" title="'
                                     + title + '" class="hiveTabStyle toolsFont hiveTabClickStyle">'
                                     + val
                                     + '<i class="fa fa-times" style="margin-left: 10px;font-size: 11px !important;color: #777777;"></i></a></li>');
            hiveTabShowOrHide();
            $("#dropDown").append('<li id="hiveListTab' + tabNum
                                  + '" style="display: none;border-bottom: 1px solid #ccc;">'
                                  + '<a value="' + tabNum + '" name="tab' + tabNum + '" title="'
                                  + title + '" class="hiveTabStyle toolsFont hiveTabClickStyle">'
                                  + title
                                  + '</a></li>');
            tabAddEvent();
            tabNum++;
        }

        function tabAddEvent() {
            $("#tableHeader a").unbind("click");
            $('#tableHeader a').click(function (e) {
                findHiveTable($(this).attr("title"))
            })
            $("#dropDown a").unbind("click");
            $("#dropDown a").click(function (e) {
                findHiveTable($(this).attr("title"))
            })
            $("#tableHeader a i").unbind("click");
            $("#tableHeader a i").click(function (e) {
                deletehiveTabAndTable(this);
            })
        }

        function deletehiveTabAndTable(e) {
            $(".hiveTabStyle").removeClass("hiveTabClickStyle");
            $(".hiveTab").css("display", "none");
            var param = $(e).parents("li").prev().find("> a").addClass("hiveTabClickStyle").attr("name");
            var num = $(e).parents("a").attr("value");
            $("#hiveListTab" + num).remove();
            $("#" + param).css("display", "block");
            $("#" + $(e).parent().attr("name")).remove();
            if ($(e).parents("li").prev().find("> a").length == 0) {
                tabOutputClick();
            } else {
                var param = $("#tableHeader").find("> li");
                hiveDeleteOrMix(param);
            }
            $(e).parents("li").remove();
        }

        $("#tabOutput").click(function () {
            tabOutputClick();
        });
        function tabOutputClick() {
            $(".hiveTabStyle").removeClass("hiveTabClickStyle");
            $(".hiveTab").css("display", "none");
            $("#tabOutput").addClass("hiveTabClickStyle");
            $("#showOutput").css("display", "block");
        }

        function hiveTabShowOrHide(val, e) {
            var param;
            if (val != null && val != " " && val != "undefined") {
                param = $("#tableHeader").find("> li").toArray().reverse()
            } else {
                param = $("#tableHeader").find("> li");
            }
            if ($("#tabOutput").css("display") == "block") {
                num = 2;
            } else {
                num = 1;
            }
            if ((param.length + num) * $("#tabOutput").width() > $("#tableHeader").width()) {
                $(param).each(function (index, element) {
                    if ($(element).css("display") == "block") {
                        if (!$(element).find("> a").hasClass("hiveTabClickStyle")) {
                            $(element).css("display", "none");
                            tabList(val, e);
                            return false;
                        } else {
                            if (hiveTabMinHide == "") {
                                hiveTabMinHide = $(element).find("> a").attr("value");
                                tabList(val, e);
                                return true;
                            }
                        }
                    }
                });
            }
        }

        function tabList(val, e) {
            var param;
            if (val != null && val != " " && val != "undefined") {
                param = $("#dropDown").find("> li").toArray().reverse();
            } else {
                param = $("#dropDown").find("> li");
            }
            $(param).each(function (index, element) {
                if ($(element).css("display") == "none") {
                    $(element).css("display", "block");
                    //$("#hideSelectTab").css("display","block");
                    $("#hiveListTab" + e).css("display", "none");
                    return false;
                }
            });
        }

        function hiveDeleteOrMix(param) {
            $(param).each(function (index, element) {
                if ($(element).css("display") == "none") {
                    $(element).css("display", "block");
                    tabDeleteOrMix();
                    return false;
                }
            });
        }

        function tabDeleteOrMix() {
            var param = $("#dropDown").find("> li");
            $(param).each(function (index, element) {
                if ($(element).css("display") == "block") {
                    $(element).css("display", "none");
                    return false;
                }
            });
        }

        function hiveTabClick(e, index, length) {
            //e.preventDefault()
            $(".hiveTabStyle").removeClass("hiveTabClickStyle");
            $(".hiveTab").css("display", "none");
            $("#" + $(e).attr("name")).css("display", "block");
            var param = $(e).addClass("hiveTabClickStyle").parent();
            if (param.css("display") == "none") {
                param.css("display", "block");
                if ((index + 1) < length / 2) {
                    hiveTabShowOrHide($(e).attr("value"), $(e).attr("value"));
                } else {
                    hiveTabShowOrHide(" ", $(e).attr("value"));
                }
            }
        }

        function findHiveTable(val) {
            $("#tabOutput").removeClass("hiveTabClickStyle")
            var hiveBoolear = true;
            var param = $("#tableHeader").find("> li > a").length;
            if (param > 0) {
                $("#tableHeader").find("> li > a").each(function (index, element) {
                    if ($(element).attr("title") == val) {
                        hiveTabClick(this, index, param);
                        hiveBoolear = false;
                        return false;
                    } else {
                        hiveBoolear = true;
                    }
                });
                return hiveBoolear;
            } else {
                return hiveBoolear;
            }
        }

        function removecloud() {
            $("#loadingDiv").css("display", "none");

            $(".hiveTables").dblclick(function () {
                var val = $(this).attr("name");
                if (findHiveTable(val)) {
                    excuteHive("select * from " + val);//"select * from " + param
                }
            });
            $(".tableColumn").dblclick(function () {
                writeTable(this);
            });

            $('.tree li:has(ul)').addClass('parent_li');
            $('.tree li.parent_li > span > i').on('click', function (e) {
                //var children = $(this).parent('li.parent_li').find(' > ul > li');
                var children = $(this).parent().siblings();
                if (children.find("li").length == 0) {
                    getTableField($(this).parent());
                } else {
                    findColumns($(this).parent());
                }
            });

            var menu = new BootstrapMenu('.parent_li', {
                fetchElementData: function (e) {
                    console.log(e);
                    return e[0];
                },
                actions: [{
                    name: $.i18n.prop('d_hive_open'),
                    iconClass: "fa fa-table",
                    onClick: function (e) {
                        var val = $(e).find("span").attr("name");
                        if (findHiveTable(val)) {
                            excuteHive("select * from " + val);
                        }
                        $(menu).remove();
                    },
                    isEnabled: function (e) {
                        return true;
                    }
                }, {
                    name: $.i18n.prop('d_hive_newColumn'),
                    iconClass: "fa fa-columns",
                    onClick: function (e) {
                        var val = $(e).find("span").attr("name");
                        //excuteHive("alter table "+ val +" add columns (d string) " + val);
                        var message = "<div>" + columns + '<div style="">'
                                      + '<label class="col-md-12 control-label" style="padding: 5px 0px;">SQL Preview:</label>'
                                      + '<div class="col-md-12" style="background-color: #ffffff;border: 1px solid #ccc;height: 130px;overflow: auto;">'
                                      + '<ul class="hiveSql" style="list-style: none" id="addHiveSql">'
                                      + '</ul></div></div></div>';
                        hiveDialog(message, $.i18n.prop('d_hive_newColumn'), e);
                    },
                    isEnabled: function (e) {
                        return true;
                    }
                }, {
                    name: $.i18n.prop('d_hive_newTable'),
                    iconClass: "fa fa-plus-square-o",
                    onClick: function (e) {
                        var val = $(e).find("span").attr("name");
                        var message = '<div><div style="height: 35px">'
                                      + '<label class="col-md-2 control-label" style="padding: 5px 0px;">Table:</label>'
                                      + '<div class="col-md-10">'
                                      + '<input id="table_name" class="form-control" value="table_name" type="text" style="padding: 0 10px;height: 25px;width: 300px;float: left;">'
                                      //+ '<input type="checkbox" id="EXTERNAL" value="EXTERNAL"
                                      // style="margin: 6px" name="hiveCheckbox">EXTERNAL'
                                      + '<input type="checkbox" id="EXISTS" value="EXISTS" style="margin: 6px" name="hiveCheckbox">EXISTS</div></div>'
                                      + '<label class="col-md-12 control-label" style="padding: 0px">Columns:</label>'
                                      + '<div style="height: 250px;border: 1px solid #ccc;margin-top: 21px;background-color: #ffffff;">'
                                      + '<div class="col-md-11" style="padding:0px;width: 95%;height:250px;overflow: auto;">'
                                      + '<ul style="list-style: none;padding: 0px;margin: 0px" id="hiveColumns"></ul>'
                                      + '</div><div class="col-md-1" style="background-color: #eee;border-left: 1px solid #ccc;height: 248px;width: 5%;padding: 0 2px">'
                                      + '<ul style="list-style: none;padding-left: 0px" id="newTableColumns">'
                                      + '<li><i class="fa fa-plus" style="padding: 3px"></i></li>'
                                      + '<li><i class="fa fa-minus" style="padding: 3px"></i></li>'
                                      + '<li><i class="fa fa-arrow-up" style="padding: 3px"></i></li>'
                                      + '<li><i class="fa fa-arrow-down" style="padding: 3px"></i></li></ul>'
                                      + '</div></div>'
                                      + '<div style="">'
                                      + '<label class="col-md-12 control-label" style="padding: 5px 0px;">SQL Preview:</label>'
                                      + '<div class="col-md-12" style="background-color: #ffffff;border: 1px solid #ccc;height: 130px;overflow: auto;">'
                                      + '<ul class="hiveSql" style="list-style: none" id="addHiveSql">'
                                      + '</ul></div></div></div>';
                        hiveDialog(message, $.i18n.prop('d_hive_newTable'));
                    },
                    isEnabled: function (e) {
                        return true;
                    }
                }, {
                    name: $.i18n.prop('d_hive_rename'),
                    iconClass: "fa fa-i-cursor",
                    onClick: function (e) {
                        var val = $(e).find("span").attr("name");
                        var message = '<div><label class="col-md-12 control-label" style="padding: 0px">Columns:</label>'
                                      //+ '<div style="height: 250px;border: 1px solid
                                      // #ccc;margin-top: 18px;background-color: #ffffff;">'
                                      + '<input id="table_name" class="form-control" type="text" style="padding: 0 10px;height: 25px;">'
                                      + '<div style="">'
                                      + '<label class="col-md-12 control-label" style="padding: 5px 0px;">SQL Preview:</label>'
                                      + '<div class="col-md-12" style="background-color: #ffffff;border: 1px solid #ccc;height: 130px;overflow: auto;">'
                                      + '<ul class="hiveSql" style="list-style: none" id="addHiveSql">'
                                      + '</ul></div></div>';
                        hiveDialog(message, $.i18n.prop('d_hive_rename'), e);
                    },
                    isEnabled: function (e) {
                        return true;
                    }
                }, {
                    name: $.i18n.prop('d_hive_delete'),
                    iconClass: "fa fa-trash-o",
                    onClick: function (e) {
                        var val = $(e).find("span").attr("name");
                        setTimeout(excuteHive("drop table " + val), 500);
                        if (isExcuteTrue) {
                            $(e).remove();
                        }
                        ;
                    },
                    isEnabled: function (e) {
                        return true;
                    }
                }, {
                    name: $.i18n.prop('d_hive_empty'),
                    iconClass: "fa fa-recycle",
                    onClick: function (e) {
                        var val = $(e).find("span").attr("name");
                        excuteHive("truncate table " + val);
                    },
                    isEnabled: function (e) {
                        return true;
                    }
                }]
            });
        }

        var columns = '<div style="z-index: 20;padding: 10px;background-color: #eee;height: 45px;" id="columns">'
                      + '<div class="row" style="margin: 0px 0px 5px 0px">'
                      + '<label class="col-md-2" style="padding: 5px 0px;">Name:</label>'
                      + '<div class="col-md-4" style="padding: 0px 3px">'
                      + '<input id="columnsName" class="form-control" value="column_name" type="text" style="padding: 0 10px;height: 25px"></div>'
                      + '<label class="col-md-2" style="padding: 5px;">Type:</label>'
                      + '<div class="col-md-4" style="padding: 0px 3px">'
                      + '<input id="columnsType" class="form-control" type="text" style="padding: 0 10px;height: 25px" value="Int"></div></div>'
                      /*+ '<div class="row" style="margin: 0px 0px 5px 0px">'
                       + '<label class="col-md-2" style="padding: 5px 0px;">Default:</label>'
                       + '<div class="col-md-3" style="padding: 0px 3px">'
                       + '<input id="columnsDefault" class="form-control" type="text" style="padding: 0 10px;height: 25px"></div>'
                       + '<div class="col-md-7" style="padding: 3px 0px;">'
                       + '<input type="checkbox" id="Primery" value="Primery Key" style="margin: 3px" name="hiveCheckbox">PrimeryKey'
                       + '<input type="checkbox" id="Nullable" value="null" style="margin: 3px" name="hiveCheckbox">Nullable'
                       + '<input type="checkbox" id="Auto_increment" value="Auto_increment" style="margin: 3px" name="hiveCheckbox">Auto_increment</div></div>'*/
                      + '</div>';

        function hiveDialog(message, param, e) {
            var val = $(e).find("span").attr("name");
            $(message).dialog({
                title: param,
                onBeforeShow: function () {
                    $('.xdsoft_dialog_buttons button.xdsoft_btn:first-child').text($.i18n.prop('d_confirm'));
                    $('.xdsoft_dialog_buttons button.xdsoft_btn:last-child').text($.i18n.prop('d_cancel'));
                    switch (param) {
                        case $.i18n.prop('d_hive_newTable'):
                            onShowStyle();
                            break;
                        case $.i18n.prop('d_hive_newColumn'):
                            addColumns(val);
                            break;
                        case $.i18n.prop('d_hive_rename'):
                            remnameHivetable(val);
                            break;
                    }
                },
                onAfterShow: function () {

                },
                onAfterHide: function () {
                    hiveColumnsNumber = 1;
                },
                buttons: {
                    " ": function () {
                        var str = $("#addHiveSql").text();
                        str =
                            str.substring(0, str.lastIndexOf(",")) + str.substr(str.lastIndexOf(",")
                            + 1);
                        switch (param) {
                            case $.i18n.prop('d_hive_newTable'):
                                $("#sql").val(str);
                                break;
                            case $.i18n.prop('d_hive_newColumn'):
                                excuteHive(str);
                                if (isExcuteTrue) {
                                    $("#dataTable" + $(e).find("span").attr("id")).empty();
                                }
                                ;
                                break;
                            case $.i18n.prop('d_hive_rename'):
                                var val = $("#table_name").val();
                                excuteHive(str, e, val);
                                break;
                        }
                    },
                    "": function () {
                        //return false;
                    }
                }
            });
        }

        var hiveColumnsNumber = 1;
        var isColumn = false;

        function alertStyle(param) {
            $(".xdsoft_dialog_shadow_effect").css("width", "550px");
            $(".xdsoft_dialog_content").css("background-color", "#eee");
            $(".xdsoft_dialog_buttons").css("background-color", "#eee");
            $("#addHiveSql").append(param);
        }

        function remnameHivetable(val) {
            $("#table_name").val(val);
            var param = "<li id='hqlColumn0'>ALTER TABLE " + val + " RENAME TO <span>" + val
                        + "</span>;</li>";
            alertStyle(param);
            $("#table_name").on("input", function () {
                $("#hqlColumn0 span:first").text($(this).val());
            })
        }

        function addColumns(val) {
            var param = "<li id='hqlColumn0'>ALTER TABLE " + val + " ADD COLUMNS (<span>"
                        + $("#columnsName").val() + "</span>"
                        + "<span>  " + $("#columnsType").val() + "</span>);</li>";
            $("#columns").css("padding-left", "1px");
            alertStyle(param);
            $("#columnsType").on("input", function () {
                $("#hqlColumn0 span:last").text(" " + $(this).val());
            })
            $("#columnsName").on("input", function () {
                $("#hqlColumn0 span:first").text(" " + $(this).val());
            })
        }

        function onShowStyle() {
            var param = "<li id='hqlColumn0'>CREATE<span></span> TABLE<span></span> <i>table_name</i>(</li><li>);</li>";
            alertStyle(param);
            $("#newTableColumns").find(".fa-plus").click(function () {
                columnsEvent();
            })
            $("#newTableColumns").find(".fa-minus").click(function () {
                var param = parseInt($("#showColumns").find(".showColumns").attr("name")) + 1;
                $("#showColumns").remove();
                $("#hqlColumn" + param).remove();
            })
            $("#newTableColumns").find(".fa-arrow-up").click(function () {
                $("#hiveColumns").find("").addChild("");
            })
            $("#newTableColumns").find(".fa-arrow-down").click(function () {
                $("#hiveColumns").find("").addChild("");
            })
            $("#table_name").on("input", function (e) {
                $("#hqlColumn0 i").text($(this).val());
            })
            $("#table_name").siblings().on("change", function (e) {
                /*if($("#EXTERNAL").is(':checked')) {
                 $("#hqlColumn0 span:first").text(" EXTERNAL ");
                 }else {
                 $("#hqlColumn0 span:first").text(" ");
                 }*/
                if ($("#EXISTS").is(':checked')) {
                    $("#hqlColumn0 span:last").text(" IF NOT EXISTS ");
                } else {
                    $("#hqlColumn0 span:last").text(" ");
                }
            })
        }

        function columnsEvent() {
            if (isColumn) {
                isColumn = false;
                columnsAddLi();
                $("#showColumns").find(".showColumns").css("padding", "5px");
            }
            columnsAddLiZero();
            columnsAddLi();
            columnsAddLiTow();
            columnsAddLiThree();
        }

        function columnsAddLiZero() {
            $("#columns").remove();
            $("#showColumns").attr("id", "");
            $(".showColumns").css("display", "block");
            $("#hiveColumns").append("<li id='showColumns'><div class='showColumns'  style='display: none' name='"
                                     + hiveColumnsNumber + "'></div>" + columns + "</li>");
            $("#columnsName").val("column_" + hiveColumnsNumber);
            hiveColumnsNumber++;
        };

        function inputChange() {
            var param = parseInt($("#showColumns").find(".showColumns").css("display",
                                                                            "none").text("").attr("name"))
                        + 1;
            columnsAddLi();
            $("#hqlColumn" + param).text($("#showColumns").find(".showColumns").text() + ",");
        }

        function columnsAddLi() {
            $($("#columns").find("input[type='text']")).each(function (index, element) {
                if (index == 2 && $(element).val() != null && $(element).val() != "undefined"
                    && $(element).val() != "") {
                    $("#showColumns").find(".showColumns").append("default " + $(element).val()
                                                                  + " ");
                } else {
                    $("#showColumns").find(".showColumns").append($(element).val() + " ");
                }
            })
            /*if(!$("#Nullable").is(':checked')) {
             $("#showColumns").find(".showColumns").append("Not Null ");
            }
             $($("#columns").find("input[name='hiveCheckbox']:checked")).each(function (index,element){
             if($(element).val() == "null"){
             $("#showColumns").find(".showColumns").append(" ");
             }else{
             $("#showColumns").find(".showColumns").append($(element).val() + " ");
             }
             })*/
        }

        function columnsAddLiTow() {
            $("#addHiveSql li:last-child").remove();
            if ($("#showColumns").find(".showColumns").text() == "") {
                $("#addHiveSql").append("<li id='hqlColumn" + hiveColumnsNumber
                                        + "' style='padding-left: 25px'></li>");
            } else {
                $("#addHiveSql").append("<li id='hqlColumn" + hiveColumnsNumber
                                        + "' style='padding-left: 25px'>"
                                        + $("#showColumns").find(".showColumns").text() + ",</li>");
            }
            $("#addHiveSql").append("<li>);</li>");
            $("#showColumns").find(".showColumns").css("padding", "5px");
        }

        function columnsAddLiThree() {
            $(".showColumns").unbind("click");
            $(".showColumns").click(function () {
                isColumn = true;
                if ($("#showColumns").find(".showColumns").text() == "") {
                    columnsAddLi();
                }
                $(".showColumns").css("padding", "5px").parent().attr("id", "");
                $("#columns").siblings().css("display", "block");
                $("#columns").remove();
                var list = $(this).css("padding", "0px").text().split(" ");
                $(this).text("").parent().append(columns).attr("id", "showColumns");
                columnsLiClick(list);
                $("#columns").find("input[type='text']").on("input", function (e) {
                    inputChange();
                })
            });
            $("#columns").find("input[type='text']").on("input", function (e) {
                inputChange();
            })
            /*$("#columns").find("input[type='checkbox']").on("change",function (e){
             inputChange();
             })*/
        }

        function columnsLiClick(list) {
            $(list).each(function (index, element) {
                if (index == 0) {
                    $("#columnsName").val(element)
                    //$(":checkbox[value='null']").prop("checked",true);
                } else if (index == 1) {
                    $("#columnsType").val(element)
                }
                /*else if(index == 3 && element != "Primery" && element != "Not"){
                 $("#columnsDefault").val(element)
                 }else if(element == "Primery"){
                 $(":checkbox[value='Primery Key']").prop("checked",true);
                 }else if(element == "Auto_increment"){
                 $(":checkbox[value='Auto_increment']").prop("checked",true);
                 }else if(element == "Not"){
                 $(":checkbox[value='null']").prop("checked",false);
                 }*/
            })
        }

        function getTableField(param) {
            var num = $(param).attr("id");
            var tableName = $(param).attr("name");
            var dbName = $("#dbName").val();
            document.getElementById("Hive_name").innerHTML = param.innerText;

            settings.HttpClient("GET", null,
                                "/service/v1/get/tableDescInfoList?" + "dbName=" + dbName
                                + "&tableName=" + tableName, function (response) {
                    //console.log(response);
                    showTableField(response, num);
                    findColumns(param);
                });
        }

        function showTableField(arrayList, num) {
            $("#dataTable" + num).empty();
            if (arrayList) {
                for (var i = 0; i < arrayList.length; i++) {
                    $("#dataTable"
                      + num).append('<li style="display:none; padding: 5px 30px;" class="' + i
                                    + '" name="'
                                    + arrayList[i] + '" title="' + arrayList[i] + '">'
                                    + arrayList[i] + '</li>');
                }
            }
        }

        function findColumns(e) {
            var children = $(e).parent('li.parent_li').find(' > ul > li')
            if (children.is(":visible")) {
                children.hide(1000);
                $(e).find(' > i').addClass('ion-chevron-right').removeClass('ion-chevron-down');
            } else {
                children.show(1000);
                $(e).find(' > i').addClass('ion-chevron-down').removeClass('ion-chevron-right');
                document.getElementById("Hive_name").innerHTML = $(e).text();
            }
            //e.stopPropagation();
        }

        function clickclick(id) {
            var sql = $("#sql").val();
            var list = sql.split(";");
            var s = list[id];
            var t = document.getElementById('sql')
            if (typeof t.createTextRange != 'undefined') { //IE
                var r = t.createTextRange();
                r.findText(s);
                r.select();
            } else if (typeof window.find != 'undefined')  //firefox,chrome
            {
                window.find(s);
            }
        }

        $("#hiveHelp").click(function () {
            initDialog().initAlert($.i18n.prop('d_hive_doubleClick'), $.i18n.prop('d_hive_help'),
                                   2000);
        });
        $("#Reflact").click(function () {
            $("#loadingDiv").css("display", "block");
            setTimeout(getdbName, 200);
        });
        $("#qingchuSQL").click(function () {
            $("#sql").val("");
        });
        var displayTab = 1;
        $("#expandResults").on('click', function (event) {
            $("#content2").css("height", "100%").addClass("hiveAllScreen");
            $("#startTable").height($("#content1").height() - 35 + "px");
            mixOrMinOnclick();
            var param = $("#tableHeader").find("> li");
            while (displayTab * param.width() < $("#tableHeader").width()) {
                if (displayTab > param.length) {
                    break;
                }
                hiveDeleteOrMix(param);
                displayTab++;
            }
            //$("#hit_area").css("display","block");
            $("#hit_area2").mouseover(function () {
                $("#hit_area").css("display", "block")
                toggleUp();
            });
        });
        var hiveTabMinHide;
        $("#smallHive").click(function () {
            hiveNarrow();
        });
        document.onkeyup = function (e) {
            e = e || window.event;
            var code = e.which || e.keyCode;
            if (code == 27) {
                hiveNarrow();
            }
        }
        function hiveNarrow() {
            $("#content2").css("height", "34%").removeClass("hiveAllScreen")
            $("#startTable").height($("#content1").height() - 35 + "px");
            toggleUp();
            $("#hit_area").css("display", "none");
            $("#hit_area2").unbind("mouseover");
            hiveTabMinHide = "";
            var param = $("#tabOutput") //$("#tableHeader").find("> li");
            mixOrMinOnclick()
            while (displayTab * param.width() > $("#tableHeader").width()) {
                hiveTabShowOrHide();
                displayTab--;
            }
            $("#hiveListTab" + hiveTabMinHide).css("display", "none");
        }

        $("#hit_area").mouseover(function () {
            $(this).css("display", "none");
            toggleDown();
        });

        function mixOrMinOnclick() {
            var param = $("#tableHeader").find("> li");
            if ($("#tabOutput").css("display") == "block") {
                displayTab = 1
            } else {
                displayTab = 0
            }
            $(param).each(function (index, element) {
                if ($(element).css("display") == "block") {
                    displayTab++;
                }
            })
        }

        var down = false;

        function toggleDown() {
            if (down == false) {
                down = true;
                $("#menu_holder").css({"top": "0px", "display": "block"});
            }
        }

        function toggleUp() {
            if (down == true) {
                down = false;
                $("#menu_holder").css({"top": "-58px", "display": "none"});
            }
        }

    }

    return {
        hive_toos: init
    }
})
/*for (var key in list) {
 if (list[j].trim() == key.trim()) {
 if (arrayList[key].length != 0) {
 if(arrayList[key][0][0] == "ERROR"){
 executeResult(list[j],arrayList[key][0][1])
 return;
 }
 executeResult(list[j],"")
 appendTable(arrayList[key],list[j]);
 }else{
 //appendHiveTables({},[],list[j]);
 executeResult(list[j],"")
 }
 }
 }*/
