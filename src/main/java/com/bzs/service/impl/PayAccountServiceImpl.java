package com.bzs.service.impl;

import com.bzs.dao.PayAccountMapper;
import com.bzs.model.PayAccount;
import com.bzs.service.PayAccountService;
import com.bzs.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by dl on 2019/06/13.
 */
@Service
@Transactional
public class PayAccountServiceImpl extends AbstractService<PayAccount> implements PayAccountService {
    @Resource
    private PayAccountMapper payAccountMapper;

}
