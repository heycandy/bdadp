package com.chinasofti.ark.engine;

import com.chinasofti.ark.bdadp.entity.scenario.Scenario;
import com.chinasofti.ark.bdadp.service.scenario.ScenarioService;
import com.chinasofti.ark.bdadp.util.common.UUID;
import com.chinasofti.ark.bdadp.util.date.DateTimeUtil;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Orchestrator {

  private static ScenarioService scenarioService;

  private static Map<String, ScenarioExecutor> pool =
      new ConcurrentHashMap<String, ScenarioExecutor>();

  static ScenarioExecution runScenario(String id) {
    Scenario scenario;
    try {
      scenario = scenarioService.findScenarioById(id);
    } catch (Exception e) {
      return null;
    }
    if (scenario == null) {
      return null;
    }
    ScenarioExecutor executor = orchestrate(getStrategy());
    if (executor == null) {
      throw new RuntimeException(
          "Can not get available executor, please check the system environement.");
    }
    ScenarioExecution execution = ExecutionPool.put(buildScenarioExecution(scenario));
    // TODO: notify the executor with ansync mode
    int cpuNum = Runtime.getRuntime().availableProcessors();
    final int POOL_SIZE = 2;
    ExecutorService executorService = Executors.newFixedThreadPool(cpuNum * POOL_SIZE);
    executorService.execute(new Runnable() {

      @Override
      public void run() {
        executor.notify(execution);
      }

    });

    return execution;
  }

  private static ScenarioExecutor orchestrate(OrchestrateStrategy strategy) {
    // TODO: update for distributed system in the future and use strategy
    ScenarioExecutor executor = new ScenarioExecutor(UUID.getId(), new ExecutorConfig());
    return pool.put(executor.getId(), executor);
  }

  private static OrchestrateStrategy getStrategy() {
    return OrchestrateStrategy.DEFAULT;
  }

  private static ScenarioExecution buildScenarioExecution(Scenario scenario) {
    return new ScenarioExecution(UUID.getId(), scenario.getScenarioId(), scenario.getScenarioName(),
        DateTimeUtil.format(new Date(System.currentTimeMillis())));
  }

}
