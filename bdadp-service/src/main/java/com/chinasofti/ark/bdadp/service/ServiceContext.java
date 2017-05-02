package com.chinasofti.ark.bdadp.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Created by White on 2016/09/14.
 */
@Service
public class ServiceContext implements ApplicationContextAware {

    private static ApplicationContext context;

    public static Object getService(String name) {
        return context.getBean(name);
    }

    public static <T> T getService(Class<T> requireType) {
        return context.getBean(requireType);
    }

    public static <T> T getService(String name, Class<T> requireType) {
        return context.getBean(name, requireType);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ServiceContext.context = applicationContext;
    }
}
