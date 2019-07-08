package com.bzs.service.impl;

import com.bzs.dao.CardInfoMapper;
import com.bzs.model.CardInfo;
import com.bzs.service.CardInfoService;
import com.bzs.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by dl on 2019/07/05.
 */
@Service
@Transactional
public class CardInfoServiceImpl extends AbstractService<CardInfo> implements CardInfoService {
    @Resource
    private CardInfoMapper cardInfoMapper;

}
