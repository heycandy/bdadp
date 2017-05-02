package com.chinasofti.ark.bdadp.expression.variable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @Author : water
 * @Date : 2016年9月23日
 * @Desc : 获取某月的月初（默认为本月）
 * @version: V1.0
 */
public class SomeMonthStartVariable implements ArkVariable {

    private final String name = "someMonthStart";

    @Override
    public String getName() {
        return this.name;
    }

    public String add(int amount, String pattern) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, amount);
        cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天

        return new SimpleDateFormat(pattern).format(cal.getTime());
    }

    public String add(int amount) {
        return add(amount, "yyyyMMdd");
    }

    public String add() {
        return add(0);
    }

    @Override
    public String toString() {
        return add();
    }
}
