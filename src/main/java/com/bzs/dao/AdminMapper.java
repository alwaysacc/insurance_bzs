package com.bzs.dao;

import com.bzs.model.Admin;
import com.bzs.utils.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

public interface AdminMapper extends Mapper<Admin> {
    int updateLoginTime(@Param("loginTime")Date loginTime,@Param("loginName")String loginName);

    List getAdminList();
    int updateAdmin(Admin admin);

    HashSet getAdminLoginName();
}