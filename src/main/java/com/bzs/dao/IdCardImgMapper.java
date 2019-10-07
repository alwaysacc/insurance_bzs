package com.bzs.dao;

import com.bzs.model.IdCardImg;
import com.bzs.utils.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface IdCardImgMapper extends Mapper<IdCardImg> {

    int saveIdCardImg(IdCardImg idCardImg);

    List getWaitCheckList(@Param("verifiedStat")int verifiedStat);

    HashMap getVerifiedStatById(@Param("accountId")String accountId);
}