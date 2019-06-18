package com.bzs.service;
import com.bzs.model.AdminMenu;
import com.bzs.model.router.VueRouter;
import com.bzs.utils.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by dl on 2019/06/18.
 */
public interface AdminMenuService extends Service<AdminMenu> {
    List<VueRouter<AdminMenu>> getMenuByAdminName(String adminName);
}
