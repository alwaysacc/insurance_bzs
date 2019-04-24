package com.bzs.service.impl;

import com.alibaba.fastjson.JSON;
import com.bzs.dao.QuoteInfoMapper;
import com.bzs.model.*;
import com.bzs.service.*;
import com.bzs.utils.AbstractService;
import com.bzs.utils.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by alwaysacc on 2019/04/11.
 */
@Service
@Transactional
public class QuoteInfoServiceImpl extends AbstractService<QuoteInfo> implements QuoteInfoService {
    @Resource
    private QuoteInfoMapper quoteInfoMapper;
    @Resource
    private QuoteInfoService quoteInfoService;
    @Resource
    private CarInfoService carInfoService;
    @Resource
    private CustomerService customerService;
    @Resource
    private InsuranceFollowInfoService insuranceFollowInfoService;
    @Resource
    private InsuredInfoService insuredInfoService;


    @Override
    public Map quoteDetails(String carInfoId) {
        //报价信息
        QuoteInfo quoteInfo=quoteInfoService.findBy("carInfoId",carInfoId);
        //车辆信息
        CarInfo carInfo=carInfoService.findBy("carInfoId",carInfoId);
        //客户信息
        Customer customer=null;
        if (carInfo.getCustomerId()!="" && carInfo.getCustomerId()!=null){
            customer=customerService.findBy("customerId",carInfo.getCustomerId());
        }
        //跟进信息
        InsuranceFollowInfo insuranceFollowInfo=insuranceFollowInfoService.findBy("carInfoId",carInfoId);
        //投保信息
        InsuredInfo insuredInfo=insuredInfoService.findBy("carInfoId",carInfoId);
        Map map=new HashMap();
        map.put("quote",quoteInfo);
        map.put("customer",customer);
        map.put("carInfo",carInfo);
        map.put("insuranceFollowInfo",insuranceFollowInfo);
        map.put("insuredInfo",insuredInfo);
        return map;
    }
}
