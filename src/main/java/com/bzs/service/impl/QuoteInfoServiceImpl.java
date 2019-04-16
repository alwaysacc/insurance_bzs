package com.bzs.service.impl;

import com.alibaba.fastjson.JSON;
import com.bzs.dao.QuoteInfoMapper;
import com.bzs.model.*;
import com.bzs.service.*;
import com.bzs.utils.*;
import com.bzs.utils.commons.ThirdAPI;
import com.bzs.utils.httpUtil.HttpClientUtil;
import com.bzs.utils.httpUtil.HttpResult;
import com.bzs.utils.jsontobean.InsurancesList;
import com.bzs.utils.jsontobean.PCICResponseBean;
import com.bzs.utils.jsontobean.ParamsData;
import com.bzs.utils.jsontobean.QuoteParmasBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private  static Logger logger =LoggerFactory.getLogger(QuoteInfoServiceImpl.class);

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
            ParamsData data = params.getData();
            if (null != data) {
                data.setInsurancesList(list);
                //System.out.println(list.get(0).getFlag());
                String host=ThirdAPI.HOST;
                String port=ThirdAPI.PORT;
                String uuids=UUIDS.getDateUUID();
                Map<String, Object> quoteMap= getQuoteDetailsByApi(1L,host,port,uuids,params);
                String status=(String)quoteMap.get("status");
                String msg=(String)quoteMap.get("msg");
                PCICResponseBean bean=(PCICResponseBean) quoteMap.get("data");
                if("1".equals(status)){
                    if(null!=bean){
                        System.out.println("报价成功，输出信息："+msg);
                    }
                }
            }
        }
        return ResultGenerator.gen("成功","",ResultCode.SUCCESS);
    }

    public Map<String, Object> getQuoteDetailsByApi(Long source, String host, String port, String createdBy, QuoteParmasBean params) {
        Map<String, Object> result = new HashMap<>();
        String jsonStr = JSON.toJSONString(params);
        String api = null;
        if (null == source) {
            result.put("status", "400");
            result.put("msg", "参数错误");
            result.put("data", "");
            return result;
        } else {
            if (1 == source) {
               // api = ThirdAPI.CPIC_QUOTE_NAME;
                api=ThirdAPI.CPIC_QUOTE_ALL;
            } else if (2 == source) {
                api = ThirdAPI.PAIC_QUOTE_NAME;
            } else if (4 == source) {
                api = ThirdAPI.PICC_QUOTE_NAME;
            }

        }
        if (StringUtils.isNotBlank(host) && StringUtils.isNotBlank(port)) {
            String url = host + ":" + port + "/" + api;
            logger.info("保司枚举值:"+source+"，报价请求接口"+url);
            String jsonStrs = JSON.toJSONString(params);
            logger.info("报价请求参数"+url);
            HttpResult httpResult = HttpClientUtil.doPost(url, null, "JSON", PCICResponseBean.class, jsonStrs);
            int code = httpResult.getCode();
            String msg = httpResult.getMessage();
            if (200 == code) {
                PCICResponseBean bean = (PCICResponseBean) httpResult.getT();
                String state = bean.getState();
                String retMsg = bean.getRetMsg();
                if ("1".equals(state)) {
                    result.put("status", "1");
                    result.put("msg", retMsg);
                    result.put("data", bean);
                } else if ("0".equals(state)) {//拒保的各种原因是0
                    result.put("status", "0");
                    result.put("msg", retMsg);
                    result.put("data", bean);
                }else if ("0099".equals(state)) {//爬虫接口调用反回失败
                    result.put("status", "400");
                    result.put("msg", "爬虫接口调用反回失败");
                    result.put("data", bean);
                }else{
                    result.put("status", "400");
                    result.put("msg", "爬虫接口调用反回未处理");
                    result.put("data", bean);
                }
                return result;
            } else {
                result.put("status", "400");
                result.put("msg", "调用爬虫接口出险异常，报错代码："+code);
                result.put("data", "");
                return result;
            }
        } else {
            result.put("status", "400");
            result.put("msg", "参数错误");
            result.put("data", "");
            return result;
        }
    }

}
