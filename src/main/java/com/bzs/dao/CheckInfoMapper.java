package com.bzs.dao;

import com.bzs.model.CheckInfo;
import com.bzs.utils.Mapper;

import java.util.List;

public interface CheckInfoMapper extends Mapper<CheckInfo> {
    /**
     * 添加或者修改
     * @param checkInfo
     * @return
     */
    int updateOrAdd(CheckInfo checkInfo);

    /**
     * 条件差查询
     * @param checkInfo
     * @return
     */
    CheckInfo checkByDifferConditions(CheckInfo checkInfo);

    List<CheckInfo> getListByDifferConditions(CheckInfo checkInfo);
}
