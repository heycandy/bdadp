package com.chinasofti.ark.bdadp.service.scenario.impl;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.chinasofti.ark.bdadp.component.ComponentProps;
import com.chinasofti.ark.bdadp.component.api.Listener;
import com.chinasofti.ark.bdadp.component.api.options.PipelineOptionsFactory;
import com.chinasofti.ark.bdadp.component.api.options.ScenarioOptions;
import com.chinasofti.ark.bdadp.component.support.SimpleTask;
import com.chinasofti.ark.bdadp.component.support.TaskLogProvider;
import com.chinasofti.ark.bdadp.entity.components.Component;
import com.chinasofti.ark.bdadp.entity.scenario.Scenario;
import com.chinasofti.ark.bdadp.entity.scenario.ScenarioGraphDAG;
import com.chinasofti.ark.bdadp.entity.scenario.ScenarioGraphEdge;
import com.chinasofti.ark.bdadp.entity.scenario.ScenarioGraphVertex;
import com.chinasofti.ark.bdadp.entity.task.Task;
import com.chinasofti.ark.bdadp.entity.task.TaskConfig;
import com.chinasofti.ark.bdadp.expression.support.ArkConversionUtil;
import com.chinasofti.ark.bdadp.service.components.ComponentService;
import com.chinasofti.ark.bdadp.service.flow.FlowExecutorService;
import com.chinasofti.ark.bdadp.service.flow.bean.CallableFlow;
import com.chinasofti.ark.bdadp.service.flow.bean.SimpleCallableFlow;
import com.chinasofti.ark.bdadp.service.flow.bean.SimpleCallableFlowVertex;
import com.chinasofti.ark.bdadp.service.graph.bean.Edge;
import com.chinasofti.ark.bdadp.service.graph.bean.Graph;
import com.chinasofti.ark.bdadp.service.graph.bean.SimpleEdge;
import com.chinasofti.ark.bdadp.service.graph.bean.SimpleGraph;
import com.chinasofti.ark.bdadp.service.graph.bean.SimpleTaskVertex;
import com.chinasofti.ark.bdadp.service.graph.bean.Vertex;
import com.chinasofti.ark.bdadp.service.scenario.ScenarioExecutorService;
import com.chinasofti.ark.bdadp.service.scenario.ScenarioGraphDagService;
import com.chinasofti.ark.bdadp.service.scenario.ScenarioService;
import com.chinasofti.ark.bdadp.service.scenario.bean.ScenarioInspectCondition;
import com.chinasofti.ark.bdadp.service.scenario.bean.ScenarioInspectListener;
import com.chinasofti.ark.bdadp.service.scenario.bean.ScenarioScheduleListener;
import com.chinasofti.ark.bdadp.service.scenario.bean.ScenarioServiceAssert;
import com.chinasofti.ark.bdadp.util.common.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

/**
 * Created by White on 2016/09/18.
 */

public class ScenarioExecutorServiceImpl implements ScenarioExecutorService {

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  protected final String SCENARIO_SELECTED_KEYS = "scenario.vertex.selected";

  @Autowired
  protected ScenarioService scenarioService;

  @Autowired
  protected ScenarioGraphDagService scenarioGraphDagService;

  @Autowired
  protected ComponentService componentService;

  @Autowired
  protected FlowExecutorService executorService;

  @Override
  public String inspect(String scenarioId, ScenarioInspectCondition condition)
      throws Exception {

    ScenarioOptions options = PipelineOptionsFactory
        .fromSettings(defaultSettings())
        .as(ScenarioOptions.class);

    options.setScenarioId(scenarioId);
    options.setExecutionId(UUID.getId());
    options.setDebug(true);

    // Skip
    String keys = Joiner.on(',').join(condition.getSelected());
    options.getSettings().put(SCENARIO_SELECTED_KEYS, keys);

    return execute(options, ScenarioInspectListener.class);
  }

  @Override
  public String schedule(String scenarioId, String... args) throws Exception {
    ScenarioOptions options = PipelineOptionsFactory
        .fromSettings(defaultSettings())
        .fromArgs(args)
        .as(ScenarioOptions.class);

    options.setScenarioId(scenarioId);
    options.setExecutionId(UUID.getId());
    options.setDebug(true);

    return execute(options, ScenarioScheduleListener.class);
  }

  protected Map<String, String> defaultSettings() throws Exception {
    return ImmutableMap.<String, String>builder().build();
  }

  @Override
  public String execute(ScenarioOptions options, Class<? extends Listener>... listeners)
      throws Exception {
    Scenario scenario = scenarioService.findScenarioById(options.getScenarioId());
    ScenarioServiceAssert.nonExistsScenario(scenario == null, options.getScenarioId());

    options.setScenarioName(scenario.getScenarioName());

    Graph graph = newSimpleGraph(options.getScenarioId(), options, listeners);
    CallableFlow flow = new SimpleCallableFlow(
        options.getScenarioId(), options.getScenarioName(),
        options.getScenarioId(), options.getExecutionId(), graph);

    for (Class<? extends Listener> listenerClass : listeners) {
      flow.addListener(listenerClass.newInstance());
    }

    executorService.submit(flow);

    return options.getExecutionId();
  }

  public Object execute(String taskId, String executionId) throws IOException {
    String
        filename =
        TaskLogProvider.getFilename(taskId, executionId, TaskLogProvider.DEFAULT_FILE_CHILD);

    File file = new File(filename);

    if (file.exists()) {
      List<String> alines = Files.readAllLines(file.toPath(), Charset.defaultCharset());

      // 匹配JSON正则表达式
      String regEx = "^\\{[\\s\\S]{1,}\\}$";
      // 编译正则表达式
      Pattern pattern = Pattern.compile(regEx);
      // 匹配JSON字符串
      for (String aline : alines) {
        Matcher matcher = pattern.matcher(aline);
        if (matcher.matches()) {
          return aline;
        }
      }
      // 未匹配
      return alines;
    } else {
      logger.debug("log not exists {}", file);
      return Collections.singletonList("log not exists");
    }

  }

  Graph newSimpleGraph(String scenarioId, ScenarioOptions options,
                       Class<? extends Listener>... listeners) throws Exception {

    ScenarioGraphDAG graphDAG = scenarioGraphDagService.findScenarioByScenarioId(scenarioId);

    List<ScenarioGraphVertex> graphVertices = graphDAG.getGraphVertexs();
    List<ScenarioGraphEdge> graphEdges = graphDAG.getGraphEdges();

    // Skip vertices.
    String selectedKeys = java8.util.Maps.getOrDefault(
        options.getSettings(), SCENARIO_SELECTED_KEYS, "");
    options.getSettings().remove(SCENARIO_SELECTED_KEYS);

    Map<Integer, Vertex> vertexMap = buildVertexes(graphVertices, options, listeners);
    Collection<Edge> edges = buildEdges(graphEdges, vertexMap);
    Graph graph = new SimpleGraph(graphDAG.getGraphId());

    graph.addVertex(vertexMap.values());
    graph.addEdge(edges);

    // If vertex has been selected that recursion to skip its previous vertexes.
    if (!selectedKeys.isEmpty()) {
      Iterable<String> source = Splitter.on(',').split(selectedKeys);
      List<String> selected = Lists.newArrayList(source);
      Map<String, Integer> vertexMapKey = StreamSupport.stream(graphVertices)
          .collect(Collectors.toMap(
              vertex -> vertex.getTask().getTaskId(), ScenarioGraphVertex::getKeyId));

      StreamSupport.stream(graph.getStartVertexes()).forEach(vertex -> {
        stepVertex(graph, selected, vertexMapKey, vertex);

      });
    }

    return graph;
  }

  // Step vertex.
  private void stepVertex(
      Graph graph, List<String> selected, Map<String, Integer> vertexMapKey, Vertex curr) {
    String vertexId = curr.getId();
    Integer keyId = java8.util.Maps.getOrDefault(vertexMapKey, vertexId, 0);

    // if the curr vertex does not need to skip, then nothing todo.
    if (!selected.contains(keyId.toString())) {
      if (graph.isTerminalVertex(vertexId)) {
        StreamSupport.stream(graph.getVertexOutEdges(vertexId))
            .map(Edge::getToVertex)
            .forEach(vertex -> stepVertex(graph, selected, vertexMapKey, vertex));
      }

      curr.setSkip(true);
    }
  }

  Map<Integer, Vertex> buildVertexes(
      Collection<ScenarioGraphVertex> vertexes,
      ScenarioOptions options,
      Class<? extends Listener>... listeners) {
    Map<Integer, Vertex> vertexMap = Maps.newHashMap();

    for (ScenarioGraphVertex v : vertexes) {
      try {
        Task task = v.getTask();

        String taskId = task.getTaskId();
        String taskName = task.getTaskName();
        String taskType = task.getTaskType();

        String relationId = task.getRelationId();

        switch (taskType) {
          case "component":
            com.chinasofti.ark.bdadp.component.support.Task
                simpleTask =
                newSimpleTask(taskId, taskName, options, relationId);

            ComponentProps props = getTaskGeneratedProperties(task);
            simpleTask.configure(props);

            // TODO
            for (Class<? extends Listener> listenerClass : listeners) {
              simpleTask.addListener(listenerClass.newInstance());
            }

            Vertex taskVertex = new SimpleTaskVertex(simpleTask);

            vertexMap.put(v.getKeyId(), taskVertex);
            break;
          case "scenario":
            Graph innerGraph = newSimpleGraph(relationId, options, listeners);

            CallableFlow
                simpleCallableFlow =
                new SimpleCallableFlow(taskId, taskName, options.getScenarioId(),
                                       options.getExecutionId(), innerGraph);

            // TODO
            for (Class<? extends Listener> listenerClass : listeners) {
              simpleCallableFlow.addListener(listenerClass.newInstance());
            }

            Vertex callableFlowVertex = new SimpleCallableFlowVertex(simpleCallableFlow);

            vertexMap.put(v.getKeyId(), callableFlowVertex);

            break;
          default:
            throw new UnsupportedOperationException(String.format("unknown task type %s.", task));
        }

      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    return vertexMap;
  }

  Collection<Edge> buildEdges(Collection<ScenarioGraphEdge> edges, Map<Integer, Vertex> vertexMap) {
    return StreamSupport.stream(edges)
        .map(e -> new SimpleEdge(e.getEdgeId(), vertexMap.get(e.getFromKey()),
                                 vertexMap.get(e.getToKey())))
        .collect(Collectors.toList());
  }

  com.chinasofti.ark.bdadp.component.support.Task newSimpleTask
      (String taskId, String taskName, ScenarioOptions options, String relationId)
      throws ClassNotFoundException, IOException {
    Component component = componentService.findComponentById(relationId);

    String componentId = component.getComponentId();
    String componentName = component.getComponentName();
    String componentType = component.getComponentType();

    Class clazz = Class.forName(componentType);

    return new SimpleTask(taskId, taskName, options, clazz);
  }

  ComponentProps getTaskGeneratedProperties(Task task) {
    ComponentProps props = new ComponentProps();
    ArkConversionUtil util = new ArkConversionUtil();
    for (TaskConfig c : task.getTaskConfigs()) {
      String paramValue = c.getParamValue();
      logger.debug("param parse before: {}", paramValue);
      String parseValue = util.parseVariableByDefined(paramValue);
      logger.debug("param parse after: {}", parseValue);
      props.setProperty(c.getComponentConfig().getParamName(), parseValue);
    }

    return props;

  }

}
