package com.chinasofti.ark.bdadp.expression.variable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by White on 2016/09/11.
 */
public class TimeVariable {

    /**
     * 获取当前amount周前（后）的日期
     */
    public String week(int amount, String pattern) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, amount);
        return new SimpleDateFormat(pattern).format(calendar.getTime());
    }

//	/**
//	 * 计算某一天日期
//	 * 
//	 * @param amount
//	 * @param pattern
//	 * @return
//	 */
//	public String day(int amount, String pattern) {
//
//		Calendar calendar = Calendar.getInstance();
//		calendar.add(Calendar.DAY_OF_YEAR, amount);
//		return new SimpleDateFormat(pattern).format(calendar.getTime());
//	}

//	/**
//	 * 计算某一周的周一
//	 * 
//	 * @param amount
//	 *            零为当前日期所在周的周一，
//	 * @param pattern
//	 * @return
//	 */
//	public String getWeekMonday(int amount, String pattern) {
//
//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.DATE, amount * 7);
//		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//		return new SimpleDateFormat(pattern).format(cal.getTime());
//	}

    /**
     * 计算某月的月初
     */
    public String getMonthFirst(int amount, String pattern) {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, amount);
        cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天

        return new SimpleDateFormat(pattern).format(cal.getTime());
    }

}
