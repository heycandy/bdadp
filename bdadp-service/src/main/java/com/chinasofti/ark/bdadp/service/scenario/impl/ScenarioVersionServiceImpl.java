package com.chinasofti.ark.bdadp.service.scenario.impl;

import com.chinasofti.ark.bdadp.dao.scenario.ScenarioVersionDao;
import com.chinasofti.ark.bdadp.entity.scenario.ScenarioVersion;
import com.chinasofti.ark.bdadp.service.scenario.ScenarioVersionService;
import com.chinasofti.ark.bdadp.util.common.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by wgzhang on 2016/8/30.
 */
@Service
@Transactional
public class ScenarioVersionServiceImpl implements ScenarioVersionService {

    @Autowired
    private ScenarioVersionDao scenarioVersionDao;

    /**
     * 记录日志
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 根据场景id查询版本信息
     */
    public ScenarioVersion findByScenarioId(String scenarioId) {
        ScenarioVersion scenarioVersion = new ScenarioVersion();
        try {
            //定义一个list对象
            List<ScenarioVersion> myList =
                    scenarioVersionDao.findByScenarioIdOrderByCreateTimeDesc(scenarioId);
            if (!myList.isEmpty()) {
                scenarioVersion = myList.get(0);
            }
        } catch (Exception e) {
            logger.error("根据场景id查询版本信息service", e);
        }

        return scenarioVersion;
    }

    @Override
    public ScenarioVersion createVersionByScenarioId(String scenarioId) {
        return scenarioVersionDao.save(new ScenarioVersion(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                new Date(),
                null,
                scenarioId
        ));
    }

}
