package com.chinasofti.ark.bdadp.service.user.impl;

import com.chinasofti.ark.bdadp.dao.user.UserGroupDao;
import com.chinasofti.ark.bdadp.entity.user.UserGroup;
import com.chinasofti.ark.bdadp.service.user.UserGroupService;
import com.chinasofti.ark.bdadp.service.user.UserService;
import com.chinasofti.ark.bdadp.util.common.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Created by White on 2016/08/31.
 */
@Service
public class UserGroupServiceImpl implements UserGroupService {

    @Autowired
    private UserGroupDao dao;

    @Autowired
    private UserService service;

    @Override
    public UserGroup create(UserGroup s) {
        s.setGroupId(UUID.randomUUID().toString());
        s.setCreateTime(new Date());

//    if (!s.getUsers().isEmpty()) {
//      service.create(s.getUsers());
//    }

        return dao.save(s);
    }

    @Override
    public Iterable<UserGroup> create(Iterable<UserGroup> iterable) {
        for (UserGroup group : iterable) {
            group.setGroupId(UUID.randomUUID().toString());
            group.setCreateTime(new Date());

            if (!group.getUsers().isEmpty()) {
                service.create(group.getUsers());
            }
        }

        return dao.save(iterable);
    }

    @Override
    public void delete(String id) {
        dao.delete(id);
    }

    @Override
    public UserGroup findOne(String id) {
        return dao.findOne(id);
    }

    @Override
    public Iterable<UserGroup> findAll() {
        return dao.findAll();
    }

    @Override
    public UserGroup update(UserGroup s) {
        s.setModifiedTime(new Date());

//    if (!s.getUsers().isEmpty()) {
//      service.update(s.getUsers());
//    }

        return dao.save(s);
    }

    @Override
    public Iterable<UserGroup> update(Iterable<UserGroup> iterable) {
        for (UserGroup group : iterable) {
            if (StringUtils.isEmpty(group.getGroupId())) {
                group.setGroupId(UUID.randomUUID().toString());
                group.setCreateTime(new Date());
            } else {
                group.setModifiedTime(new Date());
            }

            if (!group.getUsers().isEmpty()) {
                service.update(group.getUsers());
            }
        }

        return dao.save(iterable);
    }
}
