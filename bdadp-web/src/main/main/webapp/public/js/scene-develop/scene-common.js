/**
 * Created by labo on 2016/8/31.
 */

define([], function () {
  return {
    floatFun: {
      sceneFloatShow: function (that, thatClass) {
        if (thatClass) {
          for (var i = 0; i < thatClass.length; i++) {
            $(that).find("." + thatClass[i]).css("display", "block");
          }
        }
      },
      sceneFloatHide: function (that, thatClass) {
        if (thatClass) {
          for (var i = 0; i < thatClass.length; i++) {
            $(that).find("." + thatClass[i]).css("display", "none");
          }
        }
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
	return (myYear + split + myMonth + split + myDay + " " + myHour.substr(myHour.length - 2, 2)
		+ ":" + myMinute.substr(myMinute.length - 2, 2) + ":"
		+ mySecond.substr(mySecond.length - 2, 2))
      } else {
	return (myYear + "-" + myMonth + "-" + myDay + " " + myHour.substr(myHour.length - 2, 2)
		+ ":" + myMinute.substr(myMinute.length - 2, 2) + ":"
		+ mySecond.substr(mySecond.length - 2, 2))
      }

    },
    transFromDateToMilli: function (date) {
      var time = Date.parse(new Date(date));													//var stringTime = "2014-07-10 10:21:12";
      time = time / 1000;
      return time;
    },
    setLayoutLeft: function (eleArr, otherHeight) {
      var bodyHeight = window.innerHeight;
      var scrollBar = (bodyHeight - otherHeight) + "px";
      for (var i = 0; i < eleArr.length; i++) {
	$(eleArr[i]).css("overflow-y", "auto");
	$(eleArr[i]).css("height", scrollBar);
      }
    },
    getStrLength: function (str, len) {
      var strLen = str.length;
      var sceneName = "";
      if (strLen > len) {
	sceneName = str.substring(0, len) + '...';
      } else {
	sceneName = str;
      }
      return sceneName;
    },
    validate: function (id, callback, callback1) {
      var bootstrapValidator = $("#" + id).data('bootstrapValidator');
      bootstrapValidator.validate();
      var loginValid = bootstrapValidator.isValid();
      if (loginValid) {
	callback();
	return true;
      } else {
	callback1();
	return false;
      }
    }
    }
})


