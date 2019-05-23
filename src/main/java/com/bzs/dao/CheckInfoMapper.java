package com.bzs.dao;

import com.bzs.model.CheckInfo;
import com.bzs.utils.Mapper;

public interface CheckInfoMapper extends Mapper<CheckInfo> {
    /**
     * 添加或者修改
     * @param checkInfo
     * @return
     */
    int updateOrAdd(CheckInfo checkInfo);
}
