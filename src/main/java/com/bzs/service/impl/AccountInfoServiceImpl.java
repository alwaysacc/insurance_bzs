package com.bzs.service.impl;

import com.bzs.dao.AccountInfoMapper;
import com.bzs.model.AccountInfo;
import com.bzs.model.Verification;
import com.bzs.model.query.SeveralAccount;
import com.bzs.redis.RedisUtil;
import com.bzs.service.AccountInfoService;
import com.bzs.service.VerificationService;
import com.bzs.utils.*;
import com.bzs.utils.stringUtil.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;


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
    @Autowired
    private RedisUtil redisUtil;
    @Resource
    private VerificationService verificationService;
    private  static final String CODE="CODE_LIST";
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
        return accountInfoMapper.login(userName);
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

    @Override
    public Map registerForWX(AccountInfo accountInfo) {
        AccountInfo accountInfo1=accountInfoService.findBy("invitecode",accountInfo.getSuperiorinvitecode());
        HashSet codeList=new HashSet<>();
        int code= UUIDS.getCode();
        if (redisUtil.hasKey(CODE)){
            codeList= (HashSet) redisUtil.get(CODE);
        }else{
            codeList=accountInfoMapper.getAllCode();
        }
        boolean b=false;
        do{
            code=UUIDS.getCode();
            b=codeList.contains(code);
        }while (b);
        codeList.add(code);
        redisUtil.set(CODE,codeList,720000);
        System.out.println(codeList.toString());
        Integer pInvitecode= accountInfo1.getInvitecode();
        String pAssociation=accountInfo1.getAssociationLevel();
        if(StringUtils.isNotBlank(pAssociation)){
            accountInfo.setAssociationLevel(pAssociation+"-"+code);
            int count=StringUtil.getCharacterCount(pAssociation,"-")+2;
            accountInfo.setInviteCodeLevel(count);
        }else{
            accountInfo.setAssociationLevel(pInvitecode+"-"+code);
            accountInfo.setInviteCodeLevel(2);
        }
        accountInfo.setInvitecode(code);
        accountInfo.setAccountId(UUIDS.getUUID());
        accountInfo.setRoleId("3");
        accountInfo.setAccountState(2);
        accountInfo.setRoleName("业务员");
        accountInfo.setLoginPwd(MD5Utils.encrypt(accountInfo.getLoginName().toLowerCase(), accountInfo.getLoginPwd()));
        accountInfo.setParentId(accountInfo1.getAccountId());
        accountInfo.setSuperior(accountInfo1.getUserName());
        accountInfoService.save(accountInfo);
        Map map=new HashMap();
        map.put("superior",accountInfo1);
        map.put("account",accountInfo);
        return map;
    }
    @Override
    public Result getParentOrChildList(String id, Integer deep, String isOwner,String type,int accountState) {
        if(null==deep||deep<1){
            deep=1;
        }
        if(StringUtils.isNotBlank(id)){
            if(StringUtils.isBlank(type)||!"1".equals(type)){
                type="0";//0子节点1父节点
            }
            if(StringUtils.isBlank(isOwner)||!"1".equals(isOwner)){
                isOwner="0";//0包括 1排除
            }
            List<AccountInfo>list= accountInfoMapper.getParentOrChildList(id,deep,isOwner,type,accountState);
            if(CollectionUtils.isNotEmpty(list)){
                return ResultGenerator.genSuccessResult(list,"成功");
            }
            return ResultGenerator.genFailResult("不存在，获取信息失败");
        }else{
            return ResultGenerator.genFailResult("参数错误");
        }
    }

    @Override
    public Result getParentLevel(String createBy) {
        if(StringUtils.isBlank(createBy)){
            return ResultGenerator.genFailResult("参数错误");
        }
       SeveralAccount data= accountInfoMapper.getParentLevel(createBy);
       if(null!=data){
           return ResultGenerator.genSuccessResult(data,"成功");
       }
        return ResultGenerator.genFailResult("获取失败");
    }

    @Override
    public Result updateMoney(BigDecimal balanceTotal, BigDecimal commissionTotal, BigDecimal drawPercentageTotal, String accountId, Verification verification) {
        try {
            accountInfoMapper.updateMoney(balanceTotal, commissionTotal, drawPercentageTotal, accountId);
            verificationService.save(verification);
            return ResultGenerator.genSuccessResult("修改成功");
        }catch (Exception e){
            return ResultGenerator.genFailResult("修改异常");
        }


    }
    @Override
    public AccountInfo getWithdraw(String accountId) {
      return   accountInfoMapper.getWithdraw(accountId);
    }


    @Override
    public int deleteUser(String[] accountId, int status) {
        return accountInfoMapper.deleteUser(accountId,status);
    }

}
