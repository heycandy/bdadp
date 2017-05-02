package com.chinasofti.ark.bdadp.util.common;


/**
 * @author wgzhang
 * @create 2016-09-08 20:00
 */
public class StringUtils {

    public static String message = "Parameter can not be empty or NULL!";

    /**
     * Check if string is null or empty.
     */
    public static boolean isNulOrEmpty(String input) {
        if (input == null || input.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Check if there's empty string in array.
     */
    public static boolean hasNullOrEmpty(String[] array) {
        for (String element : array) {
            if (StringUtils.isNulOrEmpty(element)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param strs
     * @return
     */
    public static boolean isBlank(String... strs) {
        if (strs != null) {
            for (String str : strs) {
                if (org.apache.commons.lang.StringUtils.isBlank(str)) {
                    return true;
                } else {
                    continue;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * @param strs
     */
    public static void assertIsBlank(String... strs) {

        if (isBlank(strs)) {
            throw new IllegalArgumentException(message);
        }

    }


}
