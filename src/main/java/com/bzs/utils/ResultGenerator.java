package com.bzs.utils;

import org.apache.poi.hssf.record.formula.functions.T;

/**
 * 响应结果生成工具
 */
public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

    public static Result genSuccessResult() {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE);
    }

    public static <T> Result<T> genSuccessResult(T data) {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
    }

    public static Result genFailResult(String message) {
        return new Result()
                .setCode(ResultCode.FAIL)
                .setMessage(message);
    }
    public static <T> Result<T> genSuccessResult(T data,String message) {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setMessage(message)
                .setData(data);
    }
    public static Result genParamsErrorResult(String message) {
        return new Result()
                .setCode(ResultCode.PARAMS_ERROR)
                .setMessage(message);
    }
    public static <T>Result<T> gen(String message,T data,ResultCode code){
        return  new Result().setCode(code).setMessage(message).setData(data);
    }
    public static <T>Result<T> gen(String message,T data,int code){
        return  new Result().setCode(code).setMessage(message).setData(data);
    }

}
