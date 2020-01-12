package com.bzs.service.impl;

import com.bzs.dao.PartnerInfoMapper;
import com.bzs.model.PartnerInfo;
import com.bzs.service.PartnerInfoService;
import com.bzs.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by alwaysacc on 2020/01/04.
 */
@Service
@Transactional
public class PartnerInfoServiceImpl extends AbstractService<PartnerInfo> implements PartnerInfoService {
    @Resource
    private PartnerInfoMapper partnerInfoMapper;

}
