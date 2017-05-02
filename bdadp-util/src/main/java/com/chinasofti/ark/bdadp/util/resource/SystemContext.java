package com.chinasofti.ark.bdadp.util.resource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wumin on 2016/9/21.
 */
public class SystemContext {

    private final static Map<String, Class<?>>
            componentsClasses =
            new ConcurrentHashMap<String, Class<?>>();

    public static Class<?> getComponentClass(String className) {
        return componentsClasses.get(className);
    }

    static void setComponentClass(String className, Class<?> clz) {
        componentsClasses.put(className, clz);
    }
}
