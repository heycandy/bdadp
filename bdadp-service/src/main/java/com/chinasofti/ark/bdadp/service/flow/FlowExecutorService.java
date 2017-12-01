package com.chinasofti.ark.bdadp.service.flow;

import com.chinasofti.ark.bdadp.service.flow.bean.CallableFlow;
import com.chinasofti.ark.bdadp.service.graph.bean.Vertex;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by White on 2016/09/27.
 */
public interface FlowExecutorService extends InitializingBean, DisposableBean {

    void submit(CallableFlow flow);

    void submit(CallableFlow flow, Vertex vertex);

  CallableFlow remove(String executionId);

    FlowExecutorService getExecutor();

}
