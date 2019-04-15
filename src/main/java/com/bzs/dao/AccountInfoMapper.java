package com.bzs.dao;

import com.alibaba.fastjson.JSONObject;
import com.bzs.model.AccountInfo;
import com.bzs.utils.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccountInfoMapper extends Mapper<AccountInfo> {
    String getRoleIdByAccountId(String account_id);
    /**
     * 根据用户名和密码查询对应的用户
     */
    List getUserInfo(@Param("username") String username, @Param("password") String password);
}