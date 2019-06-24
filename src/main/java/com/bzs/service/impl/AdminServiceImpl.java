package com.bzs.service.impl;

import com.bzs.dao.AdminMapper;
import com.bzs.model.Admin;
import com.bzs.redis.RedisUtil;
import com.bzs.service.AdminService;
import com.bzs.utils.AbstractService;
import com.bzs.utils.MD5Utils;
import com.bzs.utils.redisConstant.RedisConstant;
import com.bzs.utils.stringUtil.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.util.StrUtils;
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
public class AdminServiceImpl extends AbstractService<Admin> implements AdminService {
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
    public List getAdminList() {
        return adminMapper.getAdminList();
    }

    @Override
    public int updateAdmin(Admin admin) {
        if (StringUtils.isNotBlank(admin.getLoginPwd())){
            admin.setLoginPwd(MD5Utils.encrypt(admin.getLoginName().toLowerCase(),admin.getLoginPwd()));
        }
        return adminMapper.updateAdmin(admin);
    }
    @Override
    public boolean checkAdminLoginName(String loginName) {
        HashSet set;
        if (!redisUtil.hasKey(RedisConstant.ADMIN_LOGIN_NAME_LIST)){
            log.info("管理员账号存入redis");
            set=adminMapper.getAdminLoginName();
            System.out.println(set.toString());
            redisUtil.set(RedisConstant.ADMIN_LOGIN_NAME_LIST,set,720000);
        }else{
            log.info("从redis取出管理员账号");
            set= (HashSet) redisUtil.get(RedisConstant.ADMIN_LOGIN_NAME_LIST);
            System.out.println(set.toString());
        }
        return  set.contains(loginName);
    }
}
