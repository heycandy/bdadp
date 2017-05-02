package com.chinasofti.ark.bdadp.dao.components;

import com.chinasofti.ark.bdadp.entity.components.ComponentConfig;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Administrator on 2016/8/29.
 */

public interface ComponentConfigsDao extends CrudRepository<ComponentConfig, String> {

    Iterable<ComponentConfig> findByComponentId(String ComponentId);

    Iterable<ComponentConfig> findByComponentIdOrderByOrderId(String ComponentId);

    ComponentConfig findOneByParamName(String paramName);
}
