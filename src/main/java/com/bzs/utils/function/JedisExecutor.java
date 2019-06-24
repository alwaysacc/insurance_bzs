package com.bzs.utils.function;

import com.bzs.utils.exception.RedisConnectException;
@FunctionalInterface
public interface JedisExecutor<T, R> {
    R excute(T t) throws RedisConnectException;
}
