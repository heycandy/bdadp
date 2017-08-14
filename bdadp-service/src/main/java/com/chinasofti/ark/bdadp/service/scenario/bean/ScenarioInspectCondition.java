package com.chinasofti.ark.bdadp.service.scenario.bean;

import java.util.List;

/**
 * Created by White on 2017/5/25.
 */
public class ScenarioInspectCondition {


  private String userName;

  private List<Integer> selected;

  public List<Integer> getSelected() {
    return selected;
  }

  public void setSelected(List<Integer> selected) {
    this.selected = selected;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

}
