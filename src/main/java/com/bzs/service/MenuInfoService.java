package com.bzs.service;
import com.bzs.model.MenuInfo;
import com.bzs.model.router.VueRouter;
import com.bzs.utils.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by alwaysacc on 2019/04/15.
 */
public interface MenuInfoService extends Service<MenuInfo> {
    List<MenuInfo> getUserPermissions(String username);

    ArrayList<VueRouter<MenuInfo>> getUserMenu(String username);

}
