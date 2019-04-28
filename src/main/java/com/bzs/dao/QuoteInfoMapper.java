package com.bzs.dao;

import com.bzs.model.QuoteInfo;
import com.bzs.utils.Mapper;
import org.apache.ibatis.annotations.Param;

public interface QuoteInfoMapper extends Mapper<QuoteInfo> {
    public int updatePayInfo (@Param("payUrl") String payUrl,@Param("payTime") String payTime,@Param("proposalNo") String proposalNo,@Param("payNo")String payNo,@Param("checkNo")String checkNo);
}