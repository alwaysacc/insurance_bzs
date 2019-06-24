package com.bzs.model.query;

import com.bzs.model.CrawlingCarInfo;

/**
 * @program: insurance_bzs
 * @description: 爬取查询类
 * @author: dengl
 * @create: 2019-06-20 17:15
 */
public class CrawlingQuery extends CrawlingCarInfo {
    private String sStatus;//批次状态0未执行1完成2暂停
    private String sType;//批次类型1车牌2车架
    private Integer bId;//批次表的id
    private Integer page;//当前页
    private Integer pageSize;//每页展示的数量
    private Integer startRow;//开始
    private Integer enbRow;//结束
    public String getsStatus() {
        return sStatus;
    }

    public void setsStatus(String sStatus) {
        this.sStatus = sStatus;
    }

    public String getsType() {
        return sType;
    }

    public void setsType(String sType) {
        this.sType = sType;
    }

    public Integer getbId() {
        return bId;
    }

    public void setbId(Integer bId) {
        this.bId = bId;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getStartRow() {
        return startRow;
    }

    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }

    public Integer getEnbRow() {
        return enbRow;
    }

    public void setEnbRow(Integer enbRow) {
        this.enbRow = enbRow;
    }

    public CrawlingQuery() {
        super();
        if(page==null){
            page=1;
        }
        if(pageSize==null){
            pageSize=10;
        }
        startRow=(page-1)*pageSize;
    }
}
