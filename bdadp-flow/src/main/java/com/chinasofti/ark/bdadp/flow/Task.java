package com.chinasofti.ark.bdadp.flow;

import java.awt.Component;

public class Task {

  private final String id;
  private final String name;
  private final String desc;
  private final TaskConfig conf;
  private final Component cpnt;

  protected Task(String id, String name, String desc, TaskConfig conf, Component cpnt) {
    this.id = id;
    this.name = name;
    this.desc = desc;
    this.conf = conf;
    this.cpnt = cpnt;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDesc() {
    return desc;
  }

  public TaskConfig getConf() {
    return conf;
  }

  public Component getComponent() {
    return this.cpnt;
  }

}
