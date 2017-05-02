package com.chinasofti.ark.bdadp.service.queue.bean;

import com.google.common.base.Function;
import com.google.common.collect.Sets;
import java8.util.stream.StreamSupport;

import java.util.Collection;
import java.util.Date;

/**
 * Created by White on 2016/11/23.
 */
public abstract class AbstractQueueTask implements QueueTask {

    private final String id;
    private final String name;
    private final Date createDate;

    private final Collection<Function<QueueTask, Void>> listener;

    protected double progress;

    public AbstractQueueTask(String id, String name) {
        this.id = id;
        this.name = name;

        this.createDate = new Date();
        this.listener = Sets.newHashSet();
    }

    @Override
    public String getCreateUser() {
        return null;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDesc() {
        return null;
    }

    @Override
    public double getProgress() {
        return 0;
    }

    @Override
    public void addListener(Function<QueueTask, Void> e) {
        this.listener.add(e);

        e.apply(this);
    }

    @Override
    public void reportAll() {
        StreamSupport.stream(this.listener)
                .forEach(function -> function.apply(this));
    }

    @Override
    public boolean remove() {
        return true;
    }

    @Override
    public Date getCreateTime() {
        return this.createDate;
    }
}
