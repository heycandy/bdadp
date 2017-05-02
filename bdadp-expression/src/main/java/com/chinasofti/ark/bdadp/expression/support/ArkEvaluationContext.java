package com.chinasofti.ark.bdadp.expression.support;

import org.springframework.expression.TypeConverter;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * Created by White on 2016/09/11.
 */
public class ArkEvaluationContext extends StandardEvaluationContext {

    private TypeConverter typeConverter;

    @Override
    public TypeConverter getTypeConverter() {
        if (this.typeConverter == null) {
            this.typeConverter = new ArkTypeConverter();
        }

        return this.typeConverter;
    }
}
