package com.bzs.dao;

import com.bzs.model.InsuranceTypeInfo;
import com.bzs.utils.Mapper;

import java.util.List;

public interface InsuranceTypeInfoMapper extends Mapper<InsuranceTypeInfo> {
    int insertBatch(List<InsuranceTypeInfo> list);
}