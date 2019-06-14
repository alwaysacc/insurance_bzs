package com.bzs.dao;

import com.bzs.model.DrawCash;
import com.bzs.utils.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DrawCashMapper extends Mapper<DrawCash> {
    /**
     * 批量插入
     * @param list
     * @return
     */
    int insertBatch(List<DrawCash> list);

    List getDrawCashList(@Param("incomePerson") String incomePerson,@Param("type") int type);

    /**
     * 新的批量插入
     * @param list
     * @return
     */
    int insertBatch2(List<DrawCash> list);
}