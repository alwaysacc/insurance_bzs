package com.bzs.service;
import com.bzs.model.CarInfo;
import com.bzs.utils.Result;
import com.bzs.utils.Service;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * Created by alwaysacc on 2019/04/11.
 */
public interface CarInfoService extends Service<CarInfo> {

    List getUserList( String accountId, String roleId, String salesman, String customerStatus,String plan);

    List searchUserList(
            String accountId,String roleId,
            String carNumber,String frameNumber,
            String customerName,String customerTel,
            String lincenseOwner
    );
    List getRecoverUser(String accountId,String roleId);

    int recoverUser(String[] carInfoId,int status);

    Result getCarInfoIdInfo(String carNo, String vinNo, String operatorId);

    Map userDetail(String carInfoId);

}
