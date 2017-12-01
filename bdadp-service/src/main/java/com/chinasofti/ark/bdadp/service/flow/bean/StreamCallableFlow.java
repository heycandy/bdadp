package com.chinasofti.ark.bdadp.service.flow.bean;

import com.chinasofti.ark.bdadp.component.api.options.ScenarioOptions;
import com.chinasofti.ark.bdadp.component.api.options.SparkScenarioOptions;
import com.chinasofti.ark.bdadp.service.graph.bean.Graph;
import com.chinasofti.ark.bdadp.service.graph.bean.Vertex;
import com.chinasofti.ark.bdadp.service.graph.bean.VertexState;

import java8.util.stream.StreamSupport;

/**
 * Created by White on 2017/12/1.
 */
public class StreamCallableFlow extends SimpleCallableFlow {

  private final ScenarioOptions options;

  public StreamCallableFlow(ScenarioOptions options, Graph graph) {
    super(options.getScenarioId(), options.getScenarioName(), options.getScenarioId(),
          options.getExecutionId(), graph);

    this.options = options;
    setState(FlowState.READY.name());
  }

  @Override
  protected void status(Vertex vertex) {
    boolean terminal = this.getGraph().isTerminalVertex(vertex.getId()) &&
                       vertex.getState() == VertexState.SUCCESS.ordinal();
    boolean anyMatch = StreamSupport.stream(this.getGraph().getAllVertex())
        .anyMatch(v -> v.getState() == VertexState.COMPLETING.ordinal());
    boolean allMatch = StreamSupport.stream(this.getGraph().getEndVertexes())
        .allMatch(v -> v.getState() == VertexState.SUCCESS.ordinal());
    System.out.println(String.format(
        "###SSE-10819### %s, %s, %s, %s", vertex, terminal, anyMatch, allMatch));
    if (terminal || anyMatch) {
      reportAll();
    } else if (allMatch) {
      // For real time flow scenario
      if (this.options.isStreaming()) {
        this.options.as(SparkScenarioOptions.class).startAndAwaitTermination();
      }

      setState(FlowState.SUCCESS.name());
    } else {
      setState(FlowState.FAILURE.name());
    }
  }

}
