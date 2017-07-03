package com.chinasofti.ark.bdadp.service.flow.bean;

import com.chinasofti.ark.bdadp.component.support.TaskLogProvider;
import com.chinasofti.ark.bdadp.service.graph.bean.Edge;
import com.chinasofti.ark.bdadp.service.graph.bean.Graph;
import com.chinasofti.ark.bdadp.service.graph.bean.Vertex;
import com.chinasofti.ark.bdadp.service.graph.bean.VertexState;

import java8.util.stream.StreamSupport;

/**
 * Created by White on 2016/09/22.
 */
public class SimpleCallableFlow extends CallableFlow {

  private final String _scenarioId;
  private final String _executionId;

  public SimpleCallableFlow(String id, String name, String scenarioId, String executionId,
                            Graph graph) {
    super(id, name, executionId, graph);

    _scenarioId = scenarioId;
    _executionId = executionId;

    setState(FlowState.READY.name());
  }

  @Override
  public CallbackExecutor onSubmit() {
    setState(FlowState.COMPLETING.name());

    return (executor) -> {
      for (Vertex vertex : getGraph().getStartVertexes()) {
        vertex.setState(VertexState.COMPLETING.name());

        executor.submit(getFlow(), vertex);
      }

      awaitTermination();

      if (getState() > FlowState.SUCCESS.ordinal()) {
        TaskLogProvider.close(getLog());
        throw new RuntimeException(getName() + " execution is not completed.");
      }

      TaskLogProvider.close(getLog());
    };

  }

  @Override
  public CallbackExecutor onSuccess(Vertex vertex) {
    this.completing(vertex);

    return (executor) -> {
      if (getGraph().isTerminalVertex(vertex.getId())) {
        for (Edge e : getGraph().getVertexOutEdges(vertex.getId())) {
          Vertex v = e.getToVertex();
          synchronized (v) {
            if (v.getState() == VertexState.READY.ordinal()
                && (!getGraph().isJoinVertex(v.getId()) || getGraph().isJoinReady(v.getId()))) {
              v.setState(VertexState.COMPLETING.name());

              executor.submit(getFlow(), v);
            }
          }
        }
      }
    };
  }

  @Override
  public CallbackExecutor onFailure(Vertex vertex, Throwable throwable) {
    this.completing(vertex, throwable);

    return (executor) -> {
    };
  }

  protected void completing(Vertex vertex) {
    this.completing(vertex, null);
  }

  protected synchronized void completing(Vertex vertex, Throwable throwable) {
    if (throwable == null) {
      vertex.setState(VertexState.SUCCESS.name());
      info("Success: " + vertex);
    } else {
      vertex.setState(VertexState.FAILURE.name());
      error("Failure: " + vertex, throwable);
    }

    boolean terminal = this.getGraph().isTerminalVertex(vertex.getId()) &&
                       vertex.getState() == VertexState.SUCCESS.ordinal();
    boolean anyMatch = StreamSupport.stream(this.getGraph().getAllVertex())
        .anyMatch(v -> v.getState() == VertexState.COMPLETING.ordinal());
    boolean allMatch = StreamSupport.stream(this.getGraph().getEndVertexes())
        .allMatch(v -> v.getState() == VertexState.SUCCESS.ordinal());
    System.out.println(String.format(
        "###SSE-00000### %s, %s, %s, %s", vertex, terminal, anyMatch, allMatch));
    if (terminal || anyMatch) {
      reportAll();
    } else if (allMatch) {
      setState(FlowState.SUCCESS.name());
    } else {
      setState(FlowState.FAILURE.name());
    }

  }

  protected CallableFlow getFlow() {
    return this;
  }

  public String getScenarioId() {
    return _scenarioId;
  }

  public String getExecutionId() {
    return _executionId;
  }

}
