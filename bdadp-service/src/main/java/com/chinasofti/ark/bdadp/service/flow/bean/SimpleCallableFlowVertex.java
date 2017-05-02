package com.chinasofti.ark.bdadp.service.flow.bean;

import com.chinasofti.ark.bdadp.service.graph.bean.VertexState;

/**
 * Created by White on 2016/09/25.
 */
public class SimpleCallableFlowVertex implements CallableFlowVertex {

    private final String id;
    private final CallableFlow flow;

    private VertexState state;

    public SimpleCallableFlowVertex(CallableFlow flow) {
        this.id = flow.getId();
        this.flow = flow;

        setState(VertexState.READY.name());
    }

    @Override
    public CallableFlow get() {
        return flow;
    }

    @Override
    public int getState() {
        return this.state.ordinal();
    }

    @Override
    public void setState(String name) {
        this.state = VertexState.valueOf(name);
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "FlowVertex{" +
                "id='" + id + '\'' +
                ", flow=" + flow +
                ", state=" + state +
                '}';
    }
}
