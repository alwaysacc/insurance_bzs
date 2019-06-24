package com.bzs.model.query;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: insurance_bzs
 * @description:
 * @author: dengl
 * @create: 2019-06-18 15:36
 */
@Data
@ToString
public class QueryRequest implements Serializable {
    private static final long serialVersionUID = -4869594085374385813L;

    private int pageSize;
    private int pageNum;
    private String sortField;
    private String sortOrder;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
