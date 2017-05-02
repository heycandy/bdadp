package com.chinasofti.ark.bdadp.component.api.data;

import org.apache.spark.sql.DataFrame;

/**
 * Created by White on 2017/1/4.
 */
public class SparkData extends Data<DataFrame> {

    public SparkData(DataFrame rawData) {
        super(rawData);
    }

    @Override
    public DataType getType() {
        return DataType.SPARK;
    }

    @Override
    public DataInfo<?> getInfo() {
        return null;
    }
}
