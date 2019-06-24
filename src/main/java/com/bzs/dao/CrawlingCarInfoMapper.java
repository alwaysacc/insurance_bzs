package com.bzs.dao;

import com.bzs.model.CrawlingCarInfo;
import com.bzs.model.query.CrawlingQuery;
import com.bzs.utils.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CrawlingCarInfoMapper extends Mapper<CrawlingCarInfo> {
    /**
     * 导入时的批量添加
     * @param list
     * @return
     */
    int batchInsertImport(List<CrawlingCarInfo> list);

    /**
     * 可爬取的基础数据
     * @return
     */
    List  crawlingDataList(@Param("seriesNo") String seriesNo,@Param("startRow")Integer startRow,@Param("pageSize")Integer pageSize);
    int crawlingDataCount(@Param("seriesNo") String seriesNo);

    List exportDataListBySeriesNo(@Param("seriesNo") String seriesNo,@Param("startRow")Integer startRow,@Param("pageSize")Integer pageSize);

    /**
     * 爬取后更新
     * @param data
     * @return
     */
    int crawlingUpdate(CrawlingCarInfo data);

}