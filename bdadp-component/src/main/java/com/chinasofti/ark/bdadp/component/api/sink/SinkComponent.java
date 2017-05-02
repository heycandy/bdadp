package com.chinasofti.ark.bdadp.component.api.sink;

import com.chinasofti.ark.bdadp.component.api.AbstractComponent;
import com.chinasofti.ark.bdadp.component.api.data.Data;
import org.slf4j.Logger;

/**
 * Created by White on 2017/1/17.
 */
public abstract class SinkComponent<InputT extends Data> extends AbstractComponent {

    public SinkComponent(String id, String name, Logger log) {
        super(id, name, log);
    }

    public abstract void apply(InputT input);

}
