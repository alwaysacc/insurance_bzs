package com.bzs.service;
import com.bzs.model.Admin;
import com.bzs.utils.Service;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * Created by dl on 2019/06/17.
 */
public interface AdminService extends Service<Admin> {
    int updateLoginTime( Date loginTime, String loginName);

}
