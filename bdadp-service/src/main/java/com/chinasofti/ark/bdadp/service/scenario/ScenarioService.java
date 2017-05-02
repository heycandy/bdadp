package com.chinasofti.ark.bdadp.service.scenario;

import com.chinasofti.ark.bdadp.entity.scenario.Scenario;

import java.util.List;

/**
 * 场景service
 */
public interface ScenarioService {

    /**
     * 场景新增
     */
    public Scenario addScenario(Scenario scenario);

    /**
     * 场景所有查询
     */
    public List<Scenario> findAllScenario() throws Exception;

    /**
     * 场景某个查询
     */
    public Scenario findScenarioById(String id);

    /**
     * 场景删除
     */
    public void delScenario(String id) throws Exception;

    /**
     * 场景删除
     */
    public int delBatchScenario(List<String> scenarioIds) throws Exception;

    /**
     * 批量添加场景分类
     */
    public void addBatchCategor(String cateId, List<String> scenarioIds);

    /**
     * 批量移除场景分类
     */
    public void removeBatchCategor(String cateId, List<String> scenarioIds);

    public Scenario updateScenario(Scenario scenario) throws Exception;
}
