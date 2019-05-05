package com.bzs.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bzs.dao.InsuranceTypeInfoMapper;
import com.bzs.dao.OrderInfoMapper;
import com.bzs.dao.QuoteInfoMapper;
import com.bzs.model.*;
import com.bzs.model.CarInfo;
import com.bzs.service.*;
import com.bzs.utils.*;
import com.bzs.utils.commons.ThirdAPI;
import com.bzs.utils.dateUtil.DateUtil;
import com.bzs.utils.encodeUtil.EncodeUtil;
import com.bzs.utils.enumUtil.InsuranceItems2;
import com.bzs.utils.enumUtil.InsuranceNameEnum;
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
    private QuoteInfoService quoteInfoService;
    @Resource
    private CarInfoService carInfoService;
    @Resource
    private CustomerService customerService;
    @Resource
    private InsuranceFollowInfoService insuranceFollowInfoService;
    @Resource
    private InsuredInfoService insuredInfoService;
    @Resource
    private InsuranceTypeInfoMapper insuranceTypeInfoMapper;
    @Resource
    private OrderInfoMapper orderInfoMapper;


    @Override
    public Map quoteDetails(String carInfoId) {
        //报价信息
        List<QuoteInfo> quoteInfo=quoteInfoMapper.getQuote(carInfoId);
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
        List insuredList=null;
        if (insuredInfo!=null){
            insuredList=quoteInfoMapper.getInsurance(insuredInfo.getInsuredId(),0);
        }
        List TquoteList=null;
        List RquoteList=null;
        List PquoteList=null;
        System.out.println(quoteInfo.size());
        System.out.println(ResultGenerator.genSuccessResult(quoteInfo));
        QuoteInfo quote=null;
        for (int i=0;i<quoteInfo.size();i++){
            System.out.println(quoteInfo.get(i).getQuoteSource());
            switch (quoteInfo.get(i).getQuoteSource()){
                case "1":
                    TquoteList=quoteInfoMapper.getInsurance(quoteInfo.get(i).getQuoteId(),1);
                    break;
                case "2":
                    PquoteList=quoteInfoMapper.getInsurance(quoteInfo.get(i).getQuoteId(),1);
                    break;
                case "4":
                    TquoteList=quoteInfoMapper.getInsurance(quoteInfo.get(i).getQuoteId(),1);
                    break;
            }
        }
        Map map=new HashMap();
        map.put("quote",quoteInfo);
        map.put("customer",customer);
        map.put("carInfo",carInfo);
        map.put("insuranceFollowInfo",insuranceFollowInfo);
        map.put("insuredInfo",insuredInfo);
        map.put("insuredList",insuredList);
        map.put("TquoteList",TquoteList);
        map.put("PquoteList",PquoteList);
        map.put("TquoteList",TquoteList);
        return map;
    }

    @Override
    public Result getQuoteDetailsByApi(QuoteParmasBean params, List<InsurancesList> list, String carInfoId, String createdBy, Long source) {
        /* if (CollectionUtils.isNotEmpty(list)) {*/
        ParamsData data = params.getData();
        if (null != data) {
            data.setInsurancesList(list);
            //---------------------------  注意修改开始
            if(StringUtils.isBlank(createdBy)){
                createdBy = UUIDS.getDateUUID();
            }
            if (StringUtils.isBlank(carInfoId)) {
                carInfoId = UUIDS.getDateUUID();
            }
            if (source == null) {
                source = 1L;
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
           logger.info("当前时间+"+date);
            quoteInfo.setCreatedTime(new Date());
            // if ("1".equals(status)||"0".equals(status)) {//报价成功,其他均为报价失败
            String body = (String) quoteMap.get("body");//爬虫返回的数据
            PCICResponseBean bean = (PCICResponseBean) quoteMap.get("data");//body 转为实体对象
            if (null != bean) {
                String retCode = bean.getRetCode();
                String retMsg = bean.getRetMsg();
                if(retMsg.indexOf("需双录")>-1)retMsg+=",请人工核保";
                retMsg = EncodeUtil.unicodeToString(retMsg);
                logger.info("retCode=" + retCode + ",retMsg=" + retMsg);
                if (StringUtils.isNotBlank(retCode)) {
                    ResponseData rdata = bean.getData();
                    if (null != rdata) {
                        String ciPremium = rdata.getCiPremium();//交强险保费合计
                        String biPremium = rdata.getBiPremium();//商业险标准保费
                        String proposalNo = rdata.getProposalNo();//报价单号
                        if ("0000".equals(retCode)) {//报价 核保
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
                        }
                        List<InsuranceTypeInfo> insuranceTypeInfoList = new ArrayList<InsuranceTypeInfo>();
                        PayInfo payinfo = rdata.getPayInfo();
                        if (null != payinfo) {
                            String payUrl = payinfo.getPayUrl();
                            String payTime = payinfo.getPayTime();
                        }
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
                        quoteInfo.setBizPremiumByDis(biPremiumByDis);
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
                                InsuranceTypeInfo insuranceTypeInfo = new InsuranceTypeInfo(UUIDS.getDateUUID());
                                insuranceTypeInfo.setInfoType("1");//标记属于报价项
                                if (StringUtils.isNotBlank(insuredPremium)) {
                                    insuredPremium = insuredPremium.replaceAll(",", "");
                                    insuranceTypeInfo.setInsurancePremium(new BigDecimal(insuredPremium));
                                }
                                if (StringUtils.isNotBlank(insuredAmount)) {
                                    insuredAmount = insuredAmount.replaceAll(",", "");
                                    insuranceTypeInfo.setInsuranceAmount(new BigDecimal(insuredAmount));
                                }
                                logger.info("保险名称：本名"+name1+"后台统一"+name+"代码："+code);
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
                        if (bean.getRetCode().equals("0099")){
                            return ResultGenerator.gen("失败", bean.getRetMsg(), ResultCode.FAIL);
                        }
                        return ResultGenerator.gen("成功", bean, ResultCode.SUCCESS);
                    } else {
                        quoteInfoMapper.insert(quoteInfo);
                        //if(retMsg.indexOf("需双录")>-1)retMsg+=",请人工核保";
                        return ResultGenerator.gen(retMsg, "", ResultCode.FAIL);
                    }
                }

            } else {
                logger.info("报价内容获取为空:" + msg);
                quoteInfo.setQuoteResult(msg);
                quoteInfo.setSubmitresult(msg);
                quoteInfoMapper.insert(quoteInfo);
                return ResultGenerator.gen("报价失败", bean, ResultCode.FAIL);
            }
              /*  } else {//400
                    quoteInfo.setQuoteResult("报价失败");
                    quoteInfo.setSubmitresult("核保失败");
                    quoteInfoMapper.insert(quoteInfo);
                    return ResultGenerator.gen(msg, "", ResultCode.FAIL);
                }*/
        }

        return ResultGenerator.gen("参数异常", "", ResultCode.FAIL);
        /*} else {
            return ResultGenerator.gen("参数异常", "", ResultCode.FAIL);
        }*/
    }

    public Map<String, Object> getQuoteDetailsByApi(Long source, String createdBy, QuoteParmasBean params) {
        Map<String, Object> result = new HashMap<>();
        String jsonStr = JSON.toJSONString(params);
        String api = "";
        String host = ThirdAPI.HOST;
        String port = "";
        if (null == source) {
            result.put("status", "400");
            result.put("msg", "参数错误");
            return result;
        } else {
            if (1 == source) {
                // api = ThirdAPI.CPIC_QUOTE_NAME;
                host = ThirdAPI.CPIC_HOST;
                api = ThirdAPI.CPIC_QUOTE_ALL;
                port = ThirdAPI.CPIC_PORT;
            } else if (2 == source) {
                host = ThirdAPI.PAIC_HOST;
                api = ThirdAPI.PAIC_QUOTE_NAME;
                port = ThirdAPI.PAIC_PORT;
            } else if (4 == source) {
                host = ThirdAPI.PICC_HOST;
                api = ThirdAPI.PICC_QUOTE_ALL;
                port = ThirdAPI.PICC_PORT;
            } else {
                result.put("status", "400");
                result.put("msg", "参数错误");
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
            int code = httpResult.getCode();
            String msg = httpResult.getMessage();
            String body = httpResult.getBody();
            if (200 == code) {//远程请求成功
                PCICResponseBean bean = (PCICResponseBean) httpResult.getT();
                String state = bean.getState();
                String retCode = bean.getRetCode();
                String retMsg = bean.getRetMsg();
                retMsg = EncodeUtil.unicodeToString(retMsg);
                /*if(StringUtils.isNotBlank(state)&&"1".equals(state)){
                    result.put("status", "1");
                }
                if(StringUtils.isNotBlank(retCode)&&retCode.equals("0099")||retCode.equals("0002")||retCode.equals("0001")){
                    result.put("status", retCode);
                }*/
                result.put("status", state);
                result.put("msg", retMsg);
                result.put("data", bean);
                result.put("body", body);
                return result;
            } else {
                result.put("status", code + "");
                result.put("msg", "报价失败，报错代码：" + code);
                return result;
            }
        } else {
            result.put("status", "400");
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
    public Result getPayMentgetPayMent(String proposalNo, String pay, String money, String createdBy, String carInfoId, String quoteId, Long source) {
        if (StringUtils.isBlank(proposalNo)) {
            return ResultGenerator.genFailResult("参数异常");
        }
        if (StringUtils.isNotBlank(pay) && "1".equals(pay)) {
            pay = "alipay";
        } else {
            pay = "weixin";
        }
        String host = "";
        String port = "";
        String api = "";
        if (null == source || 1 == source) {//太保报价
           host=ThirdAPI.CPIC_HOST;
           port=ThirdAPI.CPIC_PORT;
           api = ThirdAPI.CPIC_PAY;
        } else if (2 == source) {//平安
            host=ThirdAPI.PAIC_HOST;
            port=ThirdAPI.PAIC_PORT;
            api = ThirdAPI.PAIC_PAY;
        } else if (4 == source) {//人保
            host=ThirdAPI.PICC_HOST;
            port=ThirdAPI.PICC_PORT;
            api = ThirdAPI.PICC_PAY;
        } else {
            return ResultGenerator.genFailResult("参数异常");
        }
        String URL = host + ":" + port + "/" + api;
        JSONObject json = new JSONObject();
        json.put("proposalNo", proposalNo);
        json.put("pay", pay);
        String sendTime=DateUtil.getDateToString(new Date(),"yyyy-MM-dd HH:mm:ss");
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
                    String retCode=bean.getRetCode();
                    if ("1".equals(state)&&"0000".equals(retCode)) {
                        String retMsg = bean.getRetMsg();
                        PayInfoData payinfo = bean.getData();
                        if (payinfo != null) {
                            String payUrl = payinfo.getPayUrl();
                            String payTime = payinfo.getPayTime();
                            String checkNo=bean.getCheckNo();
                            String payNo=bean.getPayNo();
                            String paymentNotice=payinfo.getPaymentNotice();
                            String serialNo=payinfo.getSerialNo();
                            retMsg=EncodeUtil.unicodeToString(retMsg);
                            payinfo.setPayMsg(retMsg);
                            String payEndDate=DateUtil.getDateStringFromString(retMsg);
                            payinfo.setPayEndDate(payEndDate);
                            bean.setPaymentNotice(paymentNotice);
                            bean.setSerialNo(serialNo);
                           // PayInfo payinfos = payinfo.getPayInfo();
                            quoteInfoMapper.updatePayInfo(payUrl, payTime, proposalNo,payNo,checkNo,paymentNotice,serialNo,payEndDate,retMsg);
                            OrderInfo orderInfo = new OrderInfo();
                            orderInfo.setOrderId(UUIDS.getDateUUID());
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
                            orderInfoMapper.insert(orderInfo);
                            return ResultGenerator.gen("获取成功", bean, ResultCode.SUCCESS);
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
}
