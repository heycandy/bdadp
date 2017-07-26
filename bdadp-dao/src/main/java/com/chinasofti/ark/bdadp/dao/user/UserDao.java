package com.chinasofti.ark.bdadp.dao.user;

import com.chinasofti.ark.bdadp.entity.user.Role;
import com.chinasofti.ark.bdadp.entity.user.User;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by White on 2016/08/30.
 */
public interface UserDao extends CrudRepository<User, String> {

  User findOneByUserNameAndUserPwd(String userName, String userPwd);

  List<User> findAllByRoles(Role role);
}
