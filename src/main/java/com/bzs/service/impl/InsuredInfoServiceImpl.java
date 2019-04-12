package com.bzs.service.impl;

import com.alibaba.fastjson.JSON;

import com.bzs.dao.InsuredInfoMapper;
import com.bzs.model.*;
import com.bzs.service.*;
import com.bzs.utils.*;

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
    public Result checkByCarNoOrVinNo(String checkType, String carNo, String idCard, String vinNo, String engineNo, String lastYearSource, String insuredArea) {
        if (StringUtils.isBlank(checkType)) {
            return ResultGenerator.genFailResult("参数异常");
        } else {
            String url = "http://192.168.1.106:5000";
            String api = "/cpic_xubao";
            Map<String, Object> paramMap = new HashMap<String, Object>();
            if ("0".equals(checkType)) {//车牌续保
                if (StringUtils.isNotBlank(carNo)) {
                    paramMap.put("carNo", carNo);
                } else {
                    return ResultGenerator.genFailResult("参数异常,车牌号不能为空");
                }
            } else if ("1".equals(checkType)) {//车架续保
                if (StringUtils.isNotBlank(vinNo)) {
                    paramMap.put("vinNo", vinNo);
                } else {
                    return ResultGenerator.genParamsErrorResult("参数异常,车架号不能为空");
                }
            } else {//待拓展

            }
            HttpResult result = HttpClientUtil.doPost(url + api, paramMap, "JSON");
            if (null != result) {
                int code = result.getCode();
                if (code == 200) {//远程请求成功
                    String body = result.getBody();
                    if (StringUtils.isNotBlank(body)) {
                        String uuid = UUIDS.getDateUUID();
                        CheckInfo checkInfo = new CheckInfo(uuid);
                        checkInfo.setCarInfoId(uuid);
                        //待修改
                        checkInfo.setCreateBy(uuid);
                        //注意此处为暂时设置,实现登录后可从session 获取
                        // checkInfo.setCheckInfoId(uuid);
                        InsuredInfo insuredInfo = new InsuredInfo(uuid);
                        insuredInfo.setCreateId(uuid);
                        insuredInfo.setCarInfoId(uuid);
                        CarInfo carInfo = new CarInfo(uuid);
                        carInfo.setCreatedBy(uuid);
                        carInfo.setCarNumber(carNo);
                        //QuoteInfo quoteInfo=new QuoteInfo(uuid);
                        RenewalBean bean = JSON.parseObject(body, RenewalBean.class);
                        String state = bean.getState();
                        RenewalData data = bean.getData();
                        String sendTime = bean.getSendTime();
                        List<InsuranceTypeInfo> insuranceTypeInfoList = null;
                        if ("1".equals(state)) {//查询成功并返回值
                            if (data != null) {

                                insuranceTypeInfoList = InsuranceTypeBase.getInsuranceTypeInfoList(data, uuid, uuid, "0");
                                String engineNoNew = data.getEngineNo();//发送机号
                                String registerDate = data.getFirstRegisterDate();//车辆注册日期
                                String bizStartDate = data.getBiBeginDate();//商业险下次起保日期
                                String bizPreminm = data.getBiPremium();//商业险保额
                                String forceStartDate=data.getCiBeginDate();//交强险下次起保日期
                                String forcePreminm=data.getCiPremium();//交强险保额
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
                            }
                        } else if ("0099".equals(state)) {//查询返回失败

                        } else {

                        }
                        checkInfoService.save(checkInfo);
                        insuredInfoMapper.insert(insuredInfo);
                        insuranceTypeInfoService.save(insuranceTypeInfoList);//保存
                        carInfoService.save(carInfo);
                        return ResultGenerator.genSuccessResult(body);
                    }
                } else {
                    System.out.println("请求代码" + code);
                    return ResultGenerator.genFailResult("异常");
                }
            } else {
            }

        }


        return null;
    }


}
