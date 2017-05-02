package com.chinasofti.ark.bdadp.component.api.source;

import com.chinasofti.ark.bdadp.component.api.data.Data;
import com.chinasofti.ark.bdadp.component.api.options.SparkScenarioOptions;

/**
 * Created by White on 2017/1/17.
 */
public interface SparkSourceAdapter<OutputT extends Data> {

    OutputT spark(SparkScenarioOptions options);
}
