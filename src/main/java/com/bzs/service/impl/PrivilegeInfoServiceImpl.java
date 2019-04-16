package com.bzs.service.impl;

import com.bzs.dao.PrivilegeInfoMapper;
import com.bzs.model.PrivilegeInfo;
import com.bzs.service.PrivilegeInfoService;
import com.bzs.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by alwaysacc on 2019/04/15.
 */
@Service
@Transactional
public class PrivilegeInfoServiceImpl extends AbstractService<PrivilegeInfo> implements PrivilegeInfoService {
    @Resource
    private PrivilegeInfoMapper privilegeInfoMapper;

}
