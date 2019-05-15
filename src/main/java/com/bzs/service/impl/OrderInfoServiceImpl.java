package com.bzs.service.impl;

import com.bzs.dao.CarInfoMapper;
import com.bzs.dao.OrderInfoMapper;
import com.bzs.model.CarInfo;
import com.bzs.model.OrderInfo;
import com.bzs.service.CarInfoService;
import com.bzs.service.OrderInfoService;
import com.bzs.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by alwaysacc on 2019/04/12.
 */
@Service
@Transactional
public class OrderInfoServiceImpl extends AbstractService<OrderInfo> implements OrderInfoService {
    @Resource
    private OrderInfoMapper orderInfoMapper;
    @Resource
    private CarInfoService carInfoService;

    @Override
    public List getOrderList(String accountId, int payStatus) {
        return orderInfoMapper.getOrderList(accountId,payStatus);
    }

    @Override
    public List searchOrderList(String accountId, int payStatus, String carNumber, String postedName, int deliveryWay, String insuranceCompany) {
        String carInfoId=null;
        if (carNumber!=null){
           CarInfo carInfo=carInfoService.findBy("car_number",carNumber);
           carInfoId=carInfo.getCarInfoId();
        }
        return orderInfoMapper.searchOrderList(accountId,payStatus,carInfoId,postedName,deliveryWay,insuranceCompany);
    }

    @Override
    public int updatePayStatus(OrderInfo orderInfo) {
        return orderInfoMapper.updatePayStatus(orderInfo);
    }

    @Override
    public int updatePayStatus(String orderId) {
        return orderInfoMapper.updatePayStatus(orderId);
    }
}
