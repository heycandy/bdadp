package com.chinasofti.ark.bdadp.entity.scenario;

import com.chinasofti.ark.bdadp.entity.variable.AbstractEntity;

import javax.persistence.*;
import java.io.Serializable;


@Entity(name = "scenariovariable")
public class ScenarioVariable extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = -1741053527902666614L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variable_id")
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

}
