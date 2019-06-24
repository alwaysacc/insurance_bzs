package com.bzs.service;
import com.bzs.model.CrawlingExcelInfo;
import com.bzs.utils.Result;
import com.bzs.utils.Service;


/**
 * Created by dl on 2019/06/20.
 */
public interface CrawlingExcelInfoService extends Service<CrawlingExcelInfo> {

    int add(CrawlingExcelInfo data);


}
