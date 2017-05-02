package com.chinasofti.ark.bdadp.service.components.impl;

import com.chinasofti.ark.bdadp.dao.components.ComponentConfigsDao;
import com.chinasofti.ark.bdadp.entity.components.ComponentConfig;
import com.chinasofti.ark.bdadp.service.components.ComponentConfigsService;
import com.chinasofti.ark.bdadp.util.common.BeanHelper;
import com.chinasofti.ark.bdadp.util.common.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/29.
 */
@Component
@Transactional
public class ComponentConfigsServiceImpl implements ComponentConfigsService {

    @Autowired
    private ComponentConfigsDao dao;

    @Override
    public Iterable<ComponentConfig> findComponentConfigs(String ComponentId) {
        return dao.findByComponentIdOrderByOrderId(ComponentId);
    }

    @Override
    public ComponentConfig create(ComponentConfig componentConfig) {
        Assert.hasText(componentConfig.getParamName(),
                "param name is null, please check your component configuration.");

        componentConfig.setParamId(UUID.getId());

        return dao.save(componentConfig);
    }

    @Override
    public Iterable<ComponentConfig> create(Iterable<ComponentConfig> iterable) {
        List<ComponentConfig> componentConfigs = new ArrayList<>();
        for (ComponentConfig componentConfig : iterable) {
            componentConfigs.add(create(componentConfig));
        }

        return componentConfigs;
    }

    @Override
    public void delete(String id) {
        dao.delete(id);
    }

    @Override
    public ComponentConfig findOne(String id) {
        return dao.findOne(id);
    }

    @Override
    public Iterable<ComponentConfig> findAll() {
        return dao.findAll();
    }

    @Override
    public ComponentConfig update(ComponentConfig componentConfig) {
        ComponentConfig target = dao.findOneByParamName(componentConfig.getParamName());
        if (target == null) {
            return create(componentConfig);
        } else {
            BeanHelper.mergeProperties(componentConfig, target);
        }

        return target;
    }

    @Override
    public Iterable<ComponentConfig> update(Iterable<ComponentConfig> iterable) {
        List<ComponentConfig> componentConfigs = new ArrayList<>();
        for (ComponentConfig componentConfig : iterable) {
            componentConfigs.add(update(componentConfig));
        }

        return componentConfigs;
    }
}
