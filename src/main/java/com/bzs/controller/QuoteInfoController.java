package com.bzs.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bzs.model.AccountInfo;
import com.bzs.model.CarInfo;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.QuoteInfo;
import com.bzs.service.QuoteInfoService;
import com.bzs.utils.UUIDS;
import com.bzs.utils.commons.ThirdAPI;
import com.bzs.utils.dateUtil.DateUtil;
import com.bzs.utils.jsontobean.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by alwaysacc on 2019/04/11.
 */
@RestController
@RequestMapping("/quoteinfo")
public class QuoteInfoController {
    @Resource
    private QuoteInfoService quoteInfoService;

    @PostMapping("/quoteDetails")
    public Result quoteDetails(String carInfoId) {
        Map map = quoteInfoService.quoteDetails(carInfoId);
        return ResultGenerator.genSuccessResult(map);
    }

    @PostMapping("/add")
    public Result add(QuoteInfo quoteInfo) {
        quoteInfoService.save(quoteInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        quoteInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(QuoteInfo quoteInfo) {
        quoteInfoService.update(quoteInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        QuoteInfo quoteInfo = quoteInfoService.findById(id);
        return ResultGenerator.genSuccessResult(quoteInfo);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<QuoteInfo> list = quoteInfoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 获取报价信息,通过第三方接口
     *
     * @param params
     * @param list
     * @return
     */
    @PostMapping("/getQuoteInfo")
    public Result getQuoteInfo(QuoteParmasBean params, @RequestBody(required = false) List<InsurancesList> list, String carInfoId, String createdBy, Long source) {
        return quoteInfoService.getQuoteDetailsByApi(params, list, carInfoId, createdBy, source);
    }

    /**
     * @param salesPerson          销售人员姓名 根据保代公司决定（太平洋必传）
     * @param ciBeginDate          交强险起期
     * @param biBeginDate          商业险起期
     * @param carTransDate         过户日期
     * @param carIsTrans           是否过户 0：否 1：是
     * @param carEnergyType
     * @param carVehicleFgwCode
     * @param carUse
     * @param carVehicleType
     * @param carUseProperty
     * @param carFirstRegisterDate
     * @param carEngineNo
     * @param carFrameNo
     * @param carColor
     * @param carNoType
     * @param carNo
     * @param personName
     * @param personMobile
     * @param personCardID
     * @param carInfoId
     * @param createdBy
     * @param source
     * @return
     */

    @PostMapping("/getQuoteInfoAllParams")
    public Result getQuoteInfo(@RequestParam String personName, @RequestParam String personMobile, @RequestParam String personCardID,
                               @RequestParam String carNo, @RequestParam String carFrameNo, @RequestParam String carEngineNo,
                               @RequestParam String salesPerson, @RequestParam String carFirstRegisterDate,String lists,
                               String ciBeginDate, String biBeginDate, String carTransDate, String carIsTrans, String carEnergyType,
                               String carVehicleFgwCode, String carUse, String carVehicleType, String carUseProperty,
                               String carColor, String carNoType, String carInfoId, String createdBy, Long source, String account, String accountPwd) {
        List list=null;
        System.out.println(lists);
        if(StringUtils.isNotBlank(lists)){
            list= JSON.parseArray(lists);
        }
        if (null == ciBeginDate) {
            ciBeginDate = "";
        }
        if (null == biBeginDate) {
            biBeginDate = "";
        }
        if (null == carVehicleFgwCode) {
            carVehicleFgwCode = "";
        }
        if (StringUtils.isBlank(carNoType)) {
            carNoType = "02";
        }
        if (StringUtils.isBlank(carColor)) {
            carColor = "1";
        }
        if (StringUtils.isBlank(carUseProperty)) {
            carUseProperty = "101";
        }
        if (StringUtils.isBlank(carUse)) {
            carUse = "01";
        }
        if (StringUtils.isBlank(carEnergyType)) {
            carEnergyType = "0";
        }
        String personSex = "", personAge = "";
       /* if(StringUtils.isBlank(personAge)){
            personAge="32";
        }*/
        if (StringUtils.isBlank(carVehicleType)) {
            carVehicleType = "A01";
        }
        String flag = "2";//只报价
        /*if(StringUtils.isNotBlank(flag)&&"1".equals(flag)){
            flag="1";
        }else{
            flag="2";
        }*/

        String pay = "";
        String refId = "";
        if (StringUtils.isBlank(pay) || "0".equals(pay) || "weixin".equals(pay)) {
            pay = "weixin";
        } else {
            pay = "alipay";
        }
        if (StringUtils.isNotBlank(carIsTrans) && "1".equals(carIsTrans) && StringUtils.isBlank(carTransDate)) {
            return ResultGenerator.genFailResult("选择过户，必传过户日期");
        } else {
            carIsTrans = "0";//默认非过户
            carTransDate = "";
        }
        String kilometres = "";
        QuoteParmasBean params = new QuoteParmasBean();
        params.setFlag(flag);
        params.setPay(pay);
        if (StringUtils.isBlank(refId)) {
            refId = UUIDS.getDateUUID();
        }
        params.setRefId(refId);
        String sendTime = DateUtil.getDateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
        params.setSendTime(sendTime);
        ParamsData data = new ParamsData();
        // data.setInsurancesList(list);
        data.setCiBeginDate(ciBeginDate);
        data.setBiBeginDate(biBeginDate);
        data.setSalesPerson(salesPerson);
        params.setData(data);
        com.bzs.utils.jsontobean.CarInfo carInfo = new com.bzs.utils.jsontobean.CarInfo();
        carInfo.setIsTrans(carIsTrans);
        carInfo.setTransDate(carTransDate);
        carInfo.setCarNo(carNo);
        carInfo.setCarUse(carUse);
        carInfo.setEnergyType(carEnergyType);
        carInfo.setCarUse(carUse);
        carInfo.setFirstRegisterDate(carFirstRegisterDate);
        carInfo.setNoType(carNoType);
        carInfo.setColor(carColor);
        carInfo.setEngineNo(carEngineNo);
        carInfo.setFrameNo(carFrameNo);
        carInfo.setVehicleType(carVehicleType);
        carInfo.setVehicleFgwCode(carVehicleFgwCode);
        carInfo.setUseProperty(carUseProperty);
        data.setCarInfo(carInfo);
        PersonInfo personInfo = new PersonInfo();
        String personAddress = "";
        personInfo.setAddress(personAddress);
        personInfo.setAge(personAge);
        personInfo.setCardID(personCardID);
        personInfo.setMobile(personMobile);
        personInfo.setName(personName);
        personInfo.setSex(personSex);
        data.setPersonInfo(personInfo);
        if(StringUtils.isBlank(account)){
            account=ThirdAPI.PAIC_ACCOUNT;
        }
        if(StringUtils.isBlank(accountPwd)){
            accountPwd=ThirdAPI.PAIC_PWD;
        }
        InsuranceAccountInfo accountInfo = new InsuranceAccountInfo(account, accountPwd);
        data.setAccountInfo(accountInfo);
        String userName = (String) SecurityUtils.getSubject().getSession().getAttribute("userName");
        //或者(String) request.getSession().getAttribute("userName");
        //账号信息
        AccountInfo a = (AccountInfo) SecurityUtils.getSubject().getSession().getAttribute("accountInfo");

        return quoteInfoService.getQuoteDetailsByApi(params, list, carInfoId, createdBy, source);
    }

    /**
     * @param proposalNo 核保单号
     * @param pay        支付方式 0微信1支付宝
     * @param money      支付金额
     * @param createdBy  创建人
     * @param carInfoId  车辆信息id
     * @param quoteId    报价id
     * @param source     报价公司的枚举值
     * @return
     */
    @PostMapping("/pay")
    public Result getPayMent(String proposalNo, String pay, String money, String createdBy, String carInfoId, String quoteId, Long source) {
        return quoteInfoService.getPayMentgetPayMent(proposalNo, pay, money, createdBy, carInfoId, quoteId, source);
    }

    @PostMapping("/updatePayInfo")
    public Result updatePayInfo(String payUrl, String payTime, String proposalNo) {
        Map<String, Object> result = quoteInfoService.updatePayInfo(proposalNo);
        String status = (String) result.get("status");
        return ResultGenerator.genSuccessResult(status);
    }

}
