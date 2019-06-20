package com.bzs.dao;

import com.bzs.model.AdminMenu;
import com.bzs.utils.Mapper;

import java.util.List;

public interface AdminMenuMapper extends Mapper<AdminMenu> {
    List<AdminMenu> getMenuByAdminName(String adminName);

    List<AdminMenu> getMenuList( );

    List<Long> getMenuIdByRoleId(Long roleId);
}