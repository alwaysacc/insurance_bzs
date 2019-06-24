package com.bzs.dao;

import com.bzs.model.CommissionPercentage;
import com.bzs.utils.Mapper;

import java.util.List;

public interface CommissionPercentageMapper extends Mapper<CommissionPercentage> {
    int addOrUpdate(CommissionPercentage domain);
    CommissionPercentage getLastUpdateData();

    List getListAndUpdateBy();
}