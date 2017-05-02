package com.chinasofti.ark.bdadp.flow;

import com.chinasofti.ark.bdadp.entity.scenario.ScenarioGraphDAG;
import com.chinasofti.ark.bdadp.entity.scenario.ScenarioGraphEdge;
import com.chinasofti.ark.bdadp.entity.scenario.ScenarioGraphVertex;
import com.chinasofti.ark.bdadp.service.ServiceContext;
import com.chinasofti.ark.bdadp.service.scenario.ScenarioGraphDagService;
import java.util.HashMap;
import java.util.Map;
import org.jgrapht.EdgeFactory;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;

public class TaskFlowBuilder {

  public static TaskFlow buildFlow(String scenarioId)
      throws Exception {
    return buildFlow(getScenarioDag(scenarioId));
  }

  public static TaskFlow buildFlow(DirectedAcyclicGraph<Task, TaskLink> dag) {
    return new TaskFlow(dag);
  }

  private static DirectedAcyclicGraph<Task, TaskLink> getScenarioDag(String scenarioId)
      throws Exception {
    DirectedAcyclicGraph<Task, TaskLink> dag =
        new DirectedAcyclicGraph<Task, TaskLink>(new EdgeFactory<Task, TaskLink>() {

          @Override
          public TaskLink createEdge(Task source, Task target) {
            return new TaskLink(source, target);
          }

        });
    // TODO: search scenario dag by scenario id, and then fill the DirectedAcyclicGraph object

    ScenarioGraphDAG
        graphDAG =
        ServiceContext.getService(ScenarioGraphDagService.class)
            .findScenarioByScenarioId(scenarioId);

    Map<Integer, Task> taskMap = new HashMap<>();
    for (ScenarioGraphVertex vertex : graphDAG.getGraphVertexs()) {
      com.chinasofti.ark.bdadp.entity.task.Task entityTask = vertex.getTask();
      Task
          task =
          new Task(entityTask.getTaskId(), entityTask.getTaskName(), entityTask.getTaskDesc(), null,
              null);
      taskMap.put(vertex.getKeyId(), task);
    }

    for (ScenarioGraphEdge edge : graphDAG.getGraphEdges()) {
      Task fromVertex = taskMap.get(edge.getFromKey());
      Task toVertex = taskMap.get(edge.getToKey());
      dag.addDagEdge(fromVertex, toVertex);
    }

    return dag;
  }

}
