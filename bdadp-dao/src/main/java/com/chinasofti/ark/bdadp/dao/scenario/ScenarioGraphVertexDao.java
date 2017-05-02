package com.chinasofti.ark.bdadp.dao.scenario;

import com.chinasofti.ark.bdadp.entity.scenario.ScenarioGraphVertex;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by wgzhang on 2016/8/28.
 */
public interface ScenarioGraphVertexDao extends CrudRepository<ScenarioGraphVertex, String> {

    Iterable<ScenarioGraphVertex> findByGraphId(String graphId);

    void deleteByGraphId(String graphId);
}
