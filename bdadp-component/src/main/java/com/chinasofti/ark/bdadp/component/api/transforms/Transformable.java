package com.chinasofti.ark.bdadp.component.api.transforms;

import com.chinasofti.ark.bdadp.component.api.data.Data;

/**
 * Created by White on 2016/11/05.
 */
public interface Transformable<InputT extends Data, OutputT extends Data> {

    OutputT apply(InputT t);

}
