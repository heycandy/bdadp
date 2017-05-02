package com.chinasofti.ark.bdadp.component.api.sink;

import com.chinasofti.ark.bdadp.component.api.data.StringData;

/**
 * Created by White on 2017/1/17.
 */
public interface StringSinkAdapter<InputT extends StringData> {

    void apply(InputT input);
}
