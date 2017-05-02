package com.chinasofti.ark.bdadp.service.flow.impl;

import com.chinasofti.ark.bdadp.service.flow.FlowExecutorService;
import com.chinasofti.ark.bdadp.service.flow.bean.CallableFlow;
import com.chinasofti.ark.bdadp.service.flow.bean.CallableFlowVertex;
import com.chinasofti.ark.bdadp.service.graph.bean.TaskVertex;
import com.chinasofti.ark.bdadp.service.graph.bean.Vertex;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.Executors;

/**
 * Created by White on 2016/09/27.
 */
public class SimpleFlowExecutorService implements FlowExecutorService {

    private ListeningExecutorService executor;

    @Override
    public void submit(CallableFlow flow, Vertex vertex) {
        Futures.addCallback(executor.submit(() -> {
            if (vertex instanceof TaskVertex) {
                TaskVertex taskVertex = ((TaskVertex) vertex);
                taskVertex.get().run();

            } else if (vertex instanceof CallableFlowVertex) {
                CallableFlowVertex flowVertex = ((CallableFlowVertex) vertex);
                flowVertex.get().onSubmit().call(getExecutor());
            }

            return vertex;
        }), new FutureCallback<Vertex>() {
            @Override
            public void onSuccess(Vertex vertex) {
                flow.onSuccess(vertex).call(getExecutor());
            }

            @Override
            public void onFailure(Throwable throwable) {
                flow.onFailure(vertex, throwable).call(getExecutor());

            }
        }, executor);
    }

    @Override
    public FlowExecutorService getExecutor() {
        return this;
    }

    @Override
    public void submit(CallableFlow flow) {
        executor.submit(() -> flow.onSubmit().call(getExecutor()));
    }

    @Override
    public void destroy() throws Exception {
        executor.shutdown();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        executor = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
    }

}
