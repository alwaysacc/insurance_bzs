package com.bzs.utils.exception;

/**
 * @program: insurance_bzs
 * @description:
 * @author: dengl
 * @create: 2019-06-18 14:32
 */
public class RedisConnectException extends Exception {
    private static final long serialVersionUID = 1639374111871115063L;

    public RedisConnectException(String message) {
        super(message);
    }
}
