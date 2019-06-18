package com.bzs.service.impl;

import com.bzs.dao.VerificationMapper;
import com.bzs.model.Verification;
import com.bzs.service.VerificationService;
import com.bzs.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by dl on 2019/06/14.
 */
@Service
@Transactional
public class VerificationServiceImpl extends AbstractService<Verification> implements VerificationService {
    @Resource
    private VerificationMapper verificationMapper;

    @Override
    public int updateVerification(Verification verification) {
        return verificationMapper.updateVerification(verification);
    }
}
