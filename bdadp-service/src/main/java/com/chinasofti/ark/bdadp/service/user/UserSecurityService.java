package com.chinasofti.ark.bdadp.service.user;

import com.chinasofti.ark.bdadp.entity.user.User;

/**
 * Created by White on 2016/09/13.
 */
public interface UserSecurityService {

    String login(User s);

    String logged(User s);

    String logout(User s);
}
