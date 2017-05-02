package com.chinasofti.ark.bdadp.component.api;

import org.slf4j.Logger;

/**
 * Created by White on 2016/09/03.
 */
public abstract class RunnableComponent extends AbstractComponent implements java.lang.Runnable {

    public RunnableComponent(String id, String name, Logger log) {
        super(id, name, log);
    }

    @Override
    public abstract void run();

}
