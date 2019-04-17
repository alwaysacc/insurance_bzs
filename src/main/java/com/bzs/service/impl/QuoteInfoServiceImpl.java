package com.bzs.service.impl;

import com.alibaba.fastjson.JSON;
import com.bzs.dao.QuoteInfoMapper;
import com.bzs.model.*;
import com.bzs.model.CarInfo;
import com.bzs.service.*;
import com.bzs.utils.*;
import com.bzs.utils.commons.ThirdAPI;
import com.bzs.utils.httpUtil.HttpClientUtil;
import com.bzs.utils.httpUtil.HttpResult;
import com.bzs.utils.jsontobean.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by alwaysacc on 2019/04/11.
 */
@Service
@Transactional
public class QuoteInfoServiceImpl extends AbstractService<QuoteInfo> implements QuoteInfoService {
    private static Logger logger = LoggerFactory.getLogger(QuoteInfoServiceImpl.class);

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
    public Result getQuoteDetailsByApi(QuoteParmasBean params, List<InsurancesList> list, String carInfoId, String createdBy) {
        if (CollectionUtils.isNotEmpty(list)) {
            ParamsData data = params.getData();
            if (null != data) {
                data.setInsurancesList(list);
                String isTrans = data.getCarInfo().getIsTrans();
                if (StringUtils.isBlank(isTrans)) {
                    data.getCarInfo().setIsTrans("");
                }
                if (StringUtils.isNotBlank(isTrans) && "0".equals(isTrans)) {
                    data.getCarInfo().setTransDate("");
                }
                //System.out.println(list.get(0).getFlag());
                String host = ThirdAPI.HOST;
                String port = ThirdAPI.PORT;
                //注意修改
                createdBy = UUIDS.getDateUUID();
                Map<String, Object> quoteMap = getQuoteDetailsByApi(1L, host, port, createdBy, params);
                String status = (String) quoteMap.get("status");
                String msg = (String) quoteMap.get("msg");
                PCICResponseBean bean = (PCICResponseBean) quoteMap.get("data");
                if ("1".equals(status)) {
                    if (null != bean) {
                        String uuids = UUIDS.getDateUUID();
                        QuoteInfo quoteInfo = new QuoteInfo(uuids);
                        quoteInfo.setCreatedBy(createdBy);
                        List<InsuranceTypeInfo> insuranceTypeInfoList = new ArrayList<InsuranceTypeInfo>();
                        ResponseData rdata = bean.getData();
                        List<InsurancesList> responseInsurancesList = rdata.getInsurancesList();
                        if (CollectionUtils.isNotEmpty(responseInsurancesList)) {
                            for (InsurancesList item : responseInsurancesList) {
                                String code = item.getInsuranceCode();//险别代码
                                String name1 = item.getInsuranceName();//险别名称
                                String name = InsuranceItems2.getName(code);//根据险别代码获取统一名称
                                String insuredAmount = item.getInsuredAmount();//保额
                                String insuredPremium = item.getInsuredPremium();//保单保费
                                String standardPremium = item.getStandardPremium();//标准保费
                                String flag = item.getFlag();//如下险别需要给定flag值，具体约定如下：
                                //玻璃单独破碎险：1是国产，2是进口 ；
                                //修理期间费用补偿险：格式：” 天，金额”， 如：“10，50”表示10天，每天50元钱（天数是1到90，金额是50到500）；
                                String amount = item.getAmount();//用于报价成功后返回的保额；
                                if (StringUtils.isNotBlank(amount)) {
                                    insuredAmount = amount;
                                } else {
                                    insuredAmount = "1";
                                }
                                InsuranceTypeInfo insuranceTypeInfo = new InsuranceTypeInfo(UUIDS.getDateUUID());
                                insuranceTypeInfo.setInfoType("1");
                                if (StringUtils.isNotBlank(insuredPremium)) {
                                    insuranceTypeInfo.setInsurancePremium(new BigDecimal(insuredPremium));
                                }
                                if (StringUtils.isNotBlank(insuredAmount)) {
                                    insuranceTypeInfo.setInsuranceAmount(new BigDecimal(insuredAmount));
                                }
                                insuranceTypeInfo.setInsuranceName(name);
                                insuranceTypeInfo.setStandardPremium(standardPremium);
                                insuranceTypeInfo.setCreatedBy(createdBy);
                                insuranceTypeInfo.setTypeId(uuids);
                                insuranceTypeInfoList.add(insuranceTypeInfo);
                            }
                        }

                        System.out.println("报价成功，输出信息：" + msg);
                    }


                    return ResultGenerator.gen("成功", bean, ResultCode.SUCCESS);
                } else if ("0".equals(status)) {//报价失败
                    return ResultGenerator.gen(msg, "", ResultCode.FAIL);
                } else {//400
                    return ResultGenerator.gen("获取失败，" + msg, "", ResultCode.FAIL);
                }
            }
            return ResultGenerator.gen("参数异常", "", ResultCode.FAIL);
        } else {
            return ResultGenerator.gen("成功", "", ResultCode.SUCCESS);
        }
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
                api = ThirdAPI.CPIC_QUOTE_ALL;
            } else if (2 == source) {
                api = ThirdAPI.PAIC_QUOTE_NAME;
            } else if (4 == source) {
                api = ThirdAPI.PICC_QUOTE_NAME;
            }

        }
        if (StringUtils.isNotBlank(host) && StringUtils.isNotBlank(port)) {
            String url = host + ":" + port + "/" + api;
            logger.info("保司枚举值:" + source + "，报价请求接口" + url);
            String jsonStrs = JSON.toJSONString(params);

            logger.info("报价请求参数" + jsonStrs);
            String jsonStrs2 = jsonStrs.replace("noType", "NoType");
            HttpResult httpResult = HttpClientUtil.doPost(url, null, "JSON", PCICResponseBean.class, jsonStrs2);
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
                } else if ("0099".equals(state)) {//爬虫接口调用反回失败
                    result.put("status", "400");
                    result.put("msg", "爬虫接口调用反回失败");
                    result.put("data", bean);
                } else {
                    result.put("status", "400");
                    result.put("msg", "爬虫接口调用反回未处理");
                    result.put("data", bean);
                }
                return result;
            } else {
                result.put("status", "400");
                result.put("msg", "调用爬虫接口出险异常，报错代码：" + code);
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
