/**
 * Created by msc on 2016/9/14.
 */
define([], function () {
    'use strict';
    return function () {
        function init() {
            $(".hdfs").click(function () {
                $(".tools").load("./html/tools/hdfs.html", function () {
                    srcCss("hdfs");
                    require(["./js/tools/hdfs"], function (hdfsTool) {
                        settings.interFun();
                        hdfsTool.hdfs_tools();
                    })
                });
            });

            $(".hive").click(function () {
                $(".tools").load("./html/tools/hive.html", function () {
                    srcCss("hive");
                    require(["./js/tools/hive"], function (hiveTool) {
                        settings.interFun();
                        hiveTool.hive_toos();
                    })
                });
            });

            $(".hbase").click(function () {
                $(".tools").load("./html/tools/hbase.html", function () {
                    srcCss("hbase");
                    require(["./js/tools/hbase"], function (hbaseTool) {
                        settings.interFun();
                        hbaseTool.hbase_tool();
                    })
                });
            });
        }

        function srcCss(param) {
            $("#" + param).attr("src", "./img/tools/" + param + "_Click.png");
            $("." + param).css("background", "#f2f2f2").siblings().css("background", "#e6e6e6");
            var src = $("." + param).siblings().find("img");
            for (var i = 0; i < src.length; i++) {
                var aa = $(src[i]).attr("src").replace("_Click", "");
                $(src[i]).attr("src", aa);
                console.log($(src[i]).attr("src").replace("_Click", ""));
            }
            //src.substring(src.indexOf("_"),src.indexOf("."));
        }

        return {
            allTools: init
        };
    }
})
