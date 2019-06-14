package com.bzs.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bzs.dao.AccountInfoMapper;
import com.bzs.dao.InsuranceTypeInfoMapper;
import com.bzs.dao.OrderInfoMapper;
import com.bzs.dao.QuoteInfoMapper;
import com.bzs.model.*;
import com.bzs.model.CarInfo;
import com.bzs.model.query.SeveralAccount;
import com.bzs.redis.RedisUtil;
import com.bzs.service.*;
import com.bzs.utils.*;
import com.bzs.utils.base64Util.Base64Util;
import com.bzs.utils.bihujsontobean.*;
import com.bzs.utils.bihujsontobean.JsonRootBean;
import com.bzs.utils.commons.BaseContect;
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
import javax.servlet.http.HttpServletRequest;
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
    private AccountInfoMapper accountInfoMapper;
    @Resource
    private RedisUtil redisUtil;
    private CommissionPercentageService commissionPercentageService;

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
                                   Long quoteGroup, Long submitGroup, String isSame, int forceTax, Double purchasePrice) {


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
        if (null != purchasePrice) {//新车购置价
            param += "&PurchasePrice=" + purchasePrice;
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
                        repeatInsurance = "交强险重复";
                    } else if ("2".equals(repeatSubmitResult)) {
                        repeatInsurance = "商业险重复";
                    } else if ("3".equals(repeatSubmitResult)) {
                        repeatInsurance = "双险都重复投保";
                    } else if ("0".equals(repeatSubmitResult)) {
                        //repeatInsurance = "不重复";
                    }
                    qpc.setSubmitresult(repeatSubmitResult);//添加是否重复
                    //报价失败
                    if (quoteStatus <= 0) {
                        logger.info("报价失败>>>" + quoteResult);

                        qpc.setQuoteStatus(0);//报价失败
                        qpc.setSubmitStatus(-1);//未核保
                        // qpc.setSubmitresult("报价失败");//未核保
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
                    //qpc.setSubmitresult("未核保");//报价信息
                    quoteInfoMapper.insert(qpc);
                    return resultMap;
                } else {
                    resultMap.put("code", "400");// 报价失败
                    resultMap.put("data", qpc);
                    resultMap.put("msg", "报价失败,获取报价内容为空");
                    qpc.setQuoteResult("报价失败,获取报价内容为空");//报价失败描述
                    qpc.setQuoteStatus(0);//报价失败
                    qpc.setSubmitStatus(-1);//未核保
                    //qpc.setSubmitresult("未核保");//未核保
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
                //qpc.setSubmitresult("未核保");//未核保
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
    public Map<String, Object> getPayAddress(String carVin, String licenseNo, int payMent, Long source, String bizNo, String forceNo, String buid, String channelId, String quoteId, String createBy, int isGetPayWay, String carInfoId) {
        Map<String, Object> map = new HashMap<>();
        if (2 != payMent) {//
            payMent = 1;//1微信支付2pos
        }
        if (1 != isGetPayWay) {//
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
        String nowdate = DateUtil.getDateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
        List<QuoteInfo> list = quoteInfoMapper.findListByDifferCondition(new QuoteInfo(quoteId));
        if (CollectionUtils.isNotEmpty(list)) {
            QuoteInfo qpc = list.get(0);
            if (null != qpc) {
                String payUrl = qpc.getPayUrl();
                String payEndDate = qpc.getPayEndDate();
                if (StringUtils.isNotBlank(payUrl) && StringUtils.isNotBlank(payEndDate)) {
                    int res = DateUtil.compareDate(payEndDate, nowdate, "yyyy-MM-dd HH:mm:ss");
                    if (res >= 1) {//1大于 0小于2等于-1有空值
                        map.put("code", "200");
                        map.put("msg", "查询成功");
                        map.put("data", qpc);
                        return map;
                    }
                }
            }
        }
        // Map quoteList=this.findListByDifferCondition(quoteId,null,null,null);


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
        try {
            String ULR = ThirdAPI.PayAddressURL;
            HttpResult httpResult = HttpClientUtil.doGet(ULR + param, null);
            int code = httpResult.getCode();
            String body = httpResult.getBody();
            message = httpResult.getMessage();
            if (200 == code) {//请求成功
                JSONObject json = JSONObject.parseObject(body);
                int BusinessStatus = json.getIntValue("BusinessStatus");
                String payAddessStatusMessage = json.getString("StatusMessage");
                if (BusinessStatus == 1) {
                    JSONObject data = json.getJSONObject("Data");
                    if (data != null) {
                        QuoteInfo qpc = new QuoteInfo(quoteId);
                        String payUrl = data.getString("PayUrl");//人保支付地址
                        String name = data.getString("Name");//车主
                        if (null != data.get("PayNum")) {//流水号
                            String payNum = data.getString("PayNum");//太平洋等于校验码
                            qpc.setCheckNo(payNum);//人保  payNum=serialrNo
                        }
                        String transactionNum = data.getString("TransactionNum");//交易通知单号
                        //JFCD-JS201906061213556412082
                        String serialrNo = data.getString("SerialrNo");//交易通知单号
                        qpc.setSerialNo(serialrNo);
                        String failureTimeStamp = data.getString("FailureTimeStamp");//支付链接的截止日期
                        Double money = data.getDouble("Money");
                        //0=保司收款平台  1=pos
                        //2=微信  3=微信公众号 4=H5
                        String payWay = data.getString("PayWay");
                        if (StringUtils.isNotBlank(failureTimeStamp)) {
                            failureTimeStamp = DateUtil.stampToDate(failureTimeStamp, "yyyy-MM-dd HH:mm:ss");
                        }
                        qpc.setPayUrl(payUrl);
                        qpc.setPayTime(nowdate);
                        qpc.setPayMsg(payAddessStatusMessage);
                        qpc.setPayEndDate(failureTimeStamp);
                        qpc.setPaymentNotice(transactionNum);
                        this.updateByQuoteId(qpc);
                        // (payUrl, nowdate, null, null, null, transactionNum, payNum, failureTimeStamp, payAddessStatusMessage);
                        OrderInfo orderInfo = new OrderInfo();
                        String oid = UUIDS.getDateUUID();
                        orderInfo.setOrderId(oid);
                        orderInfo.setPayType("2");//保单订单
                        orderInfo.setPayTypeId(quoteId);
                        orderInfo.setCarInfoId(carInfoId);
                        orderInfo.setPayStatus(0);
                        orderInfo.setPayment(payWay);
                        orderInfo.setCreateBy(createBy);
                        orderInfo.setPayMoney(new BigDecimal(money));
                        orderInfoService.save(orderInfo);
                        map.put("code", "200");
                        map.put("msg", "查询成功");
                        map.put("data", orderInfo);
                        return map;
                    } else {
                        map.put("code", "400");
                        map.put("msg", "查询失败");
                        map.put("data", "");
                        return map;
                    }

                } else {
                    map.put("code", "400");
                    map.put("msg", "获取支付查信息失败:" + payAddessStatusMessage);
                    map.put("data", null);
                    return map;
                }

            } else {
                map.put("code", "400");
                map.put("msg", "获取支付查信息失败:请求异常");
                map.put("data", "");
                return map;

            }
        } catch (Exception e) {
            map.put("code", "500");
            map.put("msg", "请求异常");
            map.put("data", "");
            return map;
        }
    }

    @Override
    public Map<String, Object> getPayInfo(String carVin, String licenseNo, Long source, String buid, String bizNo, String forceNo, String channelId, String transactionNum, String orderId, String createBy, String quoteId) {
        Map<String, Object> map = new HashMap<>();

        String factCode = "400";
        String message = "";
        if (StringUtils.isBlank(carVin) || StringUtils.isBlank(licenseNo) || StringUtils.isBlank(buid) || StringUtils.isBlank(channelId) || StringUtils.isBlank(orderId) || null == source) {
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
        int agent = ThirdAPI.AGENT;
        String secretKey = ThirdAPI.SECRETKEY;
        String param = "Agent=" + agent + "&BiztNo=" + bizNo + "&ForcetNo="
                + forceNo + "&CarVin=" + carVin + "&LicenseNo=" + licenseNo
                + "&Source=" + source + "&ChannelId="
                + channelId + "&BuId=" + buid;
        String SecCode = MD5Utils.md5(param + secretKey);
        param = param + "&SecCode=" + SecCode;
        param = param.replaceAll(" ", "%20");
        try {
            String URL = ThirdAPI.PayResult;
            HttpResult httpResult = HttpClientUtil.doGet(URL + param, null);
            int code = httpResult.getCode();
            String body = httpResult.getBody();
            message = httpResult.getMessage();
            if (200 == code) {
                JSONObject jsonObject = JSONObject.parseObject(body);
                int BusinessStatus = jsonObject.getIntValue("BusinessStatus");
                message = jsonObject.getString("StatusMessage");
                if (BusinessStatus == 1) {
                    int findPayResult = jsonObject.getJSONObject("Data").getIntValue("FindPayResult");
                    String money = jsonObject.getJSONObject("Data").getString("Money");
                    String bizpNo = jsonObject.getJSONObject("Data").getString("BizpNo");
                    String forcepNo = jsonObject.getJSONObject("Data").getString("ForcepNo");
                    map.put("code", "200");

                    Date nowDate=DateUtil.getDateToDate(new Date(),"yyyy-MM-dd");

                    //支付状态0待支付,1完成2取消3过期4作废5已重新获取
                    Integer status = 0;
                    OrderInfo orderInfo = new OrderInfo(orderId);
                    if (findPayResult == 1) {
                        map.put("msg", "支付成功");
                        status = 1;
                        orderInfo.setFinishTime(nowDate);
                        QuoteInfo quoteInfo = quoteInfoService.findBy("quoteId", quoteId);
                        BigDecimal biz= quoteInfo.getBizTotal();//商业险
                        BigDecimal force=  quoteInfo.getForceTotal();//交强险
                        SeveralAccount data = accountInfoMapper.getParentLevel(createBy);//获取父级两层id
                        CommissionPercentage percentage = commissionPercentageService.getLastUpdateData();
                        BigDecimal bp=new BigDecimal(15);
                        BigDecimal fp =  new BigDecimal(4);
                        BigDecimal po =new  BigDecimal(1);
                        BigDecimal pw =new  BigDecimal(0.5);
                        BigDecimal rate=new BigDecimal(1.06);
                        if(null==percentage){
                            bp = new BigDecimal(percentage.getBizPercentage());//商业险百分比
                            fp =  new BigDecimal(percentage.getForcePercentage());//交强险百分比
                            po =  new BigDecimal(percentage.getLevelOne());//父一级提成
                            pw =  new BigDecimal(percentage.getLevelTwo());//父二级提成
                        }

                        int level=data.getLevel();
                        int level1=data.getLevel1();
                        int level2=data.getLevel2();
                        if(level==1){//本人做单，只有佣金
                            BigDecimal balance=  data.getBalanceTotal();//剩余余额
                            BigDecimal commission=   data.getCommissionTotal();//已有佣金
                            BigDecimal bizCommission=biz.divide(rate,2,BigDecimal.ROUND_DOWN).multiply(bp).divide(new BigDecimal(100),2,BigDecimal.ROUND_DOWN);
                            BigDecimal forceCommission=force.divide(rate,2,BigDecimal.ROUND_DOWN).multiply(fp).divide(new BigDecimal(100),2,BigDecimal.ROUND_DOWN);
                            balance= balance.add(bizCommission).add(forceCommission);
                            commission=commission.add(bizCommission).add(forceCommission);
                            DrawCash drawCash=new DrawCash();

                        }
                        if(level1==1){//本人做单，父一级只拿提成
                            String parentLevelOne = data.getbAccountId();
                            BigDecimal balance=    data.getbBalanceTotal();//剩余余额
                            BigDecimal drawPer=  data.getbDrawPercentageTotal();//已有提成
                        }
                        if(level2==2){//本人做单，父二级只拿提成
                            String parentLevelTwo = data.getcAccountId();
                            BigDecimal balance=   data.getcBalanceTotal();//剩余余额
                            BigDecimal drawPer=   data.getcDrawPercentageTotal();//已有提成
                        }







                    } else if (findPayResult == 11) {
                        map.put("msg", "作废");
                        status = 4;
                    } else if (findPayResult == 0) {
                        map.put("msg", "待交费");
                    }
                    orderInfo.setPayStatus(status);
                    orderInfoService.updatePayStatus(orderInfo);
                    map.put("data", body);
                    return map;
                } else {
                    if (BusinessStatus == -1001) {
                        map.put("status", "200");
                        map.put("msg", "获取结果成功:" + message);
                        map.put("data", "");
                        return map;
                    } else {
                        map.put("status", "400");
                        map.put("msg", "获取结果成功" + message);
                        map.put("data", "");
                        return map;
                    }

                }

            } else {
                if (StringUtils.isNotBlank(body)) {
                    message = body;
                } else {
                    message = "请求出错";
                }
                map.put("status", "400");
                map.put("msg", message);
                map.put("data", "");
                return map;
            }
        } catch (Exception e) {
            map.put("status", "400");
            map.put("msg", "请求出错");
            map.put("data", "");
            return map;

        }
    }

    @Override
    public Map<String, Object> doVoidPay(String carVin, String licenseNo, Long source, String buid, String orderId, String bizNo, String transactionNum, String forceNo, String channelId, String payWay, String quoteId, String cancelMsg) {
        String factCode = "400";
        String message = "";
        Map<String, Object> map = new HashMap();
        if (StringUtils.isBlank(carVin) || StringUtils.isBlank(licenseNo) || StringUtils.isBlank(buid) || StringUtils.isBlank(channelId) || StringUtils.isBlank(transactionNum) || StringUtils.isBlank(orderId) || null == source) {
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


        int agent = ThirdAPI.AGENT;
        String secretKey = ThirdAPI.SECRETKEY;
        String param = "Agent=" + agent + "&BiztNo=" + bizNo + "&ForcetNo="
                + forceNo + "&CarVin=" + carVin + "&LicenseNo=" + licenseNo
                + "&Source=" + source + "&ChannelId="
                + channelId + "&BuId=" + buid + "&TransactionNum=" + transactionNum;
        if (1 == source) {//太保
            if (StringUtils.isBlank(payWay)) {//太保必须 6=刷卡、 2=划卡、  1=支票、
                //chinapay=银联电子支付、 weixin=微信支付、5=网银转账、3A=集中支付
                payWay = "weixin";
                param += "&PayWay=" + payWay;
            }
        }
        String SecCode = MD5Utils.md5(param + secretKey);
        param = param + "&SecCode=" + SecCode;
        param = param.replaceAll(" ", "%20");
        try {
            String URL = ThirdAPI.VoidPay;
            HttpResult httpResult = HttpClientUtil.doGet(URL + param, null);
            int code = httpResult.getCode();
            String body = httpResult.getBody();
            message = httpResult.getMessage();
            if (200 == code) {
                int BusinessStatus = JSONObject.parseObject(body).getIntValue("BusinessStatus");
                String StatusMessage = JSONObject.parseObject(body).getString("StatusMessage");
                if (BusinessStatus == 1) {
                    map.put("code", "200");
                    QuoteInfo qpc = new QuoteInfo(quoteId);
                    qpc.setCheckNo(null);//人保  payNum=serialrNo
                    qpc.setSerialNo(null);
                    qpc.setPayUrl(null);
                    qpc.setPayTime(null);
                    qpc.setPayMsg(null);
                    qpc.setPayEndDate(null);
                    qpc.setPaymentNotice(null);
                    this.insertOrUpdate(qpc);
                    // this.insertOrUpdate(qpc);
                    //此处修改订单信息为作废状态
                    System.out.println(cancelMsg);
                    orderInfoService.updatePayStatus(new OrderInfo(orderId, 2, cancelMsg));
                } else {
                    map.put("code", "400");
                }
                map.put("msg", StatusMessage);

            } else {
                map.put("code", "400");
                if (StringUtils.isNotBlank(body)) {
                    map.put("msg", body);
                } else {
                    map.put("msg", "请求异常，错误代码" + code);
                }
            }
            map.put("data", "");
            return map;
        } catch (Exception e) {
            map.put("code", "500");
            map.put("msg", "请求出错，错误代码：500");
            map.put("data", "");
            return null;
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

    @Override
    public Map<String, Object> getContinuedPeriods() {
        int agent = ThirdAPI.AGENT;
        String secretKey = ThirdAPI.SECRETKEY;
        String URL = ThirdAPI.GetContinuedPeriods;
        String param = "Agent=" + agent;
        String SecCode = MD5Utils.md5(param + secretKey);
        param = param + "&SecCode=" + SecCode;
        Map<String, Object> map = new HashMap();
        try {
            HttpResult httpResult = HttpClientUtil.doGet(URL + param, null);
            int code = httpResult.getCode();
            String body = httpResult.getBody();
            String message = httpResult.getMessage();
            if (200 == code) {
                CityChannelJsonBean cityChannelBean = JSONObject.parseObject(body, CityChannelJsonBean.class);
                int businessStatus = cityChannelBean.getBusinessStatus();
                String StatusMessage = cityChannelBean.getStatusMessage();
                List<CityChannelItem> items = null;
                if (1 == businessStatus) {
                    map.put("code", "200");
                    map.put("message", StatusMessage);
                    items = cityChannelBean.getItems();
                  /*  if(CollectionUtils.isNotEmpty(items)){
                        for (CityChannelItem item:items){
                            System.out.println("sss"+item.getCityCode());
                        }
                    }*/
                } else {
                    map.put("code", "400");
                    map.put("message", StatusMessage);
                }
                map.put("data", items);
            } else {
                map.put("code", "400");
                map.put("message", message);
                map.put("data", null);
            }
        } catch (Exception e) {
            map.put("code", "400");
            map.put("message", "请求异常，错误代码:500");
            map.put("data", null);
        }
        return map;
    }

    @Override
    public Map<String, Object> getFirstVehicleInfo(String carVin, String engineNo, String moldName, int cityCode) {
        Map<String, Object> map = new HashMap<>();
        String factCode = "";
        if (StringUtils.isBlank(carVin) || StringUtils.isBlank(engineNo) || StringUtils.isBlank(moldName)) {
            factCode = "18000";
            map.put("code", "400");
            map.put("msg", "获取支付信息失败:参数异常");
            map.put("data", "");
            return map;
        }
        int agent = ThirdAPI.AGENT;
        String secretKey = ThirdAPI.SECRETKEY;
        String custKey = ThirdAPI.CUSTKEY;
        String param = "EngineNo=" + engineNo + "&CarVin=" + carVin
                + "&MoldName=" + moldName + "&CityCode=" + cityCode + "&Agent=" + agent
                + "&CustKey=" + custKey;
        String SecCode = MD5Utils.md5(param + secretKey);
        param = param + "&SecCode=" + SecCode;
        param = param.replaceAll(" ", "%20");
        try {
            String URL = ThirdAPI.GetFirstVehicleInfo;
            HttpResult httpResult = HttpClientUtil.doGet(URL + param, null);
            int code = httpResult.getCode();
            String body = httpResult.getBody();
            String message = httpResult.getMessage();
            List<NewCarVehicleInfoItem> items = null;
            NewCarVehicleInfoJsonBean javaBean = JSONObject.parseObject(body, NewCarVehicleInfoJsonBean.class);

            if (200 == code) {
                int businessStatus = javaBean.getBusinessStatus();
                message = javaBean.getStatusMessage();
                if (1 == businessStatus) {
                    items = javaBean.getItems();
                    if (CollectionUtils.isNotEmpty(items)) {
                        map.put("code", "200");
                    } else {
                        message = "请求成功,但未获取相关信息";
                        map.put("code", "400");
                    }
                } else {
                    map.put("code", "400");
                }
            } else if (202 == code) {
                map.put("code", "400");
                message = javaBean.getStatusMessage();
            } else {
                map.put("code", "400");
            }
            map.put("msg", message);
            map.put("data", items);
            return map;
        } catch (Exception e) {
            map.put("code", "400");
            map.put("msg", "请求异常,错误代码：500");
            map.put("data", null);
            return map;
        }
    }

    @Override
    public Map<String, Object> getModelNameForImportCar(Integer cityCode, String carVin) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(carVin)) {
            map.put("code", "400");
            map.put("msg", "参数异常，不能为空");
            map.put("data", "");
            return map;
        }
        int agent = ThirdAPI.AGENT;
        String custKey = ThirdAPI.CUSTKEY;
        String secretKey = ThirdAPI.SECRETKEY;
        String param = "CarVin=" + carVin
                + "&CityCode=" + cityCode + "&Agent=" + agent
                + "&CustKey=" + custKey;
        String str = param + secretKey;
        String SecCode = MD5Utils.md5(str);
        param = param + "&SecCode=" + SecCode;
        System.out.println(param);
        param = param.replaceAll(" ", "%20");
        try {
            String URL = ThirdAPI.GetModelName;
            HttpResult httpResult = HttpClientUtil.doGet(URL + param, null);
            int code = httpResult.getCode();
            String body = httpResult.getBody();
            String msg = httpResult.getMessage();
            String MoldName = null;
            if (200 == code) {
                int BusinessStatus = JSONObject.parseObject(body).getIntValue("BusinessStatus");
                String StatusMessage = JSONObject.parseObject(body).getString("StatusMessage");
                MoldName = JSONObject.parseObject(body).getString("MoldName");
                if (1 == BusinessStatus) {
                    map.put("code", "200");
                } else {
                    map.put("code", "400");
                }
                msg = StatusMessage;
            } else if (202 == code) {
                msg = JSONObject.parseObject(body).getString("StatusMessage");
                map.put("code", "400");
            } else {
                map.put("code", "400");
                msg = body;
            }
            map.put("msg", msg);
            map.put("data", MoldName);

        } catch (Exception e) {
            map.put("code", "400");
            map.put("msg", "请求异常，错误代买：500");
            map.put("data", null);
        }
        return map;
    }

    @Override
    public Map<String, Object> getCreditDetailInfo(String licenseNo, Integer renewalCarType) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(licenseNo)) {
            map.put("code", "400");
            map.put("msg", "参数异常，车牌不能为空");
            map.put("data", "");
            return map;
        }

        String URL = ThirdAPI.GetCreditDetailInfo;
        String CustKey = ThirdAPI.CUSTKEY;
        int Agent = ThirdAPI.AGENT;
        String secretKey = ThirdAPI.SECRETKEY;
        String param = "LicenseNo=" + licenseNo + "&RenewalCarType=" + renewalCarType + "&Agent=" + Agent
                + "&CustKey=" + CustKey;
        String SecCode = MD5Utils.md5(param + secretKey);
        param = param + "&SecCode=" + SecCode;
        // param = param.replaceAll(" ", "%20");
        try {
            HttpResult httpResult = HttpClientUtil.doGet(URL + param, null);
            int code = httpResult.getCode();
            String body = httpResult.getBody();
            String msg = httpResult.getMessage();
            if (200 == code) {
                CreditDetailInfoJsonBean javaBean = JSONObject.parseObject(body, CreditDetailInfoJsonBean.class);
                int businessStatus = javaBean.getBusinessStatus();
                msg = javaBean.getStatusMessage();
                List<CreditDetailInfoItems> lists = null;
                if (1 == businessStatus) {
                    lists = javaBean.getList();
                    if (CollectionUtils.isNotEmpty(lists)) {
                        map.put("code", "200");
                    } else {
                        msg = "请求成功，但是未查询到信息";
                        map.put("code", "400");
                    }
                } else {
                    map.put("code", "400");
                }
                map.put("msg", msg);
                map.put("data", lists);
            } else {
                map.put("code", "400");
                map.put("msg", "参数异常，车牌不能为空");
                map.put("data", "");
            }
        } catch (Exception e) {
            map.put("code", "400");
            map.put("msg", "参数异常，车牌不能为空");
            map.put("data", "");

        }
        return map;
    }

    @Override
    public Map<String, Object> uploadImgForPingAn(String info, String buid, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(info) || StringUtils.isBlank(buid)) {
            map.put("code", "400");
            map.put("msg", "参数异常，不能为空");
            map.put("data", "");
            return map;
        }
        int agent = ThirdAPI.AGENT;
        String URL = ThirdAPI.UploadMultipleImg;
        String secretKey = ThirdAPI.SECRETKEY;
        String param = "Agent=" + agent + "&BuId=" + buid;
        String SecCode = MD5Utils.md5(param + secretKey);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Agent", agent);
        jsonObject.put("BuId", buid);
        jsonObject.put("SecCode", SecCode);

        List<BaseContect> list = new ArrayList<BaseContect>();
       /* String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        String rUrl = request.getServletContext().getRealPath("/images");*/
        String base64 = "/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEB\n" +
                "AQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/2wBDAQEBAQEBAQEBAQEBAQEBAQEBAQEB\n" +
                "AQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/wAARCACCANIDASIA\n" +
                "AhEBAxEB/8QAHgABAAICAwEBAQAAAAAAAAAAAAgJBwoBAgYFAwT/xABSEAAABgIBAgMEBAkEDA8B\n" +
                "AAABAgMEBQYABxEIEgkTIRQVMVEWQWGBFxkiIyUycZHRQqGxwRgkJiczRVJXcpKW8DU2N1ViZIOE\n" +
                "hZS10tPU4fH/xAAdAQEAAQQDAQAAAAAAAAAAAAAABwIFBggBAwQJ/8QANBEAAgIBAwQBAwMEAQIH\n" +
                "AAAAAQIDBAUABhEHEhMhMQgiQRQVIzJRYXEWwfAXGCQzQrHh/9oADAMBAAIRAxEAPwDf4xjGNNMY\n" +
                "xjTTGMY00xjGNNMYxjTTGMY00xjGNNMYxjTTGMY00xjGNNMYxjTTGMY00xjGNNMYxjTTGMY00xjG\n" +
                "NNMYxjTTGMY00xjGNNMYxjTTGMY00xjGNNMYxjTTGMY00xjGNNMYxjn/AL/7/wBjTTGMY00xjGNN\n" +
                "dePQP5Ihzx9fH8ecpZ6zvE/uXTTvKc1RXtbxVjZwsfEOlZR9IP0VlV5SObyHYCLQOwiSRHRCFExv\n" +
                "MOYhx7QIBTGuoyrbxH7FDaWqURuBLQVE2s5cTbSDs0nYolJd5Fxy7Zz7C6XdooGcnbA6SQYJCsYy\n" +
                "SSrtJMO0D8li7q9LnquzreQwG4jtmbHSpbuZEUWyBFJAwlTwKrNxyyuWCntCkngc6l3ohHtu1v7H\n" +
                "43cu0v8AmlbLQy4+jhf3RcR35KZ4mrS/rHkiQEKksaxs6h2lXjkgA14fju9pB6fgar/y/wCE5n0z\n" +
                "ofxwdmJj2qafrSQ/I8tMFH9xh5/myQHQbu3pr6sbBPUa1dPutahco1iMxGosYRguwlIwjhJsuCSj\n" +
                "tt53tzVVwgZRABHuQP5pAACqASCfi868peut1U+KpFaiaywcVMHKzWHZIMkVFhfugA502yaZDG7S\n" +
                "lKAmATAAB68Zp7uPdXVrE9PT1ExHVmvnsULUVXxwY2OvMHklWJg6yxcoyN77WHJHHH4Ot6dnbM6G\n" +
                "5/qwnSXcHQm/tXPNTmumS1n57cAhjgWxGUNez2yrKvCq6MwBDc+wRrNpPG+2ccvJNOV05fmSVlxD\n" +
                "4/UJeQ+71+vO347zaP8Amar4/wDicz/Vkq/DI0lpa2dI1etl6oVRmX5ZG5KvZeaiI5dbyGFlmEyC\n" +
                "s6dIHMBEWyQF5OftIkQPgUoZBzZPXHqJrtuboOpek3VNqjkbIFUrTmQjG5H1hlRdpxiJm4M0Qbey\n" +
                "v5M4psDAYPNQOiqIlE+XKXPdU8Ztra24s71jr40bsgrS46iMN+qslp1icR9sMTE9ncqswAHLKOdW\n" +
                "iLbfRjMbw3ttLa3093822xrVuvlsl/yc0accdWV4hMZLNlFQyeJ3SPvL9qMf93b9C/VRJ9WWp3t+\n" +
                "ma03q8hG2KRgHTNo4VctTqMkmiwLIqLiZYAEjspTkV4MVQhvyQDjnDXU94o2m+mzYznWD2Asd1sk\n" +
                "WikpOp14rIqMKu5SSctGDtR+6a97xw0XRdlI3BQpG6pBVOU5yFPO3UFVh6tQIBrFVCFo6j6LZSMp\n" +
                "X4Jm3ZsGUq8ZorPkSkbETBUyK5jo+abvUMVIoCcQAuUtdQPQjV7l1lSu5bbu7WEdWn9xqFlnKDYH\n" +
                "6DacVi4JlApP4ZVFV2RIQl0YsxU1VBTKVJ5wKag8HHYLfGR6l4DYO2Y9u5Gle3HZsUoMpmr0cFeG\n" +
                "OvIheW2YLDRoOOF7owrP293Cd3xq308x/R/cXUndk+8MdlMVs6rSyV3B7cxc1u1ZmuRTRR1sb+sq\n" +
                "JLIQymQpMXSMv2hnKk69Z+PE1H/mi2L94wXH/qY/0ZEvqd8YPZ14Crl6bXVr1GDAZE1lVloGkTpp\n" +
                "oFvZPd5EiTMZYytis/Ledx2/sxlTLFBTzQIUCdrB1S9I8LulfW6HSxRpCvNrgyrg2VORYiVdgs6b\n" +
                "M3EsRUqnu8CkMqq5IBnIJggQoqGBTuIFsjbTfh5Lt266kLpZI6yCaopnna8ByGUIU4lEDSADyHPB\n" +
                "vkIfXkK1Mr1S6hU8viMd1k23AaNiOvemp1pMXbhlVgQK9rwwycExkFoXPK+iSD72Fu4bop0qv7ez\n" +
                "uc+n3e9hMnVkvY2tlMjWzmPtQPEik2qJvWYSyLOrrHYjBVyHUd6crWP04+MtYqbTXcZ1DR1w2lbj\n" +
                "Szlw0sMVE1CESTijotQQYHZxLWvM+9Bcro4LC0OscqpQOsfgpSyF/Hiaj9f70Oxv9aC9f2/pP+OS\n" +
                "VuOtPD2rFYnbChV9Oy6sPGu5AsYynK6Lp4LZE6hW6Ae8eAVVMUE0xNwXvMACIAPOVhaM6gukreuw\n" +
                "k9YzfTdRdes7DHSaKNsXfs0iRTwEi+ynUO8URb/yxKYElTn84CAmApic4VXc91T2T+y7et9X9sWb\n" +
                "t2PwY7z1HvWLTqyKgsXGhkCu7FVEk8gB9EngE6ox+3OiPUQbj3bjegm+6eMxjrbzApZSLF06MTr3\n" +
                "yfo8al6AmOKNHcw1ISAPtVSxANvPSJ4g2puriUm63W2EzVrZDNiP/o9Plbe1v4sTkRVk2KrJd02U\n" +
                "bNl1UkHBVFSLJKqk4SMkYhzf29SXiD6M6Yriyol5cTLmxOo/3oqziol67KzZqHAjZRdciXk8uhBb\n" +
                "yypHUEoIH83yx7QNCLw8eh6N0LvOY2GhvDX+wxLU5Kvs4Oou0nL8iEnJxDxWQfEByoKCaRYxBHsI\n" +
                "VQoqrHMZQQEhCy/6tOiDp53JKSO5NpJyDOQga+ok+kW825jWqkfHJqOECui+cRuXyRBUpFCikY3n\n" +
                "GA5jD2CWXsPnur2Q6WvkmGDr7xq3JxNYtPG1B8dXPc1g/pnaJHZP/jyAFB5APGoNzOA6DY7rJ+2R\n" +
                "ybqudOLePrNWgoRWFy8eXtJGi00F2OOxJCkhb7uGZpGChig4FZfV54v8vPQdZjOlafmKdLi/WeWO\n" +
                "wvq5Cu1CsUUlEUIpBjaomWZn9rVWTdqukGoLIlaAkC5SqnKfxnSJ4sF/gZuySHVPtSdukMeOQbV6\n" +
                "CY0iqs/JfmcgqtJHd1SrxLoRI2TO1Bu5dnbiC4q+QZQpTkqICsNNhbXPVtZRb/3dZravHVKMUKs5\n" +
                "fJRzyQOSNK5DzFVjnRZmSVeHMcxk+1Y4n7Sel+21vCv0ZrHpun7o6POO71WKIg+dvSSzsGTqdasE\n" +
                "RduiMhUFIiS7kFTlR/UImbgpC8doaobX3n1731ntw70xGdiNLaiSG5UNqxBgJv08Lhlipq3jncpG\n" +
                "0nsfPDH2RreDfHT36W+mW29pdPc9tmx+6b7lgXH5NadSzuyp+rtVirWckyGetGstiOv/ABt2hA6K\n" +
                "Dw2s8/jl+l8A4BC2/wCz7/j6uf5HPz+v5evp68h4y/TB6fmbcPz/ALn3/r+z83/H6vt515ejXVFY\n" +
                "3X1Naw1bcSuj1q0S060kyNHCjVyZNhVLDLoARdMwKJiLuMbd4kNz2AYogICPOxSXwd+lcSgIp2f1\n" +
                "AB/4wvQ454/6X+/9Mn9Ouo/1D9TMTazGAbaiVKl6THy/q4WifzxRxyEKAW5QrKvB54J/txqHurPS\n" +
                "b6VOjuepbd3ON+y3r2Lr5iI0J0ni/TWJZYUDOewiTvgkJX8Ar7Osq6H8S/QXUDsqB1ZUDWFCy2MJ\n" +
                "D3WWRhH7do4PGRruXdomdGSFFFQGDB0smCxiAr5RiEMJ+AHG/iHeITL9JUrVaRR60wnbdYY4846c\n" +
                "TXtYRkdDHcP49uZErRdBVzILP2C/CJ1CIpIJiop3GOmQ+U9F+G30+6D2RCbSpzWcVs9dI/LEqSE2\n" +
                "+ctWp5GOdRTpf2QVARWVMwfOkCGXKoCZFjCmBTD3jT941wh/ZF65A3Hb+DyPA3dx29oWqz888/Vx\n" +
                "8efTgPXn6s+3/unqvtHo9lcruG9jKO6jlqlavcxCBoYKNmWtH3fyAjyhjNyfgIV/Oos6YbO6K786\n" +
                "/bdwG1cdm8nsV8Let3KGfkaO1ZydOpdnZO6FgxrcLX7R3clw4PI9a/Jv40XUZHrtXMlR6g4Zicpj\n" +
                "t12cuzI5JwUTETcFfckESjyU5QOAAICJDh6Gu4pnXbomS1drrYV5t0Tr93sKDUmGFdnn7Ykqmm2d\n" +
                "uY52cyCSypjNQfsnSCDsOUFfLECnEwHKFJ3iU2LT0xoHp0a66maBIzrNFALA3qb6vOpFucK3FpiD\n" +
                "xOIOo4ST80vYILFITu8spg7gAA+HPae6XLD09dL9939um4awmpHW7+uw8ZXqy/sSMjEQtxs0ijJK\n" +
                "Jx0BMuGpjnn1CeasdNs4TFLyCioCgmivbHUrqZtnPbrxMm6MbvFMdgMZlILWZs16GPhnuvjy6NbM\n" +
                "iIhRLTp2NMO91HAB9amDePSro9vHauwNx1dk5vp4+a3fncBeo7bpXcvlp6uLhywjkjxy15pJPPNj\n" +
                "4ZfKlVvHFIwJK+9Z36ofGHtCOwVqn01tY6QrEWok1Na3zFV0vZJI5hBROIYOCgKcakYyTdJZZsR2\n" +
                "6dgt5ZBQ8hRTGMD4uvU5rW/M4zdVKSXiU1Gp5uvO4Q1enk41cwHM8jO/2QDOTIAY7T2rvZOR4KJy\n" +
                "FEVCec0PF+FzpzYcLsKY3tethuq+uR5Ews9ra4oxCMkicirORXRY0tss7cMlEyqN0lljNO4eVkFh\n" +
                "7BJnLq42z4ZHVi9hpyX2xaKbbYdD2Itkr+uLwDt/GFETJR0g3dVBduu3bKHUVbmKmmskoc3Chicp\n" +
                "Db5NxdScjWyG6ZusW2cbn1vxT4/atfP444x6alS8Tv52hHIK9gct3qGLkMw5vUW2OkGKv4fZdf6e\n" +
                "98Zvaj4mWvl9+XNpZxNwJkpAvjsRQmiltgCJGkaJE8bPGK8fjRu22SgdenS7sJOsoQu04AkpaBbI\n" +
                "R8S9VVj3/trgvJWa7d8RBVut3lOQSuCJ+oBzx3AAzHIcpygYo8gYpTAPzAwcgP3hmqxobQvQBPbf\n" +
                "1/D0zqi2NN2h1ZI0IOGfa+moprJyKThNZqxPISNNasWguFkykKdwumURHtIYFBKIbUiBQIgiQo9w\n" +
                "ESTKA/MCkAAH7wDnNq+ju9NzbzxV61uRNvmWnNDXhnwGSgyMExMQaVpngmnWKTnhgncOQ3x+daYd\n" +
                "dNh7N2FnMbS2hNu1oL1axbsV93YW3hbdZfMFrrXS3TpSTxFfIpkEbAFOC5JIH64xjJk1BuuB5+r9\n" +
                "3w5+/wCrjMZ7g1tXdva4tmvbSyQfw9liHce5buC9xQMdMRQVDj1BRu5Kk4TEBAQUSIP1DmSFBOVF\n" +
                "QwfrgmcS/wCkBREvw+3jNXbf/XD181nd+2q1WVrRH1evbFukFWUG2uzLoHrsTZZRhCOUnisM4M9T\n" +
                "dRjdssm9Kqok6Kcq6Q9qgCMXdU994PZGET9+xORy1LMtLj5K2Oqi0SssREnlRmUdhQkc++TwONS5\n" +
                "0d6bbk6j7kavtfM4jB5PBpBmIbuXvtj40kr2YvE1eZI5GM6S9rqABxxzz/eGUVSt6dG3U20k2NSu\n" +
                "RXevro9TbyDSuyqzGz1grtwxUXanQROzWazUIv3JD5yhGblZI6gee14yTHi32lveNi6htzVq7Zt7\n" +
                "FrCKl0mcggo2ftCyKijsrZ42WKRVu6RBUE3CZyFMRUpymDkOM8K465ev54oVR4FgdKl/VUcaparH\n" +
                "LyPPb3q1wwgHIAPHPBR9eOeciL1B7g3VuGxRcxutWSPNx8d7FGlkYEtfMVgKx1eE2hWbIpyCoY35\n" +
                "YI8fyQHgoAHzc3JuHb2I2NurbOA/5T+3ZzL08jUp5bFLVq45opxIyLYFmTuLoyoP417u1SeD619a\n" +
                "tnbT3jnup+xN67qXYqZTbGAyOIyN/b+fkv3sylmvHHEz03x9cII5VaUE2H8QldV5BJNotU3JbNYe\n" +
                "FlW69Roqef2nZlot9RbuoOOeP1ImNc2mdczLpYrVFUxQcRLd4xRHgDkWdJqlDgomJ4rwqOkewXnd\n" +
                "aW1NhVGZi6nrz9JRQT8K5j2c3ZFiLIsvZCv0EFVRhTKDIAs3IKSbpJsUqgnSUIWJ+mOqTq11vr+O\n" +
                "purxmF6QxdyDhgkhREbC0I5ePXDp6ZJ6tDvgE4u1lxOAK9xDCZMeAKBS5ibdeHiDsyFTaOLO2SL8\n" +
                "Em+r0ECBzyI8ESrpCByPI8gH1gPxzK8Hu/aV3IdPs9uOtu/IQbPw+Oq1cNXwqSY9rNRI+ZlsGz/K\n" +
                "jT/ykmH7gqoftGsG3HsDqHjcV1U2xtKz0/xVjqFuPL37+47m5poswtG/MVSs1NcdxBJHV4g4Wywj\n" +
                "Z5JV5djzt4lKUCeWTjtKQCF4EB/J44/mD9+UG9Wvhcbl3dvzYe2K9f4BjBWx5HPmUe7JI+1sEmMD\n" +
                "FxaiCnlgdI4mWj1F0xSOBAKqQol7imHMq+GB1HdT+67fsiN3qpMuoeHhYd1COJWonr5Un675dByi\n" +
                "m8BgyRdmO3KUwt+DKoATzRMJFSgW5tVRAAMmqqmXuKJRAyhSjwYOB9BMA/Xm8E2P2l122Rj7eRp5\n" +
                "WljGsSTVa88r422k1fyQDyLG/PZ7bhSxBBB49a+dMGT319NfUPK0cTkcHazcFKGpbtV4Yczjpa9t\n" +
                "a9ziE2IgO8EICwUMpBHPvWhJP6umILbrvU7p+0UmmlsQqykiAqey+2LvkWQOO4Q83sIqsUxuQ7gA\n" +
                "B9OMtWZ+DVv580bvENlVgU3KCKxR/SgiAKJlN2jwQfUO4AH9mZL8QTw17w92FbN86VkWsuxm1gnp\n" +
                "6q+3Mo2TgnzZuIun8G4Os1K7auBRQcAyE/vBF0LtVFZZJRu1b4A6fPFo3zps6VT2szR2RAxvcxUG\n" +
                "QSLE2uOBoTyEWibxoVJm4BDsAhxfMFHJu31cmN6hozitmbD6dbwzuC6tUM3Sx1u677cy9G1YFN6a\n" +
                "SPwZHrOXlZkMY7h3up5DKpJI+kea6j9Turewdr7m6C5bbeUyuMxsUe8du5GlSOUhyMsdfhYEuIIq\n" +
                "4R0sEIzxxyoVkjdlAB+pdfCJ3nSapP2mV2NWVGUHGO5FdEvvIqiybRIyx0iioUpPyiFH9fkPT0+Q\n" +
                "1zdPmkrFvzakPrGsSbWJmJUrk6L9yC3kJEagQVRHyOVORKf0+r5jkzd7eIl1LdWckbWlQKWo1ezL\n" +
                "Kw5KrWEvNkJpo9MRJMkzMvfOXRKQphMspHjFIdgn89QyYF4sv8Njw7rJoq1m3Vt6Uj29qUh1YyBq\n" +
                "DNyzeJxKT5dNV1ISb5BVdFaQVRbtEmqLRUE2QGeFXVdHXSFv3Vtg7L6mdQsHS6a47NSbVxtlBuPL\n" +
                "37kwjaLvVz+mkmkMsRaNSqe1dyeezga8tvql1F6NdK9yZPrHmttwb7zdR22ZtzF0KhspKsKxg24a\n" +
                "6eGwizyI0zAPDCgAaUl1GvSdBHh1bW6YN0Otl3G8Q0xEL1CVrxYmPLIA5WdSEjCPUXahlykQKi1S\n" +
                "jFk+0wHUMdwUUxIBTBkk/E8b7OkOlW2w+sYaUnnsu8i2FgYQ7JeRklK0oqqaRM3ZNUl3bn86mzIs\n" +
                "m3SOczdRbuKKXmANhqaiSnokomcC/wCQcpuPlz2iPGVG9cHiSvOlzaLPWMZrpOzHNBozL+QevDpI\n" +
                "GB6ocjRBqiiQ5hEhUVvPMsHaYTpAl+qoObq7gwex+mnS7LYI5K9hsBfSem15XnyFuKfIR+JmQ/fI\n" +
                "3wTx6XgEeudfPXbG4uo3V/rHg9zRYnG7k3XjZaeTXGlKmJoT18NKkyrKCY4l+5l+/hnLMDwQvrXR\n" +
                "1bXupDT91h9g0nV+xGVmgTrKRb5xrOyPwaKuETt1VCIPINVDzBQVVS5MQe0DnAAAREclJsHrD8QW\n" +
                "0Uuw167Mb2nVJSOWaTRnuqXcY2KxOXtV818NcQK1J2CblQVSFKX4j8Mld+OklP8AMdB/+bX/APr5\n" +
                "jvbPi0SOzdc22hqaeh4otliHEZ7wSdLGO19oKBfN7TN/yu0OQ7e4vIiUeeOedIYa2w9u4PK09udY\n" +
                "9zV4p4bMrY2DF3a9e7O8Sr45wpCkTACN2b12n36BGvorPc6p7u3Pgsnu76dtlXLNW1j665q3nsXc\n" +
                "u42pFZjk8tcyRF+avc08UcZB8g+37jzqrLTF12Lr7Z9WuGp0n6uwoZ2+XrqcbEKTr87h1EyLF8VC\n" +
                "KTbOjOx91u34qJ+zqgml5iwgBUxMFi39nH4mPpxH7H+z+849+H+zOQG6dNxKaI3VSNtkhUbAeoPp\n" +
                "N4EQscU03vvCBloUSCoBFO3ySyguCj2D+UiQOQ/WC3T8dHKccfgOg/T/AK2v9X/d8xjpPk8XTwd5\n" +
                "L3VPcWzZGykzLjsTVuTV508cH/q3aBgolc8oVPsrGpJ4I1mvXTD5+/unHTYzobs7qLAuFqRvmM9f\n" +
                "x1a3WlE85OORLUTyGvCGWVGDdndNJwOQSZReG/1JdX+29l2qv75ibGNYa1wr9g9naG7qYNZMj9si\n" +
                "VFF6eNjm7sXKCyphQMVRVMG4qAbsEwFhD42Sfm9RGvUuePM1yxT5+XfabQXn7ucmz0geKE939vWo\n" +
                "6idarj64lb/fCacuyenE7RaJgJefAyqCqBAXSVRiVW4AQwKpqKkU4MmVUxIT+Nir2dRGu1eP8Hrh\n" +
                "ipx8+y02g/H7R44/bmw+/wDKYzJfTld/R7pyO7oKu4KNezmMnDNDaY/rK0rxFZfvZY4pAFbkjg8D\n" +
                "861L6YYjNYf6uMIuQ2RiNgW7m28jbqbew1ivaoxocXcgSdXr8RBppoHLIUUgjlgeeTE/qn6KpXpk\n" +
                "11qrYjy+IWlPYIIrNo4sau2CP5i2UmAgdZVUq6YC58kQMkkJiFKIkHkShOff9v1JYekLpP21u/WM\n" +
                "laJWVjp6vJr0t8aux0YnEuVWiaThqicqJUH4NSuEESlIQjlNUTL8ggkrC7q5612fU1rHUuvGVLeQ\n" +
                "C2uEkUzvVXwOwf8AZEMY382kRBISdyjYygfEew6f185ZJdNOWG+eEXQ2LGuSDi21qMTtMbFmj3Yy\n" +
                "/sSNtfyDzyI8EyvBUeQZzqolBLvORRI3aJR9Yy2tTw9vIdT62wIRcwq7EoXoY7daW3BNlKTY6WeP\n" +
                "suoxdzILSIvxwSUBUAiXt6ZDcmPxHQ671YnOO3M3VPL4y7PRu18dar4LKR5itTcTYqVEiiWH9vml\n" +
                "KMGPYBIe8sNRNmKD0UtemyE6iIfV98lmD60OajNQydsED1+ValIqYHi/tHaZJwksidr5X50wrJAs\n" +
                "mj3HAkielbpK6Jep7VU7sxjWrdVC1h+/j5uLkrOsc6BmbNu+K8TUbqqkFo5buieUJxIqB0liKEIJ\n" +
                "Mi50x0K7bB6O+rPS8nTLS3dwDGF2lT2y8DLtXUxOskHInjotJw0RO4fK/RxkkDRuCizkHYJeWcDG\n" +
                "LnwOku57M1Vofq2pa1RvTR1bKQyQqgFrM6mqznVmthjnqyJDMSnFUEjRomKBfyDocm9Q7DerCWsT\n" +
                "BldrX81szEy4LMbRvTWYjhIV8OexsdhX5aOIOpmmrJwhPHE4AULxrx7jpbgs4PfWJ271E3BHurbX\n" +
                "UPF1qVhNzWJBY2pnJsfLCfHPYMLrTq3pQ0oBfupMSzMT3Zr6J1eku8dU9NhNf6bvTOZgZRxPwszJ\n" +
                "Wk7lswPCmIq1k5Jmg78pwkkoKankiK6ZV/IEUlDE709oAhQApAAOO0oAAfIOADjNbHwZdI2RttfY\n" +
                "mzrVWpqDLX6/HwcO8nYl/GFerzz16tKJMDv27YHJ2pYdgZ35JT+T7QgU5iGNwOyjz8A+fwzar6Z6\n" +
                "1wbClyV2hVxz5TLXZ4a1WlFRC1kKQw98Uapy38bcMw5K8e9aa/Vzax3/AIpLh8blLuXiwWBxlKxc\n" +
                "u5OxlHa7LGbVhUnnlm7VXzIexH4DFvWmMYzYvWreuB9QH05+z558NxWoF0qddxERyyyggJ1FWTZQ\n" +
                "5h+HJjnTExh+0R5z7uM6pYIJwBPDHMAeQJEVwD/cBgQDrtinngJaGaWEkcExOyEjnnglSOR6HrXn\n" +
                "folWv+Y4r6v8XtPq/wCy+P2/UPrmuX4uuh9oW3c1OkNa6hvduiC1MUXLui0OfsDFu594O+W7txX4\n" +
                "l4gi5EnaoCS5iqimYh+O0xRzZWD7Q4+zPzOikp/hEkz/AOmQpvh+0ByOupHTLDdRdsz7btN+3QzT\n" +
                "15zZqQxCZTA4YAFl9A8cH18f51KHSnq1uDpPvCrvDFpHlLdStarLVyM1h6rJaj8TswSRSWVRyvv5\n" +
                "96rT8MTVcxU+lKnxmxKK/rdh9621daKtleXiJ1BFxaJVZqd7HSzNtINgWbCmqgDhEoKIqJqpcpnA\n" +
                "xrDvonW/X9BxQB6cfo9p/wDF/wD3nPQFKUgdpClKUPqKAFD9wcBnbL/tjaGK2zgMTgIYYrUWJpQU\n" +
                "47E8ERllWBFUO/2/1MV7vXr/AK4xu3eeX3dubObntyvVs5zJWclNXqzzrXhktStK0cQZyQik8LyS\n" +
                "ePn/AD81lERsb3e72LRn3/r+zt0Ue7j4d3lELzwPr68/Z6emUgddGm+vy5b0fTWj5awI68NBRiEW\n" +
                "jEWCMYIIOklHhn5FWayjdbzjHMioRZQXBjpm7QOmQhSmvQ+Aeo/fxnHaQfXtKP3B/DPHvXY9PeeF\n" +
                "XCyZDJYWBLEdhZsLYFKblOfsLKjDsPJ5Xj54P+7l0+6hZDp9uE7jr4vD5+ya0lU19w1DkKvbIY+Z\n" +
                "OxpFPlQIArFjwpK8e+daocv0U+JlfnCTWfeWp2UyYpcyN7aMWHlEBTtK5IhJFRUARVMJRFBZXu4E\n" +
                "pTnIQAltobwVWSZkJ/f15eyT04oOxrFaBNm2TX4EV2srLuCPXUiQ5hDk8f7tEA5L5igiBi7A3aQB\n" +
                "/VKAj9gfwztkV4n6Zen1S8MhmnzG6p0/9oZ6+1qGM+vYiVYwx9fDll+PR96mjOfV91WyGLfEYEYD\n" +
                "ZFSUATttTFihZl/x53lmMY/AMSI49nvPPrXC3Z4L1vrzh1YenW/OJAyH51hW7KokykyqmOYwg3sr\n" +
                "JNm3ImQDdhCrsDK/yjuDeoZguJ6SPFCqhFI6Jk7k1QIIJ+WS6R7tEATE4l8g7iRXKRLk49hUhKXs\n" +
                "4KAAUpADaw+Aeo/fxnAkKI89pft/JD1+/Oq/9MWw5Lz3sHczu12mPM0GEyDV67knkkRsshXn44DB\n" +
                "QAAF4513Yv6wuqMGMhxe4qm196x1lVILO58Oty6iqFHa00csKyf093c8ZcsSS59cVD+HRrDrQoVt\n" +
                "vTjqMlJlzWHsXFJxDeYmY6UOeUScOxWVbJNhWWbJEamTIoPnppqHP+WgJilUGx6/6J1LtB80kr7R\n" +
                "YCzP2KaiLR5JsEHDhBJYUxUSKqcveJDmSIIFERAohyXjuNzlrtKHPAAHPx49P6M5+v4fH4jz8vhk\n" +
                "t7b2Ni8BtuDbNiWxuClC7yGTOsl6WRnk8g7zIpU9rf0+uR+DqDt19Q8zufdlreFeGptjJWY4o/Ht\n" +
                "lZcVBCscKwnw+GXyKXVfvIf7vg+tRoDo76auP+SGnc/bFN//AG/0DmIt79E2m5nUV9i9f6oqTa3v\n" +
                "a8/SgFEWCCCgSIImM3AiwF5Ic6hQKUwD6GEMnrz8A+fwzj4fH6/gHH7/ANvP25Vf6f7Qv0bdJtvY\n" +
                "iJbVaauZIcdUSVFljMZaNxFyrgHlW98H3x6HFGN6l77xmRo5GLde4JpKFyvcSKxmcjJBK9aZJljm\n" +
                "jNniSJygV0PplJH51qpdFfh67va9R1Am9t6odNdbxDiZf2ZOxtyptF269emWEe1BFTvK5cklXse4\n" +
                "Ml+V5ZEDqj3ATNhQvR101gHrqKniPp6jFNxD4evp25JgCEL6lKUo/YUA/oDOfUQ+Q/vzEdgdEtlb\n" +
                "CxVjF16EOXFi7JdazlqtWzOrSJGvjRzCOI17B2qOAOWPyeNZz1N+oXqJ1NzlXN3srZwUlXHQY5Km\n" +
                "Av5CjUdIHlkE8kS2eHncykPIfZUIp9AawnTOnPSmvZ1vZKbrqtQE61TVTbyjCNbou0CLkOksVFYC\n" +
                "d6QKJqHTU7DF7yHEoiJeQyk3xbenTdO2N46/n9ea2tl0hUqQ1i3L2uwz2VSbPkLHPOlWjs7RE5Wp\n" +
                "ztXrZRIy5yJimcwnOkBQE+w8A8hznUyZDjydMhuB9BMUpvqD19Q+77svO9emO3t47Wm2mYUxGOnt\n" +
                "1rcgxsENfmSvKkoJRY+wl/GFcleSvrkcax3YPVvdOwt6099RWJM5maNS3SifNWbdz+K3XkrkeVpv\n" +
                "NxGszPGFftD/AD+dQW1Z0IdNMTWaZLS2lqqjbWcHCKyCi7RVU6MslHtRdCqksqZI6hHXmd4Kpm7j\n" +
                "cmMAmEw5Fzru1111S+yK+n0wyL+M1c1pjFk4i4l1WmzYthTlJoX5lmMmgKxklYpSETREonaj5RyF\n" +
                "TTUIcVbjvT4fLAgA/EAH9oc5Rlel23ru2325jfJtuOUV1mv7fWHG35hXCgCSeGIFw/b96sD3cn2C\n" +
                "dc4bq5unH7sr7sy7xbwnqvbevjd2yWs1jITc57vFXszkRmPkePsK9vautYdDS/i0te/2SVm2nmhw\n" +
                "r7KrRW3m8cjwr5LRMDhyI8AfkA5HgPUefxDR3iwF7+17IlBUOFAKTX4eZx6fnCgx/K+seTc+vr8R\n" +
                "HNn0SF+ohR+zgP4Y7Cf5Jf8AVD+GRl/5aMOVVTvffJEfd2D949L3cFuB4fQb2W4+T8/nmYh9W24F\n" +
                "Z3HTfpgHk7fIw24eX7AAnJ/U8nsAPbyfX44/GuFq/UniqxN6qa0tLzTavJTscpMJpu6SyZmj03KR\n" +
                "nguU2rMBWKLYFCdvkqnOYSFDs9FU9jpv3ggiCn+E8pPv/wBLtDu/nzt2kDke0ofbwH7P/wAzuP2B\n" +
                "z9mSr0/6e1tgVLlSrms3mI7ciPzmbhttB2KR2wHtTxq3PLAA8nj+2oZ6mdTrnU29j793bu2tvy0I\n" +
                "ZYe3bmO/b0teUxsXtDySGV08fEZJ+0Fvnu9MYxkh6jHTGMY01izZW69V6hGuo7FvEHWJO4SaMLTK\n" +
                "86ci5tV0l3EnDwxIym1KPI8s1tfpSVghG7prXYmTWZBJtF3hEG6oLBX/ABHiu6cYTdtruz6pYadJ\n" +
                "1DdF91nPnq6x9gMKNT6ZaK/SGW29uOE4uuudcVmzXGwIV2PZCxsLkkiDdNJ099s4Q46p+rHXN3oe\n" +
                "0VdBVrXO/rNoijXG+y+1JqIi7jp/ULhGlSS6SMdaAaSbG1bFtse5Vr0TV6Y5XbNkXE0e+zUHGxbm\n" +
                "Jl4B+H9sKwalnumaM1Y21Ken9WFvnq9tITa5rzC1p2HWtIaXqYk0rFQ+ofZJmL9VC1qwkVSrJXNb\n" +
                "Q9RST98ROmat9KJNaX7gg49jn7eT/jkqPjkc+z7+OBzySeOMEyu4LceXrUqk8SV2P8sghMvHfLHA\n" +
                "g4LoX5mbtLIe1ATzyfQ2QIuTjpqNj5eIkGcrFSzJtJRcpHuEXsfJR71Ajpk/YvGqijZ2zdtlUnDZ\n" +
                "y3VOg4QUIqkoYhymH6ABx6BnACAFAfgHAfz5yAcegZ06zpeSqknkkD3xwCeP7e+P9aYxjGuddTHK\n" +
                "QpjGHgCgJjfMAAOR9MwpT+oXVd+rF2tlTlp+YjtdS0tAXKNRol7Rt8NOQjZu7kYY9Cc1xG8O5Ujd\n" +
                "02Vax7GvOnciVwkEak7E4AOLbmz6lltm7esFUK3j6fXdZVFDU8S7lGDlhfr42mpqy2pKUbKqB9G2\n" +
                "j9gyhKIrJO23tTVnKupyJemWQFBngfYniD9O1C1XeNhVGdqkRsqLm4de66euMcpUdqTM+1UrsDO1\n" +
                "2RqShmdhPa28G3aQDS1FY2OsRxIlo4UfyFWiFXCGHZDc8dLyzWZo8XXrJbZzegk7rK1+5fJV4kQu\n" +
                "EZO8p2MZI3Qxt75OVY7bVjINBXp15MrauSY+KFcdYRkrSXDCUiuEwyJGZhN4VYyxiCaORZhypUTu\n" +
                "1XtvXu66i1vGs7E3stbdOHbH2xFs+YOWkhHrC3fRkrFSbZlLQ0ozVACuouWYspBsBiGWbEIoQx8k\n" +
                "5iPSslTrLQ46+U2kPaA22GopcJmDmacejWj6RSZEUZV1bYRZo0cnsYqNUmz6UWF8SUSaNnbCUlIp\n" +
                "Rg+Xy5mR42aWxRqzzSwzPNDHJ5oFZIpQ6hg6I7MyBgQwUsxHPBJ451YL8UcF21DFFPAkU8kaw2XW\n" +
                "SeLsYqY5ZESNHkQgqzLGgJHIVeeNMZ8C1fSj6L2L6FDBfTL3FL/RMbR7f9GvpL7A49xfSH3V+lPc\n" +
                "fvT2X3t7t/t/2D2j2P8Atny88lqD8MA66r/4eR1yG2P0t9KfwTBZfwf8e+5L3H7gC4f3Rc/Rz3R7\n" +
                "194/4795ex/2j7Lnv49c8j/X514PJ/KIux/aFu/j7BwQOCf7nn1rJZjdv9QYDnj1Hgfn6fP93wx6\n" +
                "CP2h/XnnLfcalr+uyNvvVnr1NqsOVueWstqmo6vQEYV27bx7QZCYlnLOOZg6fu2rJuLlymCzxy3b\n" +
                "Jd6yyZDcarZgoLMQFA5JPoAD8nUed6dWurtORWz46NmIW/bZ1lrS5bPk9Qwc0orYmkJTa7H2V0rc\n" +
                "V4eLsZ9dRsgwl4U8dMWuOZt5EJZkMQlKKrERPh/R3iR6H3NbqPr0yknVbXeK/DqxEw68l/qqd2Kr\n" +
                "XaxP23UdH2WIRzW3XWimtkQwmGowcMLhd2xRape8JBvHDXb4h3UDUdq6zrN0o2tKgz0ttza9A1bY\n" +
                "953bXkOW7bZhIklgsQrUNna5bXb9jr2oqxriLLdbXf8AXb9eUl5YKTKVmLjnt2dy58PHYV2Z3rYf\n" +
                "TOq11ejqnTmttV3Sgmo9LaVV+i03THObyyi13Fc23t6nzjGHaunKStmj7fbpS5PnP0qlbvZZCRfS\n" +
                "j7t7VC8/Pof5+4++PR5A4BIb2D6/zzgy565Y3AtGKdEp8xoGEHeJJJO6VV5LoyhokbiT2p4HCkc8\n" +
                "2x4xjOrWdaYxnU5uwhjfHtKY37gEf6s4JABJ+ACT/oe9B79D5Osc7F2zQdVt64tfLG0rgXC0xFHr\n" +
                "J3ibg5ZS2WAy6cNDpC3RWBJZ8dut2LOBRaJAmIruEi8CMfNT9VtbkSNoTblnq1ZuFm3juDU1EYIM\n" +
                "5GJaTpqHepmArbA7mQfyrUtlnYBpEP00FJFmWakpMrKDj/PMmyCpPqa6jk94dVetqnZW259asarb\n" +
                "IyO1fHVq9V/WV8QGyECJn7TbqmSFt+zIuxXZOYgK/QddWCEoL49JkpG1WOfgYKyzv0X9d0OvrLVe\n" +
                "urYlKucHa1LavIXdeUc3VV3ebrX4W9M4DY7D3k41cuh0+atgrI8jyysy5jzysjN2l3X6zHsY1v2u\n" +
                "wgSXqrLc3fWxeNjX9Ec7+weaWGwqrN40lladSEBeTxTLV4PY0f8AMWIBi1PsfSH9Hs+3mcrIwuLt\n" +
                "Y7qjrw2KTNNW/VLViWpIjykQ1jZrtk/InlSXmqIoyUsHYID4B68/bnOfgg4QcEFRuqmskVRVIVEj\n" +
                "lUICiCh0V0+4giAKIrEUSVJ+smomdM4FOUQD98ntSGAIIPIBBHwf8j/GoC4I9EEEfIPyP96YxjOd\n" +
                "NMYxjTWL90awj9zak2VqiRfqRDTY1Hs9MWl27f2lxEjYod5FJSyLX2hqV2rGquSPk2ijhFJ0duDd\n" +
                "ZQqSh8r3p/hzTmoto9MEnrbaruxag0XsDaN5dVPZiZFLfFO9o1R5ETjSiy9QioWls6l74bxMtH0F\n" +
                "Oi1tOLl5W7WE1vlDTLCvxlrGMqDsBx+P/wBB/wCmrZbxFC7Kk88PM0Zj7ZFJVh45lmUevkeRQT+D\n" +
                "8/50xjGU6uY9etMYxjTTP5jM2h+QM1bmAfiAop+v7fyfX78/pxlDxRyf1xo/z/Uob545+Qfngc6q\n" +
                "DMv9LMv+iR/9a6lKUhQKQpSFD4FKAFKH7ADgAztjGVABQAAAAOAB6AA/A1TpjGM500wIAIcD6gOM\n" +
                "Y01Ejq66QaP1h1Cn0a9T9grcLWLkra1nNXRgjzD9JWnWyrGjGi1kip+GjTC4sbWW96KQMk/aGiSp\n" +
                "RQxr5ylLMfC9NvStsTSW/wDd2zLZsSP2PXthUfT1PrEu9SmEdhKI6siZatIuthqvl5OPsFmfQgwj\n" +
                "ufukZJRqVssys7Kp0uns1GcWWeOcB8uPQPh68/78ZV3Egg8Hnj3+fXx7/Prke/76tj4ig9xL/gC2\n" +
                "o5FkEikjuZI2jXuUHhgquwHI9fjXOMYynVz0xjGNNQz2J0d1m/b6iN2knwrCf0WdVu8wddrkKwnN\n" +
                "iJlloCRhyTl9KgayM4qOTgUWTmOiDsn8m0K0YnnG0Og9ipT+JXpYQ0u+jbl0pw1Tqk41ixhLnR59\n" +
                "zLNK9t2L84izJ9a7i2aWOystgV9wpIPYrYr6KuUpJJS07EWaOlwlWEvXZs8/EPl8c4+PAgHPx9fl\n" +
                "92Yu+ztvmWzajoRQXLVj9UbsY4sw2CEBkgkbkxE9v3IvCOWbvRu5uckG7twmvWpS5KeehVrrUWhK\n" +
                "wNOWspJWKxAvas5TniOWTumiCxeKVDFH2R/6X69sSs6Qp0dthoSN2Q7PYLDc2Cbpo/SZWK12WYtE\n" +
                "q0QdsnT5o4QbvJdZFFRu8cpikmXtWPx3ZIHGMvtGotCnVprJLMtaGOBZZm75XEaBQ0jcDl245Y8D\n" +
                "kn498ast+21+7bvPFFC9uzNZaGuhSCJppGkMcKEsUiQsVReWKoAOTxzpjGM9evLpjGMaaYxjGmmM\n" +
                "YxppjGMaaYxjGmmMYxppjGMaaYxjGmmMYxppjGMaaYxjGmmMYxppjGMaaYxjGmmMYxppjGMaaYxj\n" +
                "GmmMYxppjGMaaYxjGmmMYxppjGMaaYxjGmmMYxppjGMaaYxjGmmMYxppjGMaa//Z";
        String strBase = "data:image/png;base64," + base64;
        if (info.indexOf("投保人") > -1) {
            if (info.indexOf("港澳身份证") > -1) {
                list.add(new BaseContect(strBase, "T4", 0));
            } else if (info.indexOf("身份证") > -1) {
                list.add(new BaseContect(strBase, "T1", 0));
                //list.add(new BaseContect(strBase,"T1",0));
            } else if (info.indexOf("组织机构") > -1) {
                list.add(new BaseContect(strBase, "T2", 0));
            } else if (info.indexOf("营业执照") > -1) {
                list.add(new BaseContect(strBase, "T3", 0));
            } else if (info.indexOf("通行证") > -1) {
                list.add(new BaseContect(strBase, "T5", 0));
            }
        }
        if (info.indexOf("被保人") > -1) {
            if (info.indexOf("港澳身份证") > -1) {
                list.add(new BaseContect(strBase, "B4", 0));
            } else if (info.indexOf("身份证") > -1) {
                list.add(new BaseContect(strBase, "B1", 0));
                //list.add(new BaseContect(strBase,"T1",0));
            } else if (info.indexOf("组织机构") > -1) {
                list.add(new BaseContect(strBase, "B2", 0));
            } else if (info.indexOf("营业执照") > -1) {
                list.add(new BaseContect(strBase, "B3", 0));
            } else if (info.indexOf("通行证") > -1) {
                list.add(new BaseContect(strBase, "B5", 0));
            }
        }
        if (info.indexOf("车主") > -1) {
            if (info.indexOf("港澳身份证") > -1) {
                list.add(new BaseContect(strBase, "C4", 0));
            } else if (info.indexOf("身份证") > -1) {
                list.add(new BaseContect(strBase, "C1", 0));
                //list.add(new BaseContect(strBase,"T1",0));
            } else if (info.indexOf("组织机构") > -1) {
                list.add(new BaseContect(strBase, "C2", 0));
            } else if (info.indexOf("营业执照") > -1) {
                list.add(new BaseContect(strBase, "C3", 0));
            } else if (info.indexOf("通行证") > -1) {
                list.add(new BaseContect(strBase, "C5", 0));
            }
        } else if (info.indexOf("行驶本") > -1 || info.indexOf("行驶证") > -1) {
            list.add(new BaseContect(strBase, "C6", 0));
        } else if (info.indexOf("居住证") > -1) {
            list.add(new BaseContect(strBase, "C7", 0));
        }
        if (info.indexOf("完税") > -1) {
            list.add(new BaseContect(strBase, "I3", 0));
        }
        if (info.indexOf("在京") > -1) {
            list.add(new BaseContect(strBase, "I4", 0));
        }
        if (info.indexOf("验车") > -1) {
            list.add(new BaseContect(strBase, "I2", 0));
        }
        //其他未添加
        List<BaseContect> reslutList = BaseContect.getList(list);
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(reslutList));
        String res = jsonArray.toString();
        String res1 = res.replaceAll("imgType", "ImgType");
        String res2 = res1.replaceAll("isUpload", "IsUpload");
        String res3 = res2.replaceAll("strBase", "StrBase");
        jsonObject.put("ListBaseContect", res3);
        String params2 = "{\"ListBaseContect\":" + res3 + ",\"BuId\":" + buid + ",\"Agent\":" + agent + ",\"SecCode\":\"" + SecCode + "\"}";

        try {
            HttpResult httpResult = HttpClientUtil.doPost(URL, params2);
            // HttpResult httpResult=   HttpClientUtil.doPost(URL,null,"JSON",null,jsonObject.toJSONString());
            int code = httpResult.getCode();
            String body = httpResult.getBody();
            if (200 == code) {
                JSONObject object = JSONObject.parseObject(body);
                if (object.containsKey("resultcode")) {
                    int businessStatus = object.getIntValue("resultcode");// 请求状态值1成功，<0失败
                    map.put("code", "200");
                    if (1 != businessStatus) {
                        map.put("code", "400");
                    }
                    map.put("msg", object.getString("message"));
                    map.put("data", body);
                } else if (object.containsKey("BusinessStatus")) {
                    int businessStatus = object.getIntValue("BusinessStatus");// 请求状态值1成功，<0失败
                    map.put("code", "200");
                    if (1 != businessStatus) {
                        map.put("code", "400");
                    }
                    map.put("msg", object.getString("StatusMessage"));
                    map.put("data", body);
                }
            } else {
                map.put("code", "400");
                map.put("msg", "请求出现错误");
                map.put("data", body);

            }
            return map;
        } catch (Exception e) {
            map.put("code", "400");
            map.put("msg", "请求异常");
            map.put("data", null);
            return map;
        }
    }

}
