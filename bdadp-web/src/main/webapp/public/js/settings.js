/**
 * the setting of global arguments or function by 'settings'
 * @type {{}}
 */
var settings = (function () {

    return {
        globalVariableUrl: function () {
            var strFullPath = window.document.location.href;
            var strPath = window.document.location.pathname;
            var pos = strFullPath.indexOf(strPath);
            var path = strFullPath.substring(0, pos);
            var href = window.location.href;
            if(href.indexOf("/public") !== -1) {
                var pos = href.indexOf("/public");
                return href.substring(0,pos);
            }
            if(strPath.indexOf('.html')>-1){
                strPath = strPath.substr(0,strPath.lastIndexOf('/'))
            }
            return path + strPath;

        },
        testLangType: function (lang) {
            if (!lang || lang.length < 2) {
                lang = (navigator.languages) ? navigator.languages[0]
                    : (navigator.language || navigator.userLanguage /* IE */ || 'en');
            }
            lang = lang.toLowerCase();
            lang = lang.replace(/-/, "_"); // some browsers report language as en-US instead of
                                           // en_US
            if (lang.length > 3) {
                lang = lang.substring(0, 3) + lang.substring(3).toUpperCase();
            }
            return lang;
        },
        formatSeconds: function (value) {
            var theTime = parseInt(value);// 秒
            var theTime1 = 0;// 分
            var theTime2 = 0;// 小时
            if (theTime > 60) {
                theTime1 = parseInt(theTime / 60);
                theTime = parseInt(theTime % 60);
                if (theTime1 > 60) {
                    theTime2 = parseInt(theTime1 / 60);
                    theTime1 = parseInt(theTime1 % 60);
                }
            }
            var result = "" + parseInt(theTime) + "s";
            if (parseInt(theTime1) > 0) {
                result = "" + parseInt(theTime1) + "m" + result;
            }
            if (parseInt(theTime2) > 0) {
                result = "" + parseInt(theTime2) + "h" + result;
            }
            return result;
        },
        interFun: function (path) {
            var isExist = true;
            InterI18n.HuaweiI18nLoad(path);
            InterI18n.HuaweiI18n(InterI18n.HuaweigetAllI18nKeys(), path);
        },
        currentSceneTab: {
            editScenes: [],
            exist: function (sceneId) {
                var flag = false;
                for (var i = 0; i < this.editScenes.length; i++) {
                    if (sceneId == this.editScenes[i]['sceneId']) {
                        flag = true;
                        break;
                    }
                }
                return flag;
            },
            set: function (sceneId, sceneName) {
                var flag = false;
                for (var i = 0; i < this.editScenes.length; i++) {
                    if (this.editScenes[i]['sceneId'] == sceneId) {
                        flag = true;
                    }
                }
                if (!flag) {
                    this.editScenes.push({'sceneId': sceneId, 'sceneName': sceneName})
                }
            }
        },
        HttpClient: function (method, inputData, url, callbackTrue, callbackFalse) {
            var url = this.globalVariableUrl() + url;
            var ARK_PLATFORM_TOKEN = sessionStorage.ARK_PLATFORM_TOKEN
                ? sessionStorage.ARK_PLATFORM_TOKEN : "";
            var response = null;
            $.ajax({
                async: true,
                type: method,
                url: url,
                data: (typeof (inputData) != "undefined" || inputData != null) ? JSON
                    .stringify(inputData) : "",
                dataType: "json",
                contentType: "application/json; charset=UTF-8",
                headers: {
                    "ARK_PLATFORM_TOKEN": ARK_PLATFORM_TOKEN,
                    "Access-Control-Allow-Origin": "*",
                    "Access-Control-Allow-Methods": "POST, GET, PUT, OPTIONS, DELETE",
                    "Access-Control-Max-Age": "3600",
                    "Access-Control-Allow-Headers": " Origin, X-Requested-With, Content-Type, Accept",
                    "If-Modified-Since":"0"
                },
                success: function (data) {
                    if (!data["resultCode"]) {
                        response = data["result"];
                        if (callbackTrue instanceof Function) {
                            callbackTrue(response);
                        }
                    } else if (callbackFalse instanceof Function) {
                        response = data["resultMessage"];
                        callbackFalse(response);
                    }
                },
                error: function (xhr, msg, e) {
                    var errorMsg = "Error requesting " + ": "
                                   + xhr.statusText + " " + xhr.statusText
                                   + ", errors: " + msg;
                    throw new Error(errorMsg);
                }
            });
            //return response;
        },
        //增删class，判断是否存在该class
        containClass: function (ele, thisClass) {
            var checkClass = $(ele).attr("class");
            if (checkClass.indexOf(thisClass) >= 0) {
                return true;
            }
            return false;
        },
        InterNation: function (selectorArr, eleArr) {
            var len = selectorArr.length;
            for (var i = 0; i < len; i++) {
                $(selectorArr[i]).text($.i18n.prop(eleArr[i]));
            }
        },
        transformMilliseconds: function (milliseconds, split) {
            var myDate = new Date(parseInt(milliseconds));
            var myYear = myDate.getFullYear(); //获取完整的年份(4位,1970-????)
            var myMonth = myDate.getMonth(); //获取当前月份(0-11,0代表1月)
            myMonth = myMonth + 1;
            var myDay = myDate.getDate(); //获取当前日(1-31)
            var myHour = "0" + myDate.getHours(); //获取当前小时数(0-23)
            var myMinute = "0" + myDate.getMinutes(); //获取当前分钟数(0-59)
            var mySecond = "0" + myDate.getSeconds(); //获取当前秒数(0-59)

            if (split) {
                return (myYear + split + myMonth + split + myDay + " " + myHour.substr(myHour.length
                                                                                       - 2, 2) + ":"
                        + myMinute.substr(myMinute.length - 2, 2) + ":"
                        + mySecond.substr(mySecond.length - 2, 2))
            } else {
                return (myYear + "-" + myMonth + "-" + myDay + " " + myHour.substr(myHour.length
                                                                                   - 2, 2) + ":"
                        + myMinute.substr(myMinute.length - 2, 2) + ":"
                        + mySecond.substr(mySecond.length - 2, 2))
            }

        },
        roleManage: function (adminCallback, operCallback, devCallback) {
            var roleName = sessionStorage.roleName;
            switch (roleName) {
                case "admin":
                    adminCallback();
                    break;
                case "oper":
                    operCallback();
                    break;
                case "dev":
                    devCallback();
                    break;

            }
        },
        socket: (function () {
            function singleton() {
                return io("http://" + location.hostname + ':8081');
            }

            var instance;
            var _static = {
                getInstance: function () {
                    if (instance === undefined) {
                        instance = new singleton();
                    }
                    return instance;
                }
            };
            return _static;
        })()
    }
})();
