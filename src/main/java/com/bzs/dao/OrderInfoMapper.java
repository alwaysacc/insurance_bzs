package com.bzs.dao;

import com.bzs.model.OrderInfo;
import com.bzs.model.query.OrderAndAccount;
import com.bzs.utils.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderInfoMapper extends Mapper<OrderInfo> {
    List getOrderList(@Param("accountId")String accountId,@Param("payStatus")int payStatus);

    List searchOrderList(@Param("accountId")String accountId,@Param("payStatus")int payStatus,
                      @Param("carInfoId")String carInfoId, @Param("postedName")String postedName,
                      @Param("deliveryWay") int deliveryWay,@Param("insuranceCompany") String insuranceCompany
                      );
    /**
     * 根据订单id修改订单状态
     * @param orderInfo
     * @return
     */
    int  updatePayStatus(OrderInfo orderInfo);
    /**
     * 根据订单id修改订单状态
     * @param orderId
     * @return
     */
    int  updatePayStatusById(@Param("orderId") String  orderId);

    String getOrderIdByQuoteId(@Param("quoteId") String quoteId);

    List<OrderAndAccount> getNextLevelOrder(@Param("createBy")String createBy, @Param("status")String status, @Param("payStatus")String payStatus);
}