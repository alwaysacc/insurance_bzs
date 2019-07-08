package com.bzs.service;
import com.bzs.model.CrawlingCarInfo;
import com.bzs.model.query.CrawlingQuery;
import com.bzs.utils.Result;
import com.bzs.utils.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * Created by dl on 2019/06/19.
 */
public interface CrawlingCarInfoService extends Service<CrawlingCarInfo> {

    public int batchInsertImport (List<CrawlingCarInfo> list);

    /**
     * 远程爬取
     * @param username
     * @param password
     * @param flag
     * @param no
     * @return
     */
    public String httpCrawling(String username,String password,String flag,String no);

    /**
     * 用于爬取时的计算总数，以便于分页
     * @param seriesNo 序列号
     * @return
     */
    int crawlingDataCount(String seriesNo);
    /**
     *用于爬取是的分页
     * @param seriesNo 序列号
     * @param startRow 起始行
     * @param pageSize 页码大小
     * @return
     */
    public List crawlingDataList(String seriesNo,Integer startRow,Integer pageSize);

    /**
     * 导出数据
     * @param seriesNo
     * @return
     */
    public List<CrawlingCarInfo> exportDataListBySeriesNo(String seriesNo);

    /**
     * 通过序列号查询
     * @param seriesNo
     * @return
     */
    public int exportDataCountBySeriesNo(String seriesNo);

    /**
     * 开始爬取
     * @return
     */
    Result startCrawling(String  seriesNo);

    void startCrawling1();

    /**
     * 爬取2019-07-08
     * @return
     */
    String  startCrawling();

    String exportCrawlingDataList(HttpServletResponse response, HttpServletRequest request, String seriesNo);

    int getProgress(String seriesNo);
}
