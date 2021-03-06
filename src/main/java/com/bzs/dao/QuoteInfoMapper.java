package com.bzs.dao;

import com.bzs.model.QuoteInfo;
import com.bzs.utils.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuoteInfoMapper extends Mapper<QuoteInfo> {

    /**
     * @Author 孙鹏程
     * @Description  获取险种   infoType 0去年投保险种  1 报价险种
     * @Date 2019/4/25/025 14:30
     * @Param [quoteId, infoType]
     * @return java.util.List
     **/
    List getInsurance(@Param("quoteId")String quoteId,@Param("infoType")int infoType);

    List getQuote(String carInfoId);

    int updatePayInfo (@Param("payUrl") String payUrl,@Param("payTime") String payTime,@Param("proposalNo") String proposalNo,@Param("payNo")String payNo,@Param("checkNo")String checkNo,@Param("paymentNotice")String paymentNotice,@Param("serialNo")String serialNo,@Param("payEndDate")String payEndDate,@Param("payMsg")String payMsg);

    /**
     * 条件查询
     * @param quoteInfo
     * @return
     */
    List findListByDifferCondition(QuoteInfo quoteInfo);

    /**
     * 添加或更新，可根据情况拓展
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
}