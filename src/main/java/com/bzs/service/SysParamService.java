package com.bzs.service;
import com.bzs.model.SysParam;
import com.bzs.utils.Service;


/**
 * Created by alwaysacc on 2019/12/15.
 */
public interface SysParamService extends Service<SysParam> {
    int getShowToday();
    String getRole();
}
