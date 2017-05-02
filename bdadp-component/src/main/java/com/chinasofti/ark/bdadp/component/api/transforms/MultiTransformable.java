package com.chinasofti.ark.bdadp.component.api.transforms;

import com.chinasofti.ark.bdadp.component.api.data.Data;

import java.util.Collection;

/**
 * Created by White on 2016/11/05.
 */
public interface MultiTransformable<InputT extends Collection<? extends Data>, OutputT extends Data> {

    OutputT apply(InputT t);

}
