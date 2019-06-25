package com.bzs.service;
import com.bzs.model.AccountInfo;
import com.bzs.model.Verification;
import com.bzs.utils.Result;
import com.bzs.utils.Service;
import com.bzs.model.query.QueryRequest;

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

    Result updateMoney(BigDecimal balanceTotal, BigDecimal commissionTotal, BigDecimal drawPercentageTotal, String accountId, Verification verification);

    AccountInfo getWithdraw(String accountId);

    int deleteUser(String[] accountId,int status);

    /**
     * 账号管理添加或者修改
     * @param accountInfo
     * @return
     */
    Result addOrUpdateAccountForMananger(AccountInfo accountInfo);

    List getUserListByAdmin();

    int updateAccount(AccountInfo accountInfo);

    boolean checkUserLoginName(String loginName);


    /**
     * 通过用户名查找用户
     *
     * @param username username
     * @return user
     */
    AccountInfo findByName(String username);

    /**
     * 通过用户 id 查找用户
     *
     * @param userId userId
     * @return user
     */

    /**
     * 查询用户详情，包括基本信息，用户角色，用户部门
     *
     * @param user    user
     * @param request QueryRequest
     * @return List<User>
     */
    List<AccountInfo> findUserDetail(AccountInfo user, QueryRequest request);

    List<AccountInfo> getUserNameAndId();
}
