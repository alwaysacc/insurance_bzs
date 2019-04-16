package com.bzs.dao;

import com.bzs.model.RoleInfo;
import com.bzs.utils.Mapper;

import java.util.List;

public interface RoleInfoMapper extends Mapper<RoleInfo> {
    List<RoleInfo> getUserRole(String userName);
}