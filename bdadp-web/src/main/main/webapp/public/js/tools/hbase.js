define(["component/BdaTable", "js/modeldialog"], function (table, initDialog) {

    function init() {
        var table_contents = new Array();
        $("#shellString").val("");
        $("#hbaseQuery").click(function () {
            removeClass(this);
            $("#tab1").css("display", "block");
            $("#tab2").css("display", "none");
        })

        $("#hbaseScript").click(function () {
            removeClass(this);
            $("#tab2").css("display", "block");
            $("#tab1").css("display", "none");
        })
        function removeClass(e) {
            $(e).addClass("hbaseClickTab")
            $(e).siblings().removeClass("hbaseClickTab");
        }

        $("#resetShell").click(function () {
            $("#shellString").val("");
        })

        $("#executeShell").click(function () {
            executeShell();
        })
        getHbaseInfo();
        /**
         * show htable details including htable tree and tab title
         * @param e
         */
        function showTableDetails(e) {
            // tabs title
            var tabs = document.getElementById("htab_title");
            if (tabs.style.display == 'none') {
                tabs.style.display = 'block'
            }
            document.getElementById("htable_name").innerHTML = e.innerText;
            $("#queryTable").empty();
            $("#pagination").empty();

            var array = $(e).attr("name").split(",");
            for (i = 0; i < array.length; i++) {
                $(e.nextSibling).append('<li style="padding: 5px 0px 5px 30px;display: none"> '
                                        + array[i] + '</li>');
            }
            var children = $(e).parent().find(' > ul > li');//.find(' > ul > li')
            if (children.is(":visible")) {
                children.hide(200);
                $(e).find(' > i').addClass('ion-chevron-right').removeClass('ion-chevron-down');
                $(e.nextSibling).empty();
            } else {
                children.show(200);
                $(e).find(' > i').addClass('ion-chevron-down').removeClass('ion-chevron-right');
            }
            event.stopPropagation();
        }

        function insertAfter(newEl, targetEl) {
            var parentEl = targetEl.parentNode;

            if (parentEl.lastChild == targetEl) {
                parentEl.appendChild(newEl);
            } else {
                parentEl.insertBefore(newEl, targetEl.nextSibling);
            }
        }

        /**
         * get all htables and their basic para
         */
        function getHbaseInfo() {
            settings.HttpClient("GET", null, "/service/v1/get/HbaseInfo/tables",
                                function (response) {
                                    console.log(response);
                                    showTableTree(response);
                                });
        }

        /**
         * show htable tree
         * @param data
         */
        function showTableTree(data) {
            $("#tableTreee").empty();
            var map = data["tables"];
            $.each(map, function (key, value) {
                $("#tableTreee").append('<li style="margin-top: 0px;"><span style="width: 100%" name="'
                                        + value["cfs"] + '" class="hbasetable" >'
                                        + key
                                        + '<i class="ion-chevron-right" style="float: right;padding-right: 10%"></i></span>'
                                        + '<ul style="padding-left: 25%;background: #fafafa"></ul></li>');
            });
            $(".hbasetable").click(function () {
                showTableDetails(this);
            });
        }

        function testTableTree(data) {
            var map =
            {
                'table1': 'des1',
                'table2': 'des2',
                'table3': 'des3'
            };

            $.each(map, function (key, value) {
                $("#tableTreee").append('<p><a href="#" onclick="showTableDetails(this);">' + key
                                        + '</a></p>');
            });
        }

        $("#findHbaseTables").click(function () {
            queryForRsp();
        });
        /**
         * Scan table to get response.
         */
        function queryForRsp() {
            var head = {};
            var dataArr = [];
            var tableName = document.getElementById("htable_name").innerHTML;
            if (tableName == "No-Table-Selected") {
                console.log("error: no table selected.");
                initDialog().initAlert($.i18n.prop('d_hbase_placeSelect'),
                                       $.i18n.prop('d_hbase_warn'), 2000);
                return;
            }
            var startrow = document.getElementById("startkey").value;
            var endrow = document.getElementById("endkey").value;

            $("#queryTable").empty();
            $("#pagination").empty();
            table_contents = [];

            var serializeObj = {};
            serializeObj["startkey"] = startrow;
            serializeObj["endkey"] = endrow;
            serializeObj["tablename"] = tableName;
            settings.HttpClient("POST", serializeObj, "/service/v1/get/HbaseInfo/scan",
                                function (response) {
                                    console.log(response);
                                    //appendTable(response);
                                    head = {
                                        "dataField": ["Rowkey", "CF", "Qualifier", "Value"],
                                        "headText": ["Rowkey", "CF", "Qualifier", "Value"],
                                        "events": [{}, {}, {}, {}]
                                    };
                                    var data = response["kvs"];
                                    for (var i = 0; i < response["kvs"].length; i++) {
                                        dataArr.push({
                                            "Rowkey": data[i]["rowkey"],
                                            "CF": data[i]["cf"],
                                            "Qualifier": data[i]["qualifier"],
                                            "Value": data[i]["value"]
                                        })
                                    }
                                    appendHbaseTables(head, dataArr);
                                });
        }

        function appendHbaseTables(head, dataArr) {
            var userTables = new table.BdadpTable("userTableId", head, dataArr);
            var tableHtml = userTables.creatTable();
            $("#queryTable").append(tableHtml);
            var $table = $('#userTableId');
            //添加数据
            $table.bootstrapTable({
                data: dataArr
            });
            $("div .fixed-table-body .fixed-table-loading").empty();
        }

        /**
         * execute hbase shell and display response
         */
        function executeShell() {
            var str = $("#shellString").val();
            var serializeObj = {};
            serializeObj["shellStr"] = str;
            settings.HttpClient("POST", serializeObj, "/service/v1/get/HbaseInfo/shell",
                                function (response) {
                                    console.log(response);
                                    showShellRsp(response);
                                });
        }

        /**
         * display response of hbase shell execuation.
         * @param data
         */
        function showShellRsp(data) {
            $("#shellRsp").empty();
            var message = data["message"];
            var error = data["throwable"];
            console.log("message>>> " + message);
            console.log("error>>> " + error);
            $("#shellRsp").append("<p>").append(message).append("</p>");
            $("#shellRsp").append("<p>").append(error).append("</p>");
        }
    }

    return {
        hbase_tool: init
    }
})


