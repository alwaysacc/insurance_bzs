package com.bzs.dao;

import com.bzs.model.CommissionPercentage;
import com.bzs.utils.Mapper;

public interface CommissionPercentageMapper extends Mapper<CommissionPercentage> {
    int addOrUpdate(CommissionPercentage domain);
    CommissionPercentage getLastUpdateData();
}