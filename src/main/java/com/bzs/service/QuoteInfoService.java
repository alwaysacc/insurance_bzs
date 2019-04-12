package com.bzs.service;
import com.bzs.model.QuoteInfo;
import com.bzs.utils.Service;

import java.util.Map;


/**
 * Created by alwaysacc on 2019/04/11.
 */
public interface QuoteInfoService extends Service<QuoteInfo> {
    Map quoteDetails(String carInfoId);
}
