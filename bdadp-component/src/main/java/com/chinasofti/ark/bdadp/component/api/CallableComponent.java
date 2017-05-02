package com.chinasofti.ark.bdadp.component.api;

import org.slf4j.Logger;

/**
 * Created by White on 2016/09/03.
 */
public abstract class CallableComponent<V> extends AbstractComponent
        implements Callable<V> {

    public CallableComponent(String id, String name, Logger log) {
        super(id, name, log);
    }

    @Override
    public abstract V call() throws Exception;

}
