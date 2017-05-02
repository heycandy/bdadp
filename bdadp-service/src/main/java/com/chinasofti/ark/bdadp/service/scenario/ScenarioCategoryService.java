package com.chinasofti.ark.bdadp.service.scenario;

import com.chinasofti.ark.bdadp.entity.scenario.ScenarioCategory;

/**
 * Created by White on 2016/08/27.
 */
public interface ScenarioCategoryService {

    /**
     * 创建场景分类
     */
    public ScenarioCategory createCategory(ScenarioCategory s);

    /**
     * 查询所有场景分类
     */
    public Iterable<ScenarioCategory> getAllCategory();

    /**
     * 删除某个场景分类
     */
    public void delCategory(String id);
}
