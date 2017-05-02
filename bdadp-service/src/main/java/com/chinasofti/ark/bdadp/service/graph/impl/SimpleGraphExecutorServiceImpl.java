package com.chinasofti.ark.bdadp.service.graph.impl;

import com.chinasofti.ark.bdadp.service.graph.GraphExecutorService;
import com.chinasofti.ark.bdadp.service.graph.bean.*;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.Executors;

/**
 * Created by White on 2016/09/23.
 */
public class SimpleGraphExecutorServiceImpl implements GraphExecutorService {

    private ListeningExecutorService executor;

    @Override
    public void submit(Graph graph) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                for (Vertex v : graph.getStartVertexes()) {
                    submitAndCallback(v);
                }
            }

            synchronized void submitAndCallback(Vertex vertex) {
                if (vertex.getState() > VertexState.READY.ordinal() || (
                        graph.isJoinVertex(vertex.getId()) && !graph.isJoinReady(vertex.getId()))) {
                    return;
                }

                vertex.setState(VertexState.COMPLETING.name());

                Futures.addCallback(executor.submit(() -> {
                    if (vertex instanceof TaskVertex) {
                        ((TaskVertex) vertex).get().run();
                    }

                    return vertex;
                }), new FutureCallback<Vertex>() {
                    @Override
                    public void onSuccess(Vertex vertex) {
                        vertex.setState(VertexState.SUCCESS.name());

                        if (graph.isTerminalVertex(vertex.getId())) {
                            for (Edge e : graph.getVertexOutEdges(vertex.getId())) {
                                submitAndCallback(e.getToVertex());
                            }
                        }

                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        vertex.setState(VertexState.FAILURE.name());

                    }
                }, executor);
            }
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        executor = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
    }

    @Override
    public void destroy() throws Exception {
        executor.shutdown();
    }
}
