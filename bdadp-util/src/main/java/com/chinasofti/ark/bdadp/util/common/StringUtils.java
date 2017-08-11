package com.chinasofti.ark.bdadp.util.common;


/**
 * @author wgzhang
 * @create 2016-09-08 20:00
 */
public class StringUtils extends org.apache.commons.lang.StringUtils {

  public static String message = "Parameter can not be empty or NULL!";
  public static String regex = ",";


  public static boolean isNulOrEmpty(String input) {
    if (input == null || input.isEmpty()) {
      return true;
    }
    return false;
  }


  public static boolean hasNullOrEmpty(String[] array) {
    for (String element : array) {
      if (StringUtils.isNulOrEmpty(element)) {
        return true;
      }
    }
    return false;
  }


  public static boolean isBlank(String... strs) {
    if (strs != null && strs.length > 0) {
      for (String str : strs) {
        if (isBlank(str)) {
          return true;
        } else {
          continue;
        }
      }
      return false;
    }
    return true;
  }


  public static void assertIsBlank(String... strs) {

    if (isBlank(strs)) {
      throw new IllegalArgumentException(message);
    }

  }

  public static String[] trimSplit(String str, String regex) {
    return str.isEmpty() ? null : deleteWhitespace(str).split(regex);
  }

  public static String[] trimSplit(String str) {
    if (isBlank(str)) {
      throw new IllegalArgumentException(message);
    }
    return trimSplit(str, regex);
  }

  public static boolean equals(String str,String anotherStr){
    return str.equalsIgnoreCase(anotherStr);
  }

}
