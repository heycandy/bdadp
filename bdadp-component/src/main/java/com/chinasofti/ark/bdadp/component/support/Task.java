package com.chinasofti.ark.bdadp.component.support;

import com.chinasofti.ark.bdadp.component.ComponentProps;
import com.chinasofti.ark.bdadp.component.api.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.lang.Runnable;

/**
 * Created by White on 2016/09/05.
 */
public interface Task<V>
        extends Future<V>, Runnable, Stateable, Listenable, Configureable, Component {

    @Override
    String getId();

    @Override
    String getName();

    @Override
    double getProgress() throws Exception;

    @Override
    void configure(ComponentProps props);

    @Override
    boolean cancel(boolean var1);

    @Override
    boolean isCancelled();

    @Override
    boolean isDone();

    @Override
    V get() throws InterruptedException, ExecutionException;

    @Override
    V get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException;

    @Override
    void report(Listener listener);

    @Override
    void reportAll();

    @Override
    void addListener(Listener listener);

    @Override
    void removeListener(Listener listener);

    @Override
    void removeAllListener();

    @Override
    void run();

    @Override
    int getState();

    ComponentProps props();

}
