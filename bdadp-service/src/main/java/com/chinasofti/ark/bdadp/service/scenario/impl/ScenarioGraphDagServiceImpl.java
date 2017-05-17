package com.chinasofti.ark.bdadp.service.scenario.impl;

import com.chinasofti.ark.bdadp.dao.scenario.ScenarioDao;
import com.chinasofti.ark.bdadp.dao.scenario.ScenarioGraphDagDao;
import com.chinasofti.ark.bdadp.dao.scenario.ScenarioGraphEdgeDao;
import com.chinasofti.ark.bdadp.dao.scenario.ScenarioGraphVertexDao;
import com.chinasofti.ark.bdadp.dao.task.TaskConfigDao;
import com.chinasofti.ark.bdadp.dao.task.TaskDao;
import com.chinasofti.ark.bdadp.entity.components.ComponentConfig;
import com.chinasofti.ark.bdadp.entity.scenario.ScenarioGraphDAG;
import com.chinasofti.ark.bdadp.entity.scenario.ScenarioGraphEdge;
import com.chinasofti.ark.bdadp.entity.scenario.ScenarioGraphVertex;
import com.chinasofti.ark.bdadp.entity.task.Task;
import com.chinasofti.ark.bdadp.entity.task.TaskConfig;
import com.chinasofti.ark.bdadp.service.components.ComponentService;
import com.chinasofti.ark.bdadp.service.scenario.ScenarioGraphDagService;
import com.chinasofti.ark.bdadp.service.scenario.ScenarioVersionService;
import com.chinasofti.ark.bdadp.service.scenario.bean.ScenarioServiceAssert;
import com.chinasofti.ark.bdadp.service.scenario.bean.ScenarioServiceException;
import com.chinasofti.ark.bdadp.util.common.UUID;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by wgzhang on 2016/8/28.
 */
@Service
@Transactional
public class ScenarioGraphDagServiceImpl implements ScenarioGraphDagService {

    @Autowired
    private ScenarioDao scenarioDao;
    @Autowired
    private ScenarioVersionService scenarioVersionService;
    @Autowired
    private ScenarioGraphDagDao scenarioGraphDagDao;
    @Autowired
    private ScenarioGraphEdgeDao scenarioGraphEdgeDao;
    @Autowired
    private ScenarioGraphVertexDao scenarioGraphVertexDao;
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private TaskConfigDao taskConfigDao;
    @Autowired
    private ComponentService componentService;

    public ScenarioGraphDAG updateScenarioGraph(ScenarioGraphDAG scenarioGraphDAG)
            throws ScenarioServiceException {
        return updateScenarioGraph(scenarioGraphDAG, true);
    }

    @Override
    public ScenarioGraphDAG updateScenarioGraph(ScenarioGraphDAG scenarioGraphDAG, boolean isResolve)
            throws ScenarioServiceException {
        ScenarioServiceAssert.nonExistsGraph(scenarioGraphDAG.getScenarioId() == null);
        String graphId = scenarioGraphDAG.getGraphId();

        Iterable<ScenarioGraphVertex> vertexes = scenarioGraphVertexDao.findByGraphId(graphId);

        scenarioGraphEdgeDao.deleteByGraphId(graphId);
        scenarioGraphVertexDao.deleteByGraphId(graphId);

        for (ScenarioGraphVertex vertex : vertexes) {
            Task task = vertex.getTask();
            ScenarioServiceAssert.nonExistsTask(task == null);
            taskConfigDao.deleteByTaskId(task.getTaskId());
            taskDao.delete(task);

        }

        return createScenarioGraph(scenarioGraphDAG, isResolve);
    }

    /**
     * 根据id查询某个图
     */
    public ScenarioGraphDAG findScenarioByScenarioId(String scenarioId) {
        ScenarioServiceAssert.nonExistsGraph(Strings.isNullOrEmpty(scenarioId), scenarioId);
        return this.findScenarioByScenarioId(scenarioId, Locale.getDefault().getLanguage());
    }

    @Override
    public ScenarioGraphDAG findScenarioByScenarioId(String scenarioId, String language) {
        ScenarioServiceAssert.nonExistsGraph(Strings.isNullOrEmpty(scenarioId), scenarioId);
        Iterable<ScenarioGraphDAG> iterable = scenarioGraphDagDao.findByScenarioId(scenarioId);
        ScenarioGraphDAG dag = null;

        if (iterable.iterator().hasNext()) {
            dag = iterable.iterator().next();
            for (ScenarioGraphVertex vertex : dag.getGraphVertexs()) {
                Task task = vertex.getTask();
                ScenarioServiceAssert.nonExistsTask(task == null);
                List<TaskConfig> taskConfigs = task.getTaskConfigs();
                if (!taskConfigs.isEmpty()) {
                    Iterable<ComponentConfig> elements =
                            componentService.findComponentConfigs(task.getRelationId(), language);
                    List<ComponentConfig> list = Lists.newArrayList(elements);
                    for (TaskConfig taskConfig : taskConfigs) {
                        StreamSupport.stream(list)
                                .filter(c -> c.getParamId().equals(taskConfig.getParamId()))
                                .findFirst()
                                .ifPresent(taskConfig::setComponentConfig);

                    }
                }

                List<TaskConfig> sortedByOrderId = StreamSupport.stream(taskConfigs)
                        .sorted((o1, o2) -> {
                            if (o1 == null || o1.getComponentConfig() == null ||
                                    o2 == null || o2.getComponentConfig() == null) {
                                return 0;
                            } else {
                                return o1.getComponentConfig().getOrderId() - o2.getComponentConfig().getOrderId();
                            }
                        }).collect(Collectors.toList());

                task.setTaskConfigs(sortedByOrderId);
            }
        }

        return dag;
    }

    public ScenarioGraphDAG
    findScenarioByScenarioIdAndByVersionId(String scenarioId, String versionId) {
        ScenarioServiceAssert.nonExistsGraph(Strings.isNullOrEmpty(scenarioId), scenarioId);
        return scenarioGraphDagDao.findByScenarioIdAndVersionId(scenarioId, versionId);

    }

    @Override
    public ScenarioGraphDAG createScenarioGraph(ScenarioGraphDAG scenarioGraphDAG)
            throws ScenarioServiceException {
        return createScenarioGraph(scenarioGraphDAG, true);
    }

    @Override
    public ScenarioGraphDAG createScenarioGraph(ScenarioGraphDAG scenarioGraphDAG, boolean isResolve)
            throws ScenarioServiceException {
        if (isResolve) {
            isCycleDependency(scenarioGraphDAG);
        }

        String graphId = scenarioGraphDAG.getGraphId();
        String scenarioId = scenarioGraphDAG.getScenarioId();
        String versionId = scenarioGraphDAG.getVersionId();

        if (graphId == null) {
            graphId = UUID.randomUUID().toString();
            if (versionId == null) {
                versionId = scenarioVersionService.createVersionByScenarioId(scenarioId).getVersionId();
            }

            scenarioGraphDAG.setCreateTime(new Date());

        } else {
            if (versionId == null) {
                versionId = scenarioVersionService.findByScenarioId(scenarioId).getVersionId();
            }

            scenarioGraphDAG.setModifiedTime(new Date());

        }

        scenarioGraphDAG.setGraphId(graphId);
        scenarioGraphDAG.setVersionId(versionId);

        List<ScenarioGraphEdge> edges = scenarioGraphDAG.getGraphEdges();
        for (int i = 0; i < edges.size(); i++) {
            ScenarioGraphEdge edge = edges.get(i);

            String edgeId = UUID.randomUUID().toString();

            edge.setEdgeId(edgeId);
            edge.setOrderId(i);
            edge.setCreateTime(new Date());
            edge.setGraphId(graphId);
        }

        Iterable<ScenarioGraphVertex> vertexs = scenarioGraphDAG.getGraphVertexs();
        for (ScenarioGraphVertex vertex : vertexs) {
            String vertexId = UUID.randomUUID().toString();

            vertex.setVertexId(vertexId);
            vertex.setCreateTime(new Date());
            vertex.setGraphId(graphId);

            Task task = vertex.getTask();
            ScenarioServiceAssert.nonExistsTask(task == null);
            String taskId = UUID.randomUUID().toString();

            task.setTaskId(taskId);
            task.setCreateTime(new Date());
            task.setVersionId(versionId);

            Iterable<TaskConfig> configs = task.getTaskConfigs();
            for (TaskConfig config : configs) {
                String configId = UUID.randomUUID().toString();

                config.setConfigId(configId);
                config.setCreateTime(new Date());
                config.setTaskId(taskId);
            }
            taskConfigDao.save(configs);
            taskDao.save(task);
        }

        scenarioGraphVertexDao.save(vertexs);
        scenarioGraphEdgeDao.save(edges);
        scenarioGraphDagDao.save(scenarioGraphDAG);

        return scenarioGraphDAG;
    }

    private void isCycleDependency(ScenarioGraphDAG graphDAG) {
        ScenarioServiceAssert.nonExistsGraph(graphDAG == null);
        List<String> visited = Lists.newArrayList(graphDAG.getScenarioId());

        resolveEmbeddedFlows(graphDAG, visited);

    }

    private void resolveEmbeddedFlows(ScenarioGraphDAG graphDAG, List<String> visited) {
        if (graphDAG == null) {
            return;
        }

        for (ScenarioGraphVertex vertex : graphDAG.getGraphVertexs()) {
            Task task = vertex.getTask();
            ScenarioServiceAssert.nonExistsTask(task == null);
            if (task.getTaskType().equals("scenario")) {
                String relationId = task.getRelationId();
                resolveEmbeddedFlow(relationId, visited);
            }
        }
    }

    private void resolveEmbeddedFlow(String relationId, List<String> visited) {
        if (visited.contains(relationId)) {
            visited.add(relationId);

            int index = visited.indexOf(relationId);

            String args = StreamSupport.stream(visited)
                    .skip(index)
                    .map(s -> scenarioDao.findOne(s).getScenarioName())
                    .reduce((s1, s2) -> s1 + " -> " + s2)
                    .orElse("");

            ScenarioServiceAssert.isCycleDependency(true, args);
        }

        visited.add(relationId);

        ScenarioGraphDAG embeddedGraph = findScenarioByScenarioId(relationId);

        resolveEmbeddedFlows(embeddedGraph, visited);
    }

}
