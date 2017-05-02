package com.chinasofti.ark.engine;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExecutionPool {

  private static Map<String, ScenarioExecution> POOL =
      new ConcurrentHashMap<String, ScenarioExecution>();

  public static ScenarioExecution put(ScenarioExecution execution) {
    return POOL.put(execution.getExecId(), execution);
  }

  public static ScenarioExecution get(String execId) {
    return POOL.get(execId);
  }

  public static Collection<ScenarioExecution> getAll() {
    return POOL.values();
  }
}
