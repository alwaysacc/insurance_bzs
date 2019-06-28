package com.bzs.dao;

import com.alibaba.fastjson.JSONObject;
import com.bzs.model.AccountInfo;
import com.bzs.model.query.SeveralAccount;
import com.bzs.utils.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authc.Account;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

public interface AccountInfoMapper extends Mapper<AccountInfo> {
    String getRoleIdByAccountId(String account_id);
    /**
     * 根据用户名和密码查询对应的用户
     */
    List getUserInfo(@Param("username") String username, @Param("password") String password);

    List<AccountInfo> getAccountAndThridAccount(AccountInfo accountInfo);

    /**
     * 插入或更新
     *
     * @param accountInfo
     * @return
     */
    int addOrUpdate(AccountInfo accountInfo);

    List getUserList(@Param("roleId") String roleId, @Param("accountId") String accountId);

    /**
     * 获取父节点
     *
     * @param id
     * @param deep
     * @param isOwner
     * @return
     */
    List<AccountInfo> getParentList(@Param("id") String id, @Param("deep") Integer deep, @Param("isOwner") String isOwner);

    /**
     * 获取子节点
     *
     * @param id
     * @param deep
     * @param isOwner
     * @return
     */
    List<AccountInfo> getChildList(@Param("id") String id, @Param("deep") Integer deep, @Param("isOwner") String isOwner);

    List<AccountInfo> getParentOrChildList(@Param("id") String id, @Param("deep") Integer deep, @Param("isOwner") String isOwner, @Param("type") String type,
                                           @Param("accountState")int accountState
                                           );

    HashSet getAllCode();

    /**
     * 获取父级信息  2级
     * @param createBy
     * @return
     */
    SeveralAccount getParentLevel(@Param("createBy") String createBy);

    /**
     * 修改余额和佣金或者提成
     * @param balanceTotal
     * @param commissionTotal
     * @param drawPercentageTotal
     * @param accountId
     * @return
     */
    int updateMoney(@Param("balanceTotal") BigDecimal balanceTotal,@Param("commissionTotal") BigDecimal commissionTotal,@Param("drawPercentageTotal") BigDecimal drawPercentageTotal,@Param("accountId") String accountId);

    AccountInfo getWithdraw(String accountId);

   int deleteUser(@Param("accountId")String[] accountId,@Param("status")int status);

    AccountInfo login(String loginName);

    int updateAccountStat(@Param("accountId")String accountId,@Param("status")int status);

    List getUserListByAdmin(@Param("userName")String userName);

    int updateAccount(AccountInfo accountInfo);

    HashSet getUserLoginName();

    List<AccountInfo> getUserNameAndId();
    // 获取今日登陆的用户数量
    int getTodayLoginCount();
}