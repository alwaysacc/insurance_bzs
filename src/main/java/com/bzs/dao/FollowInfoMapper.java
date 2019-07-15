package com.bzs.dao;

import com.bzs.model.FollowInfo;
import com.bzs.utils.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FollowInfoMapper extends Mapper<FollowInfo> {
    List<FollowInfo> getFollowInfoByCarInfoId(@Param("carInfoId") String carInfoId);
}