package com.chinasofti.ark.bdadp.component.api.sink;

import com.chinasofti.ark.bdadp.component.api.data.SparkData;

/**
 * Created by White on 2017/1/17.
 */
public interface SparkSinkAdapter<InputT extends SparkData> {

    void apply(InputT input);
}
