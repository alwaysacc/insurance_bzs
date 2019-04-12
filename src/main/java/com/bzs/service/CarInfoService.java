package com.bzs.service;
import com.bzs.model.CarInfo;
import com.bzs.utils.Result;
import com.bzs.utils.Service;

import java.util.List;


/**
 * Created by alwaysacc on 2019/04/11.
 */
public interface CarInfoService extends Service<CarInfo> {

    List getUserList(String accountId,String roleId);

    Result getCarInfoIdInfo(String carNo, String vinNo, String operatorId);
}
