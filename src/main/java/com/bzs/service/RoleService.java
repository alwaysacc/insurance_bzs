package com.bzs.service;
import com.bzs.model.Role;
import com.bzs.utils.Service;

import java.util.List;


/**
 * Created by dl on 2019/06/16.
 */
public interface RoleService extends Service<Role> {
    List<Role> findUserRoleByAccountId(String AccountId);
    public List<Role> findAllRole(Role role);
}
