package com.bzs.service;

import com.bzs.model.QuoteInfo;
import com.bzs.utils.Result;
import com.bzs.utils.Service;
import com.bzs.utils.jsontobean.InsurancesList;
import com.bzs.utils.jsontobean.QuoteParmasBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

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
                            Long SubmitGroup, String isSame, int forceTax);

    /**
     * 获取报价信息，在postPrecisePrice基础上
     * @param licenseNo
     * @param quoteGroup
     * @return
     */
    Map getPrecisePrice(String licenseNo, Long quoteGroup);

}
