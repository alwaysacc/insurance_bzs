package com.bzs.service;
import com.bzs.model.OrderInfo;
import com.bzs.utils.Service;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * Created by alwaysacc on 2019/04/12.
 */
public interface OrderInfoService extends Service<OrderInfo> {
    List getOrderList(String accountId, int payStatus);

    List searchOrderList(@Param("accountId") String accountId, @Param("payStatus") int payStatus,
                         @Param("carInfoId") String carInfoId, @Param("postedName") String postedName,
                         @Param("deliveryWay") int deliveryWay, @Param("insuranceCompany") String insuranceCompany
    );
    Map orderDetails(String orderId,String quoteId);
    int updatePayStatus(OrderInfo orederNo);
    int updatePayStatusById(String orederNo);
}
