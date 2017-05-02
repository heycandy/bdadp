package com.chinasofti.ark.bdadp.entity.scenario;

import com.chinasofti.ark.bdadp.entity.variable.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;


@Entity(name = "scenariouservariable")
public class ScenarioUserVariable extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = -1741053527902666614L;
    @Column(name = "modified_user")
    protected String modifiedUser;
    @Column(name = "modified_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date modifiedTime = new Date();
    @Column(name = "scenario_id")
    protected String scenarioId;
    @Id
    @Column(name = "variable_id", nullable = false)
    private String variableId;
    @Column(name = "variable_name")
    private String variableName;
    @Column(name = "variable_desc")
    private String variableDesc;
    @Column(name = "variable_expr")
    private String variableExpr;

    public String getVariableId() {
        return variableId;
    }

    public void setVariableId(String variableId) {
        this.variableId = variableId;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getVariableDesc() {
        return variableDesc;
    }

    public void setVariableDesc(String variableDesc) {
        this.variableDesc = variableDesc;
    }

    public String getVariableExpr() {
        return variableExpr;
    }

    public void setVariableExpr(String variableExpr) {
        this.variableExpr = variableExpr;
    }

    public String getModifiedUser() {
        return modifiedUser;
    }

    public void setModifiedUser(String modifiedUser) {
        this.modifiedUser = modifiedUser;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }


}
