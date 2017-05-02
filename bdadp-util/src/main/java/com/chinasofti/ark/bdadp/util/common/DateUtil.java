package com.chinasofti.ark.bdadp.util.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Date Utility Class This is used to convert Strings to Dates and Timestamps
 */
public class DateUtil {

    // ~ Static fields/initializers
    // =============================================
    private static Logger log = LoggerFactory.getLogger(DateUtil.class);
    private static String defaultDatePattern = null;
    private static String timePattern = "HH:mm";
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat sdf4 = new SimpleDateFormat("yyyyMMdd");
    private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
    private static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat fullSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private int dayOfweek;

    // ~ Methods
    // ================================================================

    /**
     * 获得服务器当前日期，以格式为：yyyy-MM-dd的日期字符串形式返回
     */
    public static String getDate() {
        try {
            return sdf.format(Calendar.getInstance().getTime());
        } catch (Exception e) {
            log.debug("DateUtil.getDate():" + e.getMessage());
            return "";
        }
    }

    /**
     * 获得服务器当前时分秒，以格式为：HHmmss的时间字符串形式返回
     */
    public static String getTime() {
        Calendar cale = Calendar.getInstance();
        try {
            return sdf3.format(cale.getTime());
        } catch (Exception e) {
            log.debug("DateUtil.getTime():" + e.getMessage());
            return "";
        }
    }

    /**
     * 获得服务器当前日期，以格式为：yyyyMMdd的日期字符串形式返回
     */
    public static String getDateAnother() {
        try {
            return sdf4.format(Calendar.getInstance().getTime());
        } catch (Exception e) {
            log.debug("DateUtil.getDate():" + e.getMessage());
            return "";
        }
    }

    /**
     * 获得服务器当前日期及时间，以格式为：yyyyMMddHHmmss的日期字符串形式返回
     */
    public static String getDateTime() {
        try {
            return sdf2.format(Calendar.getInstance().getTime());
        } catch (Exception e) {
            log.debug("DateUtil.getDateTime():" + e.getMessage());
            return "";
        }
    }

    /**
     * 获得服务器当前日期及时间，以格式为：yyyy-MM-dd HH:mm:ss的日期字符串形式返回
     */
    public static String getDateTimeAnother() {
        try {
            return sdf3.format(Calendar.getInstance().getTime());
        } catch (Exception e) {
            log.debug("DateUtil.getDateTime():" + e.getMessage());
            return "";
        }
    }

    /**
     * 返回日期加X天后的日期
     */
    public static String addDay(String date, int i) {
        try {
            GregorianCalendar gCal = new GregorianCalendar(Integer.parseInt(date.substring(0, 4)),
                    Integer.parseInt(date.substring(5, 7)) - 1,
                    Integer.parseInt(date.substring(8, 10)));
            gCal.add(GregorianCalendar.DATE, i);
            return sdf.format(gCal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 比较两个日期相差的天数
     */
    public static int getMargin(String date1, String date2) {
        int margin;
        try {
            ParsePosition pos = new ParsePosition(0);
            ParsePosition pos1 = new ParsePosition(0);
            Date dt1 = sdf.parse(date1, pos);
            Date dt2 = sdf.parse(date2, pos1);
            long l = dt1.getTime() - dt2.getTime();
            margin = (int) (l / (24 * 60 * 60 * 1000));
            return margin;
        } catch (Exception e) {
            log.debug("DateUtil.getMargin():" + e.toString());
            return 0;
        }
    }

    /**
     * 获取amount天前或后的时间
     */
    public static Date getSomeDay(int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, amount);
        return calendar.getTime();
    }

    /**
     * This method generates a string representation of a date/time in the format you specify on
     * input
     *
     * @param aMask   the date pattern the string is in
     * @param strDate a string representation of a date
     * @return a converted Date object
     * @see java.text.SimpleDateFormat
     */
    public static final Date convertStringToDate(String aMask, String strDate) throws ParseException {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(aMask);

        if (log.isDebugEnabled()) {
            log.debug("converting '" + strDate + "' to date with mask '" + aMask + "'");
        }

        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            // log.error("ParseException: " + pe);
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }

        return (date);
    }

    public static final String convertDateToString(Date date) {
        // DateFormat format = new SimpleDateFormat(sdf);
        return sdf.format(date);

    }

    public static String convertTimeToString(Date date) {
        String timeStr = convertDateToString(date);
        return timeStr;

    }

    /**
     * This method returns the current date time in the format: MM/dd/yyyy HH:MM a
     *
     * @param theTime the current time
     * @return the current date/time
     */
    public static String getTimeNow(Date theTime) {
        return getDateTime(timePattern, theTime);
    }

    /**
     * This method generates a string representation of a date's date/time in the format you specify
     * on input
     *
     * @param aMask the date pattern the string is in
     * @param aDate a date object
     * @return a formatted string representation of the date
     * @see java.text.SimpleDateFormat
     */
    public static final String getDateTime(String aMask, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate == null) {
            log.error("aDate is null!");
            returnValue = "-";
        } else {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }
        return returnValue;
    }

    /**
     * 将时间yyyyMMdd变成yyyy-mm-dd
     */
    public static String stringForDate(String str) {
        if (str == null) {
            return null;
        }
        if (str.trim().equals("")) {
            return "";
        }

        StringBuffer dateString = new StringBuffer(str);

        dateString.insert(4, "-");
        dateString.insert(7, "-");

        return dateString.toString();
    }

    /**
     * 将时间yyyyMMddhhmmss变成yyyy-mm-dd hh:mm:ss
     */
    public static String stringForTime(String str) {
        if (str == null) {
            return null;
        }
        if (str.trim().equals("")) {
            return "";
        }
        StringBuffer timeString = new StringBuffer(str);

        timeString.insert(4, "-");
        timeString.insert(7, "-");
        timeString.insert(10, " ");
        timeString.insert(13, ":");
        timeString.insert(16, ":");

        return timeString.toString();
    }

    public static String formatTime(String str) {
        if (str == null) {
            return null;
        }
        if (str.trim().equals("")) {
            return "";
        }
        StringBuffer timeString = new StringBuffer(str);
        timeString.insert(2, ":");
        timeString.insert(5, ":");
        return timeString.toString();
    }

    /**
     * 获取当月最后一天的日期
     */
    public static String getCurrentMonthLastDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return sdf4.format(cal.getTime());
    }

    /**
     * 获取当月第一天
     */
    public static String getCurrentMonthFirstDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return sdf4.format(cal.getTime());
    }

    /***
     * 获取上个月当前的日期的后一天
     */
    public static String getPreviousMonthCurrentDay() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return sdf4.format(cal.getTime());

    }

    /***
     * 获取一周前的日期
     */
    public static String getPreviousWeek() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_WEEK, -6);
        return sdf4.format(cal.getTime());
    }

    /**
     * 获取今天日期
     */
    public static String getCurrentDay() {
        Calendar cal = Calendar.getInstance();

        return sdf4.format(cal.getTime());
    }

    /**
     * 获得当前时间的时间戳java.sql.Timestamp
     */
    public static Timestamp getTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(new Date());
    }

    /**
     * @param dateStr 格式 yyyyMMdd
     */
    public static int parseDay(String dateStr) {
        String
                day =
                (dateStr.substring(6)).startsWith("0") ? dateStr.substring(7) : dateStr.substring(6);
        return Integer.valueOf(day).intValue();
    }

    /**
     * 获取7天内的所有时间
     *
     * @return list集合
     */
    public List getDayOfWeeks() {
        int mondPlus = getMondPlus();
        List list = new ArrayList();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                /*
     * for (int i = 7 - dayOfweek; i >0; i--) { java.util.GregorianCalendar
		 * currentdate = new GregorianCalendar();
		 * currentdate.add(currentdate.DATE, mondPlus - i); Date date =
		 * currentdate.getTime(); String perdate = sf.format(date);
		 * System.out.println("===>"+perdate); list.add(perdate); }
		 */

        for (int j = 0; j < dayOfweek; j++) {
            GregorianCalendar currentdate = new GregorianCalendar();
            currentdate.add(currentdate.DATE, mondPlus + j);
            Date date = currentdate.getTime();
            String perdate = sf.format(date);
            System.out.println(perdate);
            list.add(perdate);
        }
        return list;

    }

    /**
     * 获取当前日期到本周星期一的天数
     */
    private int getMondPlus() {
        Calendar cl = Calendar.getInstance();
        dayOfweek = cl.get(cl.DAY_OF_WEEK) - 1;
        if (dayOfweek == 1) {
            return 0;
        } else {
            return 1 - dayOfweek;
        }
    }

}
