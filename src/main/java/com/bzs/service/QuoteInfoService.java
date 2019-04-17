package com.bzs.service;
import com.bzs.model.QuoteInfo;
import com.bzs.utils.Result;
import com.bzs.utils.Service;
import com.bzs.utils.jsontobean.InsurancesList;
import com.bzs.utils.jsontobean.QuoteParmasBean;

import java.util.List;
import java.util.Map;


/**
 * Created by alwaysacc on 2019/04/11.
 */
public interface QuoteInfoService extends Service<QuoteInfo> {
    Map quoteDetails(String carInfoId);
    Result getQuoteDetailsByApi(QuoteParmasBean params,List<InsurancesList> list,String carInfoId,String createdBy);
}
