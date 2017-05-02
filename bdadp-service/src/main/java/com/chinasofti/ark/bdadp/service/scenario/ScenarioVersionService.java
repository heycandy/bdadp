package com.chinasofti.ark.bdadp.service.scenario;

import com.chinasofti.ark.bdadp.entity.scenario.ScenarioVersion;

/**
 * Created by wgzhang on 2016/8/30.
 */
public interface ScenarioVersionService {

    /**
     * 根据场景id查询版本信息
     */
    public ScenarioVersion findByScenarioId(String scenarioId);

    ScenarioVersion createVersionByScenarioId(String scenarioId);
}
