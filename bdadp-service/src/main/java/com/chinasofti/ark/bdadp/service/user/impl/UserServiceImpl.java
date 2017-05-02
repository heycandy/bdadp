package com.chinasofti.ark.bdadp.service.user.impl;

import com.chinasofti.ark.bdadp.dao.user.UserDao;
import com.chinasofti.ark.bdadp.entity.user.User;
import com.chinasofti.ark.bdadp.service.user.RoleService;
import com.chinasofti.ark.bdadp.service.user.UserService;
import com.chinasofti.ark.bdadp.util.common.BeanHelper;
import com.chinasofti.ark.bdadp.util.common.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by White on 2016/08/30.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao dao;

    @Autowired
    private RoleService service;

    @Override
    public User create(User s) {
        s.setUserId(UUID.randomUUID().toString());
        s.setCreateTime(new Date());

//    if (!s.getRoles().isEmpty()) {
//      service.create(s.getRoles());
//    }

        return dao.save(s);
    }

    @Override
    public Iterable<User> create(Iterable<User> iterable) {
        for (User user : iterable) {
            if (StringUtils.isEmpty(user.getUserId())) {
                user.setUserId(UUID.randomUUID().toString());
                user.setCreateTime(new Date());
            } else {
                user.setModifiedTime(new Date());
            }

            if (!user.getRoles().isEmpty()) {
                service.update(user.getRoles());
            }
        }

        return dao.save(iterable);
    }

    @Override
    public void delete(String id) {
        dao.delete(id);
    }

    @Override
    public User findOne(String id) {
        return dao.findOne(id);
    }

    @Override
    public Iterable<User> findAll() {
        return dao.findAll();
    }

    @Override
    public User update(User s) {
        s.setModifiedTime(new Date());

        User t = dao.findOne(s.getUserId());
        BeanHelper.mergeProperties(s, t);

//    if (s.getRoles() != null) {
//      service.update(s.getRoles());
//    }

        return t;
    }

    @Override
    public Iterable<User> update(Iterable<User> iterable) {
        List<User> targets = new ArrayList<>();

        for (User user : iterable) {
            user.setModifiedTime(new Date());

            User target = dao.findOne(user.getUserId());
            BeanHelper.mergeProperties(user, target);

            targets.add(target);

            if (user.getRoles() != null) {
                service.update(user.getRoles());
            }
        }

        return targets;
    }

}
