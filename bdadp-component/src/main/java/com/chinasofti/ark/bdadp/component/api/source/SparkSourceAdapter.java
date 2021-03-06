package com.chinasofti.ark.bdadp.component.api.source;

import com.chinasofti.ark.bdadp.component.api.data.SparkData;
import com.chinasofti.ark.bdadp.component.api.options.SparkScenarioOptions;

/**
 * Created by White on 2017/1/17.
 */
public interface SparkSourceAdapter<OutputT extends SparkData> {

    OutputT spark(SparkScenarioOptions options);
}
