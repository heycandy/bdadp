package com.chinasofti.ark.bdadp.service.scenario.impl;

import com.chinasofti.ark.bdadp.dao.scenario.ScenarioCategoryDao;
import com.chinasofti.ark.bdadp.dao.scenario.ScenarioCategoryDetailDao;
import com.chinasofti.ark.bdadp.entity.scenario.ScenarioCategory;
import com.chinasofti.ark.bdadp.service.scenario.ScenarioCategoryService;
import com.chinasofti.ark.bdadp.util.common.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by White on 2016/08/27.
 */

@Component
@Transactional
public class ScenarioCategoryServiceImpl implements ScenarioCategoryService {

    @Autowired
    private ScenarioCategoryDao categoryDao;

    @Autowired
    private ScenarioCategoryDetailDao categoryDetailDao;

    /**
     * 记录日志
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 创建场景分类
     */
    @Override
    public ScenarioCategory createCategory(ScenarioCategory s) {
        try {
            s.setCateId(UUID.randomUUID().toString());
            s.setCreateTime(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("创建场景分类service" + e);
        }
        return categoryDao.save(s);
    }

    /**
     * 查询所有场景分类
     */
    @Override
    public Iterable<ScenarioCategory> getAllCategory() {
        return categoryDao.findAllOrderByCreateTime();
    }

    /**
     * 删除某个场景分类
     */
    @Override
    public void delCategory(String id) {
        try {
            categoryDetailDao.deleteByCateId(id);
            categoryDao.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("创建场景分类service" + e);
        }
    }

}
