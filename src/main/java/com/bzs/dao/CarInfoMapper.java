package com.bzs.dao;

import com.bzs.model.CarInfo;
import com.bzs.utils.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CarInfoMapper extends Mapper<CarInfo> {
    List getUserList(@Param("accountId") String accountId, @Param("roleId") String roleId);
    List findOneBy(CarInfo carInfo);
}