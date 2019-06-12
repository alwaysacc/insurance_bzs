package com.bzs.service.impl;

import com.bzs.dao.CarInfoMapper;
import com.bzs.dao.OrderInfoMapper;
import com.bzs.dao.QuoteInfoMapper;
import com.bzs.model.CarInfo;
import com.bzs.model.InsuredInfo;
import com.bzs.model.OrderInfo;
import com.bzs.model.QuoteInfo;
import com.bzs.model.query.OrderAndAccount;
import com.bzs.service.CarInfoService;
import com.bzs.service.InsuredInfoService;
import com.bzs.service.OrderInfoService;
import com.bzs.service.QuoteInfoService;
import com.bzs.utils.AbstractService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by alwaysacc on 2019/04/12.
 */
@Service
@Transactional
public class OrderInfoServiceImpl extends AbstractService<OrderInfo> implements OrderInfoService {
    @Resource
    private OrderInfoMapper orderInfoMapper;
    @Resource
    private OrderInfoService orderInfoServices;
    @Resource
    private CarInfoService carInfoService;
    @Resource
    private QuoteInfoService quoteInfoService;
    @Resource
    private QuoteInfoMapper quoteInfoMapper;
    @Resource
    private InsuredInfoService insuredInfoService;

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
    public Map orderDetails(String orderId,String quoteId) {
        Map map=new HashMap();
        if (StringUtils.isNotBlank(orderId)){
            orderId=orderInfoMapper.getOrderIdByQuoteId(quoteId);
        }
        OrderInfo orderInfo=orderInfoServices.findBy("orderId",orderId);
        //车辆信息
        CarInfo carInfo=carInfoService.findBy("carInfoId",orderInfo.getCarInfoId());
        QuoteInfo quoteInfo=quoteInfoService.findBy("quoteId",orderInfo.getPayTypeId());
        List insuredList=quoteInfoMapper.getInsurance(orderInfo.getPayTypeId(),1);
        InsuredInfo insuredInfo=insuredInfoService.findBy("carInfoId",orderInfo.getCarInfoId());
        map.put("orderInfo",orderInfo);
        map.put("carInfo",carInfo);
        map.put("quoteInfo",quoteInfo);
        map.put("insuredList",insuredList);
        map.put("insuredInfo",insuredInfo);
        return map;
    }

    @Override
    public int updatePayStatus(OrderInfo orderInfo) {
        return orderInfoMapper.updatePayStatus(orderInfo);
    }

    @Override
    public int updatePayStatusById(String orderId) {
        return orderInfoMapper.updatePayStatusById(orderId);
    }

    @Override
    public Map<String, Object> getNextLevelOrder(String createBy) {
        Map<String, Object> map=new HashMap<>();
        if(StringUtils.isBlank(createBy)){
            map.put("code","400");
            map.put("msg","参数异常");
            map.put("data","");
            return map;
        }
        List <OrderAndAccount>list=orderInfoMapper.getNextLevelOrder(createBy,null,null);
        if(CollectionUtils.isNotEmpty(list)){
            map.put("code","200");
            map.put("msg","查询成");
            map.put("data",list);
            return map;
        }else{
            map.put("code","400");
            map.put("msg","未查询到相关信息");
            map.put("data","");
            return map;
        }
    }
}
