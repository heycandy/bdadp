package com.chinasofti.ark.bdadp.dao.scenario;

import com.chinasofti.ark.bdadp.entity.scenario.ScenarioVersion;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by wgzhang on 2016/8/30.
 */
public interface ScenarioVersionDao extends CrudRepository<ScenarioVersion, String> {

    List<ScenarioVersion> findByScenarioIdOrderByCreateTimeDesc(String scenarioId);
}
