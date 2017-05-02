package com.chinasofti.ark.bdadp.component.api.data;

/**
 * Created by White on 2017/1/11.
 */
public class StringData extends Data<String> {

    public StringData(String rawData) {
        super(rawData);
    }

    @Override
    public DataType getType() {
        return DataType.STRING;
    }

    @Override
    public DataInfo<?> getInfo() {
        return null;
    }
}
