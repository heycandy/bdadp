/**
 * Created by Hu on 2016/9/27.
 */
define([], function () {
    'use strict';
    return function () {

        function Validate(formId, fieldName, validType) {
            this.formId = formId;
            this.fieldName = fieldName;
            this.validType = validType;

            $('#' + this.formId).bootstrapValidator({
                message: 'This value is not valid',
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                fields: {}
            })
        }

        Validate.prototype.add = function () {
            switch (this.validType) {
                case 'nullable':     // no Empty
                    // Add new field
                    $('#' + this.formId).bootstrapValidator('addField', this.fieldName, {
                        message: '输入值无效！',
                        validators: {
                            notEmpty: {
                                message: '不允许为空！'
                            }
                        }
                    });
                    break;
                default:
                    break;
            }

        }

        return {
            create: _create		//return dialog html
        }
    }

})
