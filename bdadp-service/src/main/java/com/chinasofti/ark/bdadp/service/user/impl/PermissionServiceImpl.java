package com.chinasofti.ark.bdadp.service.user.impl;

import com.chinasofti.ark.bdadp.dao.user.PermissionDao;
import com.chinasofti.ark.bdadp.entity.user.Permission;
import com.chinasofti.ark.bdadp.service.user.PermissionService;
import com.chinasofti.ark.bdadp.service.user.ScopeService;
import com.chinasofti.ark.bdadp.util.common.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Created by White on 2016/08/30.
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao dao;

    @Autowired
    private ScopeService service;

    @Override
    public Permission create(Permission s) {
        s.setPermissionId(UUID.randomUUID().toString());
        s.setCreateTime(new Date());

//    if (!s.getScopes().isEmpty()) {
//      service.create(s.getScopes());
//    }

        return dao.save(s);
    }

    @Override
    public Iterable<Permission> create(Iterable<Permission> iterable) {
        for (Permission permission : iterable) {
            permission.setPermissionId(UUID.randomUUID().toString());
            permission.setCreateTime(new Date());

            if (!permission.getScopes().isEmpty()) {
                service.create(permission.getScopes());
            }
        }

        return dao.save(iterable);
    }

    @Override
    public void delete(String id) {
        dao.delete(id);
    }

    @Override
    public Permission findOne(String id) {
        return dao.findOne(id);
    }

    @Override
    public Iterable<Permission> findAll() {
        return dao.findAll();
    }

    @Override
    public Permission update(Permission s) {
        s.setModifiedTime(new Date());

//    if (!s.getScopes().isEmpty()) {
//      service.update(s.getScopes());
//    }

        return dao.save(s);
    }

    public Iterable<Permission> update(Iterable<Permission> iterable) {
        for (Permission permission : iterable) {
            if (StringUtils.isEmpty(permission.getPermissionId())) {
                permission.setPermissionId(UUID.randomUUID().toString());
                permission.setCreateTime(new Date());
            } else {
                permission.setModifiedTime(new Date());
            }

            if (!permission.getScopes().isEmpty()) {
                service.create(permission.getScopes());
            }
        }

        return dao.save(iterable);
    }
}
