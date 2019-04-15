package com.bzs.service.impl;

import com.alibaba.fastjson.JSON;

import com.bzs.dao.InsuredInfoMapper;
import com.bzs.model.*;
import com.bzs.service.*;
import com.bzs.utils.*;

import com.bzs.utils.commons.ThirdAPI;
import com.bzs.utils.dateUtil.DateUtil;
import com.bzs.utils.httpUtil.HttpClientUtil;
import com.bzs.utils.httpUtil.HttpResult;
import com.bzs.utils.jsontobean.InsuranceTypeBase;
import com.bzs.utils.jsontobean.RenewalBean;
import com.bzs.utils.jsontobean.RenewalData;
import org.apache.commons.lang.StringUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by denglei on 2019/04/10 17:09:11.
 */
@Service
@Transactional
public class InsuredInfoServiceImpl extends AbstractService<InsuredInfo> implements InsuredInfoService {
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

    @Override
    public Result checkByCarNoOrVinNo(String checkType, String carNo, String idCard, String vinNo, String engineNo, Long lastYearSource, String insuredArea,String createdBy) {
        if (StringUtils.isBlank(checkType)) {
            return ResultGenerator.genFailResult("参数异常");
        } else {
            String url = "http://192.168.1.106";
            String port = "5000";
            String api = "cpic_xubao";//
            Map<String, Object> paramMap = new HashMap<String, Object>();
            if ("0".equals(checkType)) {//车牌续保
                if (StringUtils.isNotBlank(carNo)) {
                    paramMap.put("carNo", carNo);
                    paramMap.put("frameNo", "");
                } else {
                    return ResultGenerator.genFailResult("参数异常,车牌号不能为空");
                }
            } else if ("1".equals(checkType)) {//车架续保
                if (StringUtils.isNotBlank(vinNo)) {
                    paramMap.put("carNo", "");
                    paramMap.put("frameNo", vinNo);
                } else {
                    return ResultGenerator.genParamsErrorResult("参数异常,车架号不能为空");
                }
            } else {//待拓展
                return ResultGenerator.genParamsErrorResult("参数异常,目前仅支持车牌或者车架号续保");
            }
            if(StringUtils.isBlank(createdBy)){
                createdBy = UUIDS.getDateUUID();
            }
            if(null==lastYearSource){
                lastYearSource=1L;
            }
            Map<String, Object> renewalInfo = getDifferentSourceRenewalInfo(lastYearSource, url, port, paramMap, carNo, createdBy);
            String status = (String) renewalInfo.get("status");
            String msg=(String)renewalInfo.get("msg");
            if (StringUtils.isNotBlank(status)) {
                if ("200".equals(status)) {
                    CheckInfo checkInfo = (CheckInfo) renewalInfo.get("checkInfo");
                    checkInfo.setCheckType(checkInfo+"");//查询方式
                    InsuredInfo insuredInfo = (InsuredInfo) renewalInfo.get("insuredInfo");
                    List<InsuranceTypeInfo> insuranceTypeInfoList = (List<InsuranceTypeInfo> ) renewalInfo.get("insuranceTypeInfoList");
                    String body=(String)renewalInfo.get("body");
                    RenewalBean dataBean=(RenewalBean)renewalInfo.get("data");
                    CarInfo carInfo = (CarInfo) renewalInfo.get("carInfo");
                    checkInfoService.save(checkInfo);
                    insuredInfoMapper.insert(insuredInfo);
                    for (InsuranceTypeInfo datas : insuranceTypeInfoList) {
                        insuranceTypeInfoService.save(datas);
                    }
                    carInfoService.save(carInfo);
                   return  ResultGenerator.gen(msg,body,ResultCode.SUCCESS);
                } else if ("0099".equals(status)) {//续保选择的保险公司不对,重新续保
                    String body=(String)renewalInfo.get("body");
                    RenewalBean dataBean=(RenewalBean)renewalInfo.get("data");
                    return  ResultGenerator.gen(msg,body,ResultCode.SUCCESS);
                } else {
                    return  ResultGenerator.genFailResult(msg);
                }
            }
        }
        return ResultGenerator.genFailResult("获取失败");
    }

    /**
     * @param source    保险枚举值1太保2平安4人保
     * @param host      ip地址
     * @param port      端口号
     * @param param     第三方接口的请求参数
     * @param carNo     车牌号
     * @param createdBy 创建人id
     * @return
     */
    public Map<String, Object> getDifferentSourceRenewalInfo(Long source, String host, String port, Map<String, Object> param, String carNo, String createdBy) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (null == source) {
            result.put("status", "400");
            result.put("msg", "参数异常");
            result.put("data", null);
            return result;
        }
        if (StringUtils.isBlank(host)) {
            result.put("status", "400");
            result.put("msg", "ip异常");
            result.put("data", null);
            return result;
        }
        if (StringUtils.isBlank(port)) {
            result.put("status", "400");
            result.put("msg", "端口异常");
            result.put("data", null);
            return result;
        }
        String api = null;
        String lastYearInsuranceCompany = null;
        if (1 == source) {//太保
            api = ThirdAPI.CPIC_RENEWAL_NAME;
            lastYearInsuranceCompany = "太平洋保险";
        } else if (2 == source) {//平安
            api = ThirdAPI.PAIC_RENEWAL_NAME;
            lastYearInsuranceCompany = "平安保险";
        } else if (4 == source) {//人保
            api = ThirdAPI.PICC_RENEWAL_NAME;
            lastYearInsuranceCompany = "人保保险";
        } else {
            result.put("status", "400");
            result.put("msg", "待拓展");
            result.put("data", null);
            return result;
        }
        String URL = host + ":" + port + "/" + api;
        HttpResult httpResult = HttpClientUtil.doPost(URL, param, "JSON", RenewalBean.class,null);
        if (httpResult != null) {
            int code = httpResult.getCode();
            if (code == 200) {
                RenewalBean bean = (RenewalBean) httpResult.getT();
                String body = httpResult.getBody();
                result.put("data", bean);
                result.put("body", body);
                if (null != bean) {
                    String uuid = UUIDS.getDateUUID();
                    CheckInfo checkInfo = new CheckInfo(uuid);
                    checkInfo.setCarInfoId(uuid);
                    //待修改
                    checkInfo.setCreateBy(uuid);
                    //注意此处为暂时设置,实现登录后可从session 获取
                    // checkInfo.setCheckInfoId(uuid);
                    InsuredInfo insuredInfo = new InsuredInfo(uuid);
                    insuredInfo.setCreateId(createdBy);
                    insuredInfo.setCarInfoId(uuid);
                    insuredInfo.setLastYearSource(source + "");
                    insuredInfo.setLastYearInsuranceCompany(lastYearInsuranceCompany);
                    CarInfo carInfo = new CarInfo(uuid);
                    carInfo.setCreatedBy(uuid);
                    carInfo.setCarNumber(carNo);
                    //QuoteInfo quoteInfo=new QuoteInfo(uuid);
                    //RenewalBean bean = JSON.parseObject(body, RenewalBean.class);
                    String state = bean.getState();
                    RenewalData data = bean.getData();
                    String sendTime = bean.getSendTime();
                    checkInfo.setSendTime(sendTime);
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
                            result.put("status", "200");
                            result.put("msg", "获取上年续保成功");
                            result.put("checkInfo", checkInfo);
                            result.put("insuranceTypeInfoList", insuranceTypeInfoList);
                            result.put("carInfo", carInfo);
                            result.put("insuredInfo", insuredInfo);
                            return result;
                        }

                    } else if ("0099".equals(state)) {//查询返回成功但未获取到续保信息
                        result.put("status", "0099");
                        result.put("msg", "上年未在此保司投保");
                        result.put("checkInfo", checkInfo);
                        result.put("insuranceTypeInfoList", insuranceTypeInfoList);
                        result.put("carInfo", carInfo);
                        result.put("insuredInfo", insuredInfo);
                        return result;
                    } else {//续保失败
                        result.put("status", "400");
                        result.put("msg", "续保失败");
                        result.put("data", null);
                        return result;
                    }
                }

            } else {
                result.put("status", "500");
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

    public Map<String,Object>getDifferentSourceQuoteInfo(Long source, String host, String port, Map<String, Object> param, String carNo, String createdBy){


        return null;
    }


}
