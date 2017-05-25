package com.chinasofti.ark.bdadp.service.scenario;

import com.chinasofti.ark.bdadp.component.api.Listener;
import com.chinasofti.ark.bdadp.component.api.options.ScenarioOptions;
import com.chinasofti.ark.bdadp.service.scenario.bean.ScenarioInspectCondition;

/**
 * Created by White on 2016/09/18.
 */
public interface ScenarioExecutorService {

  String inspect(String scenarioId, ScenarioInspectCondition condition) throws Exception;

    String schedule(String scenarioId, String... args) throws Exception;

    String execute(ScenarioOptions options, Class<? extends Listener>... listeners) throws Exception;

    Object execute(String taskId, String executionId) throws Exception;
}
