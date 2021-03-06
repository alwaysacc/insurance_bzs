package com.bzs.utils.httpUtil;


/**
 * @program: insurance_bzs
 * @description: http请求返回值对象
 * @author: dengl
 * @create: 2019-04-11 13:18
 */
public class HttpResult<T> {
    //请求码200 500 404 等
    private int code;
    //存储请求返回的内容
    private String body;
    //描述信息
    private String message;
    //将body转换成实体类
    private T t;
    //续保枚举值
    private  Long source;

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
    public HttpResult(int code, String body, String message) {
        this.code = code;
        this.body = body;
        this.message = message;
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

    public Long getSource() {
        return source;
    }

    public void setSource(Long source) {
        this.source = source;
    }
}
