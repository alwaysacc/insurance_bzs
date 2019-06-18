package com.bzs.service;
import com.bzs.model.Verification;
import com.bzs.utils.Service;


/**
 * Created by dl on 2019/06/14.
 */
public interface VerificationService extends Service<Verification> {
    int updateVerification(Verification verification);
}
