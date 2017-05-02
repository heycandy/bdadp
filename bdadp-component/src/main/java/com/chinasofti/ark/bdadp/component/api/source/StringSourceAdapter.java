package com.chinasofti.ark.bdadp.component.api.source;

import com.chinasofti.ark.bdadp.component.api.data.Data;

/**
 * Created by White on 2017/1/17.
 */
public interface StringSourceAdapter<OutputT extends Data> {

    OutputT string();
}
