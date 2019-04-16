package com.bzs.service.impl;

import com.bzs.dao.AccountInfoMapper;
import com.bzs.model.AccountInfo;
import com.bzs.service.AccountInfoService;
import com.bzs.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


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

    @Override
    public List getUserInfo(String username, String password) {
        return accountInfoMapper.getUserInfo(username,password);
    }

    @Override
    public void updateLoginTime(String userName) {
        Example example=new Example(AccountInfo.class);
        example.createCriteria().andCondition("lower(login_name)=", userName.toLowerCase());
        AccountInfo accountInfo=new AccountInfo();
        accountInfo.setLoginTime(new Date());
        accountInfoMapper.updateByCondition(accountInfo,example);
    }

    @Override
    public AccountInfo findByLoginName(String userName) {
        Condition condition=new Condition(AccountInfo.class);
        condition.createCriteria().andCondition("lower(login_name)="+userName);
        return (AccountInfo) accountInfoMapper.selectByCondition(condition);
    }
}
