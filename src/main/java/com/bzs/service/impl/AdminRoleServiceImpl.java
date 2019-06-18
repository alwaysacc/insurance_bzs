package com.bzs.service.impl;

import com.bzs.dao.AdminRoleMapper;
import com.bzs.model.AdminRole;
import com.bzs.service.AdminRoleService;
import com.bzs.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by dl on 2019/06/18.
 */
@Service
@Transactional
public class AdminRoleServiceImpl extends AbstractService<AdminRole> implements AdminRoleService {
    @Resource
    private AdminRoleMapper adminRoleMapper;

}
