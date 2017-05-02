package com.chinasofti.ark.bdadp.service.scenario.impl;

import com.chinasofti.ark.bdadp.component.ComponentProps;
import com.chinasofti.ark.bdadp.component.api.Listener;
import com.chinasofti.ark.bdadp.component.api.options.ScenarioOptions;
import com.chinasofti.ark.bdadp.component.support.CacheableTask;
import com.chinasofti.ark.bdadp.entity.components.Component;
import com.chinasofti.ark.bdadp.entity.scenario.Scenario;
import com.chinasofti.ark.bdadp.entity.scenario.ScenarioGraphVertex;
import com.chinasofti.ark.bdadp.entity.task.Task;
import com.chinasofti.ark.bdadp.entity.task.TaskConfig;
import com.chinasofti.ark.bdadp.expression.support.ArkConversionUtil;
import com.chinasofti.ark.bdadp.service.components.ComponentService;
import com.chinasofti.ark.bdadp.service.flow.FlowExecutorService;
import com.chinasofti.ark.bdadp.service.flow.bean.CacheableFlow;
import com.chinasofti.ark.bdadp.service.flow.bean.CallableFlow;
import com.chinasofti.ark.bdadp.service.flow.bean.SimpleCallableFlowVertex;
import com.chinasofti.ark.bdadp.service.graph.bean.Graph;
import com.chinasofti.ark.bdadp.service.graph.bean.SimpleTaskVertex;
import com.chinasofti.ark.bdadp.service.graph.bean.Vertex;
import com.chinasofti.ark.bdadp.service.scenario.ScenarioGraphDagService;
import com.chinasofti.ark.bdadp.service.scenario.ScenarioService;
import com.chinasofti.ark.bdadp.service.scenario.bean.ScenarioServiceAssert;
import com.chinasofti.ark.bdadp.service.scenario.bean.ScenarioServiceException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * Created by White on 2016/09/18.
 */

public class ScenarioExecutorServiceImpl$Cacheable extends ScenarioExecutorServiceImpl {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ScenarioService scenarioService;

    @Autowired
    private ScenarioGraphDagService scenarioGraphDagService;

    @Autowired
    private ComponentService componentService;

    @Autowired
    private FlowExecutorService executorService;

    @Override
    public String execute(ScenarioOptions options, Class<? extends Listener>... listeners)
            throws Exception {
        Graph graph = newSimpleGraph(options.getScenarioId(), options, listeners);

        Scenario scenario = scenarioService.findScenarioById(options.getScenarioId());

        options.setScenarioName(scenario.getScenarioName());

        ScenarioServiceAssert.nonExistsScenario(scenario == null, options.getScenarioId());
        CallableFlow
                flow =
                new CacheableFlow(options.getScenarioId(), options.getScenarioName(),
                        options.getScenarioId(), options.getExecutionId(), graph);

        // TODO
        for (Class<? extends Listener> listenerClass : listeners) {
            flow.addListener(listenerClass.newInstance());
        }

        executorService.submit(flow);

        return options.getExecutionId();
    }

    @Override
    Map<Integer, Vertex> buildVertexes(Collection<ScenarioGraphVertex> vertexes,
                                       ScenarioOptions options,
                                       Class<? extends Listener>... listeners) {
        Map<Integer, Vertex> vertexMap = Maps.newHashMap();
        for (ScenarioGraphVertex v : vertexes) {
            try {
                Task task = v.getTask();
                ScenarioServiceAssert.nonExistsTask(task == null);

                String taskId = task.getTaskId();
                String taskName = task.getTaskName();
                String taskType = task.getTaskType();

                String relationId = task.getRelationId();

                switch (taskType) {
                    case "component":
                        com.chinasofti.ark.bdadp.component.support.Task
                                cacheableTask =
                                newSimpleTask(taskId, taskName, options, relationId);

                        ComponentProps props = getTaskGeneratedProperties(task);
                        cacheableTask.configure(props);

                        // TODO
                        for (Class<? extends Listener> listenerClass : listeners) {
                            cacheableTask.addListener(listenerClass.newInstance());
                        }

                        Vertex taskVertex = new SimpleTaskVertex(cacheableTask);

                        vertexMap.put(v.getKeyId(), taskVertex);
                        break;
                    case "scenario":
                        Graph innerGraph = newSimpleGraph(relationId, options, listeners);

                        CallableFlow
                                simpleCallableFlow =
                                new CacheableFlow(taskId, taskName, options.getScenarioId(),
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
            } catch (ScenarioServiceException sse) {
                throw new ScenarioServiceException(sse.getResultCode(), sse.getResultMessage(),
                        sse.getResultArgs(), sse);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return vertexMap;
    }

    @Override
    com.chinasofti.ark.bdadp.component.support.Task
    newSimpleTask(String taskId, String taskName, ScenarioOptions options, String relationId)
            throws ClassNotFoundException, IOException {
        Component component = componentService.findComponentById(relationId);

        String componentId = component.getComponentId();
        String componentName = component.getComponentName();
        String componentType = component.getComponentType();

        Class clazz = componentService.loadComponentClass(componentId);

        return new CacheableTask(taskId, taskName, options, clazz);
    }

    @Override
    ComponentProps getTaskGeneratedProperties(Task task) {
        ComponentProps props = new ComponentProps();
        ArkConversionUtil util = new ArkConversionUtil();

        for (TaskConfig c : task.getTaskConfigs()) {
            try {
                String paramValue = c.getParamValue();
                logger.debug("param parse before: {}", paramValue);
                String parseValue = util.parseVariableByDefined(paramValue);
                logger.debug("param parse after: {}", parseValue);
                props.setProperty(c.getComponentConfig().getParamName(), parseValue);
            } catch (ExpressionException e) {
                String
                        format =
                        "Scenario variable parse error, component '%s' parameter value '%s', please check off.";
                Object[] args = Lists.newArrayList(c.getParamValue(), task.getTaskName()).toArray();
                throw new ScenarioServiceException(22010, String.format(format, args), args, e);
            }
        }

        return props;

    }

}
