package com.bzs.dao;

import com.bzs.model.Role;
import com.bzs.utils.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper extends Mapper<Role> {

    /**
     * 根据账号获取角色
     * @param accountId
     * @return
     */
    List<Role> findUserRoleByAccountId(@Param("accountId")String accountId);
}