package com.bzs.utils.httpUtil;

import org.apache.poi.hssf.record.formula.functions.T;

/**
 * @program: insurance_bzs
 * @description: http请求返回值对象
 * @author: dengl
 * @create: 2019-04-11 13:18
 */
public class HttpResult<T> {
    private int code;
    private String body;
    private String message;
    private T t;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    public HttpResult(){
        super();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public HttpResult(int code, String body) {
        super();
        this.code = code;
        this.body = body;

    }

    public HttpResult(int code, String body, String message, T t) {
        this.code = code;
        this.body = body;
        this.message = message;
        this.t = t;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "code=" + code +
                ", body='" + body + '\'' +
                ", message='" + message + '\'' +
                ", t=" + t +
                '}';
    }
}
