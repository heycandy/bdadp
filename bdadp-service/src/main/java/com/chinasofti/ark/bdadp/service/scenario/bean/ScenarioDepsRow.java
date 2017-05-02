package com.chinasofti.ark.bdadp.service.scenario.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * Created by White on 2016/11/19.
 */
public class ScenarioDepsRow implements Serializable {

    private String scenarioId;
    private String scenarioName;
    private String scenarioDesc;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String createUser;
    private Collection<ScenarioDepsRow> scenarioDeps;

    public String getScenarioId() {
        return scenarioId;
    }

    public ScenarioDepsRow setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
        return this;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public ScenarioDepsRow setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
        return this;
    }

    public String getScenarioDesc() {
        return scenarioDesc;
    }

    public ScenarioDepsRow setScenarioDesc(String scenarioDesc) {
        this.scenarioDesc = scenarioDesc;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public ScenarioDepsRow setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public ScenarioDepsRow setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public Collection<ScenarioDepsRow> getScenarioDeps() {
        return scenarioDeps;
    }

    public ScenarioDepsRow setScenarioDeps(
            Collection<ScenarioDepsRow> scenarioDeps) {
        this.scenarioDeps = scenarioDeps;
        return this;
    }
}
