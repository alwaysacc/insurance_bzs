package com.bzs.service.impl;

import com.bzs.dao.CheckInfoMapper;
import com.bzs.model.CheckInfo;
import com.bzs.service.CheckInfoService;
import com.bzs.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by alwaysacc on 2019/04/12.
 */
@Service
@Transactional
public class CheckInfoServiceImpl extends AbstractService<CheckInfo> implements CheckInfoService {
    @Resource
    private CheckInfoMapper checkInfoMapper;

}
