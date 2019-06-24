package com.bzs.utils.function;


@FunctionalInterface
public interface CacheSelector<T> {
    T select() throws Exception;

}
