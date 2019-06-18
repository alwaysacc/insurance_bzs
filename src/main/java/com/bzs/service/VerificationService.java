package com.bzs.service;
import com.bzs.model.Verification;
import com.bzs.utils.Service;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;


/**
 * Created by dl on 2019/06/14.
 */
public interface VerificationService extends Service<Verification> {
    List getVerificationList(String  accountId);

    int updateVerificationStatus(String[] id,String status, String userName);
}
