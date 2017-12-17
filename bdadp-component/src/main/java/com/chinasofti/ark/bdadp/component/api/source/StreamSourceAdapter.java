package com.chinasofti.ark.bdadp.component.api.source;

import com.chinasofti.ark.bdadp.component.api.data.StreamData;
import com.chinasofti.ark.bdadp.component.api.options.SparkScenarioOptions;

/**
 * Created by White on 2017/1/17.
 */
public interface StreamSourceAdapter<OutputT extends StreamData> {

  OutputT stream(SparkScenarioOptions options);
}
