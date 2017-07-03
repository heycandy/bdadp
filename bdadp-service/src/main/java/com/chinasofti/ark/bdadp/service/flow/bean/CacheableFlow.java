package com.chinasofti.ark.bdadp.service.flow.bean;

import com.chinasofti.ark.bdadp.component.ComponentProps;
import com.chinasofti.ark.bdadp.component.support.Task;
import com.chinasofti.ark.bdadp.component.support.TaskLogProvider;
import com.chinasofti.ark.bdadp.service.flow.FlowExecutorService;
import com.chinasofti.ark.bdadp.service.graph.bean.Edge;
import com.chinasofti.ark.bdadp.service.graph.bean.Graph;
import com.chinasofti.ark.bdadp.service.graph.bean.TaskVertex;
import com.chinasofti.ark.bdadp.service.graph.bean.Vertex;
import com.chinasofti.ark.bdadp.service.graph.bean.VertexState;
import com.chinasofti.ark.bdadp.util.hdfs.common.ConfigurationClient;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import java8.util.stream.StreamSupport;

/**
 * Created by White on 2016/09/22.
 */
public class CacheableFlow extends SimpleCallableFlow {

    private CacheMode cacheMode;
    private String executionPath;

    public CacheableFlow(String id, String name, String scenarioId, String executionId, Graph graph)
            throws IOException {
        super(id, name, scenarioId, executionId, graph);
    }

    @Override
    public CallbackExecutor onSubmit() {
        setState(FlowState.COMPLETING.name());

        return (executor) -> {
            for (Vertex vertex : getGraph().getStartVertexes()) {
                vertex.setState(VertexState.COMPLETING.name());

                this.submiting(executor, vertex);
            }
            super.awaitTermination();

            if (getState() > FlowState.SUCCESS.ordinal()) {
                TaskLogProvider.close(getLog());
                throw new RuntimeException(getName() + " execution is not completed.");
            }

            TaskLogProvider.close(getLog());
        };

    }

    @Override
    public CallbackExecutor onSuccess(Vertex vertex) {
        vertex.setState(VertexState.SUCCESS.name());

        this.completing();

        return (executor) -> {
            if (getGraph().isTerminalVertex(vertex.getId())) {
                for (Edge e : getGraph().getVertexOutEdges(vertex.getId())) {
                    Vertex v = e.getToVertex();
                    synchronized (v) {
                        if (v.getState() == VertexState.READY.ordinal()
                                && (!getGraph().isJoinVertex(v.getId()) || getGraph().isJoinReady(v.getId()))) {
                            v.setState(VertexState.COMPLETING.name());

                            this.submiting(executor, v);
                        }
                    }
                }
            }
        };
    }

    @Override
    public CallbackExecutor onFailure(Vertex vertex, Throwable throwable) {
        vertex.setState(VertexState.FAILURE.name());

        this.completing();

        error("failure: " + vertex, throwable);

        return (executor) -> {
        };
    }

    protected void completing() {
        if (super.isDone()) {
            boolean isSuccess = StreamSupport.stream(getGraph().getEndVertexes())
                    .allMatch(v -> v.getState() == VertexState.SUCCESS.ordinal());

            if (isSuccess) {
                setState(FlowState.SUCCESS.name());
            } else {
                setState(FlowState.FAILURE.name());
            }
        } else {
            reportAll();
        }
    }

    protected void submiting(FlowExecutorService executor, Vertex vertex) {
        if (vertex instanceof TaskVertex) {
            Task task = ((TaskVertex) vertex).get();

            ComponentProps defaultProps = new ComponentProps();

            // file_source file_path
            // dest_source dest_path
            Collection<Edge> inEdges = getGraph().getVertexInEdges(vertex.getId());
            if (inEdges != null && !inEdges.isEmpty()) {
                String
                        fileSource =
                        StreamSupport.stream(inEdges).filter(edge -> edge.getFromVertex() instanceof TaskVertex)
                                .map(edge -> ((TaskVertex) edge.getFromVertex()).get().props()
                                        .getString("dest_source", "0"))
                                .reduce((s1, s2) -> s1 + "," + s2).orElse("");

                String
                        filePath =
                        StreamSupport.stream(inEdges).filter(edge -> edge.getFromVertex() instanceof TaskVertex)
                                .map(edge -> ((TaskVertex) edge.getFromVertex()).get().props()
                                        .getString("dest_path", ""))
                                .reduce((s1, s2) -> s1 + "," + s2).orElse("");

                defaultProps.setProperty("file_source", fileSource);
                defaultProps.setProperty("file_path", filePath);

            }

            Path destPath = Paths.get(this.executionPath, task.getId());

            defaultProps.setProperty("dest_source", String.valueOf(this.cacheMode.ordinal()));
            defaultProps.setProperty("dest_path", destPath.toString());

            if (ConfigurationClient.isInstance()) {
                defaultProps.setProperty("hive_conf",
                        String.format("url=%s&name=%s", ConfigurationClient.getInstance()
                                        .get("hive.zookeeper.quorum"),
                                ConfigurationClient.getInstance()
                                        .get("username.client.kerberos.principal")));
            }

            task.configure(defaultProps);

        } else if (vertex instanceof CallableFlowVertex) {
            CallableFlow flow = ((CallableFlowVertex) vertex).get();

            if (flow instanceof CacheableFlow) {
                ((CacheableFlow) flow).setCacheMode(this.cacheMode);
                ((CacheableFlow) flow).setExecutionPath(this.executionPath);
            }
        }

        executor.submit(this, vertex);
    }

    public void setCacheMode(CacheMode cacheMode) {
        this.cacheMode = cacheMode;
    }

    public void setExecutionPath(String executionPath) {
        this.executionPath = executionPath;
    }
}
