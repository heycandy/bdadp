package com.chinasofti.ark.bdadp.component.api.sink;

import com.chinasofti.ark.bdadp.component.api.data.StreamData;

/**
 * Created by White on 2017/1/17.
 */
public interface StreamSinkAdapter<InputT extends StreamData> {

  void apply(InputT input);
}
