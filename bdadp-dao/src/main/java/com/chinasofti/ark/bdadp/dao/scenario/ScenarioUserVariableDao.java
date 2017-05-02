package com.chinasofti.ark.bdadp.dao.scenario;

import com.chinasofti.ark.bdadp.entity.scenario.ScenarioUserVariable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author : water
 * @Date : 20160925
 * @Desc : TODO
 * @version: V1.0
 */
public interface ScenarioUserVariableDao extends JpaRepository<ScenarioUserVariable, String> {

    List<ScenarioUserVariable> findByScenarioId(String scenarioId);
}
