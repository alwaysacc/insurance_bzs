package com.bzs.utils.threadUtil;

import com.bzs.utils.httpUtil.HttpClientUtil;
import com.bzs.utils.httpUtil.HttpResult;
import com.bzs.utils.jsontobean.RenewalBean;
import org.apache.poi.hssf.record.formula.functions.T;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;


/**
 * @program: insurance_bzs
 * @description: 续保时多家同时续保
 * @author: dengl
 * @create: 2019-04-22 11:37
 */
public class RenewalThreadCallable implements Callable<HttpResult> {
    private String url;
    private Map<String,Object>param;
    private String type;
    private Class<T> bean;
    private String jsonStr;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Class<T> getBean() {
        return bean;
    }

    public void setBean(Class<T> bean) {
        this.bean = bean;
    }

    public String getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    public RenewalThreadCallable(String url, Map<String, Object> param, String type, Class<T> bean, String jsonStr) {
        this.url = url;
        this.param = param;
        this.type = type;
        this.bean = bean;
        this.jsonStr = jsonStr;
    }

    @Override
    public HttpResult call() throws Exception {
        HttpResult httpResult = HttpClientUtil.doPost(url, param, "JSON", RenewalBean.class, null);
        return httpResult;
    }
}
