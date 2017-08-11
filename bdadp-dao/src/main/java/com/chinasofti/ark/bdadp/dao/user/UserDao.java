package com.chinasofti.ark.bdadp.dao.user;

import com.chinasofti.ark.bdadp.entity.user.Role;
import com.chinasofti.ark.bdadp.entity.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by White on 2016/08/30.
 */
public interface UserDao extends JpaRepository<User, String> {

  User findOneByUserNameAndUserPwd(String userName, String userPwd);

  List<User> findAllByRoles(Role role);

  User findAllByUserName(String userName);

//  @Query(value = "select u.userDesc from com.chinasofti.ark.bdadp.entity.user.User u  where u.roles in (:roles)")
//  List<String> findUserDescByRoleId(@Param(value = "roles") List<Role> roles);
}
