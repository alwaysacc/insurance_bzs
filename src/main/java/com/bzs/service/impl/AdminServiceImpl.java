package com.bzs.service.impl;

import com.bzs.dao.AdminMapper;
import com.bzs.model.Admin;
import com.bzs.service.AdminService;
import com.bzs.utils.AbstractService;
import com.bzs.utils.MD5Utils;
import com.bzs.utils.stringUtil.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.util.StrUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * Created by dl on 2019/06/17.
 */
@Service
@Transactional
public class AdminServiceImpl extends AbstractService<Admin> implements AdminService {
    @Resource
    private AdminMapper adminMapper;

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
}
