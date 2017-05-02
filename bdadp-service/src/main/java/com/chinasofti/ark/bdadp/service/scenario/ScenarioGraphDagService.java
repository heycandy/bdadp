package com.chinasofti.ark.bdadp.service.scenario;

import com.chinasofti.ark.bdadp.entity.scenario.ScenarioGraphDAG;
import com.chinasofti.ark.bdadp.service.scenario.bean.ScenarioServiceException;

/**
 * Created by wgzhang on 2016/8/28.
 */
public interface ScenarioGraphDagService {

    ScenarioGraphDAG createScenarioGraph(ScenarioGraphDAG scenarioGraphDAG)
            throws ScenarioServiceException;

    ScenarioGraphDAG createScenarioGraph(ScenarioGraphDAG scenarioGraphDAG, boolean isResolve)
            throws ScenarioServiceException;

    ScenarioGraphDAG updateScenarioGraph(ScenarioGraphDAG scenarioGraphDAG)
            throws ScenarioServiceException;

    ScenarioGraphDAG updateScenarioGraph(ScenarioGraphDAG scenarioGraphDAG, boolean isResolve)
            throws ScenarioServiceException;

    ScenarioGraphDAG findScenarioByScenarioId(String scenarioId);

    ScenarioGraphDAG findScenarioByScenarioId(String scenarioId, String language);

    ScenarioGraphDAG findScenarioByScenarioIdAndByVersionId(String scenarioId, String versionId);

}
