package com.bzs.service;
import com.bzs.model.AdminRole;
import com.bzs.utils.Service;


/**
 * Created by dl on 2019/06/18.
 */
public interface AdminRoleService extends Service<AdminRole> {
    int addRoleAndMenu(AdminRole adminRole,String menuId);

    void deleteRole(Long roleId);
}
