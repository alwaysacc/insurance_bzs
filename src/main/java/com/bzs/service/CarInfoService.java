package com.bzs.service;
import com.bzs.model.CarInfo;
import com.bzs.utils.Result;
import com.bzs.utils.Service;

import java.util.List;
import java.util.Map;


/**
 * Created by alwaysacc on 2019/04/11.
 */
public interface CarInfoService extends Service<CarInfo> {

    List getUserList( String accountId, String roleId, String salesman, String customerStatus,String plan,int selectType,int orderByDate);

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

    /**
     * 续保时查看同一车牌和车架号的是否已经续保
     * @param carNo
     * @param vinNo
     * @param operatorId
     * @return
     */

    Map<String,Object>getCarInfoIdByCarNoOrVinNo(String carNo,String vinNo,String operatorId);

    /**
     * 更新或者插入
     * @param carInfo
     * @return
     */
    Map<String,Object>insertOrUpdate(CarInfo carInfo);

    /**
     *
     * @param carInfoId 车辆信息id
     * @param createBy 创建人
     * @param carNo 车牌号
     * @param vinNo 车架号
     * @param isEnable 是否作废0可使用 1作废
     * @param isRenewSuccess 是否续保成功 0失败 1成功
     * @return
     */
    Map<String,Object>getCarInfoAndInsurance(String carInfoId,String createBy,String carNo,String vinNo,String isEnable,String  isRenewSuccess);

    /**
     * 批量修改 isEnable
     * @param ids
     * @param isEnable
     * @return
     */
    Map updateBatchIsEnable(List ids,String isEnable);

    Result WX_GetNewVehicleInfo(String LicenseNo,String EngineNo,String CarVin,int IsNeedCarVin,
                                String MoldName);

    List getCarInfoQuote(String carInfoId,String createBy,String carNo,String vinNo,
                         String isEnable,String  isRenewSuccess,String queryTime);

}
