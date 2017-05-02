package com.chinasofti.ark.bdadp.util.dto;


/**
 * @Author : water
 * @Date : 2016年9月9日
 * @Desc : TODO
 * @version: V1.0
 */
//@Data
public class ScenarioUserVariableDTO {


    private String variableName;

    private String variableDesc;

    private String variableExpr;

    private String scenarioId;

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

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }


}
