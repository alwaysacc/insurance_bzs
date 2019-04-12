package com.bzs.service.impl;

import com.alibaba.fastjson.JSON;

import com.bzs.dao.InsuredInfoMapper;
import com.bzs.model.InsuredInfo;
import com.bzs.service.InsuredInfoService;
import com.bzs.utils.AbstractService;

import com.bzs.utils.Result;
import com.bzs.utils.ResultCode;
import com.bzs.utils.ResultGenerator;
import com.bzs.utils.httpUtil.HttpClientUtil;
import com.bzs.utils.httpUtil.HttpResult;
import com.bzs.utils.jsontobean.RenewalBean;
import org.apache.commons.lang.StringUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by denglei on 2019/04/10 17:09:11.
=======


/**
 * Created by alwaysacc on 2019/04/11.
>>>>>>> f32665f1bb289f060e3830ea3c95f08aff5a07b8
 */
@Service
@Transactional
public class InsuredInfoServiceImpl extends AbstractService<InsuredInfo> implements InsuredInfoService {
    @Resource
    private InsuredInfoMapper insuredInfoMapper;


    @Override
    public Result<RenewalBean> checkByCarNoOrVinNo(String checkType, String carNo, String idCard, String vinNo, String engineNo, String lastYearSource, String insuredArea) {
        if (StringUtils.isBlank(checkType)) {
            return ResultGenerator.genFailResult("参数异常");
        } else {
            if ("0".equals(checkType)) {//车牌续保
                if (StringUtils.isNotBlank(carNo)) {
                    String url = "http://192.168.1.106:5000";
                    String api = "/cpic_xubao";
                    Map<String, Object> paramMap = new HashMap<String, Object>();
                    paramMap.put("carNo", carNo);// 苏A99C3G
                    HttpResult result = HttpClientUtil.doPost(url + api, paramMap, "JSON");
                    if (null != result) {
                        int code = result.getCode();
                        if (code == 200) {//远程请求成功
                            String body = result.getBody();
                            if (StringUtils.isNotBlank(body)) {
                                RenewalBean bean = JSON.parseObject(body, RenewalBean.class);
                                System.out.println(bean.getSendTime());
                                ResultGenerator.gen("成功",bean,ResultCode.SUCCESS);
                            }
                        } else {
                            System.out.println("请求代码"+code);
                            return ResultGenerator.genFailResult("异常");
                        }
                    }else{

                    }


                } else {
                    return ResultGenerator.genParamsErrorResult("参数异常,车牌号不能为空");
                }
            } else if ("1".equals(checkType)) {//车架续保
                if (StringUtils.isNotBlank(vinNo)) {

                } else {
                    return ResultGenerator.genParamsErrorResult("参数异常,车架号不能为空");
                }

            }

        }


        return null;
    }

}
