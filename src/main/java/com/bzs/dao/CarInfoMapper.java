package com.bzs.dao;

import com.bzs.model.CarInfo;
import com.bzs.utils.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CarInfoMapper extends Mapper<CarInfo> {
    //获取客户列表，根据账号id，账号权限
    List getUserList(@Param("accountId") String accountId, @Param("roleId") String roleId,
                     @Param("salesman")String salesman,@Param("customerStatus")String customerStatus);
    //搜索客户
    List searchUserList(
            @Param("accountId") String accountId, @Param("roleId") String roleId,
            @Param("carNumber") String carNumber, @Param("frameNumber") String frameNumber,
            @Param("customerName") String customerName, @Param("customerTel") String customerTel
            );

    int recoverUser(String[] carInfoId);
}