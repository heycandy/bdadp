package com.chinasofti.ark.bdadp.service.user.impl;

import com.chinasofti.ark.bdadp.dao.user.RoleDao;
import com.chinasofti.ark.bdadp.entity.user.Role;
import com.chinasofti.ark.bdadp.service.user.PermissionService;
import com.chinasofti.ark.bdadp.service.user.RoleService;
import com.chinasofti.ark.bdadp.util.common.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Created by White on 2016/08/31.
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao dao;

    @Autowired
    private PermissionService service;

    @Override
    public Role create(Role s) {
        s.setRoleId(UUID.randomUUID().toString());
        s.setCreateTime(new Date());

//    if (!s.getPermissions().isEmpty()) {
//      service.create(s.getPermissions());
//    }

        return dao.save(s);
    }

    @Override
    public Iterable<Role> create(Iterable<Role> iterable) {
        for (Role role : iterable) {
            role.setRoleId(UUID.randomUUID().toString());
            role.setCreateTime(new Date());

            if (!role.getPermissions().isEmpty()) {
                service.create(role.getPermissions());
            }
        }

        return dao.save(iterable);
    }

    @Override
    public void delete(String id) {
        dao.delete(id);
    }

    @Override
    public Role findOne(String id) {
        return dao.findOne(id);
    }

    @Override
    public Iterable<Role> findAll() {
        return dao.findAll();
    }

    @Override
    public Role update(Role s) {
        s.setModifiedTime(new Date());

//    if (!s.getPermissions().isEmpty()) {
//      service.update(s.getPermissions());
//    }
        return dao.save(s);
    }

    @Override
    public Iterable<Role> update(Iterable<Role> iterable) {
        for (Role role : iterable) {
            if (StringUtils.isEmpty(role.getRoleId())) {
                role.setRoleId(UUID.randomUUID().toString());
                role.setCreateTime(new Date());
            } else {
                role.setModifiedTime(new Date());
            }

            if (!role.getPermissions().isEmpty()) {
                service.update(role.getPermissions());
            }
        }

        return dao.save(iterable);
    }
}
