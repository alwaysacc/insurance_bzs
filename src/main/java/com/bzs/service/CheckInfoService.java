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

    /**
     * 通过创建人、车辆信息id查询
     * @param createBy 创建人
     * @param carInfoId 车辆id
     * @return
     */
    Map checkByCreateByOrCarInfoId(String createBy, String carInfoId);

    Map checkByDifferConditions(CheckInfo checkInfo);

    Map getListByDifferConditions(CheckInfo checkInfo);

}
