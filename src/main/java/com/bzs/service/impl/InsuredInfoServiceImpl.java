package com.bzs.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bzs.dao.CarInfoMapper;
import com.bzs.dao.CustomerMapper;
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
    @Resource
    private CustomerService customerService;
    @Override
    public Result checkByCarNoOrVinNo(String checkType, String carNo, String idCard, String vinNo, String engineNo, Long lastYearSource, String insuredArea, String createdBy) {
        if (StringUtils.isBlank(checkType)) {
            return ResultGenerator.genFailResult("参数异常");
        } else {
            String url = ThirdAPI.HOST;
            if (StringUtils.isBlank(idCard)) {
                idCard = "";
            }//身份证号为空时为"";

            String account = ThirdAPI.PAIC_ACCOUNT;
            String pwd = ThirdAPI.PAIC_PWD;

            String uuid = UUIDS.getDateUUID();
            CheckInfo checkInfo = new CheckInfo(uuid);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("account", account);
            jsonObject.put("password", pwd);
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
            if (lastYearSource == null) {
                renewalInfo = getRenewalInfo(lastYearSource, jsonObject.toJSONString(), createdBy);
            } else {
                renewalInfo = getDifferentSourceRenewalInfo(lastYearSource, jsonObject.toJSONString(), createdBy);
            }
            String status = (String) renewalInfo.get("status");
            String msg = (String) renewalInfo.get("msg");
            Long source = (Long) renewalInfo.get("source");
            System.out.println("11111111111111");
            System.out.println(renewalInfo);
            if (StringUtils.isNotBlank(status)) {
                if ("1".equals(status)) {
                    InsuredInfo insuredInfo = (InsuredInfo) renewalInfo.get("insuredInfo");
                    insuredInfo.setInsuredId(uuid);//设置续保id
                    insuredInfo.setCreateId(createdBy);
                    insuredInfo.setCarInfoId(uuid);
                    List<InsuranceTypeInfo> insuranceTypeInfoList = (List<InsuranceTypeInfo>) renewalInfo.get("insuranceTypeInfoList");
                    String body = (String) renewalInfo.get("body");
                    RenewalBean dataBean = (RenewalBean) renewalInfo.get("data");
                    System.out.println(dataBean);
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
                        insuredInfoMapper.insert(insuredInfo);
                        for (InsuranceTypeInfo datas : insuranceTypeInfoList) {
                            datas.setTypeId(uuid);
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
                    maps.put("source", lastYearSource+"");
                    maps.put("carInfoId", uuid);
                   /* CarInfo carInfo1=carInfoService.findBy("carInfoId",uuid);
                    customerService.findBy("customerId",carInfo.getCustomerId());*/
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
    public Map<String, Object> getRenewalInfo(Long source, String jsonStr, String createdBy) {
        HttpResult httpResult = getDifferentSourceRenewalInfo(source, jsonStr);
        String uuid = UUIDS.getDateUUID();
        return getResult(httpResult, uuid, createdBy);
    }

    //续保结果
    public Map<String, Object> getResult(HttpResult httpResult, String quoteId, String createdBy) {
        Map<String, Object> result = new HashMap<>();
        if (httpResult != null) {
            int code = httpResult.getCode();
            Long source = httpResult.getSource();
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
                    retMsg = EncodeUtil.unicodeToString(retMsg);
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
                               /* if (idCardNew.indexOf("*") > -1) {//说明获取的值中加密
                                    IdCardUtil.validateCardBy14(idCardNew);
                                    carInfo.setLicenseOwnerIdCardType("-1");
                                } else {
                                    boolean b = IdCardUtil.validateCard(idCardNew);
                                    if (b) {
                                        carInfo.setLicenseOwnerIdCardType("1");//身份证
                                    } else {
                                        carInfo.setLicenseOwnerIdCardType("2"); //组织机构
                                    }
                                }*/
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
                    } /*else if ("0".equals(state)) {//查询返回成功但未获取到续保信息
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
                    }*/ else {//续保失败
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
     * @param createdBy 创建人id
     * @return
     */
    public Map<String, Object> getDifferentSourceRenewalInfo(Long source, String jsonStr, String createdBy) {
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
            logger.info("续保枚举值1：太保");
        } else if (2 == source) {//平安
            host = ThirdAPI.PAIC_HOST;
            port = ThirdAPI.PAIC_PORT;
            api = ThirdAPI.PAIC_RENEWAL_NAME;
            logger.info("续保枚举值2：平安");
        } else if (4 == source) {//人保
            host = ThirdAPI.PICC_HOST;
            port = ThirdAPI.PICC_PORT;
            api = ThirdAPI.PICC_RENEWAL_NAME;
            logger.info("续保枚举值4：人保");
        } else {
            result.put("status", "400");
            result.put("msg", "待拓展");
            result.put("data", null);
            return result;
        }

        String URL = host + ":" + port + "/" + api;
        HttpResult httpResult = HttpClientUtil.doPost(URL, null, "JSON", RenewalBean.class, jsonStr);
        String uuid = UUIDS.getDateUUID();
        return getResult(httpResult, uuid, createdBy);
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
            String piccurl = ThirdAPI.PICC_HOST + ":" + ThirdAPI.PICC_PORT + "/" + ThirdAPI.PICC_RENEWAL_NAME;
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
                logger.info("太保续保开始：");
                Long start = System.currentTimeMillis();
                HttpResult httpResult = HttpClientUtil.doPost(cpicurl, null, "JSON", RenewalBean.class, jsonStr);
                httpResult.setSource(1L);
                Long end = System.currentTimeMillis();
                logger.info("太保续保结束，总时间：" + (end - start));
                return httpResult;
            });
            CompletableFuture<HttpResult> f2 = CompletableFuture.supplyAsync(() -> {
                logger.info("人保续保开始：");
                Long start = System.currentTimeMillis();
                HttpResult httpResult = HttpClientUtil.doPost(piccurl, null, "JSON", RenewalBean.class, jsonStr);
                httpResult.setSource(4L);
                Long end = System.currentTimeMillis();
                logger.info("人保续保结束，总时间：" + (end - start));
                return httpResult;
            });
//            CompletableFuture<HttpResult> f3 = CompletableFuture.supplyAsync(() -> {
//                logger.info("平安续保开始：");
//                Long start = System.currentTimeMillis();
//                HttpResult httpResult = HttpClientUtil.doPost(paiccurl, null, "JSON", RenewalBean.class, jsonStr);
//                httpResult.setSource(2L);
//                Long end = System.currentTimeMillis();
//                logger.info("平安续保结束，总时间：" + (end - start));
//                return httpResult;
//            });
            List<CompletableFuture<HttpResult>> list = new ArrayList<CompletableFuture<HttpResult>>();
            list.add(f1);
            list.add(f2);
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
                                    } else if ("0".equals(state)) {
                                        JSONObject jsonObject = JSONObject.parseObject(body);
                                        if (jsonObject.containsKey("retMsg")) {
                                            String retMsg = jsonObject.getString("retMsg");
                                            retMsg = EncodeUtil.unicodeToString(retMsg);
                                            message += InsuranceNameEnum.getName(httpResult.getSource()) + "续保：" + retMsg + ";";
                                        }
                                    } else {
                                        message += InsuranceNameEnum.getName(httpResult.getSource()) + "续保：" + "状态值为" + state + ";";
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
                    JSONObject jsonObject = new JSONObject();
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


}
