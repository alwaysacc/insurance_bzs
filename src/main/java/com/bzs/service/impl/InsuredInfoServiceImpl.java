package com.bzs.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bzs.dao.CarInfoMapper;
import com.bzs.dao.CustomerMapper;
import com.bzs.dao.InsuredInfoMapper;
import com.bzs.model.*;
import com.bzs.model.query.CarInfoAndInsuranceInfo;
import com.bzs.service.*;
import com.bzs.utils.*;

import com.bzs.utils.commons.ThirdAPI;
import com.bzs.utils.dateUtil.DateUtil;
import com.bzs.utils.encodeUtil.EncodeUtil;
import com.bzs.utils.enumUtil.InsuranceNameEnum;
import com.bzs.utils.httpUtil.HttpClientUtil;
import com.bzs.utils.httpUtil.HttpResult;
import com.bzs.utils.jsonToMap.JsonToMapUtil;
import com.bzs.utils.jsontobean.InsuranceTypeBase;
import com.bzs.utils.jsontobean.P;
import com.bzs.utils.jsontobean.RenewalBean;
import com.bzs.utils.jsontobean.RenewalData;
import com.bzs.utils.threadUtil.CompletableFutureDemo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.*;

/**
 * Created by denglei on 2019/04/10 17:09:11.
 */
@Service
@Transactional
public class InsuredInfoServiceImpl extends AbstractService<InsuredInfo> implements InsuredInfoService {
    private static final Logger logger = LoggerFactory.getLogger(InsuredInfoServiceImpl.class);

    @Resource
    private InsuredInfoMapper insuredInfoMapper;
    @Resource
    private InsuredInfoService insuredInfoService;
    @Resource
    private CheckInfoService checkInfoService;
    @Resource
    private CarInfoService carInfoService;
    @Resource
    private QuoteInfoService quoteInfoService;
    @Resource
    private InsuranceTypeInfoService insuranceTypeInfoService;
    @Resource
    private CarInfoMapper carInfoMapper;
    @Resource
    private ThirdInsuranceAccountInfoService thirdInsuranceAccountInfoService;
    @Resource
    private CustomerService customerService;

    @Override
    public Result checkByCarNoOrVinNo2(String checkType, String carNo, String idCard, String vinNo, String engineNo, Long lastYearSource, String insuredArea, String createdBy) {
        if (StringUtils.isBlank(checkType)) {
            return ResultGenerator.genFailResult("参数异常");
        } else {
            String url = ThirdAPI.HOST;
            if (StringUtils.isBlank(idCard)) {
                idCard = "";
            }//身份证号为空时为"";
            String uuid = UUIDS.getDateUUID();
            CheckInfo checkInfo = new CheckInfo(uuid);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("checkType",checkType);
            if ("0".equals(checkType)) {//车牌续保
                if (StringUtils.isNotBlank(carNo)) {
                    jsonObject.put("carNo", carNo);
                    jsonObject.put("frameNo", "");
                    jsonObject.put("cardID", idCard);
                    checkInfo.setCarNo(carNo);
                    checkInfo.setCheckType(checkType + "");//查询方式
                } else {
                    return ResultGenerator.genFailResult("参数异常,车牌号不能为空");
                }
            } else if ("1".equals(checkType)) {//车架续保
                if (StringUtils.isNotBlank(vinNo)) {
                    jsonObject.put("carNo", "");
                    jsonObject.put("frameNo", vinNo);
                    jsonObject.put("cardID", "");
                    checkInfo.setVinNo(vinNo);
                    checkInfo.setCheckType(checkType + "");//查询方式
                } else {
                    return ResultGenerator.genParamsErrorResult("参数异常,车架号不能为空");
                }
            } else {//待拓展
                return ResultGenerator.genParamsErrorResult("参数异常,目前仅支持车牌或者车架号续保");
            }
            checkInfo.setCarInfoId(uuid);
            //待修改
            checkInfo.setCreateBy(createdBy);
            String date = DateUtil.getDateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
            checkInfo.setSendTime(date);
            checkInfoService.save(checkInfo);
            //三家同时续保
            // Map<String, Object> renewalInfo = getRenewalInfo(lastYearSource, jsonObject.toJSONString(), createdBy);
            Map<String, Object> renewalInfo = null;
            if (lastYearSource == null) {//续保三家
                renewalInfo = getRenewalInfo(jsonObject, createdBy);
            } else {//指定一家续保
                renewalInfo = getDifferentSourceRenewalInfo(lastYearSource, jsonObject, createdBy);
            }
            String status = (String) renewalInfo.get("status");
            String msg = (String) renewalInfo.get("msg");
            Long source = (Long) renewalInfo.get("source");
            if (StringUtils.isNotBlank(status)) {
                if ("1".equals(status)) {
                    InsuredInfo insuredInfo = (InsuredInfo) renewalInfo.get("insuredInfo");
                    insuredInfo.setInsuredId(uuid);//设置续保id
                    insuredInfo.setCreateId(createdBy);
                    insuredInfo.setCarInfoId(uuid);
                    List<InsuranceTypeInfo> insuranceTypeInfoList = (List<InsuranceTypeInfo>) renewalInfo.get("insuranceTypeInfoList");
                    String body = (String) renewalInfo.get("body");
                    RenewalBean dataBean = (RenewalBean) renewalInfo.get("data");
                    CarInfo carInfo = (CarInfo) renewalInfo.get("carInfo");
                    if (StringUtils.isNotBlank(carNo)) {
                        carInfo.setCarNumber(carNo);
                    }
                    if (StringUtils.isNotBlank(vinNo)) {
                        carInfo.setFrameNumber(vinNo);
                    }
                    //vinNo = carInfo.getFrameNumber();
                    carInfo.setCreatedBy(createdBy);
                    //List list=carInfoMapper.findOneBy(carInfo);
                    carInfo.setChannelType(checkType + "");
                    carInfo.setCarInfoId(uuid);
                    carInfo.setBrandModel(dataBean.getData().getCarName());
                    //carInfo.setBrandModel(body);
                    List list = carInfoMapper.findOneBy(carInfo);
                    if (CollectionUtils.isEmpty(list)) {//未查到
                        /*carInfo.setChannelType(checkType+"");
                        carInfo.setCarInfoId(uuid);*/
                        if (null != source && source != 0) {
                            lastYearSource = source;
                            insuredInfo.setLastYearSource(source + "");
                            insuredInfo.setLastYearInsuranceCompany(InsuranceNameEnum.getName(source));
                        } else {
                            if (lastYearSource != null && lastYearSource != 0) {
                                insuredInfo.setLastYearSource(lastYearSource + "");
                                insuredInfo.setLastYearInsuranceCompany(InsuranceNameEnum.getName(lastYearSource));
                            } else {
                                insuredInfo.setLastYearSource("0");
                                insuredInfo.setLastYearInsuranceCompany("获取失败");
                            }
                        }
                        insuredInfoService.save(insuredInfo);
                        for (InsuranceTypeInfo datas : insuranceTypeInfoList) {
                            if (null != datas) {
                                datas.setTypeId(uuid);
                                insuranceTypeInfoService.save(datas);
                            }

                        }
                        carInfoService.save(carInfo);
                    } else {//查到急及需要修改
                        logger.info("已经存在无需保存");
                    }
                    dataBean.getData().setSource(lastYearSource + "");
                    dataBean.getData().setList(insuranceTypeInfoList);
                    dataBean.getData().setCarNo(carNo);
                    Map maps = JsonToMapUtil.bodyJsonToMap(body);
                    maps.put("carNo", carNo);
                    maps.put("source", lastYearSource + "");
                    maps.put("carInfoId", uuid);
                    return ResultGenerator.gen(msg, maps, ResultCode.SUCCESS);
                } else if ("0099".equals(status)) {//续保选择的保险公司不对,重新续保
                    String body = (String) renewalInfo.get("body");
                    RenewalBean dataBean = (RenewalBean) renewalInfo.get("data");
                    CarInfo carInfo = new CarInfo();
                    carInfo.setCreatedBy(createdBy);
                    if (StringUtils.isBlank(carNo)) {
                        carInfo.setCarNumber(vinNo);
                    }
                    carInfo.setCarNumber(carNo);
                    carInfo.setFrameNumber(vinNo);
                    List list = carInfoMapper.findOneBy(carInfo);
                    if (CollectionUtils.isEmpty(list)) {//未查到
                        carInfo.setChannelType(checkType + "");
                        carInfo.setCarInfoId(uuid);
                        carInfoService.save(carInfo);
                    }
                    return ResultGenerator.gen(msg, dataBean, ResultCode.FAIL);
                } else {
                    return ResultGenerator.genFailResult(msg);
                }
            }
            return ResultGenerator.genFailResult("获取失败");
        }

    }

    //续保三家
    public Map<String, Object> getRenewalInfo(JSONObject jsonStr, String createdBy) {
        Long[] source = {1L, 2L, 4L};
        List<Long> sources = Arrays.asList(source);
        HttpResult httpResult = getDifferentSourceRenewalInfo(sources, jsonStr, createdBy);
        String uuid = UUIDS.getDateUUID();
        return getResult(httpResult, uuid, createdBy);
    }

    //续保结果
    public Map<String, Object> getResult(HttpResult httpResult, String quoteId, String createdBy) {
        Map<String, Object> result = new HashMap<>();
        if (httpResult != null) {
            int code = httpResult.getCode();
            Long source = httpResult.getSource();
            String msg = httpResult.getMessage();
            if (null == source) {
                result.put("source", 0L);
            } else {
                result.put("source", source);
            }
            if (code == 200) {
                RenewalBean bean = (RenewalBean) httpResult.getT();
                String body = httpResult.getBody();
                if (null == bean && StringUtils.isNotBlank(body)) {
                    bean = JSONObject.parseObject(body, RenewalBean.class);
                }
                result.put("data", bean);
                result.put("body", body);
                // result.put("data", bean);
                if (null != bean) {
                    String retMsg = bean.getMessage();
                    // retMsg = EncodeUtil.unicodeToString(retMsg);
                    //  logger.info("描述信息："+retMsg);
                    //注意此处为暂时设置,实现登录后可从session 获取
                    // checkInfo.setCheckInfoId(uuid);
                    InsuredInfo insuredInfo = new InsuredInfo();
                    // insuredInfo.setCarInfoId(uuid);
                    CarInfo carInfo = new CarInfo();
                    //carInfo.setCreatedBy(uuid);
                    String state = bean.getState();
                    RenewalData data = bean.getData();
                    String sendTime = bean.getSendTime();
                    List<InsuranceTypeInfo> insuranceTypeInfoList = null;
                    if ("1".equals(state)) {//获取成功
                        if (data != null) {
                            insuranceTypeInfoList = InsuranceTypeBase.getInsuranceTypeInfoList(data, quoteId, createdBy, "0");
                            String engineNoNew = data.getEngineNo();//发送机号
                            String registerDate = data.getFirstRegisterDate();//车辆注册日期
                            String bizStartDate = data.getBiBeginDate();//商业险下次起保日期
                            String bizPreminm = data.getBiPremium();//商业险保额
                            String forceStartDate = data.getCiBeginDate();//交强险下次起保日期
                            String forcePreminm = data.getCiPremium();//交强险保额
                            String idCardNew = data.getCardID();//车主证件
                            logger.info("车主证件号" + idCardNew);
                            String vinNONew = data.getFrameNo();//车架号
                            String tel = data.getMobile();//手机号
                            String licenseOwner = data.getName();//车主
                            String model = data.getVehicleFgwCode();//车辆型号
                            String bizEndDate = data.getBiEndDate();//商业险到期
                            String forceEndDate = data.getCiEndDate();
                            String carNo = data.getCarNo();
                            carInfo.setFrameNumber(vinNONew);
                            carInfo.setCarModel(model);
                            carInfo.setEngineNumber(engineNoNew);
                            carInfo.setLicenseOwner(licenseOwner);
                            carInfo.setLicenseOwnerIdCard(idCardNew);
                            carInfo.setRegisterDate(registerDate);
                            carInfo.setMobile(tel);
                            carInfo.setCarNumber(carNo);
                            if (StringUtils.isNotBlank(idCardNew)) {
                                boolean b = IdCardUtil.validateCardBy14(idCardNew);
                                if (b) {
                                    carInfo.setLicenseOwnerIdCardType("1");//身份证
                                } else {
                                    carInfo.setLicenseOwnerIdCardType("2"); //组织机构
                                }
                            }
                            //车辆信息设置结束
                            //投保信息设置开始
                            insuredInfo.setNextBusinesStartDate(bizStartDate);
                            insuredInfo.setNextForceStartDate(forceStartDate);
                            insuredInfo.setBusinesExpireDate(bizEndDate);
                            insuredInfo.setForceExpireDate(forceEndDate);
                            insuredInfo.setLastYearSource(source + "");
                            String lastYearInsuranceCompany = InsuranceNameEnum.getName(source);
                            insuredInfo.setLastYearInsuranceCompany(lastYearInsuranceCompany);
                            result.put("status", "1");
                            result.put("msg", "获取上年续保信息成功");
                            result.put("insuranceTypeInfoList", insuranceTypeInfoList);
                            result.put("carInfo", carInfo);
                            result.put("insuredInfo", insuredInfo);
                            return result;
                        }
                    } else if ("0099".equals(state)) {//查询返回成功但未获取到续保信息
                        result.put("status", "0099");
                        result.put("msg", "未获取续保信息");
                        return result;
                    } else if ("0".equals(state)) {//查询返回成功但未获取到续保信息
                        result.put("status", "0099");
                        if (StringUtils.isNotBlank(body)) {
                            JSONObject jsonObject = JSONObject.parseObject(body);
                            if (jsonObject.containsKey("retMsg")) {
                                retMsg = jsonObject.getString("retMsg");
                                retMsg = EncodeUtil.unicodeToString(retMsg);
                                result.put("msg", retMsg);
                            } else {
                                result.put("msg", "续保失败");
                            }
                        } else {
                            result.put("msg", "未获取续保信息");
                        }
                        return result;
                    } else if ("19000".equals(state)) {
                        result.put("status", "400");
                        result.put("msg", retMsg);
                        return result;
                    } else {//续保失败
                        result.put("status", "400");
                        result.put("msg", "续保失败");
                        return result;
                    }
                }

            } else {
                result.put("status", "400");
                result.put("msg", msg);
                result.put("data", null);
                return result;
            }
        }
        result.put("status", "400");
        result.put("msg", "续保失败");
        result.put("data", null);
        return result;
    }

    /**
     * @param source    保险枚举值1太保2平安4人保
     * @param createdBy 创建人id
     * @return
     */
    public Map<String, Object> getDifferentSourceRenewalInfo(Long source, JSONObject jsonObject, String createdBy) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (null == source) {
            result.put("status", "400");
            result.put("msg", "参数异常");
            result.put("data", null);
            return result;
        }

        String api = "";
        String port = "";
        String host = "";
        Map map = thirdInsuranceAccountInfoService.findEnbaleAccount(source, "1", createdBy);
        String code = (String) map.get("code");
        if ("200".equals(code)) {
            ThirdInsuranceAccountInfo data = (ThirdInsuranceAccountInfo) map.get("data");
            host = data.getIp();
            port = data.getPort();
            if (StringUtils.isBlank(host) || StringUtils.isBlank(port)) {
                result.put("status", "400");
                result.put("msg", "用于续保的账号信息不完整,无法续保");
                result.put("data", null);
                return result;
            }
            String dataType = data.getAccountType();
            if (StringUtils.isBlank(dataType)) {
                result.put("status", "400");
                result.put("msg", "用于续保的账号信息不完整,无法续保");
                result.put("data", null);
                return result;
            } else {
                if ("2".equals(dataType)) {//平安需要账号
                    String accountName = data.getAccountName();
                    String accountPwd = data.getAccountPwd();
                    String checkType=jsonObject.getString("checkType");
                    if("0".equals(checkType)){//身份证是不否符合规范
                        String idcard=jsonObject.getString("cardID");
                        if(StringUtils.isNotBlank(idcard)){
                            boolean isIdCard=IdCardUtil.validateCard(idcard);
                            if(!isIdCard){
                                result.put("status", "400");
                                result.put("msg", "平安续保请填写正确的身份证号");
                                result.put("data", null);
                                return result;
                            }
                        }
                    }

                    if (StringUtils.isNotBlank(accountName) && StringUtils.isNotBlank(accountPwd)) {
                        jsonObject.put("account", accountName);
                        jsonObject.put("password", accountPwd);
                    } else {
                        result.put("status", "400");
                        result.put("msg", "用于续保的平安账号信息不完整,无法续保");
                        result.put("data", null);
                        return result;
                    }

                }
            }

        } else {
            String msg = (String) map.get("msg");
            result.put("status", "400");
            result.put("msg", msg);
            result.put("data", null);
            return result;
        }
        if (1 == source) {//太保
            // host = ThirdAPI.CPIC_HOST;
            // port = ThirdAPI.CPIC_PORT;
            api = ThirdAPI.CPIC_RENEWAL_NAME;
            logger.info("续保枚举值1：太保");
        } else if (2 == source) {//平安
            //host = ThirdAPI.PAIC_HOST;
            // port = ThirdAPI.PAIC_PORT;
            api = ThirdAPI.PAIC_RENEWAL_NAME;
            logger.info("续保枚举值2：平安");
        } else if (4 == source) {//人保
            // host = ThirdAPI.PICC_HOST;
            // port = ThirdAPI.PICC_PORT;
            api = ThirdAPI.PICC_RENEWAL_NAME;
            logger.info("续保枚举值4：人保");
        } else {
            result.put("status", "400");
            result.put("msg", "待拓展");
            result.put("data", null);
            return result;
        }

        String URL = host + ":" + port + "/" + api;
        HttpResult httpResult = HttpClientUtil.doPost(URL, null, "JSON", RenewalBean.class, jsonObject.toJSONString());
        httpResult.setSource(source);
        String uuid = UUIDS.getDateUUID();
        return getResult(httpResult, uuid, createdBy);
    }

    /**
     * @param source  为空则多家续保
     * @param jsonStr 必填json参数
     * @return
     * @description 启用线程续保
     */
    public HttpResult getDifferentSourceRenewalInfo(List<Long> source, JSONObject jsonStr, String creatBy) {
        HttpResult result = null;
        Long start1 = System.currentTimeMillis();
        JSONObject jsonObject = new JSONObject();
        List<CompletableFuture<HttpResult>> list = new ArrayList<CompletableFuture<HttpResult>>();


        for (Long sour : source) {
            if (null != sour) {
                String name = InsuranceNameEnum.getName(sour);
                Map map = thirdInsuranceAccountInfoService.findEnbaleAccount(sour, "1", creatBy);
                String code = (String) map.get("code");
                if ("200".equals(code)) {
                    ThirdInsuranceAccountInfo data = (ThirdInsuranceAccountInfo) map.get("data");
                    String host = data.getIp();
                    String port = data.getPort();
                    String accountName = data.getAccountName();
                    String accountPwd = data.getAccountPwd();
                    CompletableFuture<HttpResult> f = CompletableFuture.supplyAsync(() -> {
                        logger.info(name + "开始：");
                        Long start = System.currentTimeMillis();
                        String api = "";
                        boolean flag = true;
                        String mm="";
                        if (1L == sour) {
                            api = ThirdAPI.CPIC_RENEWAL_NAME;
                        } else if (2L == sour) {
                            api = ThirdAPI.PAIC_RENEWAL_NAME;
                            if (StringUtils.isNotBlank(accountName) && StringUtils.isNotBlank(accountPwd)) {
                                jsonStr.put("account", accountName);
                                jsonStr.put("password", accountPwd);
                            } else {
                                flag = false;
                                mm="用于续保的平安账号信息不完整,无法续保;";
                            }
                            String checkType=jsonStr.getString("checkType");
                            if("0".equals(checkType)){
                                String idcard=jsonStr.getString("cardID");
                                logger.info("获取身份证号:"+idcard);
                                if(!IdCardUtil.validateCard(idcard)){//身份证是不否符合规范
                                    flag = false;
                                    mm="平安续保,未填写身份证号,无法续保;";
                                }
                            }


                        } else if (4L == sour) {
                            api = ThirdAPI.PICC_RENEWAL_NAME;
                        }else{
                            mm="待拓展公司";
                        }
                        HttpResult httpResult = null;
                        if (StringUtils.isNotBlank(host) && StringUtils.isNotBlank(port) && StringUtils.isNotBlank(api) && flag) {
                            String url = host + ":" + port + "/" + api;
                            httpResult = HttpClientUtil.doPost(url, null, "JSON", RenewalBean.class, jsonStr.toJSONString());
                        } else {
                            //String m = "用于续保的账号信息不完整,无法续保";
                            jsonObject.put("state", "19000");
                            jsonObject.put("message", mm);
                            String body = jsonObject.toJSONString();
                            httpResult = new HttpResult();
                            httpResult.setCode(200);
                            httpResult.setMessage(mm);
                            httpResult.setBody(body);
                            RenewalBean bean = JSONObject.parseObject(body, RenewalBean.class);
                            httpResult.setT(bean);
                        }
                        httpResult.setSource(sour);
                        Long end = System.currentTimeMillis();
                        logger.info(name + "结束，总时间：" + (end - start));
                        return httpResult;
                    });
                    list.add(f);
                } else {
                    logger.info(name + "开始：");
                    Long start = System.currentTimeMillis();
                    String msgs = (String) map.get("msg");
                    String messages = name + "账号异常：" + msgs;
                    CompletableFuture<HttpResult> f = CompletableFuture.supplyAsync(() -> {
                        jsonObject.put("state", "19000");
                        jsonObject.put("message", messages);
                        String body = jsonObject.toJSONString();
                        HttpResult httpResult = new HttpResult();
                        httpResult.setCode(200);
                        httpResult.setMessage(messages);
                        httpResult.setBody(body);
                        RenewalBean bean = JSONObject.parseObject(body, RenewalBean.class);
                        httpResult.setT(bean);
                        httpResult.setSource(sour);
                        Long end = System.currentTimeMillis();
                        logger.info(name + "结束，总时间：" + (end - start));
                        return httpResult;
                    });
                    list.add(f);
                }
            }
        }
        try {

            //  String cpicurl = ThirdAPI.CPIC_HOST + ":" + ThirdAPI.CPIC_PORT + "/" + ThirdAPI.CPIC_RENEWAL_NAME;
            //  String piccurl = ThirdAPI.PICC_HOST + ":" + ThirdAPI.PICC_PORT + "/" + ThirdAPI.PICC_RENEWAL_NAME;
            //  String paiccurl = ThirdAPI.PAIC_HOST + ":" + ThirdAPI.PAIC_PORT + "/" + ThirdAPI.PAIC_RENEWAL_NAME;
            // if (null == source) {//人太平同时续保
              /* Callable pciccallable = new RenewalThreadCallable(cpicurl, null, type, null, jsonStr);
               Callable picccallable = new RenewalThreadCallable(piccurl, null, type, null, jsonStr);
               Callable paiccallable = new RenewalThreadCallable(paiccurl, null, type, null, jsonStr);
               ExecutorService es = Executors.newCachedThreadPool();
               List<Future<HttpResult>> list = new ArrayList<>();
               list.add(es.submit(pciccallable));
               list.add(es.submit(picccallable));
               list.add(es.submit(paiccallable));
               for (Future future:list) {
                    result=(HttpResult) future.get();
               }
               es.shutdown();*/

               /* String cpicurl = ThirdAPI.CPIC_HOST + ":" + ThirdAPI.CPIC_PORT + "/" + ThirdAPI.CPIC_RENEWAL_NAME;
                String piccurl = ThirdAPI.PICC_HOST + ":" + ThirdAPI.PICC_PORT + "/" + ThirdAPI.PICC_RENEWAL_NAME;
                String paiccurl = ThirdAPI.PAIC_HOST + ":" + ThirdAPI.PAIC_PORT + "/" + ThirdAPI.PAIC_RENEWAL_NAME;
                CompletableFuture<HttpResult> f1 = CompletableFuture.supplyAsync(() -> {
                    logger.info("太保续保开始：");
                    Long start = System.currentTimeMillis();
                    HttpResult httpResult = HttpClientUtil.doPost(cpicurl, null, "JSON", RenewalBean.class, jsonStr.toJSONString());
                    httpResult.setSource(1L);
                    Long end = System.currentTimeMillis();
                    logger.info("太保续保结束，总时间：" + (end - start));
                    return httpResult;
                });
                CompletableFuture<HttpResult> f2 = CompletableFuture.supplyAsync(() -> {
                    logger.info("人保续保开始：");
                    Long start = System.currentTimeMillis();
                    HttpResult httpResult = HttpClientUtil.doPost(piccurl, null, "JSON", RenewalBean.class, jsonStr.toJSONString());
                    httpResult.setSource(4L);
                    Long end = System.currentTimeMillis();
                    logger.info("人保续保结束，总时间：" + (end - start));
                    return httpResult;
                });*/
//            CompletableFuture<HttpResult> f3 = CompletableFuture.supplyAsync(() -> {
//                logger.info("平安续保开始：");
//                Long start = System.currentTimeMillis();
//                HttpResult httpResult = HttpClientUtil.doPost(paiccurl, null, "JSON", RenewalBean.class, jsonStr);
//                httpResult.setSource(2L);
//                Long end = System.currentTimeMillis();
//                logger.info("平安续保结束，总时间：" + (end - start));
//                return httpResult;
//            });
              /*  List<CompletableFuture<HttpResult>> list = new ArrayList<CompletableFuture<HttpResult>>();
                list.add(f1);
                list.add(f2);*/
            //list.add(f3);
            List<HttpResult> lists = null;
            boolean flag = false;
            try {
                lists = CompletableFutureDemo.sequence(list).get();
                String message = "";
                for (HttpResult httpResult : lists) {
                    //System.out.println(i);
                    if (null != httpResult) {
                        int code = httpResult.getCode();
                        if (200 == code) {
                            String body = httpResult.getBody();
                            if (StringUtils.isNotBlank(body)) {
                                RenewalBean bean = JSONObject.parseObject(body, RenewalBean.class);
                                String state = bean.getState();
                                if ("1".equals(state)) {
                                    result = httpResult;
                                    flag = true;
                                    break;
                                } else {//上一年不在此保司投保
                                    logger.info("续保返回值的状态值" + state);
                                    if ("0099".equals(state)) {
                                        message += "上一年不在" + InsuranceNameEnum.getName(httpResult.getSource()) + "续保;";
                                    } else if ("19000".equals(state)) {
                                        message += InsuranceNameEnum.getName(httpResult.getSource()) + "账号异常：" + httpResult.getMessage();
                                    } else if ("0".equals(state)) {
                                        JSONObject jsonObjects = JSONObject.parseObject(body);
                                        if (jsonObjects.containsKey("retMsg")) {
                                            String retMsg = jsonObjects.getString("retMsg");
                                            if (StringUtils.isNotBlank(retMsg)) {
                                                retMsg = EncodeUtil.unicodeToString(retMsg);
                                                message += InsuranceNameEnum.getName(httpResult.getSource()) + "续保：" + retMsg + ";";
                                            } else {
                                                message += InsuranceNameEnum.getName(httpResult.getSource()) + "续保：未获取续保信息";
                                            }
                                        }
                                    } else {
                                        message += InsuranceNameEnum.getName(httpResult.getSource()) + "续保失败：" + "状态值为" + state + ";";
                                    }

                                }
                            }
                        } else {//请求失败
                            logger.info("续保返回值的状态值" + httpResult.getCode());
                            message += InsuranceNameEnum.getName(httpResult.getSource()) + "续保：" + "状态值为" + httpResult.getCode() + ";";
                        }
                    }
                }
                if (!flag) {
                    JSONObject jsonObjects = new JSONObject();
                    jsonObject.put("state", "0099");
                    jsonObject.put("message", message);
                    String body = jsonObject.toJSONString();
                    result = new HttpResult();
                    result.setCode(200);
                    result.setMessage(message);
                    result.setBody(body);
                    result.setSource(1L);
                    RenewalBean bean = JSONObject.parseObject(body, RenewalBean.class);
                    result.setT(bean);
                    // result = lists.get(0);
                }
            } catch (Exception e) {
                logger.error("三家同时续保异常", e);
            }
            Long end = System.currentTimeMillis();
            System.out.println("总耗时:" + (end - start1));
            return result;
          /* } else {//一家续保

           }
           return result;*/

            } catch (Exception e) {
                result = new HttpResult();
                result.setMessage("续保失败");
                result.setCode(400);
                return result;

        }

    }

    @Override
    public Result checkByCarNoOrVinNo(String checkType, String carNo, String idCard, String vinNo, String engineNo, Long lastYearSource, String insuredArea, String createdBy) {
        if (StringUtils.isBlank(checkType)) {
            return ResultGenerator.genFailResult("参数异常");
        } else {
            if (StringUtils.isBlank(idCard)) {
                //身份证号为空时为"";
                idCard = "";
            }
            String uuid = UUIDS.getDateUUID();
            CheckInfo checkInfo = new CheckInfo(uuid);
            JSONObject jsonObject = new JSONObject();
            if ("0".equals(checkType)) {//车牌续保
                if (StringUtils.isNotBlank(carNo)) {
                    jsonObject.put("carNo", carNo);
                    jsonObject.put("frameNo", "");
                    jsonObject.put("cardID", idCard);
                    checkInfo.setCarNo(carNo);
                    checkInfo.setCheckType(checkType + "");//查询方式
                } else {
                    return ResultGenerator.genFailResult("参数异常,车牌号不能为空");
                }
            } else if ("1".equals(checkType)) {//车架续保
                if (StringUtils.isNotBlank(vinNo)) {
                    jsonObject.put("carNo", "");
                    jsonObject.put("frameNo", vinNo);
                    jsonObject.put("cardID", "");
                    checkInfo.setVinNo(vinNo);
                    checkInfo.setCheckType(checkType + "");//查询方式
                } else {
                    return ResultGenerator.genParamsErrorResult("参数异常,车架号不能为空");
                }
            } else {//待拓展
                return ResultGenerator.genParamsErrorResult("参数异常,目前仅支持车牌或者车架号续保");
            }
            checkInfo.setCreateBy(createdBy);
            String date = DateUtil.getDateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
            checkInfo.setSendTime(date);
            //三家同时续保
            // Map<String, Object> renewalInfo = getRenewalInfo(lastYearSource, jsonObject.toJSONString(), createdBy);
            Map<String, Object> renewalInfo = null;
            if (lastYearSource == null) {//续保三家
                renewalInfo = getRenewalInfo(jsonObject, createdBy);
            } else {//指定一家续保
                renewalInfo = getDifferentSourceRenewalInfo(lastYearSource, jsonObject, createdBy);
            }
            String status = (String) renewalInfo.get("status");
            String msg = (String) renewalInfo.get("msg");
            Long source = (Long) renewalInfo.get("source");
            if (StringUtils.isNotBlank(status)) {
                if ("1".equals(status)) {
                    InsuredInfo insuredInfo = (InsuredInfo) renewalInfo.get("insuredInfo");
                    insuredInfo.setInsuredId(uuid);//设置续保id
                    insuredInfo.setCreateId(createdBy);
                    insuredInfo.setCarInfoId(uuid);
                    List<InsuranceTypeInfo> insuranceTypeInfoList = (List<InsuranceTypeInfo>) renewalInfo.get("insuranceTypeInfoList");
                    String body = (String) renewalInfo.get("body");
                    RenewalBean dataBean = (RenewalBean) renewalInfo.get("data");
                    CarInfo carInfo = (CarInfo) renewalInfo.get("carInfo");
                    if (StringUtils.isNotBlank(carNo)) {
                        carInfo.setCarNumber(carNo);
                    }
                    if (StringUtils.isNotBlank(vinNo)) {
                        carInfo.setFrameNumber(vinNo);
                    }
                    carInfo.setIsEnable("0");//是否作废0默认使用1作废
                    carInfo.setIsRenewSuccess("1");//是否续保成功的数据1续保成功0失败
                    logger.info("车辆信息：" + carInfo.toString());
                    List<CarInfoAndInsuranceInfo> list = carInfoMapper.getCarInfoAndInsurance(carInfo);
                    carInfo.setCreatedBy(createdBy);
                    carInfo.setChannelType(checkType + "");
                    carInfo.setCarInfoId(uuid);
                    carInfo.setBrandModel(dataBean.getData().getCarName());
                    if(StringUtils.isNotBlank(idCard)){
                        String cardID=carInfo.getLicenseOwnerIdCard();
                        if(StringUtils.isNotBlank(cardID)){
                            int index =cardID.indexOf("*");
                            if(index>-1){
                                carInfo.setLicenseOwnerIdCard(idCard);
                            }
                        }
                    }

                    if (CollectionUtils.isNotEmpty(list)) {//存在则先作废然后直接添加
                        List<String> lists = new ArrayList<>();
                        for (CarInfoAndInsuranceInfo info : list) {
                            lists.add(info.getCarInfoId());
                        }
                        carInfoMapper.updateBatchIsEnable(lists, "1");//作废
                    }
                    if (null != source && source != 0) {
                        lastYearSource = source;
                        insuredInfo.setLastYearSource(source + "");
                        insuredInfo.setLastYearInsuranceCompany(InsuranceNameEnum.getName(source));
                    } else {
                        if (lastYearSource != null && lastYearSource != 0) {
                            insuredInfo.setLastYearSource(lastYearSource + "");
                            insuredInfo.setLastYearInsuranceCompany(InsuranceNameEnum.getName(lastYearSource));
                        } else {
                            insuredInfo.setLastYearSource("0");
                            insuredInfo.setLastYearInsuranceCompany("获取失败");
                        }
                    }
                    insuredInfoService.save(insuredInfo);
                    for (InsuranceTypeInfo datas : insuranceTypeInfoList) {
                        if (null != datas) {
                            datas.setTypeId(uuid);
                            insuranceTypeInfoService.save(datas);
                        }

                    }
                    //待修改
                    checkInfo.setCarInfoId(uuid);
                    checkInfo.setIsRenewSuccess("1");//是否续保成功
                    //checkInfo.setIsCheckSuccess("1");
                    checkInfoService.save(checkInfo);
                    carInfoService.save(carInfo);
                    dataBean.getData().setSource(lastYearSource + "");
                    dataBean.getData().setList(insuranceTypeInfoList);
                    dataBean.getData().setCarNo(carNo);
                    Map maps = JsonToMapUtil.bodyJsonToMap(body);
                    maps.put("carNo", carNo);
                    maps.put("source", lastYearSource + "");
                    maps.put("carInfoId", uuid);
                    return ResultGenerator.gen(msg, maps, ResultCode.SUCCESS);
                } else if ("0099".equals(status)) {//续保失败
                    String body = (String) renewalInfo.get("body");
                    RenewalBean dataBean = (RenewalBean) renewalInfo.get("data");
                    CarInfo carInfo = new CarInfo();
                    carInfo.setCarNumber(carNo);
                    carInfo.setFrameNumber(vinNo);
                    List<CarInfoAndInsuranceInfo> list = carInfoMapper.getCarInfoAndInsurance(carInfo);
                    if (StringUtils.isBlank(carNo)) {//车牌号为空则存储车架号
                        carInfo.setCarNumber(vinNo);
                    }
                    carInfo.setCreatedBy(createdBy);
                    if (CollectionUtils.isEmpty(list)) {//未查到  不存在
                        carInfo.setChannelType(checkType + "");
                        carInfo.setCarInfoId(uuid);
                        carInfoService.save(carInfo);
                        checkInfo.setCarInfoId(uuid);
                    }else{
                        checkInfo.setIsFirstTime("1");
                    }
                    checkInfoService.save(checkInfo);
                    return ResultGenerator.gen(msg, dataBean, ResultCode.FAIL);
                } else {
                    checkInfo.setIsCheckSuccess("0");
                    checkInfoService.save(checkInfo);
                    return ResultGenerator.genFailResult(msg);
                }
            }
            return ResultGenerator.genFailResult("获取失败");
        }
    }
}
