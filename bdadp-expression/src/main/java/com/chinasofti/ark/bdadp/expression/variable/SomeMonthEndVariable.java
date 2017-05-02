package com.chinasofti.ark.bdadp.expression.variable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @Author : water
 * @Date : 2016年9月23日
 * @Desc : 获取某月的月末（默认为本月）
 * @version: V1.0
 */
public class SomeMonthEndVariable implements ArkVariable {

    private final String name = "someMonthEnd";

    @Override
    public String getName() {
        return this.name;
    }

    public String add(int amount, String pattern) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, amount + 1);
        cal.set(Calendar.DAY_OF_MONTH, 0);// 设置为1号,当前日期既为本月第一天

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
