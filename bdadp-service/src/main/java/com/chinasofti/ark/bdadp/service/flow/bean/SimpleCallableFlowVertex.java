package com.chinasofti.ark.bdadp.service.flow.bean;

import com.chinasofti.ark.bdadp.service.graph.bean.VertexState;

/**
 * Created by White on 2016/09/25.
 */
public class SimpleCallableFlowVertex implements CallableFlowVertex {

    private final String id;
    private final CallableFlow flow;

    private VertexState state;

  private boolean skip;

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
  public boolean isSkip() {
    return skip;
  }

  @Override
  public void setSkip(boolean skip) {
    this.skip = skip;
  }

    @Override
    public String toString() {
      return "SimpleCallableFlowVertex{" +
             "id='" + id + '\'' +
             ", flow=" + flow +
             ", state=" + state +
             ", skip=" + skip +
             '}';
    }
}
