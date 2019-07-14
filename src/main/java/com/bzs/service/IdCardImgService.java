package com.bzs.service;
import com.bzs.model.IdCardImg;
import com.bzs.utils.Service;

import java.util.HashMap;
import java.util.List;


/**
 * Created by dl on 2019/07/14.
 */
public interface IdCardImgService extends Service<IdCardImg> {
    int saveIdCardImg(IdCardImg idCardImg);
    List getWaitCheckList(int verifiedStat);

    HashMap getVerifiedStatById(String accountId);
}
