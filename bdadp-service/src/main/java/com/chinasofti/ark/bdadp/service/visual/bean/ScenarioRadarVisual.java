package com.chinasofti.ark.bdadp.service.visual.bean;

import com.chinasofti.ark.bdadp.entity.scenario.Scenario;

import java.util.List;

/**
 * Created by White on 2016/10/20.
 */
public class ScenarioRadarVisual {

    private Scenario scenario;
    private List<Integer> value;
    private Double weight;

    public ScenarioRadarVisual() {
    }

    public ScenarioRadarVisual(Scenario scenario, List<Integer> value, Double weight) {
        this.scenario = scenario;
        this.value = value;
        this.weight = weight;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
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
