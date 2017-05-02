package com.chinasofti.ark.bdadp.service.visual.bean;

import com.chinasofti.ark.bdadp.entity.scenario.ScenarioCategory;

import java.util.List;

/**
 * Created by Administrator on 2016/10/28.
 */
public class ScenarioColumnVisual {

    private ScenarioCategory scenarioCategory;
    private List<Integer> value;
    private Double weight;

    public ScenarioColumnVisual(ScenarioCategory scenarioCategory, List<Integer> value,
                                Double weight) {
        this.scenarioCategory = scenarioCategory;
        this.value = value;
        this.weight = weight;
    }

    public ScenarioCategory getScenarioCategory() {
        return scenarioCategory;
    }

    public void setScenarioCategory(ScenarioCategory scenarioCategory) {
        this.scenarioCategory = scenarioCategory;
    }

    public List<Integer> getValue() {
        return value;
    }

    public void setValue(List<Integer> value) {
        this.value = value;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
