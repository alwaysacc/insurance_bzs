package com.bzs.service.impl;

import com.bzs.dao.AccountInfoMapper;
import com.bzs.model.AccountInfo;
import com.bzs.service.AccountInfoService;
import com.bzs.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by alwaysacc on 2019/04/11.
 */
@Service
@Transactional
public class AccountInfoServiceImpl extends AbstractService<AccountInfo> implements AccountInfoService {
    @Resource
    private AccountInfoMapper accountInfoMapper;

    @Override
    public String getRoleIdByAccountId(String account_id) {
        return accountInfoMapper.getRoleIdByAccountId(account_id);
    }
}
