package com.bzs.service;
import com.bzs.model.CarInfo;
import com.bzs.utils.Service;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * Created by alwaysacc on 2019/04/11.
 */
public interface CarInfoService extends Service<CarInfo> {

    List getUserList(String accountId,String roleId,String salesman,String customerStatus);

    List searchUserList(
            @Param("accountId") String accountId, @Param("roleId") String roleId,
            @Param("carNumber") String carNumber, @Param("frameNumber") String frameNumber,
            @Param("customerName") String customerName, @Param("customerTel") String customerTel
    );

    int recoverUser(String[] carInfoId);
}
