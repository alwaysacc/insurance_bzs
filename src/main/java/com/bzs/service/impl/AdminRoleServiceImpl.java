package com.bzs.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.bzs.dao.AdminRoleMapper;
import com.bzs.model.AdminRole;
import com.bzs.model.AdminRoleMenu;
import com.bzs.service.AdminRoleService;
import com.bzs.utils.AbstractService;
import com.google.gson.JsonArray;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by dl on 2019/06/18.
 */
@Service
@Transactional
public class AdminRoleServiceImpl extends AbstractService<AdminRole> implements AdminRoleService {
    @Resource
    private AdminRoleMapper adminRoleMapper;

    @Override
    public int addRoleAndMenu(AdminRole adminRole, String menuId) {
        adminRoleMapper.addAdminRole(adminRole);
        Long roleId= adminRole.getId();
        List<AdminRoleMenu> list=new ArrayList<>();
        AdminRoleMenu adminRoleMenu;
        List<String> list1= JSONArray.parseArray(menuId).toJavaList(String.class);
        for (int i = 0; i <list1.size() ; i++) {
            adminRoleMenu=new AdminRoleMenu();
            adminRoleMenu.setMenuId(Long.valueOf(list1.get(i)));
            adminRoleMenu.setRoleId(roleId);
            list.add(adminRoleMenu);
        }
        return adminRoleMapper.addAdminRoleMenu(list);
    }

    @Override
    public void deleteRole(Long roleId) {
        adminRoleMapper.deleteByIds(String.valueOf(roleId));
        adminRoleMapper.deleteMenuByRoleId(roleId);
    }
}

