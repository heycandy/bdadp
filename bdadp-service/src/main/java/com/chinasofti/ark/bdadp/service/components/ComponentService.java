package com.chinasofti.ark.bdadp.service.components;

import com.chinasofti.ark.bdadp.entity.components.Component;
import com.chinasofti.ark.bdadp.entity.components.ComponentConfig;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by White on 2016/09/14.
 */
public interface ComponentService {

    void setInitProperties(Properties props);

    Iterable<Component> getComponentsBase(String language);

    Iterable<Component> getComponentsBusiness(String language);

    Component findComponentById(String id);

    Iterable<ComponentConfig> findComponentConfigs(String componentId);

    Iterable<ComponentConfig> findComponentConfigs(String componentId, String language);

    InputStream getComponentIcon(String id, String param);

    String getComponentIconAsString(String id, String param);

    Class<? extends Component> loadComponentClass(String id);

    Iterable<String> getJarFiles();

}
