package com.bzs.dao;

import com.bzs.model.OrderInfo;
import com.bzs.utils.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderInfoMapper extends Mapper<OrderInfo> {
    List getOrderList(@Param("accountId")String accountId,@Param("payStatus")int payStatus);

    List searchOrderList(@Param("accountId")String accountId,@Param("payStatus")int payStatus,
                      @Param("carInfoId")String carInfoId, @Param("postedName")String postedName,
                      @Param("deliveryWay") int deliveryWay,@Param("insuranceCompany") String insuranceCompany
                      );
}