package com.chinasofti.ark.bdadp.expression.variable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @Author : water
 * @Date : 2016年9月23日
 * @Desc : 获取某周的周一（默认为本周）
 * @version: V1.0
 */
public class SomeWeekMonDayVariable implements ArkVariable {

    private final String name = "someweekMonday";

    @Override
    public String getName() {
        return this.name;
    }

    public String add(int amount, String pattern) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.add(Calendar.DATE, amount * 7);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
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
