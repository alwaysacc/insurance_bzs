package com.bzs.service;
import com.bzs.model.FollowInfo;
import com.bzs.utils.Service;

import java.util.List;


/**
 * Created by dl on 2019/07/15.
 */
public interface FollowInfoService extends Service<FollowInfo> {

    List getFollowInfoByCarInfoId(String carInfoId);
}
