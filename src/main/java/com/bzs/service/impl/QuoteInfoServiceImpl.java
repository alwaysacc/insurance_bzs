package com.bzs.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bzs.dao.InsuranceTypeInfoMapper;
import com.bzs.dao.OrderInfoMapper;
import com.bzs.dao.QuoteInfoMapper;
import com.bzs.model.*;
import com.bzs.model.CarInfo;
import com.bzs.redis.RedisUtil;
import com.bzs.service.*;
import com.bzs.utils.*;
import com.bzs.utils.bihujsontobean.JsonRootBean;
import com.bzs.utils.commons.ThirdAPI;
import com.bzs.utils.commons.TwoPower;
import com.bzs.utils.dateUtil.DateUtil;
import com.bzs.utils.encodeUtil.EncodeUtil;
import com.bzs.utils.enumUtil.CityCodeEnum;
import com.bzs.utils.enumUtil.InsuranceItems;
import com.bzs.utils.enumUtil.InsuranceItems2;
import com.bzs.utils.enumUtil.InsuranceNameEnum;
import com.bzs.utils.httpUtil.HttpClientUtil;
import com.bzs.utils.httpUtil.HttpResult;
import com.bzs.utils.jsontobean.*;
import com.sun.org.apache.xpath.internal.operations.Quo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;


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
    private CustomerService customerService;
    @Resource
    private InsuranceFollowInfoService insuranceFollowInfoService;
    @Resource
    private InsuredInfoService insuredInfoService;
    @Resource
    private QuoteInfoService quoteInfoService;
    @Resource
    private CarInfoService carInfoService;
    @Resource
    private InsuranceTypeInfoMapper insuranceTypeInfoMapper;
    @Resource
    private OrderInfoMapper orderInfoMapper;
    @Resource
    private ThirdInsuranceAccountInfoService thirdInsuranceAccountInfoService;
    @Resource
    private OrderInfoService orderInfoService;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public Map quoteDetails(String carInfoId) {
        //报价信息
        List<QuoteInfo> quoteInfo = quoteInfoMapper.getQuote(carInfoId);
        //车辆信息
        CarInfo carInfo = carInfoService.findBy("carInfoId", carInfoId);
        //客户信息
        Customer customer = null;
        if (StringUtils.isNotBlank(carInfo.getCustomerId())) {
            customer = customerService.findBy("customerId", carInfo.getCustomerId());
        }
        //跟进信息
        InsuranceFollowInfo insuranceFollowInfo = insuranceFollowInfoService.findBy("carInfoId", carInfoId);
        //投保信息
        InsuredInfo insuredInfo = insuredInfoService.findBy("carInfoId", carInfoId);
        List insuredList = null;
        if (insuredInfo != null) {
            insuredList = quoteInfoMapper.getInsurance(insuredInfo.getInsuredId(), 0);
        }
        List TquoteList = null;
        List RquoteList = null;
        List PquoteList = null;
        System.out.println(quoteInfo.size());
        System.out.println(ResultGenerator.genSuccessResult(quoteInfo));
        QuoteInfo quote = null;
        for (int i = 0; i < quoteInfo.size(); i++) {
            System.out.println(quoteInfo.get(i).getQuoteSource());
            switch (quoteInfo.get(i).getQuoteSource()) {
                case "1":
                    TquoteList = quoteInfoMapper.getInsurance(quoteInfo.get(i).getQuoteId(), 1);
                    break;
                case "2":
                    PquoteList = quoteInfoMapper.getInsurance(quoteInfo.get(i).getQuoteId(), 1);
                    break;
                case "4":
                    RquoteList = quoteInfoMapper.getInsurance(quoteInfo.get(i).getQuoteId(), 1);
                    break;
            }
        }
        Map map = new HashMap();
        map.put("quote", quoteInfo);
        map.put("customer", customer);
        map.put("carInfo", carInfo);
        map.put("insuranceFollowInfo", insuranceFollowInfo);
        map.put("insuredInfo", insuredInfo);
        map.put("insuredList", insuredList);
        map.put("TquoteList", TquoteList);
        map.put("PquoteList", PquoteList);
        map.put("RquoteList", RquoteList);
        return map;
    }

    @Override
    public Result getQuoteDetailsByApi(QuoteParmasBean params, List<InsurancesList> list, String carInfoId, String createdBy, Long source) {
        /* if (CollectionUtils.isNotEmpty(list)) {*/
        if (StringUtils.isBlank(createdBy)) {
            return ResultGenerator.genFailResult("参数错误,未获取账号信息");
        }
        if (source == null) {
            return ResultGenerator.genFailResult("参数错误");
        }
        ParamsData data = params.getData();
        if (null != data) {
            data.setInsurancesList(list);
            //---------------------------  注意修改开始
            if (StringUtils.isBlank(carInfoId)) {
                carInfoId = UUIDS.getDateUUID();
            }
            //---------------------------  注意修改结束
            Map<String, Object> quoteMap = getQuoteDetailsByApi(source, createdBy, params);
            String status = (String) quoteMap.get("status");
            String msg = (String) quoteMap.get("msg");
            String uuids = UUIDS.getDateUUID();
            QuoteInfo quoteInfo = new QuoteInfo(uuids);
            quoteInfo.setCreatedBy(createdBy);
            quoteInfo.setCarInfoId(carInfoId);
            quoteInfo.setQuoteSource(source + "");
            quoteInfo.setQuoteInsuranceName(InsuranceNameEnum.getName(source));
            quoteInfo.setQuoteStatus(0);
            quoteInfo.setSubmitStatus(0);
            Date date = DateUtil.getDateToDate(new Date(), "yyyy-MM-dd HH:mm:ss");
            logger.info("当前时间+" + date);
            quoteInfo.setCreatedTime(new Date());
            // if ("1".equals(status)||"0".equals(status)) {//报价成功,其他均为报价失败
            //String body = (String) quoteMap.get("body");//爬虫返回的数据
            PCICResponseBean bean = (PCICResponseBean) quoteMap.get("data");//body 转为实体对象
            if (null != bean) {
                String retCode = bean.getRetCode();
                String retMsg = bean.getRetMsg();
                if (retMsg.indexOf("需双录") > -1) retMsg += ",请人工核保";
                retMsg = EncodeUtil.unicodeToString(retMsg);
                logger.info("retCode=" + retCode + ",retMsg=" + retMsg);
                int resultCode = 400;
                if (StringUtils.isNotBlank(retCode)) {
                    ResponseData rdata = bean.getData();
                    if (null != rdata) {
                        String ciPremium = rdata.getCiPremium();//交强险保费合计
                        String biPremium = rdata.getBiPremium();//商业险标准保费
                        String proposalNo = rdata.getProposalNo();//报价单号
                        String payUrl = rdata.getPayUrl();//平安直接获取支付地址
                        if (StringUtils.isNotBlank(proposalNo) || StringUtils.isNotBlank(payUrl)) {//报价单号获取成功，说明报价+核保成功
                            quoteInfo.setQuoteStatus(1);
                            quoteInfo.setQuoteResult("报价成功");//报价结果
                            quoteInfo.setSubmitStatus(1);
                            quoteInfo.setSubmitresult("核保成功");
                            if (source == 2L) {
                                quoteInfo.setPayUrl(payUrl);//平安直接获取支付地址
                                proposalNo = UUIDS.getDateUUID();//平安直接获取支付地址，设置proposalNo
                                quoteInfo.setPaymentNotice(UUIDS.getDateUUID());
                            }
                            resultCode = 200;
                        } else {
                            //报价成功-核保失败
                            if ((StringUtils.isNotBlank(ciPremium) && Double.valueOf(ciPremium) > 0) || (StringUtils.isNotBlank(biPremium) && Double.valueOf(biPremium) > 0)) {
                                quoteInfo.setQuoteStatus(1);
                                quoteInfo.setQuoteResult("报价成功");//报价结果
                                quoteInfo.setSubmitresult(retMsg);
                                resultCode = 300;
                            } else {//报价+核保失败
                                quoteInfo.setQuoteResult(retMsg);
                                quoteInfo.setSubmitresult(retMsg);
                                resultCode = 400;
                            }

                        }
                        /*if ("0000".equals(retCode)) {//报价 核保
                            logger.info("code=" + retCode + "," + retMsg);
                            quoteInfo.setQuoteStatus(1);
                            quoteInfo.setQuoteResult("报价成功");//报价结果
                            quoteInfo.setSubmitStatus(1);
                            quoteInfo.setSubmitresult("核保成功");
                        } else if ("0099".equals(retCode)) {//报价失败，报价成功但是核保失败
                            logger.info("code=" + retCode + "," + retMsg);
                            if (StringUtils.isNotBlank(retMsg)) {
                                if (retMsg.indexOf("报价失败") > -1) {
                                    quoteInfo.setQuoteResult(retMsg);//报价结果
                                    quoteInfo.setSubmitresult(retMsg);
                                } else if (retMsg.indexOf("核保失败") > -1) {
                                    //核保失败
                                    quoteInfo.setQuoteStatus(1);
                                    quoteInfo.setQuoteResult("报价成功");//报价结果
                                    quoteInfo.setSubmitresult(retMsg);
                                } else {
                                    //当交强险合计和商业险合计有一个不为0或者空则报价成功，核保失败
                                   // if(((StringUtils.isNotBlank(ciPremium)&&Double.valueOf(ciPremium)>0)||(StringUtils.isNotBlank(biPremium)&&Double.valueOf(ciPremium)>0))&&StringUtils.isBlank(proposalNo))
                                    if((StringUtils.isNotBlank(ciPremium)&&Double.valueOf(ciPremium)>0)||(StringUtils.isNotBlank(biPremium)&&Double.valueOf(ciPremium)>0)){
                                        quoteInfo.setQuoteStatus(1);
                                        quoteInfo.setQuoteResult("报价成功");//报价结果
                                        quoteInfo.setSubmitresult(retMsg);
                                    }else{
                                        quoteInfo.setQuoteResult(retMsg);//报价结果
                                        quoteInfo.setSubmitresult(retMsg);
                                    }

                                }
                            } else {
                                quoteInfo.setQuoteResult(retMsg);//报价结果
                                quoteInfo.setSubmitresult(retMsg);
                            }
                        } else if ("0001".equals(retCode)) {//重复投保
                            logger.info("code=" + retCode + "," + retMsg);
                            quoteInfo.setQuoteResult(retMsg);//报价结果
                            quoteInfo.setSubmitresult(retMsg);
                        } else {
                            logger.info("code=" + retCode + "," + retMsg);
                            quoteInfo.setQuoteResult(retMsg);//报价结果
                            quoteInfo.setSubmitresult(retMsg);
                        }*/
                        List<InsuranceTypeInfo> insuranceTypeInfoList = new ArrayList<InsuranceTypeInfo>();
                        PayInfo payinfo = rdata.getPayInfo();
                        String advDiscountRate = rdata.getAdvDiscountRate();//建议折扣率
                        String refId = rdata.getRefId();//报价流水号
                        String ciBeginDate = rdata.getCiBeginDate();//交强险起保日期
                        String ciEcompensationRate = rdata.getCiEcompensationRate();//交强险预期赔付率
                        String carshipTax = rdata.getCarshipTax();//车船税金额
                        String biBeginDate = rdata.getBiBeginDate();//商业险起期
                        String biPremiumByDis = rdata.getBiPremiumByDis();//商业险折后保费
                        String realDiscountRate = rdata.getRealDiscountRate();//实际折扣率
                        String nonClaimDiscountRate = rdata.getNonClaimDiscountRate(); //无赔款折扣系数
                        String trafficTransgressRate = rdata.getTrafficTransgressRate();//交通违法系数
                        String underwritingRate = rdata.getUnderwritingRate();//自主核保系数
                        String channelRate = rdata.getChannelRate();//自主渠道系数
                        String biEcompensationRate = rdata.getBiEcompensationRate();//商业险预期赔付率

                        //quoteInfo.setBizTotal();
                        //设置交强险金额
                        if (StringUtils.isNotBlank(ciPremium)) {
                            ciPremium = ciPremium.replaceAll(",", "");
                            quoteInfo.setForceTotal(new BigDecimal(ciPremium));
                        }
                        //设置车船税金额
                        if (StringUtils.isNotBlank(carshipTax)) {
                            carshipTax = carshipTax.replaceAll(",", "");
                            quoteInfo.setTaxTotal(new BigDecimal(carshipTax));
                        }

                        //设置自主核保系数
                        quoteInfo.setIndependentChannelDate(channelRate);
                        //自主渠道系数
                        quoteInfo.setIndependentSubmitRate(underwritingRate);
                        //交通违法系数
                        quoteInfo.setTrafficIllegalRate(trafficTransgressRate);
                        quoteInfo.setAdvDiscountRate(advDiscountRate);
                        quoteInfo.setRefId(refId);
                        quoteInfo.setForceStartTime(ciBeginDate);
                        quoteInfo.setBizStartTime(biBeginDate);
                        quoteInfo.setProposalNo(proposalNo);
                        quoteInfo.setForceEcompensationRate(ciEcompensationRate);
                        quoteInfo.setBizEcompensationRate(biEcompensationRate);
                        if (StringUtils.isNotBlank(biPremium)) {
                            biPremium = biPremium.replaceAll(",", "");
                            quoteInfo.setBizPremium(biPremium);
                            quoteInfo.setBizTotal(new BigDecimal(biPremium));
                        }
                        if (StringUtils.isNotBlank(biPremiumByDis)) {
                            biPremiumByDis = biPremiumByDis.replaceAll(",", "");
                            quoteInfo.setBizPremiumByDis(biPremiumByDis);
                        }

                        quoteInfo.setNonClaim_discountRate(nonClaimDiscountRate);
                        quoteInfo.setRealDiscountRate(realDiscountRate);

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
                                    amount = amount.replaceAll(",", "");
                                    insuredAmount = amount;
                                } else {
                                    insuredAmount = "1";
                                }
                                InsuranceTypeInfo insuranceTypeInfo = new InsuranceTypeInfo();
                                insuranceTypeInfo.setInfoType("1");//标记属于报价项
                                if (StringUtils.isNotBlank(insuredPremium)) {
                                    insuredPremium = insuredPremium.replaceAll(",", "");
                                    insuranceTypeInfo.setInsurancePremium(new BigDecimal(insuredPremium));
                                }
                                if (StringUtils.isNotBlank(insuredAmount)) {
                                    insuredAmount = insuredAmount.replaceAll(",", "");
                                    insuranceTypeInfo.setInsuranceAmount(new BigDecimal(insuredAmount));
                                }
                                logger.info("保险名称：本名" + name1 + "后台统一" + name + "代码：" + code);
                                insuranceTypeInfo.setInsuranceName(name1);
                                if (StringUtils.isNotBlank(standardPremium)) {
                                    standardPremium = standardPremium.replaceAll(",", "");
                                    insuranceTypeInfo.setStandardPremium(standardPremium);
                                }
                                insuranceTypeInfo.setCreatedBy(createdBy);
                                insuranceTypeInfo.setTypeId(uuids);//报价的id
                                insuranceTypeInfoList.add(insuranceTypeInfo);
                            }
                        }
                        //插入报价的投保项
                        if (CollectionUtils.isNotEmpty(insuranceTypeInfoList)) {
                            insuranceTypeInfoMapper.insertBatch(insuranceTypeInfoList);
                        }
                        System.out.println("报价成功，输出信息：" + msg);
                        quoteInfoMapper.insert(quoteInfo);
                       /* if (bean.getRetCode().equals("0099")){
                            return ResultGenerator.gen("失败", bean.getRetMsg(), ResultCode.FAIL);
                        }*/
                        if (200 == resultCode) {
                            return ResultGenerator.gen("报价核保成功", bean, ResultCode.SUCCESS);
                        } else if (300 == resultCode) {
                            return ResultGenerator.gen("报价成功,核保失败", bean, ResultCode.SUBMIT);
                        } else {
                            return ResultGenerator.gen(retMsg, bean, ResultCode.FAIL);
                        }
                    } else {
                        String submitResult = "";
                        if (StringUtils.isNotBlank(retMsg)) {
                            quoteInfo.setQuoteResult(retMsg);
                            submitResult = retMsg + ",核保失败";
                        } else {
                            submitResult = "报价失败" + ",核保失败";
                            quoteInfo.setQuoteResult("报价失败");
                        }
                        quoteInfo.setSubmitresult(submitResult);
                        quoteInfoMapper.insert(quoteInfo);
                        //if(retMsg.indexOf("需双录")>-1)retMsg+=",请人工核保";
                        PCICResponseBean resultBean = new PCICResponseBean();
                        resultBean.setRetMsg("报价内容:" + msg);
                        return ResultGenerator.gen(retMsg, resultBean, ResultCode.FAIL);
                    }
                }
            } else {
                logger.info("报价内容获取为空:" + msg);
                quoteInfo.setQuoteResult(msg);
                quoteInfo.setSubmitresult(msg);
                quoteInfoMapper.insert(quoteInfo);
                PCICResponseBean resultBean = new PCICResponseBean();
                resultBean.setRetMsg("报价失败:" + msg);
                return ResultGenerator.gen("报价失败:" + msg, resultBean, ResultCode.FAIL);
            }
        }
        PCICResponseBean resultBean = new PCICResponseBean();
        resultBean.setRetMsg("报价失败:参数异常");
        return ResultGenerator.gen("参数异常", resultBean, ResultCode.FAIL);
    }

    public Map<String, Object> getQuoteDetailsByApi(Long source, String createdBy, QuoteParmasBean params) {
        Map<String, Object> result = new HashMap<>();
        String api = "";
        String host = "";
        String port = "";
        List<String> portList = new ArrayList<>();
        String keyRedis = "";
        if (null == source) {
            result.put("status", "400");
            result.put("msg", "参数错误");
            return result;
        } else {
            Map map = thirdInsuranceAccountInfoService.findEnbaleAccount(source, "1", createdBy);
            String code = (String) map.get("code");
            boolean aflag = false;
            String message = null;
            String name = InsuranceNameEnum.getName(source);
            if ("200".equals(code)) {
                ThirdInsuranceAccountInfo accountInfo = (ThirdInsuranceAccountInfo) map.get("data");
                host = accountInfo.getIp();
                port = accountInfo.getPort();
                if (2L == source) {
                    api = ThirdAPI.PAIC_QUOTE_NAME;
                    String accountName = accountInfo.getAccountName();
                    String accountPwd = accountInfo.getAccountPwd();
                    if (StringUtils.isNotBlank(accountName) && StringUtils.isNotBlank(accountPwd)) {
                        aflag = true;
                        params.getData().getAccountInfo().setAccount(accountName);
                        params.getData().getAccountInfo().setPassword(accountPwd);
                    } else {
                        message = name + "账号信息不完整";
                    }
                } else if (1 == source) {
                    api = ThirdAPI.CPIC_QUOTE_ALL;
                    keyRedis = "CPIC_PORT" + createdBy;
                    System.out.println(keyRedis);
                    // host = ThirdAPI.CPIC_HOST;
                    // port = ThirdAPI.CPIC_PORT;
                    synchronized (this) {
                        if (!redisUtil.hasKey(keyRedis)) {
                            port = "5000";
                            portList.add(port);
                            redisUtil.set(keyRedis, portList, 720000);
                        } else {
                            portList = (List) redisUtil.get(keyRedis);
                            System.out.println(portList.toString());
                            if (!portList.contains("5000")) {
                                port = "5000";
                                portList.add(port);
                                redisUtil.set(keyRedis, portList, 720000);
                            } else if (!portList.contains("5001")) {
                                port = "5001";
                                portList.add(port);
                                redisUtil.set(keyRedis, portList, 720000);
                            } else if (!portList.contains("5002")) {
                                port = "5002";
                                portList.add(port);
                                redisUtil.set(keyRedis, portList, 720000);
                            } else if (!portList.contains("5003")) {
                                port = "5003";
                                portList.add(port);
                                redisUtil.set(keyRedis, portList, 720000);
                            } else if (!portList.contains("5004")) {
                                port = "5004";
                                portList.add(port);
                                redisUtil.set(keyRedis, portList, 720000);
                            } else {
                                result.put("status", "300");
                                result.put("msg", "网络繁忙，请重试");
                                result.put("data", null);
                                return result;
                            }
                        }
                    }
                    aflag = true;
                    params.getData().setSalesPerson(ThirdAPI.salesPerson);
                } else if (4 == source) {
                    api = ThirdAPI.PICC_QUOTE_ALL;
                    keyRedis = "PICC_PORT" + createdBy;
                    synchronized (this) {
                        if (!redisUtil.hasKey(keyRedis)) {
                            port = "4050";
                            portList.add(port);
                            redisUtil.set(keyRedis, portList, 720000);
                        } else {
                            portList = (List) redisUtil.get(keyRedis);
                            System.out.println(portList.toString());
                            if (!portList.contains("4050")) {
                                port = "4050";
                                portList.add(port);
                                redisUtil.set(keyRedis, portList, 720000);
                            } else if (!portList.contains("4051")) {
                                port = "4051";
                                portList.add(port);
                                redisUtil.set(keyRedis, portList, 720000);
                            } else if (!portList.contains("4052")) {
                                port = "4052";
                                portList.add(port);
                                redisUtil.set(keyRedis, portList, 720000);
                            } else if (!portList.contains("4053")) {
                                port = "4053";
                                portList.add(port);
                                redisUtil.set(keyRedis, portList, 720000);
                            } else if (!portList.contains("4054")) {
                                port = "4054";
                                portList.add(port);
                                redisUtil.set(keyRedis, portList, 720000);
                            } else {
                                result.put("status", "300");
                                result.put("msg", "网络繁忙，请重试");
                                result.put("data", null);
                                return result;
                            }
                        }
                    }
                    aflag = true;
                } else {
                    message = name + "报价业务待拓展";
                }
                if (StringUtils.isNotBlank(host) && StringUtils.isNotBlank(port)) {
                    aflag = true;
                } else {
                    message = name + "账号信息不完整";
                    aflag = false;
                }
            } else {
                message = (String) map.get("msg");
            }
            if (!aflag) {
                result.put("status", "400");
                result.put("msg", message);
                result.put("data", null);
                return result;
            }
        }
        if (StringUtils.isNotBlank(host) && StringUtils.isNotBlank(port)) {
            String url = host + ":" + port + "/" + api;
            logger.info("保司枚举值:" + source + "，报价请求接口" + url);
            String jsonStrs = JSON.toJSONString(params);
            logger.info("报价请求参数" + jsonStrs);
            String jsonStrs2 = jsonStrs.replace("noType", "NoType");
            HttpResult httpResult = HttpClientUtil.doPost(url, null, "JSON", PCICResponseBean.class, jsonStrs2);
            portList.remove(port);
            redisUtil.set(keyRedis, portList, 720000);
            int code = httpResult.getCode();
            String msg = httpResult.getMessage();
            String body = httpResult.getBody();
            if (200 == code) {//远程请求成功
                PCICResponseBean bean = JSONObject.parseObject(body, PCICResponseBean.class);
                String retMsg = bean.getRetMsg();
                String state = bean.getState();
                retMsg = EncodeUtil.unicodeToString(retMsg);
                result.put("status", state);
                result.put("msg", retMsg);
                result.put("data", bean);
                result.put("body", body);
                return result;
            } else {//code!=200请求失败
                result.put("status", code + "");
                result.put("msg", "报价失败，报错代码：" + code);
                return result;
            }
        } else {
            result.put("status", "18000");
            result.put("msg", "参数错误");
            return result;
        }
    }

    //待拓展多家同时报价，当全部报价完成后返回报价结果
    @Override
    public Result getQuoteDetailsByApi(QuoteParmasBean params, List<InsurancesList> list, String carInfoId, String createdBy, List<Long> sources, Long lastYearSource) {
        if (CollectionUtils.isNotEmpty(sources)) {

            String cpicUrl = "";//太保
            String paicUrl = "";//平安
            String piccUrl = "";//人保
            for (Long source : sources) {
                if (null != source && 1L == source) {//太保
                    cpicUrl = ThirdAPI.CPIC_HOST + ":" + ThirdAPI.CPIC_PORT + "/" + ThirdAPI.CPIC_QUOTE_ALL;
                } else if (null != source && 2L == source) {//平安
                    if (null != lastYearSource && 2 == lastYearSource) {
                        paicUrl = ThirdAPI.PAIC_HOST + ":" + ThirdAPI.PAIC_PORT + "/" + ThirdAPI.PAIC_QUOTE_ALL;//上一年在平安投保的
                    } else {
                        paicUrl = ThirdAPI.PAIC_HOST + ":" + ThirdAPI.PAIC_PORT + "/" + ThirdAPI.PAIC_QUOTE_NAME;
                    }
                } else if (null != source && 4L == source) {//人保
                    piccUrl = ThirdAPI.PICC_HOST + ":" + ThirdAPI.PICC_PORT + "/" + ThirdAPI.PICC_QUOTE_ALL;
                } else {
                    logger.info("待添加");
                }
            }
            return null;
        } else {
            return ResultGenerator.genFailResult("参数异常");
        }

    }

    @Override
    public Result getPayMentgetPayMent(String proposalNo, String pay, String money, String createdBy, String carInfoId, String quoteId, Long source, String deliveryWay, String deliveryAddress, String contactName, String contactTel) {

        if (StringUtils.isNotBlank(pay) && "1".equals(pay)) {
            pay = "alipay";
        } else {
            pay = "weixin";
        }
        String api = null;
        String host = null;
        String port = null;
        boolean bflag = true;
        if (null != source) {
            if (1L == source) {
                api = ThirdAPI.CPIC_PAY;
            } else if (2L == source) {
                //api = ThirdAPI.PAIC_PAY;
                bflag = false;
            } else if (4L == source) {
                api = ThirdAPI.PICC_PAY;
            } else {
                int is = TwoPower.anotherIs2Power(source);
                if (1 == is)
                    return ResultGenerator.genFailResult("待拓展业务");
                else return ResultGenerator.genFailResult("参数异常");
            }
            if (StringUtils.isBlank(createdBy)) {
                return ResultGenerator.genFailResult("未获取账号信息");
            }
            if (bflag) {
                Map map = thirdInsuranceAccountInfoService.findEnbaleAccount(source, "1", createdBy);
                String code = (String) map.get("code");
                if ("200".equals(code)) {
                    ThirdInsuranceAccountInfo accountInfo = (ThirdInsuranceAccountInfo) map.get("data");
                    host = accountInfo.getIp();
                    port = accountInfo.getPort();
                    if (StringUtils.isBlank(host) || StringUtils.isBlank(port)) {
                        return ResultGenerator.genFailResult("参数异常");
                    }
                    if (StringUtils.isBlank(proposalNo)) {
                        return ResultGenerator.genFailResult("参数异常");
                    }
                } else {
                    String msg = (String) map.get("msg");
                    return ResultGenerator.genFailResult(msg);
                }
            } else {
                //查询报价里的支付信息
                QuoteInfo q = quoteInfoService.findBy("quoteId", quoteId);
                String payUrl = q.getPayUrl();
                String paymentNotice = q.getPaymentNotice();
                PayInfoData payinfo = new PayInfoData();
                payinfo.setPayUrl(payUrl);
                payinfo.setPaymentNotice(paymentNotice);//交费通知单
                if (StringUtils.isNotBlank(payUrl)) {
                    payinfo.setPayMsg("获取成功");
                    OrderInfo orderInfo = new OrderInfo();
                    String oid = UUIDS.getDateUUID();
                    payinfo.setOrderId(oid);
                    orderInfo.setOrderId(oid);
                    orderInfo.setPayType("2");//保单订单
                    orderInfo.setPayTypeId(quoteId);
                    orderInfo.setCarInfoId(carInfoId);
                    orderInfo.setPayStatus(0);
                    Date date = DateUtil.getDateToDate(new Date(), "yyyy-MM-dd HH:mm:ss");
                    orderInfo.setCreateTime(date);
                    orderInfo.setPayment("2");//微信支付
                    if (StringUtils.isNotBlank(money)) {
                        money = money.replaceAll(",", "");
                        orderInfo.setPayMoney(new BigDecimal(money));
                    }
                    //  orderInfoMapper.insert(orderInfo);
                    orderInfoService.save(orderInfo);


                } else {
                    payinfo.setPayMsg("获取失败");
                }
                return ResultGenerator.gen("获取成功", payinfo, ResultCode.SUCCESS);
            }
        } else {
            return ResultGenerator.genFailResult("参数异常");
        }

        String URL = host + ":" + port + "/" + api;
        JSONObject json = new JSONObject();
        json.put("proposalNo", proposalNo);
        json.put("pay", pay);
        String sendTime = DateUtil.getDateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
        json.put("sendTime", sendTime);
        HttpResult httpResult = HttpClientUtil.doPost(URL, null, "JSON", PayInfoBean.class, json.toJSONString());
        if (httpResult != null) {
            int code = httpResult.getCode();
            String msg = httpResult.getMessage();
            if (200 == code) {
                String body = httpResult.getBody();
                PayInfoBean bean = (PayInfoBean) httpResult.getT();
                if (bean != null) {
                    String state = bean.getState();
                    String retCode = bean.getRetCode();
//                    if ("1".equals(state) && "0000".equals(retCode)) {
                    if ("1".equals(state)) {
                        String retMsg = bean.getRetMsg();
                        PayInfoData payinfo = bean.getData();
                        if (payinfo != null) {
                            String payUrl = payinfo.getPayUrl();
                            String payTime = payinfo.getPayTime();
                            String checkNo = bean.getCheckNo();//校验码
                            String payNo = bean.getPayNo();//支付号
                            String paymentNotice = payinfo.getPaymentNotice();//交费通知单
                            String serialNo = payinfo.getSerialNo();//流水号
                            retMsg = EncodeUtil.unicodeToString(retMsg);
                            payinfo.setPayMsg(retMsg);
                            String payEndDate = DateUtil.getDateStringFromString(retMsg);
                            payinfo.setPayEndDate(payEndDate);
                            bean.setPaymentNotice(paymentNotice);
                            bean.setSerialNo(serialNo);
                            // PayInfo payinfos = payinfo.getPayInfo();
                            quoteInfoMapper.updatePayInfo(payUrl, payTime, proposalNo, payNo, checkNo, paymentNotice, serialNo, payEndDate, retMsg);
                            OrderInfo orderInfo = new OrderInfo();
                            String oid = UUIDS.getDateUUID();
                            orderInfo.setOrderId(oid);
                            orderInfo.setPayType("2");//保单订单
                            orderInfo.setPayTypeId(quoteId);
                            orderInfo.setCarInfoId(carInfoId);
                            orderInfo.setPayStatus(0);
                            orderInfo.setPayment("2");//微信支付
                            orderInfo.setCreateBy(createdBy);
                            if (StringUtils.isNotBlank(money)) {
                                money = money.replaceAll(",", "");
                                orderInfo.setPayMoney(new BigDecimal(money));
                            }
                            payinfo.setOrderId(oid);
                            //  orderInfoMapper.insert(orderInfo);
                            orderInfoService.save(orderInfo);
                            return ResultGenerator.gen("获取成功", payinfo, ResultCode.SUCCESS);
                        }
                    } else {
                        String retMsg = bean.getRetMsg();
                        retMsg = EncodeUtil.unicodeToString(retMsg);
                        return ResultGenerator.genFailResult(retMsg);
                    }
                }
                return ResultGenerator.genSuccessResult(body);
            } else {
                return ResultGenerator.genFailResult(msg);
            }
        }
        return ResultGenerator.genFailResult("支付信息获取失败");
    }

    @Override
    public Map<String, Object> updatePayInfo(String proposalNo) {
        String time = DateUtil.getDateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
        int code = 1;
        Map<String, Object> result = new HashMap<>();
        if (code > 0) {
            result.put("status", "1");
            result.put("msg", "成功");
        } else {
            result.put("status", "-1");
            result.put("msg", "失败");
        }
        return result;
    }

    @Override
    public Result payCancel(String proposalNo, String createdBy, String quoteId, Long source, String orderId) {
        if (StringUtils.isBlank(createdBy)) {
            return ResultGenerator.genFailResult("未获取账号信息");
        }
        if (null != source) {
            Map map = thirdInsuranceAccountInfoService.findEnbaleAccount(source, "1", createdBy);
            String code = (String) map.get("code");
            if ("200".equals(code)) {
                ThirdInsuranceAccountInfo accountInfo = (ThirdInsuranceAccountInfo) map.get("data");
                String host = accountInfo.getIp();
                String port = accountInfo.getPort();
                String api = "";
                if (1L == source) {
                    api = ThirdAPI.CPIC_PAY_CANCEL;
                } else if (2L == source) {
                    api = ThirdAPI.PAIC_PAY_CANCEL;
                } else if (4L == source) {
                    api = ThirdAPI.PICC_PAY_CANCEL;
                } else {
                    return ResultGenerator.genFailResult("待拓展业务");
                }
                if (StringUtils.isNotBlank(proposalNo)) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("proposalNo", proposalNo);
                    String URL = host + ":" + port + "/" + api;
                    HttpResult httpResult = HttpClientUtil.doPost(URL, null, "JSON", null, jsonObject.toJSONString());
                    int codes = httpResult.getCode();
                    if (200 == codes) {
                        String body = httpResult.getBody();
                        System.out.println("body打印" + body);
                        JSONObject jsonObjects = JSONObject.parseObject(body);
                        if (jsonObjects.containsKey("state")) {
                            String state = jsonObjects.getString("state");
                            String retMsg = jsonObjects.getString("retMsg");
                            retMsg = EncodeUtil.unicodeToString(retMsg);
                            if ("1".equals(state)) {
                                if (StringUtils.isNotBlank(orderId)) {//修改订单状态值
                                    int reslut = orderInfoService.updatePayStatusById(orderId);
                                }
                                if (StringUtils.isNotBlank(retMsg)) {
                                    return ResultGenerator.genSuccessResult(retMsg);
                                }
                                return ResultGenerator.genSuccessResult("作废成功");
                            } else {
                                return ResultGenerator.genFailResult(retMsg);
                            }
                        } else {
                            return ResultGenerator.genFailResult("失败");
                        }
                    } else {
                        String msg = httpResult.getMessage();
                        return ResultGenerator.genFailResult(msg);
                    }

                } else {
                    return ResultGenerator.genFailResult("参数不能为空");
                }


            } else {
                String msg = (String) map.get("msg");
                return ResultGenerator.genFailResult(msg);
            }
        } else {
            return ResultGenerator.genFailResult("请选择作废的保险公司");
        }
        /*}*/
    }


    @Override
    public Map findListByDifferCondition(String quoteId, String createdBy, String carInfoId, String proposalNo) {
        Map<String, Object> map = new HashedMap();
        QuoteInfo quoteInfo = new QuoteInfo();
        quoteInfo.setQuoteId(quoteId);
        quoteInfo.setCreatedBy(createdBy);
        quoteInfo.setCarInfoId(carInfoId);
        quoteInfo.setProposalNo(proposalNo);
        List list = quoteInfoMapper.findListByDifferCondition(quoteInfo);
        String msg = "获取失败";
        String code = "400";
        if (CollectionUtils.isNotEmpty(list)) {
            msg = "获取成功";
            code = "200";
        }
        map.put("msg", msg);
        map.put("code", code);
        map.put("data", list);
        return map;
    }

    @Override
    public Result postPrecisePrice(String personName, String personCardID, String personCardIDType,
                                   String carNo, String carFrameNo, String carEngineNo,
                                   String carFirstRegisterDate, String lists,
                                   String ciBeginDate, String biBeginDate, String carTransDate,
                                   String carVehicleFgwCode, String carInfoId, String createdBy,
                                   Long quoteGroup, Long submitGroup, String isSame, int forceTax) {


        String custKey = ThirdAPI.CUSTKEY;
        String secretKey = ThirdAPI.SECRETKEY;
        int agent = ThirdAPI.AGENT;
        String cityAbbreviation = carNo.substring(0, 2);
        int cityCode = CityCodeEnum.getByCityName(cityAbbreviation);
        int ShowTotalRate = 1;// 是否展示折扣系数0：否 1：是
        int quoteParalelConflict = 0;// 报价并发冲突检查标识：0（默认） 1：检测。
        int ShowRepeatSubmit = 1;//是否展示重复投保信息0：否 1：是
        int ShowEndDate = 1;//是否展示报价截止时间：0否（默认）、1是
        String param = "LicenseNo=" + carNo + "&CarOwnersName="
                + personName + "&QuoteGroup=" + quoteGroup
                + "&SubmitGroup=" + submitGroup + "&CityCode=" + cityCode
                + "&EngineNo=" + carEngineNo + "&CarVin=" + carFrameNo + "&RegisterDate="
                + carFirstRegisterDate + "&ForceTax=" + forceTax + "&QuoteParalelConflict="
                + quoteParalelConflict + "&CustKey=" + custKey + "&Agent="
                + agent + "&ShowRepeatSubmit=" + ShowRepeatSubmit + "&ShowEndDate=" + ShowEndDate + "&ShowTotalRate="
                + ShowTotalRate;

        if (StringUtils.isNotBlank(personCardID)) {
            param += "&IdCard=" + personCardID + "&OwnerIdCardType="
                    + personCardIDType;
        }
        // 商业险起保时间（Unix时间戳格式）单位是秒（如果在单商业的情况下
        // ，此字段必须有值）
        if (StringUtils.isNotBlank(biBeginDate)) {
            biBeginDate = DateUtil.dateToStamp(biBeginDate);
            param += "&BizTimeStamp=" + biBeginDate;
        }

        // 交强险起保时间（Unix时间戳格式）单位是秒
        if (StringUtils.isNotBlank(ciBeginDate)) {
            ciBeginDate = DateUtil.dateToStamp(ciBeginDate);//
            param += "&ForceTimeStamp=" + ciBeginDate;
        }
        double PurchasePrice = 0;
        double ExhaustScale = 0;
        String AutoMoldCode = "";
        int SeatCount = 0;
        long VehicleSource = 0;
        if (StringUtils.isNotBlank(carVehicleFgwCode)) {
            String[] modelArry = carVehicleFgwCode.split("/");
            if (modelArry.length > 1) {
                AutoMoldCode = modelArry[0];
                carVehicleFgwCode = modelArry[1];
                PurchasePrice = Double.parseDouble(modelArry[3]);
                ExhaustScale = Double.parseDouble(modelArry[4]);
                SeatCount = Integer.parseInt(modelArry[5]);
                if (modelArry[7].equals("人保")) {
                    VehicleSource = 4;
                }
                param += "&AutoMoldCode=" + AutoMoldCode + "&PurchasePrice="
                        + PurchasePrice + "&ExhaustScale=" + ExhaustScale
                        + "&SeatCount=" + SeatCount + "&VehicleSource="
                        + VehicleSource;
            }
            param += "&MoldName=" + carVehicleFgwCode;
        }
        //if("0".equels(isSame)){
        param += "&InsuredName=" + personName + "&InsuredIdCard=" + personCardID + "&InsuredIdType=" + personCardIDType +//被保人
                "&HolderName=" + personName + "&HolderIdCard=" + personCardID + "&HolderIdType=" + personCardIDType;//投保人
        //}
        //报价险种信息

        if (StringUtils.isNotBlank(lists)) {
            param += lists;
        } else {
            lists = "&BoLi=0.0&BuJiMianCheSun=1.0&BuJiMianDaoQiang=1.0&BuJiMianSanZhe=1.0&BuJiMianChengKe=0.0&BuJiMianSiJi=0.0&BuJiMianHuaHen=0.0&BuJiMianSheShui=0.0&BuJiMianZiRan=0.0&BuJiMianJingShenSunShi=0.0&SheShui=0.0&HuaHen=0.0&SiJi=0.0&ChengKe=0.0&CheSun=1.0&DaoQiang=1.0&SanZhe=1000000.0&ZiRan=0.0&HcSanFangTeYue=0.0&HcXiuLiChang=0.0";
            param += lists;
        }
        String SecCode = MD5Utils.md5(param + secretKey);
        param = param + "&SecCode=" + SecCode;
        param = param.replaceAll(" ", "%20");
        try {
            String URL = ThirdAPI.BIHUURL + ThirdAPI.PostPrecisePrice;
            HttpResult result = HttpClientUtil.doGet(URL + param, null);
            int code = result.getCode();
            String body = result.getBody();
            String message = "";
            if (code == 200) {//请求成功

                JSONObject jsonObject = JSONObject.parseObject(body);
                int businessStatus = jsonObject.getIntValue("BusinessStatus");// 1请求报价/核保信息成功，<0失败
                String statusMessage = jsonObject.getString("StatusMessage");// 请求报价核保信息描述
                if (1 == businessStatus) {
                    code = 200;
                } else {
                    code = businessStatus;
                }
                message = statusMessage;
            } else {
                message = "报价核保准备失败，错误代码值：" + code;
            }
            return ResultGenerator.gen(message, body, code);
        } catch (Exception e) {
            logger.error("打印异常", e);
            return ResultGenerator.gen("调用异常", "", 500);
        }
    }

    @Override
    public Map getPrecisePrice(String licenseNo, Long quoteGroup, String createBy, String carInfoId) {
        Map resultMap = new HashedMap();
        if (StringUtils.isBlank(licenseNo)) {
            resultMap.put("code", "18000");
            resultMap.put("msg", "参数异常：车牌号必传");
            resultMap.put("data", "");
            return resultMap;
        }
        if (null == quoteGroup) {
            resultMap.put("code", "18000");
            resultMap.put("msg", "参数异常：意向投保公司值必传");
            resultMap.put("data", "");
            return resultMap;
        }
        int agent = ThirdAPI.AGENT;
        String custKey = ThirdAPI.CUSTKEY;
        String secretKey = ThirdAPI.SECRETKEY;
      /*  int timeFormat = 0;//按照实时起保返回到期日期（商业/交强）0（默认）：否 1：是
        int showEmail = 0;//是否展示邮箱：1：是 0：否（默认）
        int showCarInfo = 0;//是否展示其他业务信息 0：否  1：是
        int renewalCarType = 0; //大小号牌：0小车，1大车，默认0*/
        int showVehicleInfo = 1;//是否展示车型信息:0 否   1：是
        int showTotalRate = 1;//是否展示折扣系数0：否 1：是
        int showRepeatSubmit = 1; //是否展示重复投保信息0：否 1：是
        int showEndDate = 1; //是否展示报价截止时间：0否（默认）、1是
        int showXiuLiChangType = 1;//是否展示修理厂类型0（默认）:否  1：是
        int showFybc = 1;//是否展示补偿费用0:否 1：是
        int showSheBei = 1;//是否展示新增设备0:否 1:是

        String param = "LicenseNo=" + licenseNo + "&QuoteGroup=" + quoteGroup
                + "&ShowRepeatSubmit=" + showRepeatSubmit + "&ShowEndDate=" + showEndDate
                + "&ShowTotalRate=" + showTotalRate + "&Agent=" + agent
                + "&CustKey=" + custKey + "&ShowVehicleInfo=" + showVehicleInfo
                + "&ShowXiuLiChangType=" + showXiuLiChangType
                + "&ShowSheBei=" + showSheBei + "&ShowFybc=" + showFybc;
        String SecCode = MD5Utils.md5(param + secretKey);
        param = param + "&SecCode=" + SecCode;
        param = param.replaceAll(" ", "%20");
        String uuid = UUIDS.getDateUUID();
        QuoteInfo qpc = new QuoteInfo(uuid);//报价对象
        qpc.setCarInfoId(carInfoId);
        qpc.setCreatedBy(createBy);
        Date nowDate = new Date();
        String sendTime = DateUtil.getDateToString(nowDate, "yyyy-MM-dd HH:mm:ss");
        nowDate = DateUtil.getDateToDate(nowDate, "yyyy-MM-dd HH:mm:ss");
        qpc.setCreatedTime(nowDate);
        String insuranceName = InsuranceNameEnum.getName(quoteGroup);
        qpc.setQuoteInsuranceName(insuranceName);
        qpc.setQuoteSource(quoteGroup + "");
        PCICResponseBean bean = new PCICResponseBean();
        bean.setSendTime(sendTime);
        try {
            String URL = ThirdAPI.BIHUURL + ThirdAPI.GetSpecialPrecisePrice;
            HttpResult result = HttpClientUtil.doGet(URL + param, null);
            String body = result.getBody();
            int code = result.getCode();
            String message = "";
            if (200 == code) {
                if (StringUtils.isNotBlank(body)) {
                    JsonRootBean javaBean = JSONObject.parseObject(body, JsonRootBean.class);
                    int status = javaBean.getBusinessStatus();
                    String msg = javaBean.getStatusMessage();
                    if (1 != status) {//请求失败
                        qpc.setQuoteStatus(0);//报价失败
                        qpc.setSubmitStatus(-1);//未核保
                        qpc.setSubmitresult("报价失败无法核保");//未核保
                        qpc.setQuoteResult(msg);//报价失败描述
                        bean.setRetMsg("报价失败,错误代码" + status);
                        bean.setRetCode("0099");
                        bean.setState("0");
                        bean.setSendTime(sendTime);
                        resultMap.put("code", "400");
                        resultMap.put("msg", msg);
                        resultMap.put("data", qpc);
                        resultMap.put("quoteId", uuid);
                        quoteInfoMapper.insert(qpc);
                        return resultMap;
                    }
                    //请求成功
                    com.bzs.utils.bihujsontobean.Item item = javaBean.getItem();
                    com.bzs.utils.bihujsontobean.UserInfo userInfo = javaBean.getUserInfo();

                    int quoteStatus = item.getQuoteStatus();////报价状态，-1=未报价， 0=报价失败，>0报价成功
                    String quoteResult = item.getQuoteResult();
                    qpc.setQuoteResult(quoteResult);
                    qpc.setQuoteStatus(quoteStatus);
                    // 0：不重复 1：交强重复 2：商业重复 3:双险都重复投保
                    String repeatSubmitResult = item.getRepeatSubmitResult();
                    qpc.setRepeatSubmitResult(repeatSubmitResult);
                    String repeatInsurance = null;
                    if ("1".equals(repeatSubmitResult)) {
                        repeatInsurance = "交强重复";
                    } else if ("2".equals(repeatSubmitResult)) {
                        repeatInsurance = "商业重复";
                    } else if ("3".equals(repeatSubmitResult)) {
                        repeatInsurance = "双险都重复投保";
                    } else if ("0".equals(repeatSubmitResult)) {
                        repeatInsurance = "不重复";
                    }
                    qpc.setSubmitresult(repeatSubmitResult);//添加是否重复
                    //报价失败
                    if (quoteStatus <= 0) {
                        logger.info("报价失败>>>" + quoteResult);

                        qpc.setQuoteStatus(0);//报价失败
                        qpc.setSubmitStatus(-1);//未核保
                        qpc.setSubmitresult("报价失败无法核保");//未核保
                        qpc.setQuoteResult("报价失败：" + quoteResult);//报价失败描述
                        if (repeatInsurance != null) {
                            qpc.setQuoteResult("报价失败：" + repeatInsurance + ":" + quoteResult);//信息描述
                        } else {
                            qpc.setQuoteResult("报价失败：" + quoteResult);//信息描述
                        }
                        quoteInfoMapper.insert(qpc);
                        bean.setRetCode("0099");
                        bean.setState("0");
                        bean.setRetMsg("报价失败：" + quoteResult);
                        resultMap.put("code", "400");
                        resultMap.put("msg", "报价失败");
                        resultMap.put("data", qpc);
                        resultMap.put("quoteId", uuid);
                        return resultMap;
                    }
                    ResponseData data = new ResponseData();

                    //报价成功开始执行
                    logger.info("报价状态：" + quoteStatus + "，报价信息：" + quoteResult);
                    System.out.println("报价状态：" + quoteStatus + "，报价信息：" + quoteResult);
                    String autoMoldCode = userInfo.getAutoMoldCode();//精友码
                    String forceStartDate = userInfo.getForceStartDate();//(这次报价)交强险起保日期（报价成功即可返回）
                    String forceExpireDate = userInfo.getForceExpireDate();//(去年)交强险到期时间（报价成功即可返回）
                    String businessStartDate = userInfo.getBusinessStartDate();//((这次报价)商业险起保日期（报价成功即可返回）
                    String businessExpireDate = userInfo.getBusinessExpireDate();//（去年）商业险到期时间(准确率有问题，只有平安成功时才会返回内容)
                    String ForceEndDate = userInfo.getForceEndDate();//(这次报价)商业险截止日期（报价成功即可返回，需要请求参数ShowEndDate=1拉取）
                    String BusinessEndDate = userInfo.getBusinessEndDate();//(这次报价)交强险截止日期（报价成功即可返回，需要请求参数ShowEndDate=1拉取）
                    String vehicleInfo = userInfo.getVehicleInfo();
                    data.setCarModel(vehicleInfo);
                    qpc.setCarModel(vehicleInfo);//车型信息
                    double bizTotal = item.getBizTotal();
                    long buid = item.getBuId();// 获取支付接口必备参数
                    double forceTotal = item.getForceTotal();
                    double taxTotal = item.getTaxTotal();
                    double total = bizTotal + forceTotal + taxTotal;
                    long source = item.getSource();
                    double cheSunBaoE = item.getCheSun().getBaoE();
                    double cheSunBaoFei = item.getCheSun().getBaoFei();
                    double sanZheBaoE = item.getSanZhe().getBaoE();
                    double sanZheBaoFei = item.getSanZhe().getBaoFei();
                    double daoQiangBaoE = item.getDaoQiang().getBaoE();
                    double daoQiangBaoFei = item.getDaoQiang().getBaoFei();
                    double siJiBaoE = item.getSiJi().getBaoE();
                    double siJiBaoFei = item.getSiJi().getBaoFei();
                    double chengKeBaoE = item.getChengKe().getBaoE();
                    double chengKeBaoFei = item.getChengKe().getBaoFei();
                    double boLiBaoE = item.getBoLi().getBaoE();
                    double boLiBaoFei = item.getBoLi().getBaoFei();
                    double huaHenBaoE = item.getHuaHen().getBaoE();
                    double huaHenBaoFei = item.getHuaHen().getBaoFei();
                    double sheShuiBaoE = item.getSheShui().getBaoE();
                    double sheShuiBaoFei = item.getSheShui().getBaoFei();
                    double ziRanBaoE = item.getZiRan().getBaoE();
                    double ziRanBaoFei = item.getZiRan().getBaoFei();

                    double buJiMianSanZheBaoE = item.getBuJiMianSanZhe().getBaoE();
                    double buJiMianSanZheBaoFei = item.getBuJiMianSanZhe().getBaoFei();
                    double buJiMianCheSunBaoE = item.getBuJiMianCheSun().getBaoE();
                    double buJiMianCheSunBaoFei = item.getBuJiMianCheSun().getBaoFei();
                    double buJiMianDaoQiangBaoE = item.getBuJiMianDaoQiang().getBaoE();
                    double buJiMianDaoQiangBaoFei = item.getBuJiMianDaoQiang().getBaoFei();
                    double buJiMianChengKeBaoE = item.getBuJiMianChengKe().getBaoE();
                    double buJiMianChengKeBaoFei = item.getBuJiMianChengKe().getBaoFei();
                    double buJiMianSiJiBaoE = item.getBuJiMianSiJi().getBaoE();
                    double buJiMianSiJiBaoFei = item.getBuJiMianSiJi().getBaoFei();
                    double buJiMianHuaHenBaoE = item.getBuJiMianHuaHen().getBaoE();
                    double buJiMianHuaHenBaoFei = item.getBuJiMianHuaHen().getBaoFei();
                    double buJiMianSheShuiBaoE = item.getBuJiMianSheShui().getBaoE();
                    double buJiMianSheShuiBaoFei = item.getBuJiMianSheShui().getBaoFei();
                    double buJiMianZiRanBaoE = item.getBuJiMianZiRan().getBaoE();
                    double buJiMianZiRanBaoFei = item.getBuJiMianZiRan().getBaoFei();
                    double BuJiMianJingShenSunShiBaoE = item.getBuJiMianJingShenSunShi().getBaoE();
                    double BuJiMianJingShenSunShiBaoFei = item.getBuJiMianJingShenSunShi().getBaoFei();
                    double hcJingShenSunShiBaoE = item.getHcJingShenSunShi().getBaoE();
                    double hcJingShenSunShiBaoFei = item.getHcJingShenSunShi().getBaoFei();
                    double hcSanFangTeYueBaoE = item.getHcSanFangTeYue().getBaoE();
                    double hcSanFangTeYueBaoFei = item.getHcSanFangTeYue().getBaoFei();
                    double hcXiuLiChangBaoE = item.getHcXiuLiChang().getBaoE();

                    double hcXiuLiChangBaoFei = item.getHcXiuLiChang().getBaoFei();
                    String hcXiuLiChangType = item.getHcXiuLiChangType();

                    double fybcBaoE = item.getFybc().getBaoE();
                    double fybcBaoFei = item.getFybc().getBaoFei();
                    int jiaoQiang = item.getJiaoQiang();


                    double fybcDaysBaoE = item.getFybcDays().getBaoE();
                    double fybcDaysBaoFei = item.getFybcDays().getBaoFei();
                    // hcXiuLiChangTypes=quoteInfos.getString("HcXiuLiChangType");
                    double rateFactor1 = item.getRateFactor1();//费率系数1（无赔款优惠系数）
                    double rateFactor2 = item.getRateFactor2();//费率系数2（自主渠道系数）
                    double rateFactor3 = item.getRateFactor3();//费率系数3（自主核保系数）
                    double rateFactor4 = item.getRateFactor4();//费率系数4（交通违法浮动系数）
                    String totalRate = item.getTotalRate();//折扣系数
                    qpc.setBizTotal(BigDecimal.valueOf(bizTotal));
                    qpc.setBizPremiumByDis(bizTotal + "");
                    data.setBiPremiumByDis(bizTotal + "");
                    qpc.setForceTotal(BigDecimal.valueOf(forceTotal));
                    qpc.setNoReparationSaleRate(rateFactor1 + "");//无赔款优惠系数
                    qpc.setIndependentChannelDate(rateFactor2 + "");//自主渠道系数
                    qpc.setIndependentSubmitRate(rateFactor3 + "");//自主核保系数
                    qpc.setTrafficIllegalRate(rateFactor4 + "");//交通违法浮动系数
                    qpc.setTotalRate(totalRate);//折扣系数
                   /* if(){

                    }*/
                    //qpc.setQuoteSource(source + "");

                    data.setNonClaimDiscountRate(rateFactor1 + "");
                    data.setTrafficTransgressRate(rateFactor4 + "");
                    data.setUnderwritingRate(rateFactor3 + "");
                    data.setChannelRate(rateFactor2 + "");
                    qpc.setTaxTotal(BigDecimal.valueOf(taxTotal));
                    data.setCiPremium(forceTotal + "");
                    data.setCiBeginDate(forceStartDate);
                    data.setCarshipTax(taxTotal + "");
                    data.setBiBeginDate(businessStartDate);
                    qpc.setBizStartTime(businessStartDate);
                    qpc.setForceStartTime(forceStartDate);
                    int forceTax = 0;
                    /*if (bizTotal > 0 && forceTotal > 0) {//1：商业+交强车船
                        forceTax = 1;
                    } else if (bizTotal > 0) {//0:单商业
                        forceTax = 0;
                    } else if (forceTotal > 0) {//2：单交强+车船
                        forceTax = 2;
                    }*/
                    if (jiaoQiang > 0) {
                        forceTax = 1;
                    }
                    ArrayList<InsuranceTypeInfo> arrayList = new ArrayList<>();
                    InsuranceTypeInfo insuranceTypeInfo = null;
                    if (forceTax >= 1) {
                        insuranceTypeInfo = new InsuranceTypeInfo(nowDate, createBy, "1", uuid);
                        insuranceTypeInfo.setInsuranceName("交强险");
                        insuranceTypeInfo.setInsuranceAmount(BigDecimal.valueOf(1));
                        arrayList.add(insuranceTypeInfo);
                    }
                    if (cheSunBaoE > 0) {//车损险
                        insuranceTypeInfo = new InsuranceTypeInfo(nowDate, createBy, "1", uuid);

                        insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("A"));
                        BigDecimal bigDecimal = new BigDecimal(cheSunBaoE);
                        insuranceTypeInfo.setInsuranceAmount(bigDecimal);
                        insuranceTypeInfo.setInsurancePremium(new BigDecimal(cheSunBaoFei));
                        arrayList.add(insuranceTypeInfo);
                        if (buJiMianCheSunBaoE > 0) {//不计免
                            insuranceTypeInfo = new InsuranceTypeInfo(nowDate, createBy, "1", uuid);
                            insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("MA"));
                            insuranceTypeInfo.setInsuranceAmount(new BigDecimal(buJiMianCheSunBaoE));
                            insuranceTypeInfo.setInsurancePremium(new BigDecimal(buJiMianCheSunBaoFei));
                            arrayList.add(insuranceTypeInfo);
                        }
                    }
                    if (sanZheBaoE > 0) {//三者
                        insuranceTypeInfo = new InsuranceTypeInfo(nowDate, createBy, "1", uuid);
                        insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("B"));
                        insuranceTypeInfo.setInsuranceAmount(new BigDecimal(sanZheBaoE));
                        insuranceTypeInfo.setInsurancePremium(new BigDecimal(sanZheBaoFei));
                        arrayList.add(insuranceTypeInfo);
                        if (buJiMianSanZheBaoE > 0) {//不计免
                            insuranceTypeInfo = new InsuranceTypeInfo(nowDate, createBy, "1", uuid);
                            insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("MB"));
                            insuranceTypeInfo.setInsuranceAmount(new BigDecimal(buJiMianSanZheBaoE));
                            insuranceTypeInfo.setInsurancePremium(new BigDecimal(buJiMianSanZheBaoFei));
                            arrayList.add(insuranceTypeInfo);
                        }
                    }
                    if (daoQiangBaoE > 0) {//盗抢险
                        insuranceTypeInfo = new InsuranceTypeInfo(nowDate, createBy, "1", uuid);
                        insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("G1"));
                        insuranceTypeInfo.setInsuranceAmount(new BigDecimal(daoQiangBaoE));
                        insuranceTypeInfo.setInsurancePremium(new BigDecimal(daoQiangBaoFei));
                        arrayList.add(insuranceTypeInfo);
                        if (buJiMianDaoQiangBaoE > 0) {//不计免
                            insuranceTypeInfo = new InsuranceTypeInfo(nowDate, createBy, "1", uuid);

                            insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("MG1"));
                            insuranceTypeInfo.setInsuranceAmount(new BigDecimal(buJiMianDaoQiangBaoE));
                            insuranceTypeInfo.setInsurancePremium(new BigDecimal(buJiMianDaoQiangBaoFei));
                            arrayList.add(insuranceTypeInfo);
                        }
                    }
                    if (siJiBaoE > 0) {//司机险
                        insuranceTypeInfo = new InsuranceTypeInfo(nowDate, createBy, "1", uuid);
                        insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("D3"));
                        insuranceTypeInfo.setInsuranceAmount(new BigDecimal(siJiBaoE));
                        insuranceTypeInfo.setInsurancePremium(new BigDecimal(siJiBaoFei));
                        arrayList.add(insuranceTypeInfo);
                        if (buJiMianSiJiBaoE > 0) {//不计免
                            insuranceTypeInfo = new InsuranceTypeInfo(nowDate, createBy, "1", uuid);
                            insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("MD3"));
                            insuranceTypeInfo.setInsuranceAmount(new BigDecimal(buJiMianSiJiBaoE));
                            insuranceTypeInfo.setInsurancePremium(new BigDecimal(buJiMianSiJiBaoFei));
                            arrayList.add(insuranceTypeInfo);
                        }
                    }
                    if (chengKeBaoE > 0) {//乘客险
                        insuranceTypeInfo = new InsuranceTypeInfo(nowDate, createBy, "1", uuid);
                        insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("D4"));
                        insuranceTypeInfo.setInsuranceAmount(new BigDecimal(chengKeBaoE));
                        insuranceTypeInfo.setInsurancePremium(new BigDecimal(chengKeBaoFei));
                        arrayList.add(insuranceTypeInfo);
                        if (buJiMianChengKeBaoE > 0) {//不计免
                            insuranceTypeInfo = new InsuranceTypeInfo(nowDate, createBy, "1", uuid);

                            insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("MD4"));
                            insuranceTypeInfo.setInsuranceAmount(new BigDecimal(buJiMianChengKeBaoE));
                            insuranceTypeInfo.setInsurancePremium(new BigDecimal(buJiMianChengKeBaoFei));
                            arrayList.add(insuranceTypeInfo);
                        }
                    }
                    if (boLiBaoE > 0) {//玻璃
                        insuranceTypeInfo = new InsuranceTypeInfo(nowDate, createBy, "1", uuid);
                        insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("F"));
                        insuranceTypeInfo.setInsuranceAmount(new BigDecimal(boLiBaoE));
                        insuranceTypeInfo.setInsurancePremium(new BigDecimal(boLiBaoFei));
                        arrayList.add(insuranceTypeInfo);

                    }
                    if (huaHenBaoE > 0) {//划痕险
                        insuranceTypeInfo = new InsuranceTypeInfo(nowDate, createBy, "1", uuid);
                        insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("L"));
                        insuranceTypeInfo.setInsuranceAmount(new BigDecimal(huaHenBaoE));
                        insuranceTypeInfo.setInsurancePremium(new BigDecimal(huaHenBaoFei));
                        arrayList.add(insuranceTypeInfo);
                        if (buJiMianHuaHenBaoE > 0) {//不计免
                            insuranceTypeInfo = new InsuranceTypeInfo(nowDate, createBy, "1", uuid);


                            insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("ML"));
                            insuranceTypeInfo.setInsuranceAmount(new BigDecimal(buJiMianHuaHenBaoE));
                            insuranceTypeInfo.setInsurancePremium(new BigDecimal(buJiMianHuaHenBaoFei));
                            arrayList.add(insuranceTypeInfo);
                        }
                    }
                    if (sheShuiBaoE > 0) {//涉水险
                        insuranceTypeInfo = new InsuranceTypeInfo(nowDate, createBy, "1", uuid);

                        insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("X1"));
                        insuranceTypeInfo.setInsuranceAmount(new BigDecimal(sheShuiBaoE));
                        insuranceTypeInfo.setInsurancePremium(new BigDecimal(sheShuiBaoFei));
                        arrayList.add(insuranceTypeInfo);
                        if (buJiMianSheShuiBaoE > 0) {//不计免
                            insuranceTypeInfo = new InsuranceTypeInfo(nowDate, createBy, "1", uuid);

                            insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("MX1"));
                            insuranceTypeInfo.setInsuranceAmount(new BigDecimal(buJiMianSheShuiBaoE));
                            insuranceTypeInfo.setInsurancePremium(new BigDecimal(buJiMianSheShuiBaoE));
                            arrayList.add(insuranceTypeInfo);
                        }
                    }
                    if (ziRanBaoE > 0) {//自燃险
                        insuranceTypeInfo = new InsuranceTypeInfo(nowDate, createBy, "1", uuid);

                        insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("Z"));
                        insuranceTypeInfo.setInsuranceAmount(new BigDecimal(ziRanBaoE));
                        insuranceTypeInfo.setInsurancePremium(new BigDecimal(ziRanBaoFei));
                        arrayList.add(insuranceTypeInfo);
                        if (buJiMianZiRanBaoE > 0) {//不计免
                            insuranceTypeInfo = new InsuranceTypeInfo(nowDate, createBy, "1", uuid);

                            insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("MZ"));
                            insuranceTypeInfo.setInsuranceAmount(new BigDecimal(buJiMianZiRanBaoE));
                            insuranceTypeInfo.setInsurancePremium(new BigDecimal(buJiMianZiRanBaoFei));
                            arrayList.add(insuranceTypeInfo);
                        }
                    }
//
                    if (hcJingShenSunShiBaoE > 0) {//精神损失险
                        insuranceTypeInfo = new InsuranceTypeInfo(nowDate, createBy, "1", uuid);

                        insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("R"));
                        insuranceTypeInfo.setInsuranceAmount(new BigDecimal(hcJingShenSunShiBaoE));
                        insuranceTypeInfo.setInsurancePremium(new BigDecimal(hcJingShenSunShiBaoFei));
                        arrayList.add(insuranceTypeInfo);
                        if (BuJiMianJingShenSunShiBaoE > 0) {//不计免
                            insuranceTypeInfo = new InsuranceTypeInfo(nowDate, createBy, "1", uuid);
                            insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("MR"));
                            insuranceTypeInfo.setInsuranceAmount(new BigDecimal(BuJiMianJingShenSunShiBaoE));
                            insuranceTypeInfo.setInsurancePremium(new BigDecimal(BuJiMianJingShenSunShiBaoFei));
                            arrayList.add(insuranceTypeInfo);
                        }
                    }
                    if (hcSanFangTeYueBaoE > 0) {//三方特约险
                        insuranceTypeInfo = new InsuranceTypeInfo(nowDate, createBy, "1", uuid);
                        insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("Z3"));
                        insuranceTypeInfo.setInsuranceAmount(BigDecimal.valueOf(hcSanFangTeYueBaoE));
                        insuranceTypeInfo.setInsurancePremium(BigDecimal.valueOf(hcSanFangTeYueBaoFei));
                        arrayList.add(insuranceTypeInfo);
                    }
                    //hcXiuLiChangBaoE
                    if (hcXiuLiChangBaoE > 0) {//修理厂险
                        insuranceTypeInfo = new InsuranceTypeInfo(nowDate, createBy, "1", uuid);
                        insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("Q3"));
                        insuranceTypeInfo.setInsuranceAmount(BigDecimal.valueOf(hcXiuLiChangBaoE));
                        insuranceTypeInfo.setInsurancePremium(BigDecimal.valueOf(hcXiuLiChangBaoFei));
                        arrayList.add(insuranceTypeInfo);
                    }
                    //指定专修厂类型
                    if ("0".equals(hcXiuLiChangType) || "1".equals(hcXiuLiChangType)) {
                        insuranceTypeInfo = new InsuranceTypeInfo(nowDate, createBy, "1", uuid);
                        insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("Z4"));
                        insuranceTypeInfo.setInsuranceAmount(BigDecimal.valueOf(hcXiuLiChangType.indexOf(hcXiuLiChangType)));
                        arrayList.add(insuranceTypeInfo);
                    }
                    if (fybcBaoE > 0) {//修理期间费用补偿险
                        insuranceTypeInfo = new InsuranceTypeInfo(nowDate, createBy, "1", uuid);
                        insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("Z2"));
                        insuranceTypeInfo.setInsuranceAmount(BigDecimal.valueOf(fybcBaoE));
                        insuranceTypeInfo.setInsurancePremium(BigDecimal.valueOf(fybcBaoFei));
                        arrayList.add(insuranceTypeInfo);
                    }
                    if (fybcDaysBaoE > 0) {//修理期间天数
                        insuranceTypeInfo = new InsuranceTypeInfo(nowDate, createBy, "1", uuid);
                        insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("Z5"));
                        insuranceTypeInfo.setInsuranceAmount(BigDecimal.valueOf(fybcDaysBaoE));
                        insuranceTypeInfo.setInsurancePremium(BigDecimal.valueOf(fybcDaysBaoFei));
                        arrayList.add(insuranceTypeInfo);
                    }
                    data.setInsurancesLists(arrayList);//添加险种


                    // qpc.setHcXiuLiChangType(hcXiuLiChangTypes);
                    qpc.setTotal(BigDecimal.valueOf(total));
                    qpc.setBuid(buid + "");
                    double buJiMianTotal = buJiMianSanZheBaoE
                            + buJiMianSanZheBaoFei + buJiMianCheSunBaoE
                            + buJiMianCheSunBaoFei + buJiMianDaoQiangBaoE
                            + buJiMianDaoQiangBaoFei + buJiMianChengKeBaoE
                            + buJiMianChengKeBaoFei + buJiMianSiJiBaoE
                            + buJiMianSiJiBaoFei + buJiMianHuaHenBaoE
                            + buJiMianHuaHenBaoFei + buJiMianSheShuiBaoE
                            + buJiMianSheShuiBaoFei + buJiMianZiRanBaoE
                            + buJiMianZiRanBaoFei;
                    BigDecimal b = new BigDecimal(buJiMianTotal);
                    qpc.setExcludingDeductibleTotal(b);
                    // 0：不重复 1：交强重复 2：商业重复 3:双险都重复投保
                   /* resultMap.put("code", "1");// 报价成功
                    resultMap.put("data", qpc);*/
                    insuranceTypeInfoMapper.insertBatch(arrayList);
                    bean.setRetCode("0000");
                    bean.setState("1");
                    bean.setRetMsg("报价失败：" + quoteResult);
                    bean.setData(data);
                    resultMap.put("code", "200");
                    resultMap.put("msg", "报价状态：报价成功;报价内容：" + quoteResult);
                    resultMap.put("data", qpc);
                    resultMap.put("dataList", arrayList);
                    resultMap.put("quoteId", uuid);
                    qpc.setQuoteStatus(1);//报价状态
                    qpc.setQuoteResult(quoteResult);//报价信息
                    qpc.setSubmitStatus(-1);//报价状态
                    qpc.setSubmitresult("未核保");//报价信息
                    quoteInfoMapper.insert(qpc);
                    return resultMap;
                } else {
                    resultMap.put("code", "400");// 报价失败
                    resultMap.put("data", qpc);
                    resultMap.put("msg", "报价失败,获取报价内容为空");
                    qpc.setQuoteResult("报价失败,获取报价内容为空");//报价失败描述
                    qpc.setQuoteStatus(0);//报价失败
                    qpc.setSubmitStatus(-1);//未核保
                    qpc.setSubmitresult("未核保");//未核保
                    quoteInfoMapper.insert(qpc);
                    return resultMap;

                }

            } else {
                if (StringUtils.isNotBlank(body)) {
                    message = body;
                } else {
                    message = "报价失败，失败代码" + code;
                }
                resultMap.put("code", "400");
                resultMap.put("msg", message);
                resultMap.put("data", qpc);
                qpc.setQuoteResult("报价失败:" + message);//报价失败描述
                qpc.setQuoteStatus(0);//报价失败
                qpc.setSubmitStatus(-1);//未核保
                qpc.setSubmitresult("未核保");//未核保
                quoteInfoMapper.insert(qpc);
                return resultMap;
            }
        } catch (Exception e) {
            logger.error("打印异常", e);
            resultMap.put("code", "500");
            resultMap.put("msg", "调用异常");
            resultMap.put("data", qpc);
            return resultMap;
        }
    }

    @Override
    public Map<String, Object> getSubmitInfo(String licenseNo, Long submitGroup, String createBy, String carInfoId, String quoteId) {

        Map resultMap = new HashedMap();
        if (StringUtils.isBlank(licenseNo)) {
            resultMap.put("code", "18000");
            resultMap.put("msg", "参数异常：车牌号必传");
            resultMap.put("data", "");
            return resultMap;
        }
        if (null == submitGroup) {
            resultMap.put("code", "18000");
            resultMap.put("msg", "参数异常：意向核保公司值必传");
            resultMap.put("data", "");
            return resultMap;
        }
        if (StringUtils.isBlank(quoteId)) {
            resultMap.put("code", "18000");
            resultMap.put("msg", "参数异常：报价id必传");
            resultMap.put("data", "");
            return resultMap;

        }
        int agent = ThirdAPI.AGENT;
        String custKey = ThirdAPI.CUSTKEY;
        String secretKey = ThirdAPI.SECRETKEY;


        int renewalCarType = 0;//大小号牌：0小车，1大车，默认0
        int showChannel = 1;//是否在展示核保渠道id  0：不展示（默认）  1：展示
        //submitGroup = 4L;
        String param = "LicenseNo=" + licenseNo + "&Agent=" + agent
                + "&CustKey=" + custKey + "&SubmitGroup=" + submitGroup
                + "&RenewalCarType=" + renewalCarType + "&ShowChannel="
                + showChannel;
        String SecCode = MD5Utils.md5(param + secretKey);
        param = param + "&SecCode=" + SecCode;
        param = param.replaceAll(" ", "%20");
        QuoteInfo qpc = new QuoteInfo(quoteId);
        try {
            String URL = ThirdAPI.BIHUURL + ThirdAPI.GetSubmitInfo;//
            HttpResult result = HttpClientUtil.doGet(URL + param, null);
            int code = result.getCode();
            String body = result.getBody();
            String message = result.getMessage();
            int status = 0;
            if (200 == code) {
                if (StringUtils.isNotBlank(body)) {
                    status = JSONObject.parseObject(body).getIntValue("BusinessStatus");// 核保请求状态值1成功，<0失败
                    String statusMessage = JSONObject.parseObject(body).getString("StatusMessage");// 信息描述
                    if (1 == status) {
                        JSONObject item = JSONObject.parseObject(body).getJSONObject("Item");
                        // 0失败1成功2未到期未核保（无需再核保）3=人工审核中
                        // 4=非意向未核保5=报价失败未核保6=核保功能关闭
                        status = item.getIntValue("SubmitStatus");
                        message = item.getString("SubmitResult");
                        if (status == 1) {// 核保成功
                            String bizNo = item.getString("BizNo");
                            String forceNo = item.getString("ForceNo");
                            String bizRate = item.getString("BizRate");
                            String forceRete = item.getString("ForceRate");
                            Integer channelId = item.getIntValue("ChannelId");
                            qpc.setBizNo(bizNo);
                            qpc.setForceNo(forceNo);
                            qpc.setChannelId(channelId + "");
                            qpc.setBizRate(bizRate);
                            qpc.setForceRate(forceRete);
                        } else {
                            code = 400;
                        }
                    } else {
                        message = "核保失败:" + statusMessage;
                        code = 400;
                    }

                } else {
                    code = 400;
                    if (StringUtils.isNotBlank(body)) {
                        message = body;
                    } else {
                        message = "返回内容为空";
                    }

                }
                qpc.setSubmitStatus(status);
                qpc.setSubmitresult(message);
                this.insertOrUpdate(qpc);
                //this.updateByQuoteId(qpc);
                resultMap.put("data", qpc);
                resultMap.put("code", code + "");
                resultMap.put("msg", message);
                return resultMap;
            } else {
                if (StringUtils.isNotBlank(body)) {
                    message = body;
                } else {
                    message = "核保失败";
                }
                resultMap.put("code", "400");
                resultMap.put("msg", message + ",错误代码：" + code);
                qpc.setSubmitStatus(0);
                qpc.setSubmitresult(message);
                resultMap.put("data", qpc);
                return resultMap;
            }
        } catch (Exception e) {
            qpc.setSubmitStatus(0);
            qpc.setSubmitresult("核保异常");
            logger.error("打印异常", e);
            resultMap.put("code", "500");
            resultMap.put("msg", "调用异常");
            resultMap.put("data", qpc);
            return resultMap;

        }

    }

    @Override
    public Map<String, Object> getPayAddress(String carVin, String licenseNo, int payMent, Long source, String bizNo, String forceNo, String buid, String channelId, String quoteId, String createBy,int isGetPayWay,String carInfoId) {
        Map<String, Object> map = new HashMap<>();
        if(2!=payMent){//
            payMent = 1;//1微信支付2pos
        }
        if(1!=isGetPayWay){//
            isGetPayWay = 0;//是否获取链接的支付类型0否1是
        }
        String factCode = "400";
        String message = "";
        if (StringUtils.isBlank(carVin) || StringUtils.isBlank(licenseNo) || StringUtils.isBlank(buid) || StringUtils.isBlank(channelId) || StringUtils.isBlank(quoteId) || null == source) {
            factCode = "18000";
            message = "获取支付信息失败:参数异常";
            map.put("code", "400");
            map.put("msg", message);
            map.put("data", "");
            return map;
        }
        if (StringUtils.isBlank(bizNo) && StringUtils.isBlank(forceNo)) {
            factCode = "18000";
            message = "获取支付信息失败：交强险投保单号和商业投保单号不能同时为空";
            map.put("code", "400");
            map.put("msg", message);
            map.put("data", "");
            return map;
        }
        /*if(StringUtils.isBlank(createBy)){
            factCode="18000";
            message="获取支付信息失败：未获取到账号信息";
        }*/
        /*//根据支付到期日期查看是否需要重新获取
        Map<String, Object> maps = payInfoService.getPayInfoOne(quoteId, source);
        PayInfo payInfo = (PayInfo) maps.get("data");
        if (null != payInfo) {
            Date date = payInfo.getFailureTimeStamp();
            int index = DateUtil.compareDateByGetTime(new Date(), date);
            if (index == 0) {//在期限内
                return maps;
            }
        }*/
        int agent = ThirdAPI.AGENT;
        String custKey = ThirdAPI.CUSTKEY;
        String secretKey = ThirdAPI.SECRETKEY;
        String param = "Agent=" + agent + "&BiztNo=" + bizNo + "&ForcetNo="
                + forceNo + "&CarVin=" + carVin + "&LicenseNo=" + licenseNo
                + "&PayMent=" + payMent + "&Source=" + source + "&ChannelId="
                + channelId + "&BuId=" + buid + "&IsGetPayWay=" + isGetPayWay;
        String SecCode = MD5Utils.md5(param + secretKey);
        param = param + "&SecCode=" + SecCode;
        param = param.replaceAll(" ", "%20");
        try{
            String ULR=ThirdAPI.PayAddressURL;
            HttpResult httpResult=   HttpClientUtil.doGet(ULR+param,null);
            int code=httpResult.getCode();
            String body=httpResult.getBody();
            message=httpResult.getMessage();
            if(200==code){//请求成功
                JSONObject json=JSONObject.parseObject(body);
                int BusinessStatus=json.getIntValue("BusinessStatus");
                String payAddessStatusMessage = json.getString("StatusMessage");
                if (BusinessStatus == 1) {
                    JSONObject data=json.getJSONObject("Data");
                    if(data!=null){
                        String payUrl=data.getString("PayUrl");//人保支付地址
                        String name=data.getString("Name");//车主
                        String payNum=null;
                        if(null!=data.get("PayNum")){
                            payNum=data.getString("PayNum");//流水号
                        }
                        String transactionNum=data.getString("TransactionNum");//交易单号
                        String failureTimeStamp=data.getString("FailureTimeStamp");//支付链接的截止日期
                        Double money=data.getDouble("Money");
                        if(StringUtils.isNotBlank(failureTimeStamp)){
                            failureTimeStamp= DateUtil.stampToDate(failureTimeStamp,"yyyy-MM-dd HH:mm:ss");
                        }
                        String nowdate=DateUtil.getDateToString(new Date(),"yyyy-MM-dd HH:mm:ss");
                        QuoteInfo qpc=new QuoteInfo(quoteId);
                        qpc.setPayUrl(payUrl);
                        qpc.setPayTime(nowdate);
                        qpc.setPayMsg(payAddessStatusMessage);
                        qpc.setPayEndDate(failureTimeStamp);
                        qpc.setCheckNo(payNum);
                        qpc.setPaymentNotice(transactionNum);
                        quoteInfoMapper.insertOrUpdate(qpc);
                       // (payUrl, nowdate, null, null, null, transactionNum, payNum, failureTimeStamp, payAddessStatusMessage);
                        OrderInfo orderInfo = new OrderInfo();
                        String oid = UUIDS.getDateUUID();
                        orderInfo.setOrderId(oid);
                        orderInfo.setPayType("2");//保单订单
                        orderInfo.setPayTypeId(quoteId);
                        orderInfo.setCarInfoId(carInfoId);
                        orderInfo.setPayStatus(0);
                        orderInfo.setPayment("2");//微信支付
                        orderInfo.setCreateBy(createBy);
                        orderInfo.setPayMoney(new BigDecimal(money));
                        orderInfoService.save(orderInfo);
                        map.put("code", "200");
                        map.put("msg", "查询成功");
                        map.put("data", orderInfo);
                        return map;
                    }else{
                        map.put("code", "400");
                        map.put("msg", "查询失败");
                        map.put("data", "");
                        return map;
                    }

                } else {
                    map.put("code", "400");
                    map.put("msg", "获取支付查信息失败:"+payAddessStatusMessage);
                    map.put("data", null);
                    return map;
                }

            }else{
                map.put("code", "400");
                map.put("msg", "获取支付查信息失败:请求异常");
                map.put("data", "");
                return map;

            }
        }catch (Exception e){
            map.put("code", "500");
            map.put("msg", "请求异常");
            map.put("data", "");
            return map;
        }
    }

    @Override
    public int insertOrUpdate(QuoteInfo quoteInfo) {
        return quoteInfoMapper.insertOrUpdate(quoteInfo);
    }

    @Override
    public int updateByQuoteId(QuoteInfo quoteInfo) {
        return quoteInfoMapper.updateByQuoteId(quoteInfo);
    }
}
