package com.chinasofti.ark.bdadp.component.api;

/**
 * Created by White on 2016/09/04.
 */
public interface Callable<V> extends java.util.concurrent.Callable<V> {

    @Override
    V call() throws Exception;
}
