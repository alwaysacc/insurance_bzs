package com.bzs.utils.jsontobean.crawlingcar;

/**
 * @program: insurance_bzs
 * @description: 爬取的rootBean
 * @author: dengl
 * @create: 2019-06-20 10:55
 */
public class CrawlingCarRootBean {
    private int code;
    private CrawlingCarData data;
    private String msg;;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public CrawlingCarData getData() {
        return data;
    }

    public void setData(CrawlingCarData data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public CrawlingCarRootBean() {
    }

}
