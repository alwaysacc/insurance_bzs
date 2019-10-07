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
import java.util.Collection;
import java.util.List;


/**
 * Created by dl on 2019/06/18.
 */
@Service
@Transactional
public class AdminRoleServiceImpl extends AbstractService<AdminRole> implements AdminRoleService {
    @Resource
    private AdminRoleMapper adminRoleMapper;
    @Resource
    private AdminRoleService adminRoleService;

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

    @Override
    public void updateRoleAndMenu(AdminRole adminRole, String menuId, String beforeMenuId) {
        List<String> list= JSONArray.parseArray(menuId).toJavaList(String.class);
        List<String> beforeList= JSONArray.parseArray(beforeMenuId).toJavaList(String.class);
        List<AdminRoleMenu> adminRoleMenuList=new ArrayList<>();
        Long roleId= adminRole.getId();
        AdminRoleMenu adminRoleMenu;
        List<String> saveList=new ArrayList<String>(list);
        List<String> deleteList=new ArrayList<String>(beforeList);
        saveList.removeAll(beforeList);
        deleteList.removeAll(list);
        for (int i = 0; i <saveList.size() ; i++) {
            adminRoleMenu=new AdminRoleMenu();
            adminRoleMenu.setMenuId(Long.valueOf(saveList.get(i)));
            adminRoleMenu.setRoleId(roleId);
            adminRoleMenuList.add(adminRoleMenu);
        }
        if (adminRoleMenuList.size()!=0){
            adminRoleMapper.addAdminRoleMenu(adminRoleMenuList);
        }
        String[] array=deleteList.toArray(new String[deleteList.size()]);
        if (array.length!=0){
            adminRoleMapper.deleteMenuByMenuId(roleId,array);
        }
        adminRoleService.update(adminRole);
    }
}


