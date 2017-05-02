package com.chinasofti.ark.bdadp.expression.support;

import com.chinasofti.ark.bdadp.expression.ArkExpressionParser;
import com.chinasofti.ark.bdadp.expression.converter.ObjectToStringConverter;
import com.chinasofti.ark.bdadp.expression.variable.*;
import org.springframework.core.convert.support.DefaultConversionService;

/**
 * Created by White on 2016/09/11.
 */
public class ArkConversionUtil extends DefaultConversionService {

    public ArkConversionUtil() {
        super();

        addArkConverters();
    }

    public static void main(String[] args) {

        ArkConversionUtil ser = new ArkConversionUtil();

        System.out.println(ser.parseVariableByDefined("---, #{#da.add(-1)}"));
        System.out.println(ser.parseVariableByDefined("---, #{#day.add(-1)}"));
        System.out.println(ser.parseVariableByDefined("===, #{#da}"));
        System.out.println(ser.parseVariableByDefined("===, #{#day}"));
        System.out.println(ser.parseVariableByDefined("JDBC——#{#someWeekMonday }.sql"));
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

    }

    public void addArkConverters() {
        this.addConverter(SomeDayVariable.class, String.class, new ObjectToStringConverter());
        this.addConverter(SomeMonthEndVariable.class, String.class, new ObjectToStringConverter());
        this.addConverter(SomeMonthStartVariable.class, String.class, new ObjectToStringConverter());
        this.addConverter(SomeWeekMonDayVariable.class, String.class, new ObjectToStringConverter());
        this.addConverter(SomeWeekSunDayVariable.class, String.class, new ObjectToStringConverter());
    }

    public String parseVariableByDefined(String inputVariable) {
        if (null == inputVariable || "".endsWith(inputVariable)) {
            return "";
        } else {
            ArkExpressionParser parser = new ArkExpressionParser();
            String result;
            try {
                result = parser.getValue(inputVariable).toString();
            } catch (Exception e) {
                result = inputVariable;
            }
            if (inputVariable.contains(result)) {
                result = inputVariable;
            }
            return result;
        }

    }

}
