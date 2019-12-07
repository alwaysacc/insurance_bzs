package com.bzs.dao;

import com.bzs.model.SysParam;
import com.bzs.utils.Mapper;

public interface SysParamMapper extends Mapper<SysParam> {

    int getShowToday();
    String getRole();
}