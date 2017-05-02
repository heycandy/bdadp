/**
 * the setting of global arguments or function by 'settings'
 * @type {{}}
 */
var InterI18n = (function () {
    function I18nConstants() {
    }

    I18nConstants.I18nType = {
        HTML: 0,
        TEXT: 1,
        VALUE: 2
    }
    I18nConstants.PREFIX = "I_";
    I18nConstants.SUFFIX = "-";

    function HuaweiShow() {
    }

    HuaweiShow.i18nLoad = function (propPath) {
        if (!propPath) {
            propPath = './I18n/';
        }
        /*prop = typeof (propPath) != "undefined"? propPath
         : '../resources/i18n/';*/
        $.i18n.properties({
            name: 'msg',
            path: propPath,
            mode: 'both',
            cache: true,
            checkAvailableLanguages: true
        });
    }
    HuaweiShow.i18n = function (keys, propPath, type) {
        if (!propPath) {
            propPath = './I18n/';
        }
        var itype = typeof (type) != "undefined" ? type
            : I18nConstants.I18nType.HTML;
        $.i18n.properties({
            name: 'msg',
            path: propPath,
            mode: 'both',
            checkAvailableLanguages: true,
            callback: function () {
                for (index in keys) {
                    if (!isNaN(parseFloat(index))) {
                        var id = keys[index];
                        var i18nKey = "";
                        if (typeof id == "string" && id.indexOf(I18nConstants.SUFFIX) != -1) // handle the
                        // suffix for
                        // table data
                            i18nKey = id.substring(I18nConstants.PREFIX.length, id
                                .indexOf(I18nConstants.SUFFIX));
                        else if (typeof id == "string") {
                            i18nKey = id.substring(I18nConstants.PREFIX.length);
                        }
                        if (itype == I18nConstants.I18nType.TEXT) {
                            $("#" + id).text($.i18n.prop(i18nKey));
                        } else if (itype == I18nConstants.I18nType.VALUE) {
                            $("#" + id).val($.i18n.prop(i18nKey));
                        } else if (itype == I18nConstants.I18nType.HTML) {
                            $("#" + id).html($.i18n.prop(i18nKey));
                        }
                    }
                }
            }
        });
    }

    HuaweiShow.getAllI18nKeys = function (parentId) {
        var keys = new Array();
        if (typeof (parentId) != "undefined") {
            $("#" + parentId + " [id^='" + I18nConstants.PREFIX + "']").each(
                function () {
                    keys.push(this.id);
                })
            return keys;
        }
        $("[id^='" + I18nConstants.PREFIX + "']").each(function () {
            keys.push(this.id);
        })
        return keys;
    }

    return {
        HuaweiI18nLoad: HuaweiShow.i18nLoad,
        HuaweiI18n: HuaweiShow.i18n,
        HuaweigetAllI18nKeys: HuaweiShow.getAllI18nKeys
    }

})();
