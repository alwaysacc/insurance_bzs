package com.bzs.service.impl;

import com.bzs.dao.HeadlinesMapper;
import com.bzs.model.Headlines;
import com.bzs.service.HeadlinesService;
import com.bzs.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by alwaysacc on 2020/01/31.
 */
@Service
@Transactional
public class HeadlinesServiceImpl extends AbstractService<Headlines> implements HeadlinesService {
    @Resource
    private HeadlinesMapper headlinesMapper;

}
