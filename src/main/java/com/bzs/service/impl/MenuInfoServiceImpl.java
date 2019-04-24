package com.bzs.service.impl;

import com.bzs.dao.MenuInfoMapper;
import com.bzs.model.MenuInfo;
import com.bzs.model.router.RouterMeta;
import com.bzs.model.router.VueRouter;
import com.bzs.service.MenuInfoService;
import com.bzs.utils.AbstractService;
import com.bzs.utils.TreeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by alwaysacc on 2019/04/15.
 */
@Service
@Transactional
public class MenuInfoServiceImpl extends AbstractService<MenuInfo> implements MenuInfoService {
    @Resource
    private MenuInfoMapper menuInfoMapper;

    @Override
    public List<MenuInfo> getUserPermissions(String username) {
        return menuInfoMapper.getUserPermissions(username);
    }

    @Override
    public ArrayList<VueRouter<MenuInfo>> getUserMenu(String username) {
        List<VueRouter<MenuInfo>> routes = new ArrayList<>();
        List<MenuInfo> menus = this.menuInfoMapper.getUserMenu(username);
        menus.forEach(menu -> {
            VueRouter<MenuInfo> route = new VueRouter<>();
            route.setId(menu.getMenuId().toString());
            route.setParentId(menu.getParentId().toString());
            route.setIcon(menu.getIcon());
            route.setPath(menu.getPath());
            route.setComponent(menu.getComponent());
            route.setName(menu.getMenuName());
            route.setMeta(new RouterMeta(true, null));
            routes.add(route);
        });
        return TreeUtil.buildVueRouter(routes);
    }
}
