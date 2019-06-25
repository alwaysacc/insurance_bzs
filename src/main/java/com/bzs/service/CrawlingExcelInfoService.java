package com.bzs.service;
import com.bzs.model.CrawlingExcelInfo;
import com.bzs.utils.Result;
import com.bzs.utils.Service;

import java.util.Date;


/**
 * Created by dl on 2019/06/20.
 */
public interface CrawlingExcelInfoService extends Service<CrawlingExcelInfo> {

    int add(CrawlingExcelInfo data);

    /**
     * 爬取结束后更新数据
     * @param data
     * @return
     */
    int updateCrawlingFinish(CrawlingExcelInfo data);
    //爬取结束后更新数据
    int updateCrawlingFinish(String  seriesNo,Integer lastCrawling,String status,Integer finishTotal,Date finishDate);


}
