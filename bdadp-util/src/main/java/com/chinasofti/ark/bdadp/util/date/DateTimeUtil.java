package com.chinasofti.ark.bdadp.util.date;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {

    public static String format(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat();
        return formatter.format(date);
    }

    public static String format(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static String formatDateTime(long secondsCount) {
        String DateTimes = null;
        long days = secondsCount / (60 * 60 * 24);
        long hours = (secondsCount % (60 * 60 * 24)) / (60 * 60);
        long minutes = (secondsCount % (60 * 60)) / 60;
        long seconds = secondsCount % 60;
        if (days > 0) {
            DateTimes = days + "d" + hours + "h" + minutes + "m" + seconds + "s";
        } else if (hours > 0) {
            DateTimes = hours + "h" + minutes + "m" + seconds + "s";
        } else if (minutes > 0) {
            DateTimes = minutes + "m" + seconds + "s";
        } else if (seconds > 0) {
            DateTimes = seconds + "s";
        } else {
            DateTimes = "<1s";
        }

        return DateTimes;
    }

}
