package com.chinasofti.ark.bdadp.dao.user;

import com.chinasofti.ark.bdadp.entity.user.Permission;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by White on 2016/08/30.
 */
public interface PermissionDao extends CrudRepository<Permission, String> {

}
