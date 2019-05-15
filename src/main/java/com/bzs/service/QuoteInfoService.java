package com.bzs.service;

import com.bzs.model.QuoteInfo;
import com.bzs.utils.Result;
import com.bzs.utils.Service;
import com.bzs.utils.jsontobean.InsurancesList;
import com.bzs.utils.jsontobean.QuoteParmasBean;
import org.apache.ibatis.annotations.Param;

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
     * @param source 报价公司枚举值
     * @return
     */
    Result getQuoteDetailsByApi(QuoteParmasBean params, List<InsurancesList> list, String carInfoId, String createdBy, Long source);

    /**
     * @description 多家公司同时报价
     * @param params
     * @param list
     * @param carInfoId
     * @param createdBy
     * @param source 报价公司枚举值集合
     * @return
     */
    Result getQuoteDetailsByApi(QuoteParmasBean params, List<InsurancesList> list, String carInfoId, String createdBy,List<Long> source,Long lastYearSource);

    /**
     *
     * @param proposalNo 核保单号
     * @param pay 支付方式 0微信1支付宝 默认微信
     * @param money 支付金额
     * @param createdBy 创建人
     * @param carInfoId 车辆信息id
     * @param quoteId 报价id
     *  @param source 保司枚举值 默认太保
     * @return
     */
    Result getPayMentgetPayMent(String proposalNo, String pay, String money, String createdBy, String carInfoId, String quoteId,Long source);

    Map<String,Object>updatePayInfo(String proposalNo);

    /**
     * 作废支付
     * @param proposalNo
     * @param createdBy
     * @param quoteId
     * @param source
     * @return
     */

    Result payCancel(String proposalNo, String createdBy, String quoteId, Long source,String orderNo);
}
