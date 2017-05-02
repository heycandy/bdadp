package com.chinasofti.ark.bdadp.expression.support;

import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.expression.TypeConverter;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelMessage;
import org.springframework.util.Assert;

/**
 * Created by White on 2016/09/11.
 */
public class ArkTypeConverter implements TypeConverter {

    private static ConversionService defaultConversionService;
    private final ConversionService conversionService;

    public ArkTypeConverter() {
        synchronized (this) {
            if (defaultConversionService == null) {
                defaultConversionService = new ArkConversionUtil();
            }
        }

        this.conversionService = defaultConversionService;
    }

    public ArkTypeConverter(ConversionService conversionService) {
        Assert.notNull(conversionService, "ConversionService must not be null");
        this.conversionService = conversionService;
    }

    public boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return this.conversionService.canConvert(sourceType, targetType);
    }

    public Object convertValue(Object value, TypeDescriptor sourceType, TypeDescriptor targetType) {
        try {
            return this.conversionService.convert(value, sourceType, targetType);
        } catch (ConversionException var5) {
            throw new SpelEvaluationException(var5, SpelMessage.TYPE_CONVERSION_ERROR,
                    new Object[]{sourceType.toString(), targetType.toString()});
        }
    }
}
