package com.chinasofti.ark.bdadp.service.scenario.impl;

import com.chinasofti.ark.bdadp.dao.scenario.ScenarioCategoryDao;
import com.chinasofti.ark.bdadp.dao.scenario.ScenarioCategoryDetailDao;
import com.chinasofti.ark.bdadp.dao.scenario.ScenarioDao;
import com.chinasofti.ark.bdadp.dao.scenario.ScenarioVersionDao;
import com.chinasofti.ark.bdadp.entity.scenario.Scenario;
import com.chinasofti.ark.bdadp.entity.scenario.ScenarioCategoryDetail;
import com.chinasofti.ark.bdadp.entity.scenario.ScenarioVersion;
import com.chinasofti.ark.bdadp.service.scenario.ScenarioService;
import com.chinasofti.ark.bdadp.service.scenario.bean.ScenarioStatus;
import com.chinasofti.ark.bdadp.util.common.BeanHelper;
import com.chinasofti.ark.bdadp.util.common.ListUtils;
import com.chinasofti.ark.bdadp.util.common.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 场景service实现类
 */
@Service
@Transactional
public class ScenarioServiceImpl implements ScenarioService {

    @Autowired
    private ScenarioDao scenarioDao;

    @Autowired
    private ScenarioVersionDao scenarioVersionDao;

    @Autowired
    private ScenarioCategoryDao categoryDao;

    @Autowired
    private ScenarioCategoryDetailDao categoryDetailDao;

    /**
     * 记录日志
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 新增场景
     */
    public Scenario addScenario(Scenario scenario) {

        String scenarioId = UUID.randomUUID().toString();
        scenario.setScenarioId(scenarioId);
        scenario.setCreateTime(new Date());

        ScenarioVersion scenarioVersion = new ScenarioVersion();
        scenarioVersion.setVersionId(UUID.randomUUID().toString());
        scenarioVersion.setCreateTime(new Date());
        scenarioVersion.setVersionLabel(UUID.randomUUID().toString());
        scenarioVersion.setScenarioId(scenarioId);

        scenarioVersionDao.save(scenarioVersion);

        return scenarioDao.save(scenario);
    }

    /**
     * 查询所有场景
     */
    public List<Scenario> findAllScenario() throws Exception {
        List<Scenario> list = new ArrayList<Scenario>();
        try {
            Iterable<Scenario> myIterator = scenarioDao
                    .findByScenarioStatusNotOrderByCreateTimeDesc(ScenarioStatus.DELETE.ordinal());
            if (myIterator.iterator().hasNext()) {
                list = ListUtils.convertIterToList(myIterator.iterator());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询所有场景service" + e);
        }
        return list;
    }

    /**
     * 查询某个场景
     */
    public Scenario findScenarioById(String scenario_id) {
        return scenarioDao.findOne(scenario_id);
    }

    /**
     * 删除场景
     */
    public void delScenario(String scenarioId) throws Exception {
        try {
            scenarioDao.findOne(scenarioId).setScenarioStatus(ScenarioStatus.DELETE.ordinal());
            categoryDetailDao.deleteByScenarioId(scenarioId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除场景service" + e);
        }
    }

    /**
     * 批量删除场景
     */
    public int delBatchScenario(List<String> scenarioIds) throws Exception {
        int resultCode = 0;
        resultCode = isOnline(scenarioIds);
        if (resultCode == 1) {
            return resultCode;
        }
        try {
            for (String scenarioId : scenarioIds) {
                delScenario(scenarioId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("批量删除场景service" + e);
        }
        return resultCode;
    }

    public int isOnline(List<String> scenarioIds) {
        int resultCode = 0;
        for (String scenarioId : scenarioIds) {
            Scenario scenario = scenarioDao.findOne(scenarioId);
            if (scenario.getScenarioStatus() == 3) {
                resultCode = 1;
                return 1;
            }
        }
        return resultCode;
    }

    /**
     * 批量添加场景分类
     */
    public void addBatchCategor(String cateId, List<String> scenarioIds) {
        try {
            for (String scenarioId : scenarioIds) {
                ScenarioCategoryDetail scenarioCategoryDetail = new ScenarioCategoryDetail();
                scenarioCategoryDetail.setDetailId(UUID.randomUUID().toString());
                scenarioCategoryDetail.setCreateTime(new Date());
                scenarioCategoryDetail.setScenarioId(scenarioId);
                scenarioCategoryDetail.setCateId(cateId);
                categoryDetailDao.save(scenarioCategoryDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("批量添加场景分类service" + e);
        }
    }

    /**
     * 批量移除场景分类
     */
    public void removeBatchCategor(String cateId, List<String> scenarioIds) {
        try {
            for (String scenarioId : scenarioIds) {
                categoryDetailDao.deleteByCateIdAndScenarioId(cateId, scenarioId);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("批量移除场景分类service" + e);
        }
    }

    @Override
    public Scenario updateScenario(Scenario scenario) throws Exception {
        Scenario target = scenarioDao.findOne(scenario.getScenarioId());
        target.setModifiedTime(new Date());

        BeanHelper.mergeProperties(scenario, target);

        return target;
    }

}
