package com.bzs.service.impl;

import com.bzs.dao.InsuredInfoMapper;
import com.bzs.model.InsuredInfo;
import com.bzs.service.InsuredInfoService;
import com.bzs.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by alwaysacc on 2019/04/11.
 */
@Service
@Transactional
public class InsuredInfoServiceImpl extends AbstractService<InsuredInfo> implements InsuredInfoService {
    @Resource
    private InsuredInfoMapper insuredInfoMapper;

}
