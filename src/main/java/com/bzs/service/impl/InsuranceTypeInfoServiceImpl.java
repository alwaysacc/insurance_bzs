package com.bzs.service.impl;

import com.bzs.dao.InsuranceTypeInfoMapper;
import com.bzs.model.InsuranceTypeInfo;
import com.bzs.service.InsuranceTypeInfoService;
import com.bzs.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by alwaysacc on 2019/04/12.
 */
@Service
@Transactional
public class InsuranceTypeInfoServiceImpl extends AbstractService<InsuranceTypeInfo> implements InsuranceTypeInfoService {
    @Resource
    private InsuranceTypeInfoMapper insuranceTypeInfoMapper;

    @Override
    public int insertBatch(List<InsuranceTypeInfo> list) {
        return insuranceTypeInfoMapper.insertBatch(list);
    }

    @Override
    public Boolean  batchInsertOrUpdateList(List<InsuranceTypeInfo> info) {
       return  insuranceTypeInfoMapper.batchInsertOrUpdateList2(info);
    }
}
