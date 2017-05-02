package com.chinasofti.ark.bdadp.service.queue.bean;

import com.google.common.base.Function;

import java.util.Date;

/**
 * Created by White on 2016/11/17.
 */
public interface QueueTask extends Runnable {

    String getId();

    String getName();

    String getDesc();

    String getAction();

    double getProgress();

    void addListener(Function<QueueTask, Void> e);

    void reportAll();

    boolean remove();

    Date getCreateTime();

    String getCreateUser();

}
