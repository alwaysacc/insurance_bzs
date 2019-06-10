package com.bzs.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bzs.dao.CarInfoMapper;
import com.bzs.dao.CustomerMapper;
import com.bzs.dao.InsuredInfoMapper;
import com.bzs.model.*;
import com.bzs.model.query.CarInfoAndInsuranceInfo;
import com.bzs.redis.RedisUtil;
import com.bzs.service.*;
import com.bzs.utils.*;

import com.bzs.utils.BiHu.ResultData;
import com.bzs.utils.BiHu.UserInfo;
import com.bzs.utils.commons.ThirdAPI;
import com.bzs.utils.dateUtil.DateUtil;
import com.bzs.utils.encodeUtil.EncodeUtil;
import com.bzs.utils.enumUtil.InsuranceItems2;
import com.bzs.utils.enumUtil.InsuranceNameEnum;
import com.bzs.utils.httpUtil.HttpClientUtil;
import com.bzs.utils.httpUtil.HttpResult;
import com.bzs.utils.jsonToMap.JsonToMapUtil;
import com.bzs.utils.jsontobean.InsuranceTypeBase;
import com.bzs.utils.jsontobean.P;
import com.bzs.utils.jsontobean.RenewalBean;
import com.bzs.utils.jsontobean.RenewalData;
import com.bzs.utils.queueUtil.LinkQueue;
import com.bzs.utils.threadUtil.CompletableFutureDemo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by denglei on 2019/04/10 17:09:11.
 */
@Service
@Transactional
public class InsuredInfoServiceImpl extends AbstractService<InsuredInfo> implements InsuredInfoService {
    private static final Logger logger = LoggerFactory.getLogger(InsuredInfoServiceImpl.class);
    @Resource
    private RedisUtil redisUtil;
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
    public Result checkByCarNoOrVinNo2(String checkType, String carNo, String idCard, String vinNo, String engineNo, Long lastYearSource, String insuredArea, String createdBy, String carInfoId) {
        if (StringUtils.isBlank(checkType)) return ResultGenerator.genFailResult("参数异常");

        if (StringUtils.isBlank(idCard)) {
            //身份证号为空时为"";
            idCard = "";
        }
        String uuid = UUIDS.getDateUUID();
        CheckInfo checkInfoGloab = new CheckInfo();
        JSONObject jsonObject = new JSONObject();
        if ("0".equals(checkType)) {//车牌续保
            if (StringUtils.isNotBlank(carNo)) {
                jsonObject.put("carNo", carNo);
                jsonObject.put("frameNo", "");
                jsonObject.put("cardID", idCard);
                checkInfoGloab.setCarNo(carNo);
            } else {
                return ResultGenerator.genFailResult("参数异常,车牌号不能为空");
            }
        } else if ("1".equals(checkType)) {//车架续保
            if (StringUtils.isNotBlank(vinNo)) {
                jsonObject.put("carNo", "");
                jsonObject.put("frameNo", vinNo);
                jsonObject.put("cardID", "");
                checkInfoGloab.setVinNo(vinNo);
            } else {
                return ResultGenerator.genParamsErrorResult("参数异常,车架号不能为空");
            }
        } else {//待拓展
            return ResultGenerator.genParamsErrorResult("参数异常,目前仅支持车牌或者车架号续保");
        }


        //重新续保
        String date = DateUtil.getDateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
        boolean carInfoFlag = false;//标记车辆信息是否存在
        boolean checkInfoFlag = false;//标记查询信息是否存在
        //查询车辆信息
        Map<String, Object> result = carInfoService.getCarInfoAndInsurance(null, null, carNo, vinNo, null, null);
        String code = (String) result.get("code");

        CarInfoAndInsuranceInfo carInfoAndInsuranceInfoGloab = null;
        if ("200".equals(code)) {//本地已经存在此数据
            carInfoFlag = true;
            //获取查询车辆信息
            List<CarInfoAndInsuranceInfo> list = (List<CarInfoAndInsuranceInfo>) result.get("data");
            carInfoAndInsuranceInfoGloab = list.get(0);//多条取一条
            carInfoId = carInfoAndInsuranceInfoGloab.getCarInfoId();//获取车辆信息id
            //返回续保信息
         /*   String body = this.getJsonString(carInfoAndInsuranceInfoGloab);
            Map maps = JsonToMapUtil.bodyJsonToMap(body);*/
        }
        //查询查询信息
        Map checkInfoMap = null;
        if (StringUtils.isNotBlank(carInfoId)) {
            checkInfoMap = checkInfoService.checkByCreateByOrCarInfoId(createdBy, carInfoId, null, null);

        } else {
            checkInfoMap = checkInfoService.checkByCreateByOrCarInfoId(createdBy, carInfoId, carNo, vinNo);
            carInfoId = uuid;//车辆id设置为新的
        }
        String checkInfoCode = (String) checkInfoMap.get("code");
        checkInfoGloab.setCheckType(checkType);
        if ("200".equals(checkInfoCode)) {//已经存在查询信息
            checkInfoFlag = true;
            checkInfoGloab = (CheckInfo) checkInfoMap.get("data");//获取查询信息
            //修改查询信息
            checkInfoGloab.setIsFirstTime("2");
            //date = DateUtil.getDateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
            checkInfoGloab.setSendTime(date);
            if (StringUtils.isNotBlank(carNo)) {
                checkInfoGloab.setCarNo(carNo);
            }
            if (StringUtils.isNotBlank(vinNo)) {
                checkInfoGloab.setVinNo(vinNo);
            }
            checkInfoGloab.setCarInfoId(carInfoId);
            //返回查询信息
        } else {//未获取查询信息
            checkInfoFlag = false;
            checkInfoGloab.setCheckInfoId(uuid);
            checkInfoGloab.setCreateBy(createdBy);
            // checkInfoGloab.setCheckInfoId(UUIDS.getDateUUID());
            // checkInfoGloab.setCarInfoId(carInfoId);
            checkInfoGloab.setSendTime(date);
        }
        checkInfoGloab.setCarInfoId(carInfoId);//r若车辆信息不存在，则此时车辆信息id为新的
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
                // List<CarInfoAndInsuranceInfo> list = carInfoMapper.getCarInfoAndInsurance(carInfo);
                carInfo.setCreatedBy(createdBy);
                carInfo.setChannelType(checkType + "");
                carInfo.setCarInfoId(carInfoId);//设置车辆id
                carInfo.setBrandModel(dataBean.getData().getCarName());
                if (StringUtils.isNotBlank(idCard)) {
                    String cardID = carInfo.getLicenseOwnerIdCard();
                    if (StringUtils.isNotBlank(cardID)) {
                        int index = cardID.indexOf("*");
                        if (index > -1) {
                            carInfo.setLicenseOwnerIdCard(idCard);
                        }
                    }
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
                if (carInfoFlag) {//车辆信息存在，并且续保成功
                    carInfo.setUpdatedBy(createdBy);
                    // carInfoService.insertOrUpdate(carInfo);
                    InsuredInfo ins = carInfoAndInsuranceInfoGloab.getInsuredInfo();
                    String insId = "";//续保id
                    if (null != ins) {//续保信息存在
                        insId = ins.getInsuredId();
                        insuredInfo.setUpdateBy(createdBy);
                        insuranceTypeInfoService.deleteByTypeId(insId);//删除险种根据续保id
                    } else {//续保信息不存在
                        insId = uuid;
                        insuredInfo.setCreateId(createdBy);
                    }
                    insuredInfo.setInsuredId(insId);

                    //险种直接删除后添加
                    List list = carInfoAndInsuranceInfoGloab.getInsuranceTypeInfos();
                    for (InsuranceTypeInfo datas : insuranceTypeInfoList) {
                        if (null != datas) {
                            datas.setTypeId(insId);
                            insuranceTypeInfoService.save(datas);//续保险种
                        }
                    }
                } else {
                    //车辆不存在，添加新的车辆信息id
                    insuredInfo.setInsuredId(uuid);
                    //insuredInfoService.save(insuredInfo);//续保信息
                    for (InsuranceTypeInfo datas : insuranceTypeInfoList) {
                        if (null != datas) {
                            datas.setTypeId(uuid);
                            insuranceTypeInfoService.save(datas);//续保险种
                        }
                    }
                }
                carInfoService.insertOrUpdate(carInfo);//添加或更新车辆信息
                insuredInfo.setCarInfoId(carInfoId);
                insuredInfoService.insertOrUpdate(insuredInfo);//添加或更新续保信息
                checkInfoGloab.setIsRenewSuccess("1");//是否续保成功
                checkInfoGloab.setCarInfoId(carInfoId);
                checkInfoService.updateOrAdd(checkInfoGloab);//修改//查询信息
                logger.info("续保成功，排查bodyJsonToMap开始");
                Map maps = JsonToMapUtil.bodyJsonToMap(body);
                logger.info("续保成功，排查bodyJsonToMap结束");
                String newCarNo = carInfo.getCarNumber();
                if (StringUtils.isNotBlank(newCarNo)) {
                    carNo = newCarNo;
                }
                maps.put("carNo", carNo);
                maps.put("source", lastYearSource + "");
                maps.put("carInfoId", carInfoId);
                return ResultGenerator.gen(msg, maps, ResultCode.SUCCESS);
            } else if ("0099".equals(status)) {//续保失败
                String body = (String) renewalInfo.get("body");
                RenewalBean dataBean = (RenewalBean) renewalInfo.get("data");
                CarInfo carInfo = new CarInfo();
                carInfo.setCarNumber(carNo);
                carInfo.setFrameNumber(vinNo);
                carInfo.setChannelType(checkType + "");
                if (StringUtils.isBlank(carNo)) {//车牌号为空则存储车架号
                    carInfo.setCarNumber(vinNo);
                }
               /* if (carInfoFlag) {//车辆信息存在
                    carInfo.setCarInfoId(carInfoId);
                } else {
                    carInfo.setCarInfoId(uuid);
                    carInfoId = uuid;
                }*/
                carInfo.setCarInfoId(carInfoId);
                if (checkInfoFlag) {//查询信息存在
                    checkInfoGloab.setIsFirstTime("1");//非第一次
                }/* else {
                    checkInfoGloab.setCarInfoId(carInfoId);
                }*/
                checkInfoService.updateOrAdd(checkInfoGloab);//修改//查询信息
                carInfoService.insertOrUpdate(carInfo);
                String bodys = this.getJsonString(carInfoAndInsuranceInfoGloab);
                if (StringUtils.isNotBlank(bodys)) {
                    Map maps = JsonToMapUtil.bodyJsonToMap(bodys);
                    String state = (String) maps.get("state");
                    if ("1".equals(state)) {//本地成功
                        return ResultGenerator.gen("本次续保失败，获取本地信息成功", maps, ResultCode.SUCCESS);
                    } else {//本地失败
                        return ResultGenerator.gen(msg, maps, ResultCode.FAIL);
                    }
                } else {
                    Map maps = JsonToMapUtil.bodyJsonToMap(body);
                    return ResultGenerator.gen(msg, maps, ResultCode.FAIL);
                }

            } else {
                if (checkInfoFlag) {//查询信息存在
                    checkInfoGloab.setIsFirstTime("1");//非第一次
                } else {
                    checkInfoGloab.setIsCheckSuccess("0");
                }
                checkInfoService.updateOrAdd(checkInfoGloab);//修改//查询信息
                String bodys = this.getJsonString(carInfoAndInsuranceInfoGloab);
                if (StringUtils.isNotBlank(bodys)) {
                    Map maps = JsonToMapUtil.bodyJsonToMap(bodys);
                    String state = (String) maps.get("state");
                    if ("1".equals(state)) {//本地成功
                        return ResultGenerator.gen("本次续保失败，获取本地信息成功", maps, ResultCode.SUCCESS);
                    } else {//本地失败
                        return ResultGenerator.gen(msg, maps, ResultCode.FAIL);
                    }
                } else {
                    return ResultGenerator.genFailResult(msg);
                }
            }
        }
        return ResultGenerator.genFailResult("获取失败");
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
                            if (StringUtils.isNotBlank(registerDate)) {
                                DateUtil.getStringToString(registerDate, "yyyy-MM-dd");
                            }
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
        List<String> portList = new ArrayList<>();
        String keyRedis = "";
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
                    String checkType = jsonObject.getString("checkType");
                    if ("0".equals(checkType)) {//身份证是不否符合规范
                        String idcard = jsonObject.getString("cardID");
                        if (StringUtils.isNotBlank(idcard)) {
                            boolean isIdCard = IdCardUtil.validateCard(idcard);
                            if (!isIdCard) {
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
            keyRedis = "CPIC_PORT" + createdBy;
/*
            System.out.println(keyRedis);
            // host = ThirdAPI.CPIC_HOST;
            // port = ThirdAPI.CPIC_PORT;

            Integer[] ports ={5000,5001,5002,5003,5004};
            int p=5000;
            String s = (String) redisUtil.get("CPIC_" + createdBy);
            List <Integer> portList=JSONArray.parseArray(s,Integer.class);
            if(CollectionUtils.isNotEmpty(portList)){
                for(int i=0;i<ports.length;i++){
                    for (Integer pp:portList){
                        if(ports[i]!=pp){
                            continue;
                        }
                    }
                }
            }

            if (StringUtils.isNotBlank(s)) {
                List<Map<String, Object>> listMap = JsonToMapUtil.toListMap(s);
                if (CollectionUtils.isNotEmpty(listMap)) {
                    for (Map<String, Object> maps : listMap) {
                        for (Map.Entry<String, Object> entry : maps.entrySet()) {

                            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
                        }
                    }
                }
            } else {

            }
*/
            api = ThirdAPI.CPIC_RENEWAL_NAME;
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
            logger.info("续保枚举值1：太保");
        } else if (2 == source) {//平安
            //host = ThirdAPI.PAIC_HOST;
            // port = ThirdAPI.PAIC_PORT;
            api = ThirdAPI.PAIC_RENEWAL_NAME;
            logger.info("续保枚举值2：平安");
        } else if (4 == source) {//人保
            String ports = "4050,4051,4052,4053,4054";
            // host = ThirdAPI.PICC_HOST;
            // port = ThirdAPI.PICC_PORT;
            keyRedis = "PICC_PORT" + createdBy;
            api = ThirdAPI.PICC_RENEWAL_NAME;
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
            logger.info("续保枚举值4：人保");
        } else {
            result.put("status", "400");
            result.put("msg", "待拓展");
            result.put("data", null);
            return result;
        }
        String URL = host + ":" + port + "/" + api;
        HttpResult httpResult = HttpClientUtil.doPost(URL, null, "JSON", RenewalBean.class, jsonObject.toJSONString());
        portList.remove(port);
        redisUtil.set(keyRedis, portList, 720000);
        System.out.println(portList);
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
                    //String port = data.getPort();
                    String port = "";
                    String accountName = data.getAccountName();
                    String accountPwd = data.getAccountPwd();
                    CompletableFuture<HttpResult> f = CompletableFuture.supplyAsync(() -> {
                        logger.info(name + "开始：");
                        Long start = System.currentTimeMillis();
                        String api = "";
                        boolean flag = true;
                        String mm = "";
                        if (1L == sour) {
                            api = ThirdAPI.CPIC_RENEWAL_NAME;
                            List portList = (List) redisUtil.get("CPIC_RENEWAL_NAME");
                            if (portList.size() == 0) {

                            }

                        } else if (2L == sour) {
                            api = ThirdAPI.PAIC_RENEWAL_NAME;
                            if (StringUtils.isNotBlank(accountName) && StringUtils.isNotBlank(accountPwd)) {
                                jsonStr.put("account", accountName);
                                jsonStr.put("password", accountPwd);
                            } else {
                                flag = false;
                                mm = "用于续保的平安账号信息不完整,无法续保;";
                            }
                            String checkType = jsonStr.getString("checkType");
                            if ("0".equals(checkType)) {
                                String idcard = jsonStr.getString("cardID");
                                logger.info("获取身份证号:" + idcard);
                                if (!IdCardUtil.validateCard(idcard)) {//身份证是不否符合规范
                                    flag = false;
                                    mm = "平安续保,未填写身份证号,无法续保;";
                                }
                            }


                        } else if (4L == sour) {
                            api = ThirdAPI.PICC_RENEWAL_NAME;
                        } else {
                            mm = "待拓展公司";
                        }
                        HttpResult httpResult = null;
                        if (StringUtils.isNotBlank(host) && StringUtils.isNotBlank(port) && StringUtils.isNotBlank(api) && flag) {
                            String url = host + ":" + port + "/" + api;

                            //1和4
                          /*  LinkQueue<Map<String,Object>>  queueList=  (LinkQueue<Map<String,Object>>)  redisUtil.get("queue");
                            if(queueList!=null&&queueList.size()>0){
                                Map maps=new HashedMap();
                                maps.put("R1","IP1");
                                queueList.enQueue(maps);
                            }else{
                                queueList= new LinkQueue<Map<String,Object>>();
                            }*/
                        /*  Object obj= redisUtil.get("queue");
                          if(obj!=null){
                              List<Map<String,Object>> queueMap=(List<Map<String,Object>>) redisUtil.get("queue");
                              Map queueMaps=new HashedMap();
                              queueMaps.put("R2",url);
                              queueMap.add(queueMaps);
                              redisUtil.set("queue",queueMap);
                          }else{
                              Map<String,Object> queueMap=new HashedMap();
                              queueMap.put("R1",url);
                              List<Map<String,Object>> lists=new ArrayList();
                              lists.add(queueMap);
                              redisUtil.set("queue",lists);
                          }*/
                            //LinkQueue<Map<String,Object>> queue = new LinkQueue<Map<String,Object>>();
                            //redisUtil.set("queue", userName);
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
                    if (StringUtils.isNotBlank(idCard)) {
                        String cardID = carInfo.getLicenseOwnerIdCard();
                        if (StringUtils.isNotBlank(cardID)) {
                            int index = cardID.indexOf("*");
                            if (index > -1) {
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

                    Map maps = JsonToMapUtil.bodyJsonToMap(body);
                    String newCarNo = carInfo.getCarNumber();
                    if (StringUtils.isNotBlank(newCarNo)) {
                        carNo = newCarNo;
                    }
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
                    } else {
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

    @Override
    public Result checkByCarNoOrVinNo3(String checkType, String carNo, String idCard, String vinNo, String engineNo, Long lastYearSource, String insuredArea, String createdBy) {
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
            //查询车辆信息
            Map<String, Object> result = carInfoService.getCarInfoAndInsurance(null, null, carNo, vinNo, null, null);
            String code = (String) result.get("code");
            if ("200".equals(code)) {//本地已经存在此数据
                //获取查询车辆信息
                List<CarInfoAndInsuranceInfo> list = (List<CarInfoAndInsuranceInfo>) result.get("data");
                CarInfoAndInsuranceInfo data = list.get(0);//多条取一条
                String carInfoId = data.getCarInfoId();//获取车辆信息id
                //查询查询信息
                Map checkInfoMap = checkInfoService.checkByCreateByOrCarInfoId(createdBy, carInfoId, null, null);
                String checkInfoCode = (String) checkInfoMap.get("code");
                if ("200".equals(checkInfoCode)) {//已经存在查询信息
                    CheckInfo checkInfo1 = (CheckInfo) checkInfoMap.get("data");//获取查询信息
                    //修改查询信息
                    checkInfo1.setCheckType(checkType);
                    checkInfo1.setIsFirstTime("2");
                    String date = DateUtil.getDateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
                    checkInfo1.setSendTime(date);
                    if (StringUtils.isNotBlank(carNo)) {
                        checkInfo1.setCarNo(carNo);
                    }
                    if (StringUtils.isNotBlank(vinNo)) {
                        checkInfo1.setVinNo(vinNo);
                    }
                    checkInfoService.updateOrAdd(checkInfo1);//修改
                    //返回查询信息
                } else {//未获取查询信息
                    CheckInfo checkInfo1 = new CheckInfo();
                    checkInfo1.setVinNo(vinNo);
                    checkInfo1.setCarNo(carNo);
                    checkInfo1.setCheckType(checkType);
                    checkInfo1.setCreateBy(createdBy);
                    checkInfo1.setCheckInfoId(UUIDS.getDateUUID());
                    checkInfo1.setCarInfoId(carInfoId);
                    String date = DateUtil.getDateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
                    checkInfo1.setSendTime(date);
                    checkInfoService.save(checkInfo);//添加查询信息
                }

                //返回续保信息
                //data
                String body = this.getJsonString(data);
                Map maps = JsonToMapUtil.bodyJsonToMap(body);
                if (null == maps) {
                    maps = new HashedMap();
                    maps.put("carNo", carNo);
                    maps.put("source", lastYearSource + "");
                    maps.put("carInfoId", uuid);
                    return ResultGenerator.gen("复制本地数据失败", maps, ResultCode.FAIL);
                } else {
                    String state = (String) maps.get("state");
                    String retMsg = (String) maps.get("retMsg");
                    if ("1".equals(state)) {
                        return ResultGenerator.gen(retMsg, maps, ResultCode.SUCCESS);
                    } else {
                        return ResultGenerator.gen(retMsg, maps, ResultCode.FAIL);
                    }
                }
            } else {//本地不存在此数据
                checkInfo.setCreateBy(createdBy);
                String date = DateUtil.getDateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
                checkInfo.setSendTime(date);
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
                        if (StringUtils.isNotBlank(idCard)) {
                            String cardID = carInfo.getLicenseOwnerIdCard();
                            if (StringUtils.isNotBlank(cardID)) {
                                int index = cardID.indexOf("*");
                                if (index > -1) {
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

                        Map maps = JsonToMapUtil.bodyJsonToMap(body);
                        String newCarNo = carInfo.getCarNumber();
                        if (StringUtils.isNotBlank(newCarNo)) {
                            carNo = newCarNo;
                        }
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
                        } else {
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

            }
        }
        return ResultGenerator.genFailResult("续保失败");
    }


    public String getJsonString(CarInfoAndInsuranceInfo data) {
        String body = null;
        String state = "0099";
        if (null != data) {
            JSONObject j = new JSONObject();//最外层
            JSONObject j2 = new JSONObject();//data层
            String carInfoId = data.getCarInfoId();//车辆信息id
            String idCard = data.getLicenseOwnerIdCard();//身份证
            String idCardType = data.getLicenseOwnerIdCardType();//身份证类型
            String licenseOwner = data.getLicenseOwner();//车主
            String brand = data.getBrandModel();//品牌
            String model = data.getCarModel();//型号
            String engineNo = data.getEngineNumber();//发动机号
            String vinNo = data.getFrameNumber();//车架号
            String carNo = data.getCarNumber();//车牌号
            String register = data.getRegisterDate();
            String mobile = data.getMobile();
            j2.put("name", licenseOwner);
            j2.put("vehicleFgwCode", model);
            j2.put("cardID", idCard);
            j2.put("engineNo", engineNo);
            j2.put("mobile", mobile);
            j2.put("firstRegisterDate", register);
            j2.put("frameNo", vinNo);

            j.put("carNo", carNo);
            j.put("carInfoId", carInfoId);


          /*  if (StringUtils.isNotBlank(carNo)) {//车牌

            }
            if (StringUtils.isBlank(vinNo)) {//车架

            }
            if (StringUtils.isBlank(engineNo)) {//发动机

            }*/
            InsuredInfo insuredInfo = data.getInsuredInfo();
            String msg = "复制本地车辆信息成功，获取续保险种信息失败";

            if (null != insuredInfo) {//续保成功
                String insuredId = insuredInfo.getInsuredId();
                if (StringUtils.isNotBlank(insuredId)) {
                    state = "1";
                    msg = "复制本地续保信息成功";
                    String forceEndDate = insuredInfo.getForceExpireDate();//交强险到期
                    String bizEndDate = insuredInfo.getBusinesExpireDate();//商业险到期
                    String insuranceCompany = insuredInfo.getLastYearInsuranceCompany();//上一年投保公司
                    String source = insuredInfo.getLastYearSource();//上一年保司枚举值
                    String forceStartDate = insuredInfo.getNextBusinesStartDate();//交强险起保日期
                    String bizStartDate = insuredInfo.getNextForceStartDate();//商业险起保日期
                    j2.put("ciEndDate", forceEndDate);
                    j2.put("biEndDate", bizEndDate);
                    j2.put("biBeginDate", bizStartDate);
                    j2.put("ciBeginDate", forceStartDate);
                    j2.put("ciBeginDate", forceStartDate);
                    j2.put("source", source);
                    // insuredInfo.getUpdateBy();
                    List<InsuranceTypeInfo> list = (List<InsuranceTypeInfo>) data.getInsuranceTypeInfos();
                    if (CollectionUtils.isNotEmpty(list)) {
                        for (int i = 0; i < list.size(); i++) {
                            InsuranceTypeInfo insuranceTypeInfo = list.get(i);
                            if (insuranceTypeInfo != null) {
                                String name = insuranceTypeInfo.getInsuranceName();
                                if ("交强险".equals(name)) {
                                    j2.put("jiaoqiangxian", "1");
                                } else {
                                    JSONObject j3 = new JSONObject();
                                    j3.put("insuranceName", insuranceTypeInfo.getInsuranceName());
                                    j3.put("amount", insuranceTypeInfo.getInsuranceAmount());
                                    j3.put("insuredPremium", insuranceTypeInfo.getInsurancePremium());
                                    j3.put("bujimianpei", insuranceTypeInfo.getExcludingEeductible());
                                    j2.put("A" + i, j3);
                                }
                            }

                        }

                    }

                }
                /*else {
                    //续保信息获取失败

                }*/

            } /*else {


            }*/
            j.put("state", state);
            j.put("retMsg", msg);
            String date = DateUtil.getDateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
            j.put("sendTime", date);
            j.put("data", j2);
            body = j.toJSONString();
            logger.info("自己组装的body" + body);
        }
        return body;
    }

    @Override
    public int insertOrUpdate(InsuredInfo insuredInfo) {
        return insuredInfoMapper.insertOrUpdate(insuredInfo);
    }

    @Override
    public Result WX_checkByCarNoOrVinNo(String checkType, String carNo, String idCard, String vinNo, String engineNo, Long lastYearSource, String insuredArea, String createBy) {
        //Group=1&LicenseNo=%E4%BA%ACFF1235&CityCode=1&Agent=102&CustKey=123456789654
        //String SecCode = MD5Utils.md5(param + "d7eb7d66997");
        String param = "";
        if (checkType.equals("0")) {
            if (!StringUtils.isNotBlank(idCard))
                idCard = "";
            param = ThirdAPI.BEFORE + "LicenseNo=" + carNo + "&SixDigitsAfterIdCard=" + idCard + ThirdAPI.AFTER;
        } else {
            if (!StringUtils.isNotBlank(idCard))
                idCard = "";
            param = ThirdAPI.BEFORE + "CarVin=" + vinNo + "&SixDigitsAfterIdCard=" + idCard + ThirdAPI.AFTER;
        }
        String SecCode = MD5Utils.md5(param + "d7eb7d66997");
        param = param + "&SecCode=" + SecCode;
        String url = ThirdAPI.BIHUXUBAO + param;
        HttpResult httpResult = null;
        try {
            httpResult = HttpClientUtil.doGet(url, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ResultData resultData = JSONObject.parseObject(httpResult.getBody(), ResultData.class);
        if (httpResult.getCode() == 200) {
            System.out.println(resultData);
            if (resultData.getBusinessStatus() == 1) {
                UserInfo userInfo = resultData.getUserInfo();
                JSONObject object = JSONObject.parseObject(httpResult.getBody());
                //险种
                JSONObject quote = JSONObject.parseObject(object.getString("SaveQuote"));
                //车辆信息
                CarInfo carInfo = new CarInfo();
                String carInfoId = UUIDS.getUUID();
                carInfo.setCarNumber(userInfo.getLicenseNo());
                carInfo.setBrandModel(userInfo.getModleName());
                carInfo.setFrameNumber(userInfo.getCarVin());
                carInfo.setCreatedBy(createBy);
                carInfo.setLicenseOwner(userInfo.getLicenseOwner());
                //CarUsedType":1,"LicenseNo":"苏ASY000","LicenseOwner":"袁文清","InsuredName":"袁文清","PostedName":"袁文清","IdType":1,
                // "CredentislasNum":"320113196711090444","CityCode":8,"EngineNo":"CJT300053","ModleName":"凯宴CAYENNE 3.0T",
                // "CarVin":"WP1AG2920HKA10588","RegisterDate":"2017-07-01","ForceExpireDate":"2019-06-30",
                // "BusinessExpireDate":"2019-06-30","NextForceStartDate":"2019-06-30","NextBusinessStartDate":"2019-07-01",
                // "PurchasePrice":868000,"SeatCount":5,"FuelType":0,"ProofType":0,"LicenseColor":0,"ClauseType":0,"RunRegion":0,
                // "InsuredIdCard":"320113196711090444","InsuredIdType":1,"InsuredMobile":"","HolderIdCard":"320113196711090444",
                // "HolderIdType":1,"HolderMobile":"","RateFactor1":0.85,"RateFactor2":0.75,"RateFactor3":0.85,"RateFactor4":1.0,"IsPublic":2}
                // ,"SaveQuote":{"Source":1,"CheSun":805504,"SanZhe":1000000,"DaoQiang":0,"SiJi":0,"ChengKe":0,"BoLi":0,"HuaHen":0,"SheShui":0,"ZiRan":0,"BuJiMianCheSun":1,"BuJiMianSanZhe":1,"BuJiMianDaoQiang":0,"BuJiMianChengKe":0,"BuJiMianSiJi":0,"BuJiMianHuaHen":0,"BuJiMianSheShui":0,"BuJiMianZiRan":0,"BuJiMianJingShenSunShi":0,"HcSanFangTeYue":0,"HcJingShenSunShi":0},"CustKey":"bzs20171117","BusinessStatus":1,"StatusMessage":"续保成功"}', message='null', t=null}
                carInfo.setLicenseOwnerIdCard(userInfo.getCredentislasNum());
                carInfo.setLicenseOwnerIdCardType(String.valueOf(userInfo.getIdType()));
                carInfo.setMobile(userInfo.getHolderMobile());
                carInfo.setPurchasePrice(userInfo.getPurchasePrice());
                carInfo.setEngineNumber(userInfo.getEngineNo());
                carInfo.setSeatNumber(userInfo.getSeatCount());
                carInfo.setRegisterDate(userInfo.getRegisterDate());
                carInfo.setIsRenewSuccess("1");
                carInfo.setCarInfoId(carInfoId);
                //续保信息
                InsuredInfo insuredInfo = new InsuredInfo();
                String insuredId = UUIDS.getUUID();
                insuredInfo.setCarInfoId(carInfoId);
                insuredInfo.setInsuredId(insuredId);
                insuredInfo.setCreateId(createBy);
                insuredInfo.setLastYearSource(String.valueOf(quote.getString("Source")));
                switch (String.valueOf(quote.getString("Source"))) {
                    case "1":
                        insuredInfo.setLastYearInsuranceCompany("太平洋保险");
                        break;
                    case "2":
                        insuredInfo.setLastYearInsuranceCompany("平安保险");
                        break;
                    case "4":
                        insuredInfo.setLastYearInsuranceCompany("人民保险");
                        break;
                }
                insuredInfo.setBusinesExpireDate(userInfo.getBusinessExpireDate());
                insuredInfo.setForceExpireDate(userInfo.getForceExpireDate());
                insuredInfo.setNextBusinesStartDate(userInfo.getNextBusinessStartDate());
                insuredInfo.setNextForceStartDate(userInfo.getNextForceStartDate());
                insuredInfo.setLicenseOwner(userInfo.getLicenseOwner());
                insuredInfo.setLicenseOwnerIdCard(userInfo.getCredentislasNum());
                insuredInfo.setLicenseOwnerIdCardType(String.valueOf(userInfo.getIdType()));
                insuredInfo.setInsuredName(userInfo.getInsuredName());
                insuredInfo.setInsuredIdCard(userInfo.getInsuredIdCard());
                insuredInfo.setInsuredIdCardType(String.valueOf(userInfo.getInsuredIdType()));
                insuredInfo.setPostedName(userInfo.getPostedName());
                insuredInfo.setHolderIdCard(userInfo.getHolderIdCard());
                insuredInfo.setHolderIdCardType(String.valueOf(userInfo.getHolderIdType()));
                //保存险种
                InsuranceTypeInfo insuranceTypeInfo=null;
                ArrayList<InsuranceTypeInfo> arrayList = new ArrayList<>();
                if (quote != null && !quote.equals("")) {
                    if (StringUtils.isNotBlank(userInfo.getForceExpireDate())) {
                        insuranceTypeInfo = new InsuranceTypeInfo(createBy,"0",insuredId);
                        insuranceTypeInfo.setInfoType("0");
                        insuranceTypeInfo.setCreatedBy(createBy);
                        insuranceTypeInfo.setTypeId(insuredId);
                        insuranceTypeInfo.setInsuranceName("交强险");
                        insuranceTypeInfo.setInsuranceAmount(BigDecimal.valueOf(1));
                        arrayList.add(insuranceTypeInfo);
                    }
                    if (quote.getDouble("CheSun") != 0) {
                        insuranceTypeInfo = new InsuranceTypeInfo(createBy,"0",insuredId);
                        insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("A"));
                        insuranceTypeInfo.setInsuranceAmount(BigDecimal.valueOf(quote.getDouble("CheSun")));
                        arrayList.add(insuranceTypeInfo);
                        if (Integer.valueOf(quote.getString("BuJiMianCheSun")) != 0) {
                            insuranceTypeInfo = new InsuranceTypeInfo(createBy,"0",insuredId);
                            insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("MA"));
                            insuranceTypeInfo.setInsuranceAmount(BigDecimal.valueOf(1));
                            arrayList.add(insuranceTypeInfo);
                        }
                    }
                    if (quote.getDouble("SanZhe") != 0) {
                        insuranceTypeInfo = new InsuranceTypeInfo(createBy,"0",insuredId);
                        insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("B"));
                        insuranceTypeInfo.setInsuranceAmount(BigDecimal.valueOf(quote.getDouble("SanZhe")));
                        arrayList.add(insuranceTypeInfo);
                        if (Integer.valueOf(quote.getString("BuJiMianSanZhe")) != 0){
                            insuranceTypeInfo = new InsuranceTypeInfo(createBy,"0",insuredId);
                            insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("MB"));
                            insuranceTypeInfo.setInsuranceAmount(BigDecimal.valueOf(1));
                            arrayList.add(insuranceTypeInfo);
                        }
                    }
                    if (quote.getDouble("DaoQiang") != 0) {
                        insuranceTypeInfo = new InsuranceTypeInfo(createBy,"0",insuredId);
                        insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("G1"));
                        insuranceTypeInfo.setInsuranceAmount(BigDecimal.valueOf(quote.getDouble("DaoQiang")));
                        arrayList.add(insuranceTypeInfo);
                        if (Integer.valueOf(quote.getString("BuJiMianDaoQiang")) != 0){
                            insuranceTypeInfo = new InsuranceTypeInfo(createBy,"0",insuredId);
                            insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("MG1"));
                            insuranceTypeInfo.setInsuranceAmount(BigDecimal.valueOf(1));
                            arrayList.add(insuranceTypeInfo);
                        }
                    }
                    if (quote.getDouble("ChengKe") != 0) {
                        insuranceTypeInfo = new InsuranceTypeInfo(createBy,"0",insuredId);
                        insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("D3"));
                        insuranceTypeInfo.setInsuranceAmount(BigDecimal.valueOf(quote.getDouble("ChengKe")));
                        arrayList.add(insuranceTypeInfo);
                        if (Integer.valueOf(quote.getString("BuJiMianChengKe")) != 0){
                            insuranceTypeInfo = new InsuranceTypeInfo(createBy,"0",insuredId);
                            insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("MD3"));
                            insuranceTypeInfo.setInsuranceAmount(BigDecimal.valueOf(1));
                            arrayList.add(insuranceTypeInfo);
                        }
                    }
                    if (quote.getDouble("SiJi") != 0) {
                        insuranceTypeInfo = new InsuranceTypeInfo(createBy,"0",insuredId);
                        insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("D4"));
                        insuranceTypeInfo.setInsuranceAmount(BigDecimal.valueOf(quote.getDouble("SiJi")));
                        arrayList.add(insuranceTypeInfo);
                        if (Integer.valueOf(quote.getString("BuJiMianSiJi")) != 0){
                            insuranceTypeInfo = new InsuranceTypeInfo(createBy,"0",insuredId);
                            insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("MD4"));
                            insuranceTypeInfo.setInsuranceAmount(BigDecimal.valueOf(1));
                            arrayList.add(insuranceTypeInfo);
                        }
                    }
                    if (quote.getDouble("HuaHen") != 0) {
                        insuranceTypeInfo = new InsuranceTypeInfo(createBy,"0",insuredId);
                        insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("L"));
                        insuranceTypeInfo.setInsuranceAmount(BigDecimal.valueOf(quote.getDouble("HuaHen")));
                        arrayList.add(insuranceTypeInfo);
                        if (Integer.valueOf(quote.getString("BuJiMianHuaHen")) != 0){
                            insuranceTypeInfo = new InsuranceTypeInfo(createBy,"0",insuredId);
                            insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("ML"));
                            insuranceTypeInfo.setInsuranceAmount(BigDecimal.valueOf(1));
                            arrayList.add(insuranceTypeInfo);
                        }
                    }
                    if (quote.getDouble("SheShui") != 0) {
                        insuranceTypeInfo = new InsuranceTypeInfo(createBy,"0",insuredId);
                        insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("X1"));
                        insuranceTypeInfo.setInsuranceAmount(BigDecimal.valueOf(quote.getDouble("SheShui")));
                        arrayList.add(insuranceTypeInfo);
                        if (Integer.valueOf(quote.getString("BuJiMianSheShui")) != 0) {
                            insuranceTypeInfo = new InsuranceTypeInfo(createBy,"0",insuredId);
                            insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("MX1"));
                            insuranceTypeInfo.setInsuranceAmount(BigDecimal.valueOf(1));
                            arrayList.add(insuranceTypeInfo);
                        }
                    }
                    if (quote.getDouble("ZiRan") != 0) {
                        insuranceTypeInfo = new InsuranceTypeInfo(createBy,"0",insuredId);
                        insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("Z"));
                        insuranceTypeInfo.setInsuranceAmount(BigDecimal.valueOf(quote.getDouble("ZiRan")));
                        arrayList.add(insuranceTypeInfo);
                        if (Integer.valueOf(quote.getString("BuJiMianZiRan")) != 0){
                            insuranceTypeInfo = new InsuranceTypeInfo(createBy,"0",insuredId);
                            insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("MZ"));
                            insuranceTypeInfo.setInsuranceAmount(BigDecimal.valueOf(1));
                            arrayList.add(insuranceTypeInfo);
                        }
                    }
                    if (quote.getDouble("HcJingShenSunShi") != 0) {
                        insuranceTypeInfo = new InsuranceTypeInfo(createBy,"0",insuredId);
                        insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("R"));
                        insuranceTypeInfo.setInsuranceAmount(BigDecimal.valueOf(quote.getDouble("HcJingShenSunShi")));
                        arrayList.add(insuranceTypeInfo);
                        if (Integer.valueOf(quote.getString("BuJiMianJingShenSunShi")) != 0){
                            insuranceTypeInfo = new InsuranceTypeInfo(createBy,"0",insuredId);
                            insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("MR"));
                            insuranceTypeInfo.setInsuranceAmount(BigDecimal.valueOf(1));
                            arrayList.add(insuranceTypeInfo);
                        }
                    }
                    if (quote.getDouble("HcSanFangTeYue") != 0) {
                        insuranceTypeInfo = new InsuranceTypeInfo(createBy,"0",insuredId);
                        insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("Z3"));
                        insuranceTypeInfo.setInsuranceAmount(BigDecimal.valueOf(quote.getDouble("HcSanFangTeYue")));
                        arrayList.add(insuranceTypeInfo);
                    }
                    if (StringUtils.isNotBlank(quote.getString("HcXiuLiChang"))) {
                        if (!Integer.valueOf(quote.getString("HcXiuLiChang")).equals("-1")) {
                            insuranceTypeInfo = new InsuranceTypeInfo(createBy,"0",insuredId);
                            insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("Q3"));
                            insuranceTypeInfo.setInsuranceAmount(BigDecimal.valueOf(Double.valueOf(quote.getString("HcXiuLiChang"))));
                            arrayList.add(insuranceTypeInfo);
                        }
                    }
                    if (quote.getDouble("BoLi") != 0) {
                        insuranceTypeInfo = new InsuranceTypeInfo(createBy,"0",insuredId);
                        insuranceTypeInfo.setInsuranceName(InsuranceItems2.getName("F"));
                        insuranceTypeInfo.setInsuranceAmount(BigDecimal.valueOf(quote.getDouble("BoLi")));
                        arrayList.add(insuranceTypeInfo);
                    }
                }
                //查询是否第一次续保
                JSONObject jsonObject= JSON.parseObject(httpResult.getBody());
                CarInfo c=carInfoService.findBy("carNumber", carInfo.getCarNumber());
                if (c!=null){
                    carInfo.setCarInfoId(c.getCarInfoId());
                    carInfoService.update(carInfo);
                    jsonObject.put("carInfoId",c.getCarInfoId());
                }else {
                    carInfoService.save(carInfo);
                    insuredInfoService.save(insuredInfo);
                    insuranceTypeInfoService.save(arrayList);
                    jsonObject.put("carInfoId",carInfoId);
                }
                jsonObject.put("createTime",new Date());
                jsonObject.put("insurance",arrayList);
                return ResultGenerator.genSuccessResult(jsonObject);
            } else {
                return ResultGenerator.genFailResult(resultData.getStatusMessage());
            }
        } else
            return ResultGenerator.genFailResult(httpResult.getMessage());
    }
}
