package com.bzs.dao;

import com.bzs.model.AdminRole;
import com.bzs.utils.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminRoleMapper extends Mapper<AdminRole> {
    int addAdminRoleMenu(List roleMenu);

    long addAdminRole(AdminRole adminRole);

    int deleteMenuByRoleId(Long roleId);

    int deleteMenuByMenuId(@Param("roleId") Long roleId, @Param("mentId") String[] menuId);
}