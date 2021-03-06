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
    List getOrderListByAdmin(int payStatus,String userName,String carNumber,String createTime);

    List getOrderList(String accountId, int payStatus);

    List searchOrderList(String accountId,  int payStatus,
                          String carInfoId, String postedName,
                         int deliveryWay, String insuranceCompany
    );
    Map orderDetails(String orderId,String quoteId);
    int updatePayStatus(OrderInfo orederNo);
    int updatePayStatusById(String orederNo);
    /**
     * 获取下两级的支付完成的订单，包括自己的一共三级
     * @param createBy
     * @param type 默认0佣金(自己出单) 1提成(下级出单) 2佣金和提成
     * @param orderStatus 订单状态
     * @param verificationStatus 审核状态
     * @return
     */
    Map<String,Object> getNextLevelOrder(String createBy,String type,String orderStatus,String verificationStatus);
}
