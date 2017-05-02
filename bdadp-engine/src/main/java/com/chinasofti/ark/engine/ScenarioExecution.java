package com.chinasofti.ark.engine;

public class ScenarioExecution {

  private final String execId;
  private final String sceId;
  private final String sceName;
  private final String startTime;
  private final ProgressListener listener;


  public ScenarioExecution(String execId, String sceId, String sceName, String startTime) {
    this(execId, sceName, sceId, startTime, new ExecutionListener());
  }

  public ScenarioExecution(String execId, String sceId, String sceName, String startTime,
      ProgressListener listener) {
    this.execId = execId;
    this.startTime = startTime;
    this.sceId = sceId;
    this.sceName = sceName;
    this.listener = listener;
  }

  public String getExecId() {
    return execId;
  }

  public String getSceId() {
    return sceId;
  }

  public String getStartTime() {
    return startTime;
  }

  public String getSceName() {
    return sceName;
  }

  public ProgressListener getListener() {
    return this.listener;
  }

}
