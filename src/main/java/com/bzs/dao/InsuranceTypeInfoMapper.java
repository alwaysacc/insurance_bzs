package com.bzs.dao;

import com.bzs.model.InsuranceTypeInfo;
import com.bzs.utils.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InsuranceTypeInfoMapper extends Mapper<InsuranceTypeInfo> {
    int insertBatch(List<InsuranceTypeInfo> list);

    /**
     * 批量插入或者更新
     * @param list
     * @return
     */
    boolean batchInsertOrUpdateList(List <InsuranceTypeInfo> list);

    /**
     * 根据typeId删除
     * @param typeId
     * @return
     */
    int deleteByTypeId(@Param("typeId") String typeId);
}