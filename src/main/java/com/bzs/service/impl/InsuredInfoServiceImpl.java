package com.bzs.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bzs.dao.CarInfoMapper;
import com.bzs.dao.InsuredInfoMapper;
import com.bzs.model.*;
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
import com.bzs.utils.jsontobean.RenewalBean;
import com.bzs.utils.jsontobean.RenewalData;
import com.bzs.utils.threadUtil.CompletableFutureDemo;
import com.bzs.utils.threadUtil.RenewalThreadCallable;
import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    private CheckInfoService checkInfoService;
    @Resource
    private CarInfoService carInfoService;
    @Resource
    private QuoteInfoService quoteInfoService;
    @Resource
    private InsuranceTypeInfoService insuranceTypeInfoService;
    @Resource
    private CarInfoMapper carInfoMapper;

    @Override
    public Result checkByCarNoOrVinNo(String checkType, String carNo, String idCard, String vinNo, String engineNo, Long lastYearSource, String insuredArea, String createdBy) {
        if (StringUtils.isBlank(checkType)) {
            return ResultGenerator.genFailResult("参数异常");
        } else {
            String url = ThirdAPI.HOST;
            Map<String, Object> paramMap = new HashMap<String, Object>();
            if (StringUtils.isBlank(idCard)) {
                idCard = "";
            }//身份证号为空时为"";
            paramMap.put("idCard", idCard);
            String uuid = UUIDS.getDateUUID();
            CheckInfo checkInfo = new CheckInfo(uuid);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("", idCard);
            if ("0".equals(checkType)) {//车牌续保
                if (StringUtils.isNotBlank(carNo)) {
                    paramMap.put("carNo", carNo);
                    paramMap.put("frameNo", "");
                    jsonObject.put("carNo", carNo);
                    jsonObject.put("frameNo", "");
                    checkInfo.setCarNo(carNo);
                    checkInfo.setCheckType(checkType + "");//查询方式
                } else {
                    return ResultGenerator.genFailResult("参数异常,车牌号不能为空");
                }
            } else if ("1".equals(checkType)) {//车架续保
                if (StringUtils.isNotBlank(vinNo)) {
                    paramMap.put("carNo", "");
                    paramMap.put("frameNo", vinNo);
                    checkInfo.setVinNo(vinNo);
                    checkInfo.setCheckType(checkType + "");//查询方式
                } else {
                    return ResultGenerator.genParamsErrorResult("参数异常,车架号不能为空");
                }
            } else {//待拓展
                return ResultGenerator.genParamsErrorResult("参数异常,目前仅支持车牌或者车架号续保");
            }
            if (StringUtils.isBlank(createdBy)) {
                createdBy = UUIDS.getDateUUID();
            }
          /*  if (null == lastYearSource) {
                lastYearSource = 1L;
            }*/
            checkInfo.setCarInfoId(uuid);
            //待修改
            checkInfo.setCreateBy(uuid);
            String date = DateUtil.getDateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
            checkInfo.setSendTime(date);
            checkInfoService.save(checkInfo);
            // Map<String, Object> renewalInfo=getRenewalInfo( lastYearSource , carNo,  vinNo,  createdBy);
            Map<String, Object> renewalInfo = getDifferentSourceRenewalInfo(lastYearSource, paramMap, carNo, vinNo, createdBy);
            String status = (String) renewalInfo.get("status");
            String msg = (String) renewalInfo.get("msg");
            if (StringUtils.isNotBlank(status)) {
                if ("1".equals(status)) {
                    InsuredInfo insuredInfo = (InsuredInfo) renewalInfo.get("insuredInfo");
                    insuredInfo.setCreateId(createdBy);
                    List<InsuranceTypeInfo> insuranceTypeInfoList = (List<InsuranceTypeInfo>) renewalInfo.get("insuranceTypeInfoList");
                    String body = (String) renewalInfo.get("body");
                    RenewalBean dataBean = (RenewalBean) renewalInfo.get("data");
                    CarInfo carInfo = (CarInfo) renewalInfo.get("carInfo");
                    carInfo.setCarNumber(carNo);
                    vinNo = carInfo.getFrameNumber();
                    //List list=carInfoMapper.findOneBy(carInfo);
                    carInfo.setChannelType(checkType + "");
                    carInfo.setCarInfoId(uuid);
                    List list = carInfoMapper.findOneBy(carInfo);
                    if (CollectionUtils.isEmpty(list)) {//未查到
                        /*carInfo.setChannelType(checkType+"");
                        carInfo.setCarInfoId(uuid);*/
                        insuredInfoMapper.insert(insuredInfo);
                        for (InsuranceTypeInfo datas : insuranceTypeInfoList) {
                            insuranceTypeInfoService.save(datas);
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
                    maps.put("source", lastYearSource);
                    return ResultGenerator.gen(msg, maps, ResultCode.SUCCESS);
                } else if ("0099".equals(status)) {//续保选择的保险公司不对,重新续保
                    String body = (String) renewalInfo.get("body");
                    RenewalBean dataBean = (RenewalBean) renewalInfo.get("data");
                    CarInfo carInfo = new CarInfo();
                    carInfo.setCreatedBy(createdBy);
                    if (StringUtils.isBlank(carNo)) {
                        carInfo.setCarNumber(vinNo);
                    }
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
    public Map<String, Object> getRenewalInfo(Long source, String carNo, String vinNo, String createdBy) {
        Map<String, Object> result = new HashMap<>();
        JSONObject jsonStr = new JSONObject();
        if (null == carNo) carNo = "";
        if (null == vinNo) vinNo = "";
        jsonStr.put("carNo", carNo);
        jsonStr.put("frameNo", vinNo);
        HttpResult httpResult = getDifferentSourceRenewalInfo(source, jsonStr.toJSONString());
        return getResult(httpResult);
    }

    //续保结果
    public Map<String, Object> getResult(HttpResult httpResult) {
        Map<String, Object> result = new HashMap<>();
        if (httpResult != null) {
            int code = httpResult.getCode();
            Long source = httpResult.getSource();
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
                    retMsg = EncodeUtil.unicodeToString(retMsg);
                    //注意此处为暂时设置,实现登录后可从session 获取
                    // checkInfo.setCheckInfoId(uuid);
                    String uuid = UUIDS.getDateUUID();
                    InsuredInfo insuredInfo = new InsuredInfo(uuid);
                    insuredInfo.setCarInfoId(uuid);
                    CarInfo carInfo = new CarInfo(uuid);
                    carInfo.setCreatedBy(uuid);
                    String state = bean.getState();
                    RenewalData data = bean.getData();
                    String sendTime = bean.getSendTime();
                    List<InsuranceTypeInfo> insuranceTypeInfoList = null;
                    if ("1".equals(state)) {//获取成功
                        if (data != null) {
                            insuranceTypeInfoList = InsuranceTypeBase.getInsuranceTypeInfoList(data, uuid, uuid, "0");
                            String engineNoNew = data.getEngineNo();//发送机号
                            String registerDate = data.getFirstRegisterDate();//车辆注册日期
                            String bizStartDate = data.getBiBeginDate();//商业险下次起保日期
                            String bizPreminm = data.getBiPremium();//商业险保额
                            String forceStartDate = data.getCiBeginDate();//交强险下次起保日期
                            String forcePreminm = data.getCiPremium();//交强险保额
                            String idCardNew = data.getCardID();//车主证件
                            String vinNONew = data.getFrameNo();//车架号
                            String tel = data.getMobile();//手机号
                            String licenseOwner = data.getName();//车主
                            String model = data.getVehicleFgwCode();//车辆型号
                            carInfo.setFrameNumber(vinNONew);
                            carInfo.setCarModel(model);
                            carInfo.setEngineNumber(engineNoNew);
                            carInfo.setLicenseOwner(licenseOwner);
                            carInfo.setLicenseOwnerIdCard(idCardNew);
                            carInfo.setRegisterDate(registerDate);
                            carInfo.setMobile(tel);
                            if (StringUtils.isNotBlank(idCardNew)) {
                                if (idCardNew.indexOf("*") > -1) {//说明获取的值中加密
                                    carInfo.setLicenseOwnerIdCardType("-1");
                                } else {
                                    boolean b = IdCardUtil.validateCard(idCardNew);
                                    if (b) {
                                        carInfo.setLicenseOwnerIdCardType("1");//身份证
                                    } else {
                                        carInfo.setLicenseOwnerIdCardType("2"); //组织机构
                                    }
                                }
                            }
                            //车辆信息设置结束
                            //投保信息设置开始
                            insuredInfo.setNextBusinesStartDate(bizStartDate);
                            insuredInfo.setNextForceStartDate(forceStartDate);
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
                            }
                        } else {
                            result.put("msg", "未获取续保信息");
                        }
                        return result;
                    } else {//续保失败
                        result.put("status", "400");
                        result.put("msg", "续保失败");
                        return result;
                    }
                }

            } else {
                result.put("status", "400");
                result.put("msg", "续保请求异常");
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
     * @param param     第三方接口的请求参数
     * @param carNo     车牌号
     * @param createdBy 创建人id
     * @return
     */
    public Map<String, Object> getDifferentSourceRenewalInfo(Long source, Map<String, Object> param, String carNo, String vinNo, String createdBy) {
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
        String lastYearInsuranceCompany = "";
        if (1 == source) {//太保
            host = ThirdAPI.CPIC_HOST;
            api = ThirdAPI.CPIC_RENEWAL_NAME;
            port = ThirdAPI.CPIC_PORT;
        } else if (2 == source) {//平安
            host = ThirdAPI.PAIC_HOST;
            port = ThirdAPI.PAIC_PORT;
            api = ThirdAPI.PAIC_RENEWAL_NAME;
        } else if (4 == source) {//人保
            host = ThirdAPI.PICC_HOST;
            port = ThirdAPI.PICC_PORT;
            api = ThirdAPI.PICC_RENEWAL_NAME;
        } else {
            result.put("status", "400");
            result.put("msg", "待拓展");
            result.put("data", null);
            return result;
        }
        JSONObject jsonStr = new JSONObject();
        if (null == carNo) carNo = "";
        if (null == vinNo) vinNo = "";
        jsonStr.put("carNo", carNo);
        jsonStr.put("frameNo", vinNo);
        String URL = host + ":" + port + "/" + api;
        HttpResult httpResult = HttpClientUtil.doPost(URL, param, "JSON", RenewalBean.class, null);
        return getResult(httpResult);
    }

    /**
     * @param source  为空则多家续保
     * @param jsonStr 必填json参数
     * @return
     * @description 启用线程续保
     */
    public HttpResult getDifferentSourceRenewalInfo(Long source, String jsonStr) {
        HttpResult result = null;
        Long start1 = System.currentTimeMillis();
        try {
            String cpicurl = ThirdAPI.CPIC_HOST + ":" + ThirdAPI.CPIC_PORT + "/" + ThirdAPI.CPIC_RENEWAL_NAME;
            String piccurl = ThirdAPI.PICC_HOST + ":" + ThirdAPI.PICC_PORT + "/" + ThirdAPI.PAIC_RENEWAL_NAME;
            String paiccurl = ThirdAPI.PAIC_HOST + ":" + ThirdAPI.PAIC_PORT + "/" + ThirdAPI.PAIC_RENEWAL_NAME;
            String type = "JSON";
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


            CompletableFuture<HttpResult> f1 = CompletableFuture.supplyAsync(() -> {
                Long start = System.currentTimeMillis();
                HttpResult httpResult = HttpClientUtil.doPost(cpicurl, null, "JSON", RenewalBean.class, jsonStr);
                httpResult.setSource(1L);
                Long end = System.currentTimeMillis();
                logger.info("太保续保时间：" + (start - end));
                return httpResult;
            });
            CompletableFuture<HttpResult> f2 = CompletableFuture.supplyAsync(() -> {
                Long start = System.currentTimeMillis();
                HttpResult httpResult = HttpClientUtil.doPost(piccurl, null, "JSON", RenewalBean.class, jsonStr);
                httpResult.setSource(4L);
                Long end = System.currentTimeMillis();
                logger.info("人保续保时间：" + (start - end));
                return httpResult;
            });
            CompletableFuture<HttpResult> f3 = CompletableFuture.supplyAsync(() -> {
                Long start = System.currentTimeMillis();
                HttpResult httpResult = HttpClientUtil.doPost(paiccurl, null, "JSON", RenewalBean.class, jsonStr);
                httpResult.setSource(2L);
                Long end = System.currentTimeMillis();
                logger.info("平安续保时间：" + (start - end));
                return httpResult;
            });
            List<CompletableFuture<HttpResult>> list = new ArrayList<CompletableFuture<HttpResult>>();
            list.add(f1);
            list.add(f2);
            list.add(f3);
            List<HttpResult> lists = null;
            boolean flag = false;
            try {
                lists = CompletableFutureDemo.sequence(list).get();
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
                                }
                            }
                        }
                    }
                }
                if (!flag) {
                    result = lists.get(0);
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


}
