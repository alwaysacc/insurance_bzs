package com.bzs.service.impl;

import com.bzs.dao.AccountRoleInfoMapper;
import com.bzs.model.AccountRoleInfo;
import com.bzs.service.AccountRoleInfoService;
import com.bzs.utils.AbstractService;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by dl on 2019/06/16.
 */
@Service
@Transactional
public class AccountRoleInfoServiceImpl extends AbstractService<AccountRoleInfo> implements AccountRoleInfoService {
    @Resource
    private AccountRoleInfoMapper accountRoleInfoMapper;




}
