package com.bzs.dao;

import com.bzs.model.Verification;
import com.bzs.utils.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface VerificationMapper extends Mapper<Verification> {
    List getVerificationList(String  accountId);
    List getListByAdmin();
    int updateVerificationStatus(@Param("id")String[] id, @Param("status")String status,
                                 @Param("userName") String userName,@Param("verificationTime") Timestamp verificationTime
                                 );
    int updateVerification(Verification verification);
}