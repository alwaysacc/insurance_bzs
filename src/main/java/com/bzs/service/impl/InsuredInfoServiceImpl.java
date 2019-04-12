package com.bzs.service.impl;

import com.alibaba.fastjson.JSON;

import com.bzs.dao.InsuredInfoMapper;
import com.bzs.model.CarInfo;
import com.bzs.model.CheckInfo;
import com.bzs.model.InsuredInfo;
import com.bzs.model.QuoteInfo;
import com.bzs.service.*;
import com.bzs.utils.*;

import com.bzs.utils.httpUtil.HttpClientUtil;
import com.bzs.utils.httpUtil.HttpResult;
import com.bzs.utils.jsontobean.RenewalBean;
import com.bzs.utils.jsontobean.RenewalData;
import org.apache.commons.lang.StringUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.Date;
import java.util.HashMap;
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
    public Result<String> checkByCarNoOrVinNo(String checkType, String carNo, String idCard, String vinNo, String engineNo, String lastYearSource, String insuredArea) {
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
                        //注意此处为暂时设置,实现登录后可从session 获取
                        // checkInfo.setCheckInfoId(uuid);
                        InsuredInfo insuredInfo = new InsuredInfo(uuid);
                        insuredInfo.setAccountId(uuid);
                        CarInfo carInfo = new CarInfo(uuid);
                        carInfo.setCreatedBy(uuid);
                        carInfo.setCarNumber(carNo);
                        //QuoteInfo quoteInfo=new QuoteInfo(uuid);
                        RenewalBean bean = JSON.parseObject(body, RenewalBean.class);
                        String state = bean.getState();
                        RenewalData data = bean.getData();
                        Date sendTime = bean.getSendTime();
                        if ("1".equals(state)) {//查询成功并返回值
                            if (data != null) {
                                String account = data.getA().getAmount();
                                System.out.println("获取金额:" + account);
                            }
                        } else if ("0099".equals(state)) {//查询返回失败

                        } else {

                        }
                        checkInfoService.save(checkInfo);
                        insuredInfoMapper.insert(insuredInfo);
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
