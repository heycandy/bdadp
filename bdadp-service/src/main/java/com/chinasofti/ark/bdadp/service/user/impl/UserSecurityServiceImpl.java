package com.chinasofti.ark.bdadp.service.user.impl;

import com.chinasofti.ark.bdadp.dao.user.UserDao;
import com.chinasofti.ark.bdadp.entity.user.User;
import com.chinasofti.ark.bdadp.service.user.UserSecurityService;
import com.chinasofti.ark.bdadp.util.common.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by White on 2016/09/13.
 */
@Service
public class UserSecurityServiceImpl implements UserSecurityService {

//  private Map<String, String> tokenCache = new ConcurrentHashMap<>();

    @Autowired
    private UserDao dao;


    @Override
    public String login(User s) {
        s.setUserId(UUID.randomUUID().toString());
        s.setCreateTime(new Date());

        User target = dao.findOneByUserNameAndUserPwd(s.getUserName(), s.getUserPwd());
        if (target != null) {
//      String userToken = UUID.randomUUID().toString();

//      tokenCache.put(s.getUserName(), userToken);

            return target.getUserId();
        }

        return null;
    }

    public String logged(User s) {
//    return tokenCache.get(s.getUserName());
        return null;
    }

    @Override
    public String logout(User s) {
//    return tokenCache.remove(s.getUserName());
        return null;
    }
}
