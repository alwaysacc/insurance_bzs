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

    List getUserList( String accountId, String roleId, String salesman, String customerStatus);

    List searchUserList(
            @Param("accountId") String accountId, @Param("roleId") String roleId,
            @Param("carNumber") String carNumber, @Param("frameNumber") String frameNumber,
            @Param("customerName") String customerName, @Param("customerTel") String customerTel
    );

    int recoverUser(String[] carInfoId);

    Result getCarInfoIdInfo(String carNo, String vinNo, String operatorId);

    /**
     * 续保时查看同一车牌和车架号的是否已经续保
     * @param carNo
     * @param vinNo
     * @param operatorId
     * @return
     */

    Map<String,Object>getCarInfoIdByCarNoAndVinNo(String carNo,String vinNo,String operatorId);

    /**
     * 更新或者插入
     * @param carInfo
     * @return
     */
    Map<String,Object>insertOrUpdate(CarInfo carInfo);

}
