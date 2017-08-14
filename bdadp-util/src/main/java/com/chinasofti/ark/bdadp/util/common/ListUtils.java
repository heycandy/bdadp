package com.chinasofti.ark.bdadp.util.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wgzhang on 2016/8/28.
 */
public class ListUtils {

    /**
     * iterator转List
     */
    public static <T> List<T> convertIterToList(Iterator<T> iter) {
        List<T> list = new ArrayList<T>();
        while (iter.hasNext()) {
            list.add(iter.next());
        }
        return list;
    }
}
