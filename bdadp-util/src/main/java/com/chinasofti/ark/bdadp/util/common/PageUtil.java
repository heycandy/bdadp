package com.chinasofti.ark.bdadp.util.common;

/**
 *
 */

import org.springframework.data.domain.PageRequest;

/**
 * @Author : water
 * @Date : 2016年10月18日
 * @Desc : TODO
 * @version: V1.0
 */
public class PageUtil {

    public static PageRequest createNoErrorPageRequest(final Integer pageNumber,
                                                       final Integer pageSize) {
        if (null == pageNumber || null == pageSize || pageSize <= 0) {
            return new PageRequest(0, 20);
        }
        if (pageNumber - 1 <= 0) {
            return new PageRequest(0, pageSize);
        }
        return new PageRequest(pageNumber - 1, pageSize);
    }


    public static PageRequest createPageRequest(final Integer offset, final Integer limit) {
        Integer pageNumber;
        if (null == offset || null == limit) {
            pageNumber = 0;
        } else {
            pageNumber = offset / limit;
        }
        if (null == pageNumber || null == limit || limit <= 0) {
            return new PageRequest(0, 20);
        }
        if (pageNumber <= 0) {
            return new PageRequest(0, limit);
        }
        return new PageRequest(pageNumber, limit);
    }
}

