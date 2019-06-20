package com.bzs.service;
import com.bzs.model.Admin;
import com.bzs.utils.Service;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


/**
 * Created by dl on 2019/06/17.
 */
public interface AdminService extends Service<Admin> {
    int updateLoginTime( Date loginTime, String loginName);

    List getAdminList();

    int updateAdmin(Admin admin);
}
