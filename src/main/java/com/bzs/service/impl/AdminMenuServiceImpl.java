package com.bzs.service.impl;

import com.bzs.dao.AdminMenuMapper;
import com.bzs.model.AdminMenu;
import com.bzs.model.router.RouterMeta;
import com.bzs.model.router.VueRouter;
import com.bzs.service.AdminMenuService;
import com.bzs.utils.AbstractService;
import com.bzs.utils.stringUtil.StringUtil;
import com.bzs.utils.treeUtil.TreeUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by dl on 2019/06/18.
 */
@Service
@Transactional
public class AdminMenuServiceImpl extends AbstractService<AdminMenu> implements AdminMenuService {
    @Resource
    private AdminMenuMapper adminMenuMapper;

    @Override
    public List<VueRouter<AdminMenu>> getMenuByAdminName(String adminName) {
        List<VueRouter<AdminMenu>> routes = new ArrayList<>();
        List<AdminMenu> menus = adminMenuMapper.getMenuByAdminName(adminName);
        menus.forEach(menu -> {
            VueRouter<AdminMenu> route = new VueRouter<>();
            route.setId(menu.getMenuId().toString());
            route.setParentId(menu.getParentId().toString());
            route.setIcon(menu.getIcon());
            route.setPath(menu.getPath());
            route.setComponent(menu.getComponent());
            route.setName(menu.getMenuName());
            route.setMeta(new RouterMeta(menu.getMenuName(), menu.getIcon()));
            routes.add(route);
        });
        return TreeUtil.buildVueRouter(routes);
        //return routes;
    }

    @Override
    public List<VueRouter<AdminMenu>> getMenu() {
        List<VueRouter<AdminMenu>> routes = new ArrayList<>();
        List<AdminMenu> menus = adminMenuMapper.getMenuList();
        menus.forEach(menu -> {
            VueRouter<AdminMenu> route = new VueRouter<>();
            route.setId(menu.getMenuId().toString());
            route.setParentId(menu.getParentId().toString());
            route.setIcon(menu.getIcon());
            route.setPath(menu.getPath());
            route.setComponent(menu.getComponent());
            route.setName(menu.getMenuName());
            route.setMeta(new RouterMeta(menu.getMenuName(), menu.getIcon()));
            routes.add(route);
        });
        return TreeUtil.buildVueRouter(routes);
    }
}
