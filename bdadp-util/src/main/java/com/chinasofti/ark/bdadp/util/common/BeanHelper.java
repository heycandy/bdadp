package com.chinasofti.ark.bdadp.util.common;

import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by White on 2016/09/02.
 */
public class BeanHelper {

    public static void copyProperties(Object source, Object target, String... ignoreProperties) {
        BeanUtils.copyProperties(source, target, ignoreProperties);
    }

    public static void mergeProperties(Object source, Object target, String... ignoreProperties) {
        Set<String> propsSet = new HashSet<String>();

        Class<?> actualEditable = source.getClass();

        try {
            PropertyDescriptor[] sourcePds = BeanUtils.getPropertyDescriptors(actualEditable);
            for (PropertyDescriptor sourcePd : sourcePds) {
                if (sourcePd.getReadMethod().invoke(source) == null) {
                    propsSet.add(sourcePd.getName());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        propsSet.addAll(Arrays.asList(ignoreProperties));

        int size = propsSet.size();
        ignoreProperties = propsSet.toArray(new String[size]);
        BeanUtils.copyProperties(source, target, ignoreProperties);
    }
}
