package com.chinasofti.ark.bdadp.controller.scenario.bean;

import java.util.List;

/**
 * Created by wgzhang on 2016/8/30.
 */
public class ScenarioAction {

    private String action;
    private String cate_id;
    private List<String> scenario_id;
    private String name;
    private String resType;
    private String userId;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCate_id() {
        return cate_id;
    }

    public void setCate_id(String cate_id) {
        this.cate_id = cate_id;
    }

    public List<String> getScenario_id() {
        return scenario_id;
    }

    public void setScenario_id(List<String> scenario_id) {
        this.scenario_id = scenario_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResType() {
        return resType;
    }

    public void setResType(String resType) {
        this.resType = resType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
