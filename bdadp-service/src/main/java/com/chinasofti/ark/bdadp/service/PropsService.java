package com.chinasofti.ark.bdadp.service;

import java.util.Properties;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Service;

/**
 * Created by water on 2017.7.25.
 */
@Service
public class PropsService {

  public static Properties getConfigProps() {
    return (Properties) ServiceContext
        .getService(PropertySourcesPlaceholderConfigurer.class).getAppliedPropertySources()
        .get("localProperties").getSource();
  }
}
