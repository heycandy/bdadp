package com.chinasofti.ark.bdadp.dao.scenario;

import com.chinasofti.ark.bdadp.entity.scenario.ScenarioCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by White on 2016/08/27.
 */
public interface ScenarioCategoryDao extends CrudRepository<ScenarioCategory, String> {

    public Iterable<ScenarioCategory> findByCateStatusNotOrderByCreateTimeDesc(int cateStatus);

    @Query("select u from ScenarioCategory u order by u.createTime")
    Iterable<ScenarioCategory> findAllOrderByCreateTime();
}
