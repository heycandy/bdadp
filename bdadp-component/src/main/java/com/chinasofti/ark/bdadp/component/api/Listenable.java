package com.chinasofti.ark.bdadp.component.api;

/**
 * Created by White on 2016/09/04.
 */
public interface Listenable {

    void report(Listener listener);

    void reportAll();

    void addListener(Listener listener);

    void removeListener(Listener listener);

    void removeAllListener();
}
