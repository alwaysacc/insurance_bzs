package com.bzs.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "crawling_excel_info")
public class CrawlingExcelInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 表名
     */
    private String name;

    /**
     * 批次号
     */
    @Column(name = "series_no")
    private String seriesNo;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建人
     */
    @Column(name = "create_by")
    private String createBy;



    /**
     * 完成时间
     */
    @Column(name = "finish_date")
    private Date finishDate;

    /**
     * 1车牌2车架
     */
    private String type;

    /**
     * 最后一次爬取的id
     */
    @Column(name = "last_crawling")
    private Integer lastCrawling;

    /**
     * 最后一次爬取的方式0车牌2车架
     */
    @Column(name = "status")
    private String status;
    /**
     * 总数量
     */
    private Integer total;
    /**
     * 完成数量
     */
    @Column(name = "finish_total")
    private Integer finishTotal;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取表名
     *
     * @return name - 表名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置表名
     *
     * @param name 表名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取批次号
     *
     * @return series_no - 批次号
     */
    public String getSeriesNo() {
        return seriesNo;
    }

    /**
     * 设置批次号
     *
     * @param seriesNo 批次号
     */
    public void setSeriesNo(String seriesNo) {
        this.seriesNo = seriesNo;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取创建人
     *
     * @return create_by - 创建人
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置创建人
     *
     * @param createBy 创建人
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }


    /**
     * 获取完成时间
     *
     * @return finish_date - 完成时间
     */
    public Date getFinishDate() {
        return finishDate;
    }

    /**
     * 设置完成时间
     *
     * @param finishDate 完成时间
     */
    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    /**
     * 获取0车牌1车架
     *
     * @return type - 0车牌1车架
     */
    public String getType() {
        return type;
    }

    /**
     * 设置0车牌1车架
     *
     * @param type 0车牌1车架
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取最后一次爬取的id
     *
     * @return last_crawling - 最后一次爬取的id
     */
    public Integer getLastCrawling() {
        return lastCrawling;
    }

    /**
     * 设置最后一次爬取的id
     *
     * @param lastCrawling 最后一次爬取的id
     */
    public void setLastCrawling(Integer lastCrawling) {
        this.lastCrawling = lastCrawling;
    }

    /**
     * 状态0未执行1完成2暂停
     *
     * @return status - 状态0未执行1完成2暂停
     */
    public String getStatus() {
        return status;
    }

    /**
     * 状态0未执行1完成2暂停
     *
     * @param status 状态0未执行1完成2暂停
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getFinishTotal() {
        return finishTotal;
    }

    public void setFinishTotal(Integer finishTotal) {
        this.finishTotal = finishTotal;
    }

    public CrawlingExcelInfo() {
    }

    public CrawlingExcelInfo(String name, String seriesNo, String createBy, String type, Integer total) {
        this.name = name;
        this.seriesNo = seriesNo;
        this.createBy = createBy;
        this.type = type;
        this.total = total;
    }
}