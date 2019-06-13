package com.bzs.dao;

import com.alibaba.fastjson.JSONObject;
import com.bzs.model.AccountInfo;
import com.bzs.utils.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authc.Account;

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

}