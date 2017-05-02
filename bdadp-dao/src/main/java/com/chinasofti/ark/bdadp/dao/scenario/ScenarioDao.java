package com.chinasofti.ark.bdadp.dao.scenario;

import com.chinasofti.ark.bdadp.entity.scenario.Scenario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScenarioDao extends CrudRepository<Scenario, String> {

    public Iterable<Scenario> findByScenarioStatusNotOrderByCreateTimeDesc(int scenarioStatus);

    @Query("select s.scenarioId from com.chinasofti.ark.bdadp.entity.scenario.Scenario s where  s.scenarioName like %:scenarioName% ")
    List<String> findByScenarioNameLike(@Param(value = "scenarioName") String scenarioName);

}
