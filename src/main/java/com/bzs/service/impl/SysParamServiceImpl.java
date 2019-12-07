package com.bzs.service.impl;

import com.bzs.dao.SysParamMapper;
import com.bzs.model.SysParam;
import com.bzs.service.SysParamService;
import com.bzs.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by alwaysacc on 2019/12/02.
 */
@Service
@Transactional
public class SysParamServiceImpl extends AbstractService<SysParam> implements SysParamService {
    @Resource
    private SysParamMapper sysParamMapper;

    @Override
    public int getShowToday() {
        return sysParamMapper.getShowToday();
    }

    @Override
    public String getRole() {
        return sysParamMapper.getRole();
    }
}
