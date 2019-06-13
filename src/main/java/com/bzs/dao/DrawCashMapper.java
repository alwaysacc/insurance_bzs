package com.bzs.dao;

import com.bzs.model.DrawCash;
import com.bzs.utils.Mapper;

import java.util.List;

public interface DrawCashMapper extends Mapper<DrawCash> {
    /**
     * 批量插入
     * @param list
     * @return
     */
    int insertBatch(List<DrawCash> list);
}