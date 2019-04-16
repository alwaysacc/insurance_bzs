package com.bzs.service.impl;

import com.bzs.dao.RoleInfoMapper;
import com.bzs.model.RoleInfo;
import com.bzs.service.RoleInfoService;
import com.bzs.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by alwaysacc on 2019/04/15.
 */
@Service
@Transactional
public class RoleInfoServiceImpl extends AbstractService<RoleInfo> implements RoleInfoService {
    @Resource
    private RoleInfoMapper roleInfoMapper;

    @Override
    public List<RoleInfo> getUserRole(String userName) {
        return roleInfoMapper.getUserRole(userName);
    }
}
