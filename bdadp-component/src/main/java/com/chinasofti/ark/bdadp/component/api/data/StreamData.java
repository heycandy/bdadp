package com.chinasofti.ark.bdadp.component.api.data;

import java.io.InputStream;

/**
 * Created by White on 2017/1/17.
 */
public class StreamData extends Data<InputStream> {

    public StreamData(InputStream rawData) {
        super(rawData);
    }

    @Override
    public DataType getType() {
        return DataType.STREAM;
    }

    @Override
    public DataInfo<?> getInfo() {
        return null;
    }
}
