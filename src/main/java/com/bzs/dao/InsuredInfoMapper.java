package com.bzs.dao;

import com.bzs.model.InsuredInfo;
import com.bzs.utils.Mapper;

import java.util.List;

public interface InsuredInfoMapper extends Mapper<InsuredInfo> {
    /**
     * 插入或更新
     * @param insuredInfo
     * @return
     */
    public int insertOrUpdate(InsuredInfo insuredInfo);


}