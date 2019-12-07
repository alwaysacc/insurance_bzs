package com.bzs.service;
import com.bzs.model.Admins;
import com.bzs.utils.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;


/**
 * Created by dl on 2019/06/17.
 */
public interface AdminService extends Service<Admins> {
    int updateLoginTime( Date loginTime, String loginName);

    List getAdminList(String adminName);

    int updateAdmin(Admins admin);

    HashSet checkAdminLoginName();
}
