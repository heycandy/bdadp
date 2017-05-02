package com.chinasofti.ark.bdadp.controller.queue.bean;

/**
 * Created by White on 2016/11/21.
 */
public class QueueAction {

    private String action;
    private Iterable<String> iterable;
    private String userId;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Iterable<String> getIterable() {
        return iterable;
    }

    public void setIterable(Iterable<String> iterable) {
        this.iterable = iterable;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
