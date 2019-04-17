package com.bzs.service;

import com.bzs.model.InsuranceTypeInfo;
import com.bzs.utils.Service;

import java.util.List;


/**
 * Created by alwaysacc on 2019/04/12.
 */
public interface InsuranceTypeInfoService extends Service<InsuranceTypeInfo> {
    public int insertBatch(List<InsuranceTypeInfo> list);
}
