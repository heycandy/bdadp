package com.chinasofti.ark.bdadp.component.api.transforms;

import com.chinasofti.ark.bdadp.component.api.AbstractComponent;
import com.chinasofti.ark.bdadp.component.api.data.Data;
import org.slf4j.Logger;

/**
 * Created by White on 2017/1/4.
 */
public abstract class TransformableComponent<InputT extends Data, OutputT extends Data>
        extends AbstractComponent
        implements Transformable<InputT, OutputT> {

    protected TransformableComponent(String id, String name, Logger log) {
        super(id, name, log);
    }

    @Override
    public abstract OutputT apply(InputT input);

}
