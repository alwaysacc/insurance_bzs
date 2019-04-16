package com.bzs.service;
import com.alibaba.fastjson.JSONObject;
import com.bzs.model.AccountInfo;
import com.bzs.utils.Service;
import org.apache.ibatis.annotations.Param;

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
}
