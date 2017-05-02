package com.chinasofti.ark.bdadp.service.scenario;

import com.chinasofti.ark.bdadp.entity.scenario.ScenarioUserVariable;

/**
 * Created by White on 2016/09/12.
 */
public interface ScenarioVariableService {

    ScenarioUserVariable create(ScenarioUserVariable s);

    Iterable<Object> findByScenarioId(String scenarioId);

    ScenarioUserVariable update(ScenarioUserVariable s);

    void delete(String id);

    String parseVariable(String inputVariable, String scenarioId);
}
