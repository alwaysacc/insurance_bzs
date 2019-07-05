package com.bzs.dao;

import com.bzs.model.CrawlingExcelInfo;
import com.bzs.utils.Mapper;
import org.apache.ibatis.annotations.Param;

public interface CrawlingExcelInfoMapper extends Mapper<CrawlingExcelInfo> {
    int add(CrawlingExcelInfo crawlingExcelInfo);

    /**
     * 爬取结束后更新
     * @param data
     * @return
     */
    int updateCrawlingFinish(CrawlingExcelInfo data);

    int updateCrawlingStatus(@Param("seriesNo") String seriesNo,@Param("status") String status);
    int updateStatus(@Param("seriesNo") String seriesNo,@Param("status") String status);

    int getFinishTotal(String seriesNo);
}