package com.chinasofti.ark.bdadp.dao.scenario;

import com.chinasofti.ark.bdadp.entity.scenario.ScenarioGraphDAG;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by wgzhang on 2016/8/28.
 */
public interface ScenarioGraphDagDao extends CrudRepository<ScenarioGraphDAG, String> {

    Iterable<ScenarioGraphDAG> findByScenarioId(String scenarioId);

    ScenarioGraphDAG findByScenarioIdAndVersionId(String scenarioId, String versionId);

    @Query("select s.graphRaw from com.chinasofti.ark.bdadp.entity.scenario.ScenarioGraphDAG s where   s.scenarioId = :scenarioId ")
    String findGraphRawByScenarioId(@Param(value = "scenarioId") String scenarioId);
}
