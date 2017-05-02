package com.chinasofti.ark.bdadp.service.user.impl;

import com.chinasofti.ark.bdadp.dao.user.ScopeDao;
import com.chinasofti.ark.bdadp.entity.user.Scope;
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
public class ScopeServiceImpl implements ScopeService {

    @Autowired
    private ScopeDao dao;

    @Override
    public Scope create(Scope s) {
        s.setScopeId(UUID.randomUUID().toString());
        s.setCreateTime(new Date());

        return dao.save(s);
    }

    @Override
    public Iterable<Scope> create(Iterable<Scope> iterable) {
        for (Scope scope : iterable) {
            scope.setScopeId(UUID.randomUUID().toString());
            scope.setCreateTime(new Date());
        }
        return dao.save(iterable);
    }

    @Override
    public void delete(String id) {
        dao.delete(id);
    }

    @Override
    public Scope findOne(String id) {
        return dao.findOne(id);
    }

    @Override
    public Iterable<Scope> findAll() {
        return dao.findAll();
    }

    @Override
    public Scope update(Scope s) {
        s.setModifiedTime(new Date());
        return dao.save(s);
    }

    @Override
    public Iterable<Scope> update(Iterable<Scope> iterable) {
        for (Scope scope : iterable) {
            if (StringUtils.isEmpty(scope.getScopeId())) {
                scope.setScopeId(UUID.randomUUID().toString());
                scope.setCreateTime(new Date());
            } else {
                scope.setModifiedTime(new Date());
            }
        }

        return dao.save(iterable);
    }
}
