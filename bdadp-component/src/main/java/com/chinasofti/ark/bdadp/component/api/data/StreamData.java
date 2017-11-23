package com.chinasofti.ark.bdadp.component.api.data;

import org.apache.spark.streaming.dstream.DStream;

import scala.Tuple2;

/**
 * Created by White on 2017/1/17.
 */
public class StreamData extends Data<DStream<Tuple2<String, String>>> {

    public StreamData(DStream<Tuple2<String, String>> rawData) {
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
