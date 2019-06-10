package com.bzs.service;

import com.bzs.model.QuoteInfo;
import com.bzs.utils.Result;
import com.bzs.utils.Service;
import com.bzs.utils.jsontobean.InsurancesList;
import com.bzs.utils.jsontobean.QuoteParmasBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * Created by alwaysacc on 2019/04/11.
 */
public interface QuoteInfoService extends Service<QuoteInfo> {
    Map quoteDetails(String carInfoId);

    /**
     * 调用爬虫的报价接口并插入报价信息
     *
     * @param params    报价必传信息
     * @param list      报价时的投保项
     * @param carInfoId 车辆信息id
     * @param createdBy 创建人id
     * @param source    报价公司枚举值
     * @return
     */
    Result getQuoteDetailsByApi(QuoteParmasBean params, List<InsurancesList> list, String carInfoId, String createdBy, Long source);

    /**
     * @param params
     * @param list
     * @param carInfoId
     * @param createdBy
     * @param source    报价公司枚举值集合
     * @return
     * @description 多家公司同时报价
     */
    Result getQuoteDetailsByApi(QuoteParmasBean params, List<InsurancesList> list, String carInfoId, String createdBy, List<Long> source, Long lastYearSource);

    /**
     * @param proposalNo 核保单号
     * @param pay        支付方式 0微信1支付宝 默认微信
     * @param money      支付金额
     * @param createdBy  创建人
     * @param carInfoId  车辆信息id
     * @param quoteId    报价id
     * @param source     保司枚举值 默认太保
     * @return
     */
    Result getPayMentgetPayMent(String proposalNo, String pay, String money, String createdBy, String carInfoId, String quoteId, Long source, String deliveryWay, String deliveryAddress, String contactName, String contactTel);

    Map<String, Object> updatePayInfo(String proposalNo);

    /**
     * 作废支付
     *
     * @param proposalNo
     * @param createdBy
     * @param quoteId
     * @param source
     * @return
     */

    Result payCancel(String proposalNo, String createdBy, String quoteId, Long source, String orderId);

    /**
     * 根据条件查询
     *
     * @param quoteId    报价id
     * @param createBy   创建人
     * @param carInfoId  车辆id
     * @param proposalNo 支付单号
     * @return
     */
    Map findListByDifferCondition(String quoteId, String createBy, String carInfoId, String proposalNo);

    /**
     * 壁虎-报价核保基础接口
     * @param personName 车主
     * @param personCardID 车主证件号
     * @param personCardIDType 车主证件类型
     * @param carNo 车牌号
     * @param carFrameNo 车架号
     * @param carEngineNo 发动机号
     * @param carFirstRegisterDate 车辆注册日期
     * @param lists 险种信息
     * @param ciBeginDate 商业险起保日期
     * @param biBeginDate 交强险起保日期
     * @param carTransDate 过户日期
     * @param carVehicleFgwCode 车辆型号
     * @param carInfoId 车辆信息id
     * @param createdBy 操作人
     * @param QuoteGroup  需要报价的 保险资源的枚举值之和
     * @param SubmitGroup 需要核保的 保险资源的枚举值之和  这个范围应该是QuoteGroup的子集，必须报价了，才可以核保
     * @param isSame 投保人与被保人信息是否一致
     * @param forceTax 0:单商业 ，1：商业+交强车船，2：单交强+车船
     * @return
     */
    Result postPrecisePrice(String personName, String personCardID, String personCardIDType,
                            String carNo, String carFrameNo, String carEngineNo,String carFirstRegisterDate,
                            String lists,String ciBeginDate, String biBeginDate, String carTransDate,
                            String carVehicleFgwCode, String carInfoId, String createdBy, Long QuoteGroup,
                            Long SubmitGroup, String isSame, int forceTax,Double purchasePrice);

    /**
     * 获取报价信息，在postPrecisePrice基础上
     * @param licenseNo
     * @param quoteGroup
     * @param quoteGroup
     * @return
     */
    Map getPrecisePrice(String licenseNo, Long quoteGroup,String createBy,String carInfoId);

    /**
     *获取车辆核保信息
     * @param licenseNo 车牌号
     * @param submitGroup 核保公司枚举值
     * @return
     */
    Map<String, Object> getSubmitInfo(String licenseNo,Long submitGroup, String createBy,String carInfoId,String quoteId);


    /**
     * 	1、获取支付链接前需要校验一下保单的起保时间是否在当前时间之后，如果    已过起保时间无法获取支付链接；
     * 	2、支付链接的有效期为2小时且当天前，超过两小时或者超过当天时需重新获取；
     * 	3、太平洋 微信和poss支付都是返回 支付号、校验码及金额 三个参数，不返回支付链接地址；微信支付部分地区返回二维码：【北京、杭州、合肥、广州、天津、石家庄、太原、西安、济南、烟台、聊城、苏州、重庆、成都、贵阳、新乡、郑州、武汉、东莞、厦门、海口、沈阳、长春、昆明、保定】）
     * 	4、人保和平安的订单有效期最长是30天且在起保日期前，太保的有效期为当天且在起保日期前；
     * 	5、太保微信支付时不需要合作银行；
     */
    /**
     * 获取支付信息
     * @param carVin 车架
     * @param licenseNo 车牌
     * @param payMent 支付方式 1、微信  2、pos
     * @param source 获取保司枚举值
     * @param bizNo 商业险单号
     * @param forceNo 交强险当好
     * @param buid 报价接口返回
     * @param channelId  核保后返回的渠道Id
     * @param quoteId 报价id
     * @param createBy 创建人
     * @param isGetPayWay  是否获取链接的支付类型 0=否（默认）   1=是
     * @return
     */
    public Map<String, Object> getPayAddress(String carVin, String licenseNo,int payMent, Long source,String bizNo,
                                             String forceNo, String buid,String channelId,String quoteId,String createBy,int isGetPayWay,String carInfoId);

    /**
     * 查看支付结果
     * @param carVin
     * @param licenseNo
     * @param source
     * @param buid
     * @param bizNo
     * @param forceNo
     * @param channelId
     * @param transactionNum
     * @param orderId
     * @return
     */
    Map<String, Object> getPayInfo(String carVin,String licenseNo, Long source,String buid, String bizNo, String forceNo, String channelId, String transactionNum, String orderId) ;

    /**
     * 作废原支付方式
     * @param carVin
     * @param licenseNo
     * @param source
     * @param buid
     * @param orderId
     * @param bizNo
     * @param transactionNum
     * @param forceNo
     * @param channelId
     * @param payWay 原支付方式 太保必须 6=刷卡、 2=划卡、 1=支票、 chinapay=银联电子支付、 weixin=微信支付、5=网银转账、3A=集中支付
     * @return
     */

     Map<String, Object> doVoidPay(String carVin, String licenseNo, Long source, String buid, String  orderId, String bizNo,String transactionNum,String forceNo, String channelId,String payWay,String quoteId);

     /**
     * &#x6dfb;&#x52a0;&#x6216;&#x66f4;&#x65b0;
     * @param quoteInfo
     * @return
     */
    int insertOrUpdate(QuoteInfo quoteInfo);
    /**
     * 更新
     * @param quoteInfo
     * @return
     */
    int updateByQuoteId(QuoteInfo quoteInfo);

    /**
     * 壁虎接口 -获取城市渠道续保期
     * @return
     */
    Map<String,Object> getContinuedPeriods();

    /**
     * 获取新车车型信息接口 接口8
     * @param carVin
     * @param engineNo
     * @param moldName
     * @param cityCode
     * @return
     */

    Map<String,Object> getFirstVehicleInfo(String carVin, String engineNo, String moldName, int cityCode);

    /**
     * 进口车根据车架号获取品牌名称（新车报价用）
     * @param cityCode
     * @param carVin  进口车，车架号不是以字母L开头
     * @return
     */
    Map<String,Object> getModelNameForImportCar(Integer cityCode, String carVin);

    /**
     *获取车辆出险信息
     * @param licenseNo
     * @param renewalCarType 大小号牌：0小车，1大车，默认0
     * @return
     */

    Map<String,Object> getCreditDetailInfo(String licenseNo, Integer renewalCarType);

    /**
     *
     * @param info 返回的信息
     * @param buid
     * @param request
     * @return
     */

    Map<String,Object> uploadImgForPingAn(String info, String buid, HttpServletRequest request);
}
