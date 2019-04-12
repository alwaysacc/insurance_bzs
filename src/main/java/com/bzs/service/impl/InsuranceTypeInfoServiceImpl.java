package com.bzs.service.impl;

import com.bzs.dao.InsuranceTypeInfoMapper;
import com.bzs.model.InsuranceTypeInfo;
import com.bzs.service.InsuranceTypeInfoService;
import com.bzs.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by alwaysacc on 2019/04/12.
 */
@Service
@Transactional
public class InsuranceTypeInfoServiceImpl extends AbstractService<InsuranceTypeInfo> implements InsuranceTypeInfoService {
    @Resource
    private InsuranceTypeInfoMapper insuranceTypeInfoMapper;

}
