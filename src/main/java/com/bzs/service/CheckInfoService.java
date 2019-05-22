package com.bzs.service;
import com.bzs.model.CheckInfo;
import com.bzs.utils.Service;

import java.util.Map;


/**
 * Created by alwaysacc on 2019/04/12.
 */
public interface CheckInfoService extends Service<CheckInfo> {
    /**
     * 修改或者添加
     * @param checkInfo
     * @return
     */
    Map updateOrAdd(CheckInfo checkInfo);
}
