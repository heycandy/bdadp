package com.chinasofti.ark.bdadp.expression.variable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by White on 2016/09/11.
 */
public class SomeDayVariable implements ArkVariable {

    private final String name = "someday";

    @Override
    public String getName() {
        return this.name;
    }

    public String add(int amount, String pattern) {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, amount);

        return new SimpleDateFormat(pattern).format(calendar.getTime());
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

