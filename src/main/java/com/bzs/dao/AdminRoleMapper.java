package com.bzs.dao;

import com.bzs.model.AdminRole;
import com.bzs.utils.Mapper;

import java.util.List;

public interface AdminRoleMapper extends Mapper<AdminRole> {
    int addAdminRoleMenu(List roleMenu);

    long addAdminRole(AdminRole adminRole);

    int deleteMenuByRoleId(Long roleId);
}