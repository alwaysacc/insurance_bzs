package com.bzs.utils;

/**
 * 响应码枚举，参考HTTP状态码的语义
 */
public enum ResultCode {
    SUCCESS_NULL(100),//查询成功，但未查询到数据
    PARAMS_ERROR(-100),
    SUCCESS(200),//成功 (报价+核保成功)
    SUBMIT(301),//报价成功+核保失败
    FAIL(400),//失败 （报价+核保失败）
    UNAUTHORIZED(401),//未认证（签名错误）
    NOT_FOUND(404),//接口不存在
    INTERNAL_SERVER_ERROR(500);//服务器内部错误


    private final int code;

    ResultCode(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}
