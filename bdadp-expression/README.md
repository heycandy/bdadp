

## spring el系统变量 ##

 **一. 系统已经设置的变量**
-  day
- someMonthEnd
- someMonthStart
- someWeekMonday
- someWeekSunDay

 **二. 调用方法**
  
    
```
ArkConversionUtil ser = new ArkConversionUtil();

		System.out.println(ser.parseVariableByDefined(""));
		System.out.println(ser.parseVariableByDefined(null));
		System.out.println(ser.parseVariableByDefined("22"));
		System.out.println(ser.parseVariableByDefined("昨天是, #{#day.add(-1)}  + + +  #{#day.add(1)}"));

		System.out.println(ser.parseVariableByDefined("今天是 ，#{#day} "));
		System.out.println(ser.parseVariableByDefined("明天是, #{#day.add(1)}"));
		System.out.println(ser.parseVariableByDefined("昨天是, #{#day.add(-1)  } "));
		//
		System.err.println("================================");
		//
		System.out.println(ser.parseVariableByDefined("本月底是, #{#someMonthEnd }"));
		System.out.println(ser.parseVariableByDefined("下月底是, #{#someMonthEnd.add(1 ) } "));
		System.out.println(ser.parseVariableByDefined("上月底是, #{#someMonthEnd.add(- 1 )  } "));

		System.err.println("================================");

		System.out.println(ser.parseVariableByDefined("本月初, #{#someMonthStart }"));
		System.out.println(ser.parseVariableByDefined("下月初, #{#someMonthStart.add(1) } "));
		System.out.println(ser.parseVariableByDefined("上月初, #{#someMonthStart.add(-1) } "));

		System.err.println("================================");

		System.out.println(ser.parseVariableByDefined("本周一, #{#someWeekMonday }"));
		System.out.println(ser.parseVariableByDefined("下周一, #{#someWeekMonday.add(+1) } "));
		System.out.println(ser.parseVariableByDefined("上周一, #{#someWeekMonday.add( -1)  } "));

		System.err.println("================================");

		System.out.println(ser.parseVariableByDefined("本周日, #{#someWeekSunDay }"));
		System.out.println(ser.parseVariableByDefined("下周日, #{#someWeekSunDay.add(1 ) } "));
		System.out.println(ser.parseVariableByDefined("上周日, #{#someWeekSunDay.add(  - 1)  } "));
```


 **三. rest API调用方法（bdadp-controller层 ）**
 
####  1.创建自定义变量
 
   `url`：bdadp-web/service/v1/scenario/variable
   `method`: POST   
   `desc`: 创建一个用户自己的变量   
    `request body`: 
    
```
{
	"variableName":"tomorrow",
	"variableDesc":"tomorrow",
	"variableExpr":"#someday+1",
	"scenarioId":"tomorrow"
}
```
`response body`: 

```
{"resultCode":0,"resultMessage":null,"result":{"createUser":null,"createdTime":"2016-09-26 09:57:30","variableId":"c5f073ef7ce64d74927bcb2aa7a16e92","variableName":"tomorrow","variableDesc":"tomorrow","variableExpr":"#someday+1","modifiedUser":null,"modifiedTime":"2016-09-26 09:57:30","scenarioId":"tomorrow"}}
```


---
####  2. 根据输入变量解析

  `url`：bdadp-web/service/v1/scenario/variables
   `method`: get   
   `desc`: 根据输入变量解析   
    `request param`: 
    
```
inputVariable (String):
scenarioId （String）
```
`response body`: 

```
{"resultCode":0,"resultMessage":null,"result":"20160927"}
```    
      

 
 