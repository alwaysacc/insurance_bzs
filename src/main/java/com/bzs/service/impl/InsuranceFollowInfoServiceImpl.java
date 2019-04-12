package com.bzs.service.impl;

import com.bzs.dao.InsuranceFollowInfoMapper;
import com.bzs.model.InsuranceFollowInfo;
import com.bzs.service.InsuranceFollowInfoService;
import com.bzs.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by alwaysacc on 2019/04/11.
 */
@Service
@Transactional
public class InsuranceFollowInfoServiceImpl extends AbstractService<InsuranceFollowInfo> implements InsuranceFollowInfoService {
    @Resource
    private InsuranceFollowInfoMapper insuranceFollowInfoMapper;

}
