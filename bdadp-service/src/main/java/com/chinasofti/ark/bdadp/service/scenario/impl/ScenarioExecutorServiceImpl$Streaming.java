package com.chinasofti.ark.bdadp.service.scenario.impl;

import com.chinasofti.ark.bdadp.component.api.Listener;
import com.chinasofti.ark.bdadp.component.api.options.ScenarioOptions;
import com.chinasofti.ark.bdadp.entity.scenario.Scenario;
import com.chinasofti.ark.bdadp.service.flow.bean.CallableFlow;
import com.chinasofti.ark.bdadp.service.flow.bean.StreamingCallableFlow;
import com.chinasofti.ark.bdadp.service.graph.bean.Graph;
import com.chinasofti.ark.bdadp.service.scenario.bean.ScenarioServiceAssert;

/**
 * Created by White on 2016/11/23.
 */

public class ScenarioExecutorServiceImpl$Streaming extends ScenarioExecutorServiceImpl$Channel {

  @Override
  public String execute(ScenarioOptions options, Class<? extends Listener>[] listeners)
      throws Exception {
    Scenario scenario = scenarioService.findScenarioById(options.getScenarioId());
    ScenarioServiceAssert.nonExistsScenario(scenario == null, options.getScenarioId());

    options.setScenarioName(scenario.getScenarioName());

    Graph graph = newSimpleGraph(options.getScenarioId(), options, listeners);
    CallableFlow flow = new StreamingCallableFlow(options, graph);

    for (Class<? extends Listener> listenerClass : listeners) {
      flow.addListener(listenerClass.newInstance());
    }

    executorService.submit(flow);

    return options.getExecutionId();
  }
}
