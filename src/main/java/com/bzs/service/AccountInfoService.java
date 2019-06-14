package com.bzs.service;
import com.bzs.model.AccountInfo;
import com.bzs.utils.Result;
import com.bzs.utils.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * Created by alwaysacc on 2019/04/11.
 */
public interface AccountInfoService extends Service<AccountInfo> {
    String getRoleIdByAccountId(String account_id);
    /**
     * 根据用户名和密码查询对应的用户
     */
    List getUserInfo(String username, String password);

    //修改登录时间
    void updateLoginTime(String userName);

    AccountInfo findByLoginName(String userName);

    Map<String,Object> getUserInfo(AccountInfo accountInfo);

    public Result getAccountAndThridAccount(String accountId);

    /**
     * 插入或更新
     * @param accountInfo
     * @return
     */
    public Result insertOrUpdate(AccountInfo accountInfo,String type);

    List getUserList(String roleId,String accountId);

    Map registerForWX(AccountInfo accountInfo);

    /**
     * 获取父节点或者子节点
     * @param id
     * @param deep 深度
     * @param isOwner 默认0包括自己在内1不包括
     * @param type 默认0子节点1父节点
     * @return
     */
    Result getParentOrChildList(String id, Integer deep,String isOwner,String type,int accountState);

    /**
     * 获取最多三级账号信息（父级）
     * @param createBy
     * @return
     */
    Result getParentLevel(String createBy);

    /**
     * 修改余额和佣金和提成
     * @param balanceTotal
     * @param commissionTotal
     * @param drawPercentageTotal
     * @param accountId
     * @return
     */

    Result updateMoney(BigDecimal balanceTotal, BigDecimal commissionTotal, BigDecimal drawPercentageTotal, String accountId);

    AccountInfo getWithdraw(String accountId);
}
