/**
 * Created by msc on 2016/9/14.
 */
define(['js/modeldialog', "js/scene-develop/scene-common"], function (initDialog, sceneCommon) {

  function init() {
        $('#file').filestyle({
          buttonText: ''
        });
        $("#file").change(function () {
          var form = document.forms["fileForm"];
          var file = form["file"].files[0];
          $("#fileName").val(file.name);
        });

        $("#submitFile").click(function () {
          doUpload();
        });

        $("#executeShell").click(function () {
          executeShell();
        });

        $("#resetShell").click(function () {
          resetShell();
        });

        //getHdfsInfo("/");
        $("#loadingDiv").css("display", "block");
        setTimeout(getHdfsInfo, 100);
        function doUpload() {
          var param;
          var fileName = $("#fileName").val();
          var filePath = $("#filePath").val();
          param = "filePath=" + filePath + "&fileName=" + fileName;
          /* settings.HttpClient("GET", document.forms["fileForm"], "/service/v1/upload?" + param, function (response) {
           getHdfsInfo(filePath);
           });*/

          var ajax_option = {
            url: settings.globalVariableUrl()+"/service/v1/upload?" + param,
            dataType: "json",
            success: function (data) {
              if (data.status == "0") {
                initDialog().initAlert($.i18n.prop('d_hdfs_uploadSuccessfully'),
                                       $.i18n.prop('d_hdfs_uploadFile'));
                getHdfsInfo(filePath);
                console.log(data);
              } else {
                initDialog().initAlert($.i18n.prop('d_hdfs_uploadFail'),
                                       $.i18n.prop('d_hdfs_uploadFile'));
              }
            },
            error: function (data) {
              initDialog().initAlert($.i18n.prop('d_hdfs_uploadFail'),
                                     $.i18n.prop('d_hdfs_uploadFile'));
            }
            }
          $('#fileForm').ajaxSubmit(ajax_option);
        }

        /**
         * reset hbase shell input block.
         */
        function resetShell() {
          $("#shellString").val("");
        }

        function removecloud() {
          $("#loadingDiv").css("display", "none");
        }

        function getHdfsInfo(path) {
          var val;
          if (path == null || path == "" || path == "undefined") {
            val = "/";
          } else {
            val = path;
          }
          settings.HttpClient("GET", null, "/service/v1/get/HdfsInfo/?path=" + val,
                              function (response) {
                                console.log(response);
                                dfsLs(response);
                              });
        }

        function dfsOpen(filePath) {
          console.log(pwd, pwd.length);
          if (filePath == "..") {
            if (pwd.length > 0) {
              filePath = pwd.pop()
            } else {
              filePath = "/"
            }
            } else {
            pwd.push($("#filePath").val())
            }
          console.log(filePath)
          $("#filePath").val(filePath);
          getHdfsInfo(filePath);
        }

        function dfsLs(data) {
          $("#fileBrowse").empty();

          var _listGroup = '<div class="list-group" style="margin-bottom:0px;">';
          var _listGroupItems = '<a style="cursor: pointer;background: #f2f2f2;" class="list-group-item dfs-dir"><span class="glyphicon glyphicon-folder-open">..</span></a>';
          $.each(data, function (name, isDirectory) {
            if (isDirectory) {
              _listGroupItems +=
                  '<a style="cursor: pointer;background: #f2f2f2;" class="hdfsfile list-group-item dfs-dir"><span class="glyphicon glyphicon-folder-open">'
                  + name + '</span></a>';
            } else {
              _listGroupItems +=
                  '<a style="cursor: pointer;background: #f2f2f2;" name="' + name
                  + '" class="hdfsfile list-group-item dfs-file"><span class="glyphicon glyphicon-list-alt">'
                  + name + '</span></a>'
            }
          });
          var _html = _listGroup + _listGroupItems + "</div>"

          $("#fileBrowse").append(_html);
          $(".list-group-item.dfs-dir").each(function (i) {
            $(this).click(function (i) {
//                var filePath = $(this).children("span").html();
              dfsOpen(this.innerText);
            })
          });

          var menu = new BootstrapMenu(".hdfsfile", {
            fetchElementData: function (e) {
              console.log(e);
              return e[0];
                },
            actions: [{
              name: $.i18n.prop('d_hdfs_delete'),
              iconClass: "fa fa-trash-o",
              onClick: function (e) {
                dfsRm(e)
                $(menu).remove();
              },
              isEnabled: function (e) {
                return true;
              }
            },
              {
                name: $.i18n.prop('d_hdfs_open'),
                iconClass: "fa fa-file-text-o",
                onClick: function (e) {
                  if ($(e).attr("name")) {
                    var value = {"path": $(e).attr("name")};
                    settings.HttpClient("POST", value, "/service/v1/read/hdfsFile",
                                        function (response) {
                                          var message = '<div style="border: 1px solid #ccc;width: 100%;height: 300px;overflow: auto;">'
                                                        + response + '</div>';
                                          hdfsDialog(message, e);
                                        }, function (reponse) {
                          var message = '<div style="border: 1px solid #ccc;width: 100%;height: 300px;overflow: auto;">'
                                        + response + '</div>';
                          hdfsDialog(message, e);
                        });
                  } else {
                    dfsOpen(e.innerText);
                  }
                },
                isEnabled: function (e) {
                  return true;
                }
              },
              {
                name: $.i18n.prop('d_hdfs_downLoad'),
                iconClass: "fa fa-floppy-o",
                onClick: function (e) {
                  var path = $(e).text();
                  var name = path.substring(path.lastIndexOf("/") + 1,
                                             path.length);
                  console.log(name);
                  var value = {"filePath": $(e).text()};
                  settings.HttpClient("POST", value, "/service/v1/downLoad/hdfsFile",
                                      function (response) {
                                        if ($("#header-reminder").css("display") == "none") {
                                          $(".dropdown-toggle .fa-bell-o:visible").trigger("click");
                                        }
                                        var sceneLengthName = sceneCommon.getStrLength(name, 10);
                                        var exportCount = 0;
                                        if ($("#exportNum").text() == 0) {
                                          exportCount++;
                                          $("#exportNum").text(exportCount);
                                          $("#exportNum").css("display", "block")
                                        } else {
                                          var exportNum = parseInt($("#exportNum").text()) + 1;
                                          $("#exportNum").text(exportNum);

                                        }
                                        var remindetool = $(".reminder-scenario");
                                        var tools_html = "";
                                        console.log(response);
                                        var url=settings.globalVariableUrl()+'/service/v1/resources/tools/';
                                        tools_html +=
                                            '<li class="header-reminder" id=' + response + ' name='
                                            + sceneLengthName + '><i id="export-wrap">' +
                                            '<div class="export-wrap" style="padding:10px 0;">' +
                                            '<span class="statwidth"><i class=""></i><span>'
                                            + sceneLengthName + '</span><i class=""></i></span>' +
                                            '<span class="process-num"></span>' +
                                            '</div>' +
                                            '<div class="progress progress-tiao">' +
                                            '<div class="progress-bar progress-bar-danger progress-bar-striped" role="progressbar" aria-valuenow="80" aria-valuemin="0" aria-valuemax="100" style="width: 0">'
                                            +
                                            '</div>' +
                                            '</div>' +
                                            '<div class="reminder-btn">' +
                                            '<span class="statText"><span class="span_reminder_nowExport"></span></span>'
                                            +
                                            '<button type="button" class="btn btn-primary reminder-cancel btnCancel"><span class="span_reminder_cancel"></span></button>'
                                            +
                                            '<a href='+url + response
                                            + '/zip?name=' + name
                                            + '><span class="span_reminder_download"></span></a>' +
                                            '</div>' +
                                            '</i></li>';

                                        remindetool.prepend(tools_html);
                                        settings.InterNation(['.span_reminder_export',
                                                              '.span_reminder_num',
                                                              '.span_reminder_nowExport',
                                                              '.span_reminder_cancel',
                                                              '.span_reminder_download'],
                                            ['d_reminder_scenarioExport', 'span_reminder_num',
                                             'd_reminder_beingExport', 'd_reminder_cancel',
                                             'd_reminder_download']);

                                      });
                },
                isEnabled: function (e) {
                  return true;
                }
              }]
          });
          removecloud();
        }

    function hdfsDialog(message, e) {
      $(message).dialog({
        title: $.i18n.prop('d_hdfs_open'),
        onBeforeShow: function () {
          $(".xdsoft_dialog_shadow_effect").css("width", "550px");
          $('.xdsoft_dialog_buttons button.xdsoft_btn:first-child').text($.i18n.prop('d_confirm'));
          $('.xdsoft_dialog_buttons button.xdsoft_btn:last-child').text($.i18n.prop('d_cancel'));
        },
        onAfterShow: function () {

        },
        onAfterHide: function () {

        },
        buttons: {
          " ": function () {

          },
          "": function () {
            //return false;
          }
        }
      });
        }

        function dfsCommand(command, callback) {
          var parameter = "prog=hdfs&command=" + command;
          var path = "/exec/command";
          settings.HttpClient("GET", null, "/exec/command" + parameter, function (response) {
                if (callback && callback != null) {
                  initDialog().initAlert($.i18n.prop('d_hdfs_deleteSuccessfully'),
                                         $.i18n.prop('d_hdfs_deleteFile'));
                }
          });
        }

        function dfsRm(e) {
          /*dfsCommand("hdfs dfs -rm -r " + e.innerText, function () {
           $(e).remove();
           });*/
          $(e).remove();
          console.log(e.innerText)
          var value = {"path": e.innerText};
          settings.HttpClient("DELETE", value, "/service/v1/delete/hdfsFile",
                              function (response) {
                                if (response && response != null) {
                                  initDialog().initAlert($.i18n.prop('d_hdfs_deleteSuccessfully'),
                                                         $.i18n.prop('d_hdfs_deleteFile'));
                                  var filePath = $("#filePath").val();
                                  getHdfsInfo(filePath);
                                } else {
                                  initDialog().initAlert($.i18n.prop('d_hdfs_deleteFail'),
                                                         $.i18n.prop('d_hdfs_deleteFile'));
                                }
                              });
        }

        function executeShell() {
          var command = $("#shellString").val()
//        if(command.substr(1, 4) != "hdfs"){
//            return;
//        }
          dfsCommand(command, function (data) {
            var responseData = data["responseData"];
            var outputLog = responseData["outputLog"];
            var errorLog = responseData["errorLog"];
            $("#shellRsp").empty();
            if (outputLog.length > 0) {
              $("#shellRsp").append(outputLog);
            } else if (errorLog.length > 0) {
              $("#shellRsp").append(errorLog);
            } else {
              $("#shellRsp").append(data["responseMessage"]);
            }
          })
        }

        function restShell() {
          $("shellString").empty();
          $("shellRsp").empty();
        }
        var pwd = []
  }

  return {
        hdfs_tools: init
  }

})
