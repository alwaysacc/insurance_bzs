package com.bzs.dao;

import com.bzs.model.ThirdInsuranceAccountDateInfo;
import com.bzs.utils.Mapper;

public interface ThirdInsuranceAccountDateInfoMapper extends Mapper<ThirdInsuranceAccountDateInfo> {
public void saveByProcedure(ThirdInsuranceAccountDateInfo info);
}