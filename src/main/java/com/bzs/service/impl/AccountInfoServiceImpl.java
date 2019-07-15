package com.bzs.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bzs.cache.RedisAnnotation;
import com.bzs.dao.*;
import com.bzs.model.*;
import com.bzs.model.query.SeveralAccount;
import com.bzs.redis.RedisUtil;
import com.bzs.service.AccountInfoService;
import com.bzs.service.AccountRoleInfoService;
import com.bzs.service.IdCardImgService;
import com.bzs.service.VerificationService;
import com.bzs.utils.*;
import com.bzs.utils.base64Util.Base64Util;
import com.bzs.utils.juheUtil.JuHeHttpUtil;
import com.bzs.utils.redisConstant.RedisConstant;
import com.bzs.utils.stringUtil.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import com.bzs.model.query.QueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;


/**
 * Created by alwaysacc on 2019/04/11.
 */
@Slf4j
@Service
@Transactional
public class AccountInfoServiceImpl extends AbstractService<AccountInfo> implements AccountInfoService {
    private static Logger logger = LoggerFactory.getLogger(AccountInfoServiceImpl.class);
    @Resource
    private AccountInfoMapper accountInfoMapper;
    @Resource
    private AccountRoleInfoMapper accountRoleInfoMapper;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private RedisUtil redisUtil;
    @Resource
    private VerificationService verificationService;
    @Resource
    private OrderInfoMapper orderInfoMapper;
    @Resource
    private QuoteInfoMapper quoteInfoMapper;
    @Resource
    private IdCardImgService idCardImgService;
    private static final String CODE = "CODE_LIST";

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
                return ResultGenerator.gen("不存在,获取失败", "", ResultCode.FAIL);
            }
        } catch (Exception e) {
            logger.error("获取异常", e);
            return ResultGenerator.gen("获取异常", "", ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Result insertOrUpdate(AccountInfo accountInfo, String type) {
        if (null != accountInfo) {
            String msg = "";
            if (StringUtils.isNotBlank(type)) {
                if ("0".equals(type)) {
                    msg = "添加";
                } else if ("1".equals(type)) {
                    msg = "修改";
                }
            }
            try {
                int result = accountInfoMapper.addOrUpdate(accountInfo);
                return ResultGenerator.genSuccessResult(result, msg + "成功");
            } catch (Exception e) {
                logger.error("插入或者更新异常", e);
                return ResultGenerator.genFailResult(msg + "异常");
            }

        } else {
            return ResultGenerator.genFailResult("参数为空");
        }
    }

    @Override
    public List getUserList(String roleId, String accountId) {
        return accountInfoMapper.getUserList(roleId, accountId);
    }

    @Override
    public Map registerForWX(AccountInfo accountInfo) {
        AccountInfo accountInfo1 = accountInfoService.findBy("invitecode", accountInfo.getSuperiorinvitecode());
        HashSet codeList = new HashSet<>();
        int code = UUIDS.getCode();
        if (redisUtil.hasKey(CODE)) {
            codeList = (HashSet) redisUtil.get(CODE);
        } else {
            codeList = accountInfoMapper.getAllCode();
        }
        boolean b = false;
        do {
            code = UUIDS.getCode();
            b = codeList.contains(code);
        } while (b);
        codeList.add(code);
        redisUtil.set(CODE, codeList, 720000);
        System.out.println(codeList.toString());
        Integer pInvitecode = accountInfo1.getInvitecode();
        String pAssociation = accountInfo1.getAssociationLevel();
        if (StringUtils.isNotBlank(pAssociation)) {
            accountInfo.setAssociationLevel(pAssociation + "-" + code);
            int count = StringUtil.getCharacterCount(pAssociation, "-") + 2;
            accountInfo.setInviteCodeLevel(count);
        } else {
            accountInfo.setAssociationLevel(pInvitecode + "-" + code);
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
        HashSet set;
        if (!redisUtil.hasKey(RedisConstant.USER_LOGIN_NAME_LIST)) {
            log.info("用户账号存入redis");
            set = accountInfoMapper.getUserLoginName();
            redisUtil.set(RedisConstant.USER_LOGIN_NAME_LIST, set, 720000);
        } else {
            log.info("从redis取出用户账号");
            set = (HashSet) redisUtil.get(RedisConstant.USER_LOGIN_NAME_LIST);
            set.add(accountInfo.getLoginName());
            redisUtil.set(RedisConstant.USER_LOGIN_NAME_LIST, set, 720000);
        }
        Map map = new HashMap();
        map.put("superior", accountInfo1);
        map.put("account", accountInfo);
        return map;
    }

    @Override
    public Result getParentOrChildList(String id, Integer deep, String isOwner, String type, int accountState) {
        if (null == deep || deep < 1) {
            deep = 1;
        }
        if (StringUtils.isNotBlank(id)) {
            if (StringUtils.isBlank(type) || !"1".equals(type)) {
                type = "0";//0子节点1父节点
            }
            if (StringUtils.isBlank(isOwner) || !"1".equals(isOwner)) {
                isOwner = "0";//0包括 1排除
            }
            List<AccountInfo> list = accountInfoMapper.getParentOrChildList(id, deep, isOwner, type, accountState);
            if (CollectionUtils.isNotEmpty(list)) {
                return ResultGenerator.genSuccessResult(list, "成功");
            }
            return ResultGenerator.genFailResult("不存在，获取信息失败");
        } else {
            return ResultGenerator.genFailResult("参数错误");
        }
    }

    @Override
    public Result getParentLevel(String createBy) {
        if (StringUtils.isBlank(createBy)) {
            return ResultGenerator.genFailResult("参数错误");
        }
        SeveralAccount data = accountInfoMapper.getParentLevel(createBy);
        if (null != data) {
            return ResultGenerator.genSuccessResult(data, "成功");
        }
        return ResultGenerator.genFailResult("获取失败");
    }

    @Override
    public Result updateMoney(BigDecimal balanceTotal, BigDecimal commissionTotal, BigDecimal drawPercentageTotal, String accountId, Verification verification) {
        try {
            accountInfoMapper.updateMoney(balanceTotal, commissionTotal, drawPercentageTotal, accountId);
            verificationService.save(verification);
            return ResultGenerator.genSuccessResult("修改成功");
        } catch (Exception e) {
            return ResultGenerator.genFailResult("修改异常");
        }
    }

    @Override
    public Result addOrUpdateAccountForMananger(AccountInfo accountInfo) {
        if (null != accountInfo) {
            String accountId = accountInfo.getAccountId();
            String msg = "";
            int code = -1;
            if (StringUtils.isNotBlank(accountId)) {//修改
                msg = "修改";
                code = 0;
            } else {//添加
                msg = "添加";
                HashSet codeList = new HashSet<>();
                int codes = UUIDS.getCode();
                if (redisUtil.hasKey(CODE)) {
                    codeList = (HashSet) redisUtil.get(CODE);
                } else {
                    codeList = accountInfoMapper.getAllCode();
                }
                boolean b = false;
                do {
                    codes = UUIDS.getCode();
                    b = codeList.contains(codes);
                } while (b);
                codeList.add(codes);
                redisUtil.set(CODE, codeList, 720000);
                accountInfo.setAssociationLevel(codes + "");
                accountInfo.setInvitecode(codes);
                accountInfo.setInviteCodeLevel(1);
                code = 1;
                accountId = UUIDS.getDateUUID();
            }
            String loginPwd = accountInfo.getLoginPwd();
            if (StringUtils.isNotBlank(loginPwd)) {
                loginPwd = MD5Utils.encrypt(accountInfo.getLoginName().toLowerCase(), accountInfo.getLoginPwd());
                accountInfo.setLoginPwd(loginPwd);
            }
            try {
                int result = accountInfoMapper.addOrUpdate(accountInfo);
                if (code == 1) {
                    String roleIds = accountInfo.getRoleId();//存储的角色id
                    if (StringUtils.isNotBlank(roleIds)) {
                        String[] ids = roleIds.split(",");
                        for (String id : ids) {
                            accountRoleInfoMapper.insert(new AccountRoleInfo(Integer.valueOf(id), accountId, accountId));
                        }
                    }
                    return ResultGenerator.genSuccessResult(result, "添加成功");
                } else if (code == 0) {
                    Example example = new Condition(AccountRoleInfo.class);
                    Example.Criteria criteria = example.createCriteria();
                    criteria.andCondition("account_id=", accountId);
                    accountRoleInfoMapper.deleteByCondition(example);
                    String roleIds = accountInfo.getRoleIds();
                    if (StringUtils.isNotBlank(roleIds)) {
                        String[] ids = roleIds.split(",");
                        for (String id : ids) {
                            accountRoleInfoMapper.insert(new AccountRoleInfo(Integer.valueOf(id), accountId, accountId));
                        }
                    }
                    return ResultGenerator.genSuccessResult(result, "修改成功");
                } else {
                    return ResultGenerator.genFailResult(msg + "失败");
                }
            } catch (Exception e) {
                return ResultGenerator.genFailResult(msg + "异常");
            }
        } else {
            return ResultGenerator.genFailResult("参数异常");
        }
    }

    @Override
    public AccountInfo getWithdraw(String accountId) {
        return accountInfoMapper.getWithdraw(accountId);
    }


    @Override
    public int deleteUser(String[] accountId, int status) {
        return accountInfoMapper.deleteUser(accountId, status);
    }

    @Override
    public AccountInfo findByName(String username) {
        AccountInfo param = new AccountInfo();
        param.setUserName(username);
        List<AccountInfo> list = findUserDetail(param, new QueryRequest());
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<AccountInfo> findUserDetail(AccountInfo user, QueryRequest request) {
        try {
            return this.accountInfoMapper.select(user);
        } catch (Exception e) {
            logger.error("查询用户异常", e);
            return new ArrayList<>();
        }
    }

    @Override
    public List getUserListByAdmin(String userName, String mobile) {
        return accountInfoMapper.getUserListByAdmin(userName, mobile);
    }

    @Override
    public int updateAccount(AccountInfo accountInfo) {
        if (StringUtils.isNotBlank(accountInfo.getLoginPwd())) {
            accountInfo.setLoginPwd(MD5Utils.encrypt(accountInfo.getLoginName().toLowerCase(), accountInfo.getLoginPwd()));
        }
        return accountInfoMapper.updateAccount(accountInfo);
    }

    @RedisAnnotation(key = RedisConstant.USER_LOGIN_NAME_LIST, time = 3600)
    @Override
    public HashSet checkUserLoginName() {
        return accountInfoMapper.getUserLoginName();
    }

    @RedisAnnotation(key = RedisConstant.USER_MOILE, time = 3600)
    @Override
    public HashSet checkUserMobile() {
        return accountInfoMapper.checkUserMobile();
    }

    @Override
    public List<AccountInfo> getUserNameAndId() {
        /*List<AccountInfo> list;
        if (!redisUtil.hasKey(RedisConstant.USER_NAME_LIST)){
            log.info("用户账号存入redis");
            list=accountInfoMapper.getUserNameAndId();
            redisUtil.set(RedisConstant.USER_NAME_LIST,list,720000);
        }else{
            log.info("从redis取出用户账号");
            list= (List<AccountInfo>) redisUtil.get(RedisConstant.USER_NAME_LIST);
        }*/
        return accountInfoMapper.getUserNameAndId();
    }

    @RedisAnnotation(key = RedisConstant.HOME_MAP, time = 3600)
    @Override
    public HashMap getHomeInfo() {
        HashMap map;
        AccountInfo accountInfo = null;
        int userCount = accountInfoMapper.selectCount(accountInfo);
        OrderInfo orderInfo = null;
        int orderCount = orderInfoMapper.selectCount(orderInfo);
        QuoteInfo quoteInfo = null;
        int quoteCount = quoteInfoMapper.selectCount(quoteInfo);
        int todayCount = accountInfoMapper.getTodayLoginCount();
        map = new HashMap();
        map.put("userCount", userCount);
        map.put("orderCount", orderCount);
        map.put("quoteCount", quoteCount);
        map.put("todayCount", todayCount);
        return map;
    }

    /**
     * @param file
     * @param type      0头像 1国徽
     * @param accountId
     * @return
     */
    @Override
    public Result accountVerified(MultipartFile file, int type, String accountId) {
        String fileName = accountId + "-" + type;
//        Random random = new Random();
//        fileName=fileName+random.nextInt(10000);
        String path = QiniuCloudUtil.put64image(file, fileName);
        IdCardImg idCardImg = new IdCardImg();
        if (type == 0) {
            idCardImg.setFrontPath(path);
        } else {
            idCardImg.setBackPath(path);
        }
        idCardImg.setAccountId(accountId);
        idCardImgService.saveIdCardImg(idCardImg);
        AccountInfo accountInfo=new AccountInfo();
        accountInfo.setAccountId(accountId);
        accountInfo.setVerifiedStat(1);
        accountInfoService.update(accountInfo);
        return ResultGenerator.genSuccessResult();
    }

    @Override
    public int updatePassWord(AccountInfo accountInfo) {
        accountInfo.setLoginPwd(MD5Utils.encrypt(accountInfo.getLoginName().toLowerCase(), accountInfo.getLoginPwd()));
        return accountInfoMapper.updatePassWord(accountInfo);
    }
}
