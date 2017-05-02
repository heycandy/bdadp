package com.chinasofti.ark.bdadp.dao.scenario;

import com.chinasofti.ark.bdadp.entity.scenario.ScenarioGraphEdge;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by wgzhang on 2016/8/28.
 */
public interface ScenarioGraphEdgeDao extends CrudRepository<ScenarioGraphEdge, String> {

    void deleteByGraphId(String graphId);
}
