package com.bzs.dao;

import com.bzs.model.CommissionEveryDay;
import com.bzs.utils.Mapper;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

public interface CommissionEveryDayMapper extends Mapper<CommissionEveryDay> {

    int deleteCommission(@Param("id") Integer[] id);
}