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
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

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
     * @param lists
     * @param carInfoId
     * @param createdBy
     * @param source
     * @return
     */

    @PostMapping("/getQuoteInfoAllParams")
    public Result getQuoteInfo(@RequestParam String personName, @RequestParam String personMobile, @RequestParam String personCardID,
                               @RequestParam String carNo, @RequestParam String carFrameNo, @RequestParam String carEngineNo,
                               @RequestParam String salesPerson, @RequestParam String carFirstRegisterDate, String lists,
                               String ciBeginDate, String biBeginDate, String carTransDate, String carIsTrans, String carEnergyType,
                               String carVehicleFgwCode, String carUse, String carVehicleType, String carUseProperty,
                               String carColor, String carNoType, String carInfoId, String createdBy, Long source, String account, String accountPwd) {
        List list = null;
        if (StringUtils.isNotBlank(lists)) {
            list = (List) JSON.parseArray(lists);
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
        if (StringUtils.isBlank(personMobile)) {
            personMobile = "15051820077";
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
        personMobile = "15518727891";
        personInfo.setMobile(personMobile);
        personInfo.setName(personName);
        personInfo.setSex(personSex);
        data.setPersonInfo(personInfo);
      /*  if(StringUtils.isBlank(account)){
            account=ThirdAPI.PAIC_ACCOUNT;
        }
        if(StringUtils.isBlank(accountPwd)){
            accountPwd=ThirdAPI.PAIC_PWD;
        }*/
        InsuranceAccountInfo accountInfo = new InsuranceAccountInfo(account, accountPwd);
        data.setAccountInfo(accountInfo);

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
    public Result getPayMent(String proposalNo, String pay, String money, String createdBy, String carInfoId, String quoteId, Long source, String deliveryWay, String deliveryAddress, String contactName, String contactTel) {
        return quoteInfoService.getPayMentgetPayMent(proposalNo, pay, money, createdBy, carInfoId, quoteId, source, deliveryWay, deliveryAddress, contactName, contactTel);
    }

    @ApiOperation("作废支付")
    @PostMapping("/payCancel")
    public Result payCancel(String proposalNo, String createdBy, String quoteId, Long source, String orderId) {
        return quoteInfoService.payCancel(proposalNo, createdBy, quoteId, source, orderId);
    }

    @PostMapping("/updatePayInfo")
    public Result updatePayInfo(String payUrl, String payTime, String proposalNo) {
        Map<String, Object> result = quoteInfoService.updatePayInfo(proposalNo);
        String status = (String) result.get("status");
        return ResultGenerator.genSuccessResult(status);
    }

    @ApiOperation("根据不同条件获取")
    @PostMapping("/findListByDifferCondition")
    public Result findListByDifferCondition(String quoteId, String createBy, String carInfoId, String proposalNo) {
        Map map = quoteInfoService.findListByDifferCondition(quoteId, createBy, carInfoId, proposalNo);
        String code = (String) map.get("code");
        String msg = (String) map.get("msg");

        Condition c = new Condition(QuoteInfo.class);
        c.createCriteria().andCondition("quoteId", quoteId).andCondition("createBy", createBy);
        List lists = quoteInfoService.findByCondition(c);
        if ("200".equals(code)) {
            List list = (List) map.get("data");
            return ResultGenerator.genSuccessResult(list, msg);
        } else {
            return ResultGenerator.genFailResult(msg);
        }
    }

    @ApiOperation("添加或更新")
    @PostMapping("/addOrUpdate")
    public Result addOrUpdate(QuoteInfo qpc) {
        int result = quoteInfoService.insertOrUpdate(qpc);
        if (result > 0)
            return ResultGenerator.gen("成功", "", 200);
        else return ResultGenerator.gen("失败", "", 400);
    }


    /**
     * 壁虎-报价核保基础接口
     *
     * @param personName           车主
     * @param personCardID         车主证件号
     * @param personCardIDType     车主证件类型
     * @param carNo                车牌号
     * @param carFrameNo           车架号
     * @param carEngineNo          发动机号
     * @param carFirstRegisterDate 车辆注册日期
     * @param lists                险种信息
     * @param ciBeginDate          商业险起保日期
     * @param biBeginDate          交强险起保日期
     * @param carTransDate         过户日期
     * @param carVehicleFgwCode    车辆型号
     * @param carInfoId            车辆信息id
     * @param createdBy            操作人
     * @param quoteGroup           需要报价的 保险资源的枚举值之和
     * @param submitGroup          需要核保的 保险资源的枚举值之和  这个范围应该是QuoteGroup的子集，必须报价了，才可以核保
     * @param isSame               投保人与被保人信息是否一致 默认0一致
     * @param forceTax             0:单商业 ，1：商业+交强车船，2：单交强+车船
     * @return
     */

    @ApiOperation("调用第三方壁虎-报价核保基础接口")
    @PostMapping("/WX_GetPostPrecisePrice")
    public Result postPrecisePrice(@RequestParam String personName, @RequestParam String personCardID, @RequestParam String personCardIDType,
                                   @RequestParam String carNo, @RequestParam String carFrameNo, @RequestParam String carEngineNo,
                                   @RequestParam String carFirstRegisterDate, String lists,
                                   String ciBeginDate, String biBeginDate, String carTransDate,
                                   String carVehicleFgwCode, String carInfoId, String createdBy, Long quoteGroup, Long submitGroup, String isSame, int forceTax,Double purchasePrice) {
        isSame = "0";
        return quoteInfoService.postPrecisePrice(personName, personCardID, personCardIDType, carNo, carFrameNo, carEngineNo, carFirstRegisterDate, lists, ciBeginDate, biBeginDate, carTransDate, carVehicleFgwCode, carInfoId, createdBy, quoteGroup, submitGroup, isSame, forceTax,purchasePrice);
    }

    @ApiOperation("调用第三方壁虎-获取报价信息接口")
    @PostMapping("/WX_GetPrecisePrice")
    Map getPrecisePrice(String licenseNo, Long quoteGroup, String createBy, String carInfoId) {
        return quoteInfoService.getPrecisePrice(licenseNo, quoteGroup, createBy, carInfoId);
    }

    @ApiOperation("调用第三方壁虎-获取核保信息接口")
    @PostMapping("/WX_GetSubmitInfo")
    Map getSubmitInfo(String licenseNo, Long submitGroup, String createBy, String carInfoId, String quoteId) {
        return quoteInfoService.getSubmitInfo(licenseNo, submitGroup, createBy, carInfoId, quoteId);
    }

    @ApiOperation("调用第三方壁虎-获取支付信息接口")
    @PostMapping("/WX_GetPayAddress")
    Map getPayAddress(String carVin, String licenseNo, int payMent, Long source, String bizNo, String forceNo, String buid, String channelId, String quoteId, String createBy, int isGetPayWay, String carInfoId) {
        return quoteInfoService.getPayAddress(carVin, licenseNo, payMent, source, bizNo, forceNo, buid, channelId, quoteId, createBy, isGetPayWay, carInfoId);
    }

    @ApiOperation("调用第三方壁虎-获取支付结果接口")
    @PostMapping("/WX_GetPayResult")
    public Map<String, Object> getPayResult(String carVin, String licenseNo, Long source, String buid, String bizNo, String forceNo, String channelId, String orderId,String createBy,String quoteId) {
        return quoteInfoService.getPayInfo(carVin, licenseNo, source, buid, bizNo, forceNo, channelId, null, orderId,createBy,quoteId);
    }

    @ApiOperation("调用第三方壁虎-作废原支付接口")
    @PostMapping("/WX_DoVoidPay")
    public Map<String, Object> doVoidPay(String carVin, String licenseNo, Long source, String buid, String orderId, String bizNo, String transactionNum, String forceNo, String channelId, String payWay,String quoteId,String cancelMsg) {
        return quoteInfoService.doVoidPay(carVin, licenseNo, source, buid, orderId, bizNo, transactionNum, forceNo, channelId, payWay,quoteId,cancelMsg);
    }
    @ApiOperation("调用第三方壁虎-获取城市渠道续保期接口")
    @PostMapping("/WX_GetContinuedPeriods")
    public Map<String, Object>GetContinuedPeriods(){
        return quoteInfoService.getContinuedPeriods();
    }

    @ApiOperation("调用第三方壁虎-获取新车车型信息接口")
    @PostMapping("/WX_GetFirstVehicleInfo")
    public Map<String, Object>getFirstVehicleInfo(String carVin, String engineNo,String moldName,Integer cityCode){
        cityCode=8;
        return quoteInfoService.getFirstVehicleInfo(carVin,engineNo,moldName,cityCode);
    }
    /**
     * 进口车根据车架号获取品牌名称（新车报价用）
     * @param cityCode
     * @param carVin  进口车，车架号不是以字母L开头
     * @return
     */
    @ApiOperation("调用第三方壁虎-进口车根据车架号获取品牌名称（新车报价用）")
    @PostMapping("/WX_GetModelName")
    Map<String, Object> getModelNameForImportCar(Integer cityCode,String carVin){
        cityCode=8;
        return quoteInfoService.getModelNameForImportCar(cityCode,carVin);
    }

    /**
     *只有报价成功后调用这个接口才会有返回值
     * @param licenseNo
     * @param renewalCarType 大小号牌：0小车，1大车，默认0
     * @return
     */
    @ApiOperation("调用第三方壁虎-获取车辆出险信息")
    @PostMapping("/WX_GetCreditDetailInfo")
    Map<String, Object>getCreditDetailInfo(String licenseNo,Integer renewalCarType){
        renewalCarType=0;
        return quoteInfoService.getCreditDetailInfo(licenseNo,renewalCarType);
    }

    @ApiOperation("调用第三方壁虎-平安上传图片")
    @PostMapping("WX_PINGAN_UploadImg")
    Map<String,Object>UploadImgForPingAn(String info,String buid,HttpServletRequest request){
        return quoteInfoService.uploadImgForPingAn(info,buid,request);
    }
}
