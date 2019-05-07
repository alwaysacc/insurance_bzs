package com.bzs.service.impl;

import com.bzs.dao.ThirdInsuranceAccountInfoMapper;
import com.bzs.model.ThirdInsuranceAccountInfo;
import com.bzs.service.ThirdInsuranceAccountInfoService;
import com.bzs.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by dl on 2019/05/06.
 */
@Service
@Transactional
public class ThirdInsuranceAccountInfoServiceImpl extends AbstractService<ThirdInsuranceAccountInfo> implements ThirdInsuranceAccountInfoService {
    @Resource
    private ThirdInsuranceAccountInfoMapper thirdInsuranceAccountInfoMapper;

}
