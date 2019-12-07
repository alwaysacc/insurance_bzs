package com.bzs.dao;

import com.bzs.model.Admins;
import com.bzs.model.query.AdminAndRole;
import com.bzs.utils.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

public interface AdminMapper extends Mapper<Admins> {
    int updateLoginTime(@Param("loginTime")Date loginTime,@Param("loginName")String loginName);

    List getAdminList(@Param("adminName")String adminName);
    int updateAdmin(Admins admin);

    HashSet getAdminLoginName();

    /**
     * 根据账号获取角色code
     * @param adminId
     * @return
     */
    AdminAndRole adminAndRoleByAdminId(@Param("adminId")Integer adminId);

}