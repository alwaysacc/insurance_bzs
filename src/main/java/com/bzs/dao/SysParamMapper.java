package com.bzs.dao;

import com.bzs.model.SysParam;
import com.bzs.utils.Mapper;
import org.apache.ibatis.annotations.Param;

public interface SysParamMapper extends Mapper<SysParam> {

    int getShowToday();
    String getRole();

    String getParamValue(@Param("paramKey")String paramKey);
}