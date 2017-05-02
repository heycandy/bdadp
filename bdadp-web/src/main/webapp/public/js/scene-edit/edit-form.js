/**
 * Created by Hu on 2016/9/2.
 */
define(["js/scene-edit/edit-dialog", 'js/settings', 'libs/i18n/international',
        'libs/i18n/jquery.i18n.properties'], function () {
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

        /**
         * formId{componentName + key}
         * @param formId
         */
        function _form(formId) {
            var _formHtml = '<form id="' + formId
                            + '" class="form-horizontal" role="form"><fieldset> </fieldset></form>';
            return _formHtml;
        }

        function _text(param_id, param_name, param_desc) {
            this._html = new Html();
            this._html.append('<div class="form-group-sm" style="margin-top: 10px;">');
            this._html.append('<label for="' + param_id
                              + '" style=" width: 80px;text-align: center;">' + param_desc
                              + '</label>');
            this._html.append('<input title="' + $.i18n.prop('v_input_title')
                              + '" required="required"  type="text" data-name="' + param_name
                              + '" class="form-control input-sm" ' +
                              'name="' + param_id + '" id="' + param_id
                              + '" placeholder="" style="display: inline;margin-left: 10px;width: 60%;" value="">');
            this._html.append('</div>');
            return this._html;
        }

        function _textarea(param_id, param_name, param_desc) {
            this._html = new Html();
            this._html.append('<div class="form-group-sm" style="margin-top: 10px;">');
            this._html.append('<label for="' + param_id
                              + '" style=" width: 80px;text-align: center;">' + param_desc
                              + '</label>');
            this._html.append('<textarea title="" class="form-control form-textarea-p" data-name="'
                              + param_name + '" class="form-control input-sm" rows="10" ' +
                              'name="' + param_id + '" id="' + param_id
                              + '" value="" style="margin-left: 88px;" ></textarea>');
            this._html.append('</div>');
            return this._html;
        }

        function _number(param_id, param_name, param_desc) {
            this._html = new Html();
            this._html.append('<div class="form-group-sm" style="margin-top: 10px;">');
            this._html.append('<label for="' + param_id
                              + '" style=" width: 80px;text-align: center;">' + param_desc
                              + '</label>');
            this._html.append('<input title="" type="number" data-name="' + param_name
                              + '" class="form-control input-sm" ' +
                              'name="' + param_id + '" id="' + param_id
                              + '" placeholder="" style="display: inline;margin-left: 10px;width: 60%;" value="">');
            this._html.append('</div>');
            return this._html;
        }

        function _password(param_id, param_name, param_desc) {
            this._html = new Html();
            this._html.append('<div class="form-group-sm" style="margin-top: 10px;">');
            this._html.append('<label for="' + param_id
                              + '" style=" width: 80px;text-align: center;">' + param_desc
                              + '</label>');
            this._html.append('<input type="password" data-name="' + param_name
                              + '" class="form-control input-sm" ' +
                              'name="' + param_id + '" id="' + param_id
                              + '" placeholder="" style="display: inline;margin-left: 10px;width: 60%;" value="">');
            this._html.append('</div>');
            return this._html;
        }

        function _date(param_id, param_name, param_desc) {
            this._html = new Html();
            this._html.append('<div class="form-group-sm" style="margin-top: 10px;">');
            this._html.append('<label for="' + param_id
                              + '" style=" width: 80px;text-align: center;">' + param_desc
                              + '</label>');
            this._html.append('<input type="date" data-name="' + param_name
                              + '" class="form-control input-sm" ' +
                              ' name="' + param_id + ' " id="' + param_id
                              + '" placeholder="" style="display: inline;margin-left: 10px;width: 60%;" value="">');
            this._html.append('</div>');
            return this._html;
        }

        /**
         * @param param_id
         * @param param_name
         * @param param_desc
         * @param default_options  Array type
         * @private
         */
        function _options(param_id, param_name, param_desc, default_options) {
            this._html = new Html();
            this._html.append('<div class="form-group-sm" style="margin-top: 10px;">');
            this._html.append('<label  for="' + param_id
                              + '" style=" width: 80px;text-align: center;">' + param_desc
                              + '</label>');
            var $select = $('<select id="' + param_id + '" name="' + param_id + '" data-name="'
                            + param_name
                            + '" class="form-control input-sm" style="display: inline;margin-left: 10px;width: 60%;" value=""></select>');

            if (typeof(default_options) == 'string') {
                var arr = default_options.split(',');
                var _newArr = arr.map(function (opt, index) {
                    var _opt = opt.split('=');
                    return {'option_desc': _opt[0], 'option_value': _opt[1]}
                })
                if (_newArr instanceof Array) {
                    for (var i = 0; i < _newArr.length; i++) {
                        var _option = '<option value="' + _newArr[i].option_value + '">'
                                      + _newArr[i].option_desc + '</option>';
                        $select.append(_option);
                    }
                }
            }
            this._html.append($select.get(0).outerHTML);
            this._html.append('</div>');
            return this._html;
        }

        function _fileupload(param_id, param_name, param_desc) {
            this._html = new Html();
            this._html.append('<div class="form-group-sm" style="margin-top: 10px;">');
            this._html.append('<label class="sr-only" for="' + param_id
                              + '" style=" width: 80px;text-align: center;">upload</label>');
            this._html.append('<input type="file" data-name="' + param_name
                              + '" class="form-control input-sm" name="' + param_id + '" id="'
                              + param_id
                              + '" style="display: inline;margin-left: 10px;width: 60%;" value="">');
            this._html.append('</div>');
            return this._html;
        }

        function _radio(param_id, param_name, param_desc, default_options) {
            this._html = new Html();
            this._html.append('<div class="form-group-sm" style="margin-top: 10px;">');
            this._html.append('<label  for="' + param_id
                              + '" style=" width: 80px;text-align: center;">' + param_desc
                              + '</label>');
            var $controls = $('<div id="' + param_id
                              + '" class="controls" style="display: inline;margin-left: 12px"></div>');

            if (typeof(default_options) == 'string') {
                var _radios = default_options.split(',');
                for (var i = 0; i < _radios.length; i++) {
                    var _radio = '<label class="radio-inline" style="margin-top: -8px;"><input type="radio" checked=""  data-name="'
                                 + param_name + '"' +
                                 ' name="group" value="' + _radios[i].split('=')[1] + '" >'
                                 + _radios[i].split('=')[0] + '</label>';
                    $controls.append(_radio);
                }
            }
            this._html.append($controls.get(0).outerHTML);
            this._html.append('</div>');
            return this._html;
        }

        function _expression(param_id, param_name, param_desc,type) {
            this._html = new Html();
            this._html.append('<div class="form-group-sm" style="margin-top: 10px;">');
            this._html.append('<label for="' + param_id
                + '" style=" width: 80px;text-align: center;">' + param_desc
                + '</label>');
            var $expression = $('<textarea title="" class="form-control" expr-type="'
                + type + '"data-name="'+ param_name + '" class="form-control input-sm" rows="5" ' +
                'name="' + param_id + '" id="' + param_id
                + '" value="" style="width: 90%;margin-left: 6%;margin-top: 0px;" ></textarea>');
            this._html.append($expression.get(0).outerHTML);
            this._html.append('</div>');
            return this._html;
        }

        function _keyvalue(param_id, param_name, param_desc) {
            this._html = new Html();
            this._html.append('<div class="form-group-sm" style="margin-top: 10px;">');
            this._html.append('<div id="' + param_id + '" class="controls controls-row">');
            this._html.append('<label for="" style=" width: 80px;text-align: center;">' + param_desc
                              + '</label>');
            this._html.append('<input type="text" data-name="' + param_name
                              + '" class="form-control input-sm" name="' + param_id
                              + '-key" placeholder="key" style="margin-top: 5px;display: inline;margin-left: 10px;width: 29%;" value="">');
            this._html.append('<input type="text" data-name="' + param_name
                              + '" class="form-control input-sm" name="' + param_id
                              + '-value"  placeholder="value" style="margin-top: 5px;display: inline;margin-left: 10px;width: 29%;" value="">');
            this._html.append('</div>');
            this._html.append('</div>');
            return this._html;
        }

        /**
         * traverse each item of form data to render ui
         * @param $tab
         * @param result
         * @private
         */
        function _dataHandler($form, result) {
            if (result instanceof Array) {
                for (var i = 0; i < result.length; i++) {
                    var type = result[i].param_type
                        , param_desc = result[i].param_desc
                        , param_id = result[i].param_id
                        , param_name = result[i].param_name
                        , default_options = result[i].default_options;
                    switch (type) {
                        case "text":
                        {
                            var text = new _text(param_id, param_name, param_desc);
                            $form.append(text.toString());
                            break;
                        }
                        case "textarea":
                        {
                            $form.append(new _textarea(param_id, param_name,
                                                       param_desc).toString());
                            break;
                        }
                        case "number":
                        {
                            $form.append(new _number(param_id, param_name, param_desc).toString());
                            break;
                        }
                        case "password":
                        {
                            $form.append(new _password(param_id, param_name,
                                                       param_desc).toString());
                            break;
                        }
                        case "date":
                        {
                            $form.append(new _date(param_id, param_name, param_desc).toString());
                            break;
                        }
                        case "options":
                        {
                            $form.append(new _options(param_id, param_name, param_desc,
                                                      default_options).toString());
                            break;
                        }
                        case "fileupload":
                        {
                            $form.append(new _fileupload(param_id, param_name,
                                                         param_desc).toString());
                            break;
                        }
                        case "keyvalue":
                        {
                            $form.append(new _keyvalue(param_id, param_name,
                                                       param_desc).toString());
                            break;
                        }
                        case "radio":
                        {
                            $form.append(new _radio(param_id, param_name, param_desc,
                                                    default_options).toString());
                            break;
                        }
                        case "expr":
                        {
                            $form.append(new _expression(param_id, param_name, param_desc,
                                type).toString());
                            break;
                        }
                        default :
                        {
                            break;
                            ;
                        }
                    }
                }
                return $form;
            }
        }

        /**
         * formId{sceneId+key+form}
         * @param formId
         * @param descTabId
         * @returns {Html|*}
         */
        function initTabs(tabPanelClass, formId, attriTabId, descTabId, result, nodeText) {
            this._html = new Html();
            this._html.append('<div class="tabPanel ' + tabPanelClass + '">');
            this._html.append('<ul class="nav nav-tabs">');
            this._html.append('<li class="active">');
            this._html.append('<a href="#' + attriTabId
                              + '" data-toggle="tab"><span style="color: #141a1d; font-size: 13px;">'
                              + $.i18n.prop("d_edit_attribute") + '</span></a></li>');
            this._html.append('<li><a href="#' + descTabId
                              + '" data-toggle="tab"><span style="color: #141a1d;font-size: 13px;">'
                              + $.i18n.prop("d_edit_describe") + '</span></a></li>');
            this._html.append('</ul>');
            //********tab content*************
            this._html.append('<div class="tab-content">');

            this._html.append('<div class="tab-pane fade in active" id="' + attriTabId + '">');			//add
                                                                                                  // attribute
                                                                                                  // form
            var $tab_form = $('<form id="' + formId
                              + '" class="form-horizontal" style="font-size: 12px;padding:0px 5px 30px 5px;" role="form"></form>');
            this._html.append(_dataHandler($tab_form, result).get(0).outerHTML);  //add each item
                                                                                  // of form
            this._html.append('</div>');

            this._html.append('<div class="tab-pane fade" id="' + descTabId + '">');
            var $tabDesc_form = $('<form id="tabDesc_' + formId
                                  + '" class="form-horizontal" style="font-size: 12px;padding:0px 5px 30px 5px;" role="form">'
                                  +
                                  '<div class="form-group-sm" style="margin-top: 10px;"><label id="'
                                  + descTabId + '_label" name="' + descTabId
                                  + '_label" value="" style="display: block;">' + nodeText
                                  + '</label><textarea id="' + descTabId + '_textarea" name="'
                                  + descTabId
                                  + '_textarea" rows="10" value="" style="width: 100%;;"></textarea></div></form>');
            this._html.append($tabDesc_form.get(0).outerHTML);
            this._html.append('</div>');
            this._html.append('</div>');
            this._html.append('</div>');
            return this._html;
        }

        function _isChange(formId) {

            $('#' + formId + ' :input').on('input', function () {
                $('#' + formId).data('changed', true);
            })
            $('#' + formId + ' textarea').on('input propertychange', function () {
                $('#' + formId).data('changed', true);
            })
            $('#' + formId + ' select').change(function () {
                $('#' + formId).data('changed', true);
            })

            $('#tabDesc_' + formId + ' textarea').bind('input propertychange', function () {
                $('#tabDesc_' + formId).data('changed', true);
            })
        }

        function _create(tabPanelClass, formId, attriTabId, descTabId, result, nodeText) {

            return new initTabs(tabPanelClass, formId, attriTabId, descTabId, result, nodeText);
        }

        return {
            create: _create,		//return dialog html
            isChange: _isChange
        }
    }

})
