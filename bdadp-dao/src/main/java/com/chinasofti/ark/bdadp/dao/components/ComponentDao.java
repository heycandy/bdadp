package com.chinasofti.ark.bdadp.dao.components;

import com.chinasofti.ark.bdadp.entity.components.Component;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Administrator on 2016/8/27.
 */
public interface ComponentDao extends CrudRepository<Component, String> {

    Iterable<Component> findByComponentBaseTrue();

    Iterable<Component> findByComponentBaseTrueAndComponentStatusNot(int componentStatus);

    Iterable<Component> findByComponentBaseFalse();

    Iterable<Component> findByComponentBaseFalseAndComponentStatusNot(int componentStatus);

    Component findOneByComponentName(String componentName);

}

