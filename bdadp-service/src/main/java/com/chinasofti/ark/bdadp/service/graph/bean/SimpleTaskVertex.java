package com.chinasofti.ark.bdadp.service.graph.bean;

import com.chinasofti.ark.bdadp.component.support.Task;

/**
 * Created by White on 2016/09/16.
 */
public class SimpleTaskVertex implements TaskVertex {

    private final String id;
    private final Task task;

    private VertexState state;

    public SimpleTaskVertex(Task task) {
        this.id = task.getId();
        this.task = task;

        setState(VertexState.READY.name());
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public int getState() {
        return this.state.ordinal();
    }

    @Override
    public void setState(String name) {
        this.state = VertexState.valueOf(name);
    }

    public Task get() {
        return task;
    }

    @Override
    public String toString() {
        return "TaskVertex{" +
                "id='" + id + '\'' +
                ", state=" + state +
                '}';
    }

}
