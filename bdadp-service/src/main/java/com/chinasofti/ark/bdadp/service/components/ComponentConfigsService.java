package com.chinasofti.ark.bdadp.service.components;

import com.chinasofti.ark.bdadp.entity.components.ComponentConfig;
import com.chinasofti.ark.bdadp.service.DefaultService;

/**
 * Created by Administrator on 2016/8/29.
 */
public interface ComponentConfigsService extends DefaultService<ComponentConfig, String> {

    public Iterable<ComponentConfig> findComponentConfigs(String ComponentId);

}
