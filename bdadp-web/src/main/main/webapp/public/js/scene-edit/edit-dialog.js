/**
 * Created by Hu on 2016/7/23.
 */
define(['js/settings', 'libs/i18n/international', 'libs/i18n/jquery.i18n.properties'], function () {
    'use strict';
    return function () {

        function Html(html) {
            this.html = typeof (html) == "undefined" ? '' : html;

            this.append = function (html) {
                if (typeof (html) == "string") {
                    this.html += html;
                    return;
                }
                if (typeof (html) == "object" && html.constructor.name == "Html") {
                    this.html += html.toString();
                    return;
                }
            };

            this.toString = function () {
                return this.html;
            };
        }

        function init(modelId) {
            this._html = new Html();
            this._html.append("<div class='modal fade' id='" + modelId
                              + "' tabindex='-1' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>");
            this._html.append("<div class='modal-dialog'>");
            this._html.append("<div class='modal-content'>");
            this._html.append('<div class="modal-header" style="padding: 5px;background: rgb(46, 76, 122);color: white;">');
            this._html.append('<button type="button" class="close" data-dismiss="modal" aria-hidden="true" style="color: white;opacity: 1;text-shadow: 0 0px 0 #fff;">&times;</button>');
            this._html.append('</div>');
            this._html.append("<div class='modal-body'></div>");
            this._html.append("<div class='modal-footer' style='padding-top: 5px;padding-bottom: 5px;background: rgba(204,204,204,0.3);'>");
            this._html.append("<button type='button' id='cancelBtn' class='btn btn-default' data-dismiss='modal'>"
                              + $.i18n.prop('d_cancel') + "</button>");
            this._html.append("<button type='button' id='confirmBtn' class='btn' style='color: white;background-color: rgb(46, 76, 122);'>"
                              + $.i18n.prop('d_confirm') + "</button>");
            this._html.append("</div>");
            this._html.append("</div><!-- /.modal-content -->");
            this._html.append("</div><!-- /.modal-dialog -->");
            this._html.append("</div><!-- /.modal -->");
            return this._html;
        }

        function _create(modelId) {
            return new init(modelId);
        }

        function _destroy(modelId) {
            if ($("#" + modelId).length > 0) {
                $("#" + modelId).remove();
            }
        }

        function _initDialog(_modelId, _contextSelector) {
            var modelId = typeof (_modelId) == "undefined" ? 'defaultModel' : _modelId;
            var contextSelector = typeof (_contextSelector) == "undefined" ? 'body'
                : _contextSelector;
            var dialog = _create(modelId);
            $(contextSelector).append(dialog.html);
            $('#' + modelId).on('hidden.bs.modal', function () {
                if ($("#" + modelId).length > 0) {
                    $("#" + modelId).remove();
                }
            })
            $("#" + modelId + " .modal-footer").each(function (index, dom) {
                $(dom).css("border-top", "0px");
            })

            $('#' + modelId).modal({backdrop: 'static', keyboard: false});

        }

        /**
         *
         * @param callback
         * @param param  object{message,..}
         * @param _modelId
         * @param _contextSelector
         * @private
         */
        function _initConfirmDialog(callback, callbackCancel, param, DialogShow, _modelId,
                                    _contextSelector, type) {
            var _param = typeof(param) == "undefined" ? {} : param;
            if (_param) {
                _param.defaultMessage = $.i18n.prop('d_warming_reinput');
            }
            var message = _param && (_param.message || _param.defaultMessage);
            var modelId = typeof (_modelId) == "undefined" || _modelId == null ? 'defaultModel'
                : _modelId;
            var contextSelector = typeof (_contextSelector) == "undefined" || _contextSelector
                                                                              == null ? 'body'
                : _contextSelector;   //default context is body
            var confirmDialog = _create(modelId);
            $(contextSelector).append(confirmDialog.html);
            $('#' + modelId).on('hidden.bs.modal', function () {
                if ($("#" + modelId).length > 0) {
                    $("#" + modelId).remove();
                }
            })
            $("#" + modelId + " .modal-footer").each(function (index, dom) {
                $(dom).css("border-top", "0px");
            })

            //confirm dialog css setting
            $('#' + modelId).on('show.bs.modal', function () {
                $(".modal-dialog").each(function (index, dom) {
                    $(dom).css("margin-top", "10%");
                })
                $('.modal-backdrop.fade.in').attr('class', 'modal-backdrop fade');

                $(".modal-header").each(function (index, dom) {
                    if (index == 0) {
                        $(dom).append('<h4 class="modal-title" id="myModalLabel"><span>'
                                      + $.i18n.prop("d_span_execu_prompt") + '</span></h4>');
                    }
                })

                if (type == 'textarea') {
                    $(".modal-body").each(function (index, dom) {
                        if (index == 0) {
                            $(dom).append("<div class='task_log'><textarea style=' overflow:scroll;resize:none'  class='form-control' cols='300' rows='12'></textarea></div>")
                        }
                    })
                    $('.modal-dialog').css({'margin-left': '20%'});
                    if (document.body.clientWidth > 1400) {
                        $('.modal-content').css({width: '140%'});
                    } else {
                        $('.modal-content').css({width: '118%'});
                    }
                } else {
                    $(".modal-body").each(function (index, dom) {
                        if (index == 0) {
                            $(dom).append("<div class='messContent' style=''><span class='glyphicon glyphicon-info-sign' style='font-size:30px; color:rgb(46, 76, 122);'></span></div>")
                            $(".modal-dialog .modal-content .modal-body span.glyphicon").each(function (index,
                                                                                                        dom) {
                                $(dom).css("top", "7px");
                            })
                            $('.modal-body .messContent span.glyphicon').append("<span style='margin-left:10px;vertical-align: 90%;font-size: 14px;color:#333'>"
                                                                                + message
                                                                                + "</span>")
                        }
                    })
                }
                //callback success
                $("#confirmBtn").on('click', function () {
                    $('#' + modelId).modal('hide').remove();
                    $('.modal-backdrop').remove();
                    if (typeof(callback) == "function") {
                        callback();
                    }
                })
                //callbackCancel
                $("#cancelBtn").on('click', function () {
                    $('#' + modelId).modal('hide')
                    if (typeof(callbackCancel) == "function") {
                        callbackCancel();
                    }
                })

                if (typeof(DialogShow) == "function") {
                    DialogShow();
                }

            })
            $('#' + modelId).on('shown.bs.modal', function () {
                $('.modal-backdrop.fade.in').attr('class', 'modal-backdrop fade');
            })

            $('#' + modelId).modal({backdrop: 'static', keyboard: false});
            $('#' + modelId).draggable({
                handle: ".modal-header"
            });
        }

        function _initShowDialog(content, $modelDialog, _modelId, _contextSelector) {
            var modelId = typeof (_modelId) == "undefined" ? 'defaultModel' : _modelId;
            var contextSelector = typeof (_contextSelector) == "undefined" ? 'body'
                : _contextSelector;   //default context is body
            var showDialog = _create(modelId);
            $(contextSelector).append(showDialog.html);
            $('#' + modelId).on('hidden.bs.modal', function () {
                if ($("#" + modelId).length > 0) {
                    $("#" + modelId).remove();
                }
            })
            $("#" + modelId + " .modal-footer").each(function (index, dom) {
                $(dom).css("border-top", "0px");
            })

            //show dialog body  setting
            $('#' + modelId).on('show.bs.modal', function () {
                $(".modal-dialog").each(function (index, dom) {
                    $(dom).css("margin-top", "10%");
                })

                $(".modal-body").each(function (index, dom) {
                    if (index == 0) {
                        if (typeof(content) != "undefined") {
                            $(dom).load(content.url,
                                        function (response, status) {
                                            if (status
                                                == "success") {
                                                if (typeof($modelDialog)
                                                    == "function") {
                                                    $modelDialog(response);
                                                }
                                            }
                                        });
                        }
                    }
                })
                /*//callback
                 $("#confirmBtn").on('click',function(){
                 $('#' + modelId).trigger('hidden.bs.modal');
                 $(".modal-backdrop.fade.in").remove();
                 if(typeof(callback) =="function")callback();
                 })*/
            })
            $('#' + modelId).modal({backdrop: 'static', keyboard: false});
        }

        return {
            create: _create,		//return dialog html
            initDialog: _initDialog,		//return dialog object
            initConfirmDialog: _initConfirmDialog,
            initShowDialog: _initShowDialog
        }
    }

})
