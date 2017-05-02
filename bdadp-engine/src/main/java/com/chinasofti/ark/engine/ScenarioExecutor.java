package com.chinasofti.ark.engine;

import com.chinasofti.ark.bdadp.flow.TaskFlow;
import com.chinasofti.ark.bdadp.flow.TaskFlowBuilder;

public class ScenarioExecutor {

  private final String id;
  private final ExecutorConfig conf;

  ScenarioExecutor(String id, ExecutorConfig conf) {
    this.id = id;
    this.conf = conf;
  }

  String getId() {
    return this.id;
  }

  ExecutorConfig getConf() {
    return this.conf;
  }

  void notify(ScenarioExecution execution) {
    execute(execution);
  }

  private void execute(ScenarioExecution execution) {
    try {
      TaskFlow flow = TaskFlowBuilder.buildFlow(execution.getSceId());
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }


}
