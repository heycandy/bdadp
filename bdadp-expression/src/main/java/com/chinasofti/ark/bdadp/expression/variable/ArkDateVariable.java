package com.chinasofti.ark.bdadp.expression.variable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by White on 2016/09/11.
 */
public class ArkDateVariable implements ArkVariable {

    private final String name = "date";

    @Override
    public String getName() {
        return this.name;
    }

    public String day(int amount, String pattern) {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, amount);

        return new SimpleDateFormat(pattern).format(calendar.getTime());
    }

    public String day(int amount) {
        return day(amount, "yyyyMMdd");
    }

    public String day() {
        return day(0);
    }

    @Override
    public String toString() {
        return day();
    }
}
