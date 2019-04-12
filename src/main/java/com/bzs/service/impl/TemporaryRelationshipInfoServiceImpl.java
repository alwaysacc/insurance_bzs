package com.bzs.service.impl;

import com.bzs.dao.TemporaryRelationshipInfoMapper;
import com.bzs.model.TemporaryRelationshipInfo;
import com.bzs.service.TemporaryRelationshipInfoService;
import com.bzs.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by alwaysacc on 2019/04/11.
 */
@Service
@Transactional
public class TemporaryRelationshipInfoServiceImpl extends AbstractService<TemporaryRelationshipInfo> implements TemporaryRelationshipInfoService {
    @Resource
    private TemporaryRelationshipInfoMapper temporaryRelationshipInfoMapper;

}
