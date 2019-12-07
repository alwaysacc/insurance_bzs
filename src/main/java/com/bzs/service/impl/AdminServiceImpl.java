package com.bzs.service.impl;

import com.bzs.cache.RedisAnnotation;
import com.bzs.dao.AdminMapper;
import com.bzs.model.Admins;
import com.bzs.redis.RedisUtil;
import com.bzs.service.AdminService;
import com.bzs.utils.AbstractService;
import com.bzs.utils.MD5Utils;
import com.bzs.utils.redisConstant.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashSet;
import java.util.List;


/**
 * Created by dl on 2019/06/17.
 */
@Slf4j
@Service
@Transactional
public class AdminServiceImpl extends AbstractService<Admins> implements AdminService {
    @Resource
    private AdminMapper adminMapper;
    @Resource
    private RedisUtil redisUtil;
    @Override
    public int updateLoginTime(Date loginTime, String loginName) {
        loginTime=new Date();
        return adminMapper.updateLoginTime(loginTime,loginName);
    }

    @Override
    public List getAdminList(String adminName) {
        return adminMapper.getAdminList(adminName);
    }

    @Override
    public int updateAdmin(Admins admin) {
        if (StringUtils.isNotBlank(admin.getLoginPwd())){
            admin.setLoginPwd(MD5Utils.encrypt(admin.getLoginName().toLowerCase(),admin.getLoginPwd()));
        }
        return adminMapper.updateAdmin(admin);
    }
    @RedisAnnotation(key =RedisConstant.ADMIN_LOGIN_NAME_LIST,time=3600 )
    @Override
    public HashSet checkAdminLoginName() {
        return  adminMapper.getAdminLoginName();
    }


}
