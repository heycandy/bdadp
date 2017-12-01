package com.chinasofti.ark.bdadp.component.api.data;

import org.apache.spark.streaming.dstream.DStream;

/**
 * Created by White on 2017/1/17.
 */
public class StreamData extends Data<DStream<String>> {

  public StreamData(DStream<String> rawData) {
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
