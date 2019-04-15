package com.bzs.service.impl;

import com.alibaba.fastjson.JSON;
import com.bzs.dao.QuoteInfoMapper;
import com.bzs.model.*;
import com.bzs.service.*;
import com.bzs.utils.AbstractService;
import com.bzs.utils.Result;
import com.bzs.utils.commons.ThirdAPI;
import com.bzs.utils.httpUtil.HttpClientUtil;
import com.bzs.utils.jsontobean.InsurancesList;
import com.bzs.utils.jsontobean.ParamsData;
import com.bzs.utils.jsontobean.QuoteParmasBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
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
        QuoteInfo quoteInfo = quoteInfoService.findBy("carInfoId", carInfoId);
        //客户信息
        Customer customer = customerService.findBy("carInfoId", carInfoId);
        //车辆信息
        CarInfo carInfo = carInfoService.findBy("carInfoId", carInfoId);
        //跟进信息
        InsuranceFollowInfo insuranceFollowInfo = insuranceFollowInfoService.findBy("carInfoId", carInfoId);
        //投保信息
        InsuredInfo insuredInfo = insuredInfoService.findBy("carInfoId", carInfoId);
        Map map = new HashMap();
        map.put("quote", quoteInfo);
        map.put("customer", customer);
        map.put("carInfo", carInfo);
        map.put("insuranceFollowInfo", insuranceFollowInfo);
        map.put("insuredInfo", insuredInfo);
        return map;
    }
    @Override
    public Result getQuoteDetailsByApi(QuoteParmasBean params, List<InsurancesList> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            ParamsData data=params.getData();
            if(null!=data){
                data.setInsurancesList(list);
                System.out.println(list.get(0).getFlag());
            }
        }
        return null;
    }
    public Map<String,Object>getQuoteDetailsByApi(Long source,String host,String port,String createdBy,QuoteParmasBean params){
        Map<String,Object> result=new HashMap<>();
        String jsonStr=JSON.toJSONString(params);
        String api=null;
        if(null==source){
            result.put("status","400");
            result.put("msg","参数错误");
            result.put("data","");
            return result;
        }else{
            if(1==source){
                api=ThirdAPI.CPIC_QUOTE_NAME;
            }else if(2==source){
                api=ThirdAPI.PAIC_QUOTE_NAME;
            }else  if(4==source){
            }

        }
        if(StringUtils.isNotBlank(host)&&StringUtils.isNotBlank(port)){
            //HttpClientUtil.doPost()
        }else{
            result.put("status","400");
            result.put("msg","参数错误");
            result.put("data","");
            return result;
        }
        return result;
    }

}
