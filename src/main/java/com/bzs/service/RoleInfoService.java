package com.bzs.service;
import com.bzs.model.RoleInfo;
import com.bzs.utils.Service;

import java.util.List;


/**
 * Created by alwaysacc on 2019/04/15.
 */
public interface RoleInfoService extends Service<RoleInfo> {
    List<RoleInfo> getUserRole(String userName);
}
