package com.bzs.service.impl;

import com.bzs.dao.RoleMapper;
import com.bzs.model.Role;
import com.bzs.service.RoleService;
import com.bzs.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by dl on 2019/06/16.
 */
@Service
@Transactional
public class RoleServiceImpl extends AbstractService<Role> implements RoleService {
    @Resource
    private RoleMapper roleMapper;

}
