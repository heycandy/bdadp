package com.chinasofti.ark.bdadp.dao.scenario;

import com.chinasofti.ark.bdadp.entity.scenario.ScenarioCategoryDetail;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by White on 2016/08/27.
 */
public interface ScenarioCategoryDetailDao extends CrudRepository<ScenarioCategoryDetail, String> {

    public void deleteByCateId(String cateId);

    public void deleteByScenarioId(String scenarioId);

    public void deleteByCateIdAndScenarioId(String cateId, String scenarioId);
}
