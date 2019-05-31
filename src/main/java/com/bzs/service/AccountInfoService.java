package com.bzs.service;
import com.alibaba.fastjson.JSONObject;
import com.bzs.model.AccountInfo;
import com.bzs.utils.Result;
import com.bzs.utils.Service;
import io.swagger.annotations.ApiOperation;

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
     * @param isOwner 是否包括自己在内
     * @param type 默认0父节点1子节点
     * @return
     */
    Result getParentOrChildList(String id, Integer deep,String isOwner,String type);
}
