package com.bzs.service.impl;

import com.bzs.dao.AdminMapper;
import com.bzs.model.Admin;
import com.bzs.service.AdminService;
import com.bzs.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;


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
}
