package com.chinasofti.ark.bdadp.component.api.source;

import com.chinasofti.ark.bdadp.component.api.CallableComponent;
import com.chinasofti.ark.bdadp.component.api.data.Data;
import org.slf4j.Logger;

/**
 * Created by White on 2017/1/11.
 */
public abstract class SourceComponent<V extends Data> extends CallableComponent<V> {

    public SourceComponent(String id, String name, Logger log) {
        super(id, name, log);
    }

    @Override
    public abstract V call();

}
