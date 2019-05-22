package com.bzs.service.impl;

import com.bzs.dao.AccountInfoMapper;
import com.bzs.model.AccountInfo;
import com.bzs.service.AccountInfoService;
import com.bzs.utils.AbstractService;
import com.bzs.utils.Result;
import com.bzs.utils.ResultCode;
import com.bzs.utils.ResultGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by alwaysacc on 2019/04/11.
 */
@Service
@Transactional
public class AccountInfoServiceImpl extends AbstractService<AccountInfo> implements AccountInfoService {
  private  static Logger logger=LoggerFactory.getLogger(AccountInfoServiceImpl.class);
    @Resource
    private AccountInfoMapper accountInfoMapper;
    @Autowired
    private AccountInfoService accountInfoService;

    @Override
    public String getRoleIdByAccountId(String account_id) {
        return accountInfoMapper.getRoleIdByAccountId(account_id);
    }

    @Override
    public List getUserInfo(String username, String password) {
        return accountInfoMapper.getUserInfo(username, password);
    }

    @Override
    public void updateLoginTime(String userName) {
        Example example = new Example(AccountInfo.class);
        example.createCriteria().andCondition("lower(login_name)=", userName.toLowerCase());
        AccountInfo accountInfo = accountInfoService.findByLoginName(userName.toLowerCase());
        accountInfo.setLoginTime(new Date());
        accountInfoMapper.updateByCondition(accountInfo, example);
    }

    @Override
    public AccountInfo findByLoginName(String userName) {
        return accountInfoService.findBy("loginName", userName);
    }

    @Override
    public Map<String, Object> getUserInfo(AccountInfo accountInfo) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("user", accountInfo);
        return userInfo;
    }

    @Override
    public Result getAccountAndThridAccount(String accountId) {
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setAccountId(accountId);
        try {
            List<AccountInfo> list = accountInfoMapper.getAccountAndThridAccount(accountInfo);
            if (CollectionUtils.isNotEmpty(list)) {
                return ResultGenerator.genSuccessResult(list, "获取成功");
            } else {
                return ResultGenerator.gen("不存在,获取失败","",ResultCode.FAIL);
            }
        } catch (Exception e) {
            logger.error("获取异常",e);
            return ResultGenerator.gen("获取异常","",ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Result insertOrUpdate(AccountInfo accountInfo,String type) {
        if(null!=accountInfo){
            String  msg="";
            if(StringUtils.isNotBlank(type)){
                if("0".equals(type)){
                    msg="添加";
                }else if("1".equals(type)){
                    msg="修改";
                }
            }
            try{
                int result= accountInfoMapper.addOrUpdate(accountInfo);
                return ResultGenerator.genSuccessResult(result,msg+"成功");
            }catch(Exception e){
                logger.error("插入或者更新异常",e);
                return ResultGenerator.genFailResult(msg+"异常");
            }

        }else{
            return ResultGenerator.genFailResult("参数为空");
        }
    }

    @Override
    public List getUserList(String roleId, String accountId) {
        return accountInfoMapper.getUserList(roleId,accountId);
    }
}
