package com.bzs.dao;

import com.bzs.model.MenuInfo;
import com.bzs.utils.Mapper;

import java.util.List;

public interface MenuInfoMapper extends Mapper<MenuInfo> {
    List<MenuInfo> getUserPermissions(String username);

    List<MenuInfo> getUserMenu(String username);
}