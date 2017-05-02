package com.chinasofti.ark.bdadp.expression.converter;

import org.springframework.core.convert.converter.Converter;

/**
 * Created by White on 2016/09/11.
 */

public final class ObjectToStringConverter implements Converter<Object, String> {

    public ObjectToStringConverter() {
    }

    public String convert(Object source) {
        return source.toString();
    }
}

