package com.bzs.dao;

import com.bzs.model.CarInfo;
import com.bzs.model.query.CarInfoAndInsuranceInfo;
import com.bzs.utils.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CarInfoMapper extends Mapper<CarInfo> {
    List getUserList(@Param("accountId") String accountId, @Param("roleId") String roleId,
                     @Param("salesman") String salesman, @Param("customerStatus") String customerStatus,
                     @Param("plan") String plan, @Param("begDate") String begDate, @Param("endDate") String endDate,
                     @Param("orderByDate") int orderByDate);


    List searchUserList(
            @Param("accountId") String accountId, @Param("roleId") String roleId,
            @Param("carNumber") String carNumber, @Param("frameNumber") String frameNumber,
            @Param("customerName") String customerName, @Param("customerTel") String customerTel,
            @Param("lincenseOwner") String lincenseOwner
    );

    List findOneBy(CarInfo carInfo);

    int recoverUser(@Param("carInfoId") String[] carInfoId, @Param("status") int status);

    List getRecoverUser(@Param("accountId") String accountId, @Param("roleId") String roleId);

    List getUserList(@Param("accountId") String accountId, @Param("roleId") String roleId, String salesman, String customerStatus);


    /**
     * 根据车牌和车架更新车辆信息
     *
     * @param carInfo
     * @return
     */
    int updateRenewalInfoByCarNoAndVinNo(CarInfo carInfo);

    /**
     * 根据车牌或者车架获取车辆信息
     *
     * @param carNo
     * @param vinNo
     * @return
     */
    List<CarInfo> getCarInfoIdByCarNoOrVinNo(@Param("carNumber") String carNo, @Param("frameNumber") String vinNo);

    int insertOrUpdate(CarInfo carInfo);

    /**
     * 获取车辆信息和续保和续保险种信息
     *
     * @param carInfo
     * @return
     */
    List getCarInfoAndInsurance(CarInfo carInfo);

    /**
     * 查询车辆信息，报价List和续保List信息
     *
     * @param carInfoId
     * @param carNumber
     * @param createdBy
     * @param frameNumber
     * @param isEnable
     * @param isRenewSuccess
     * @param startTime
     * @param endTime
     * @return
     */
    List getCarInfoAndInsurance(@Param("carInfoId") String carInfoId, @Param("carNumber") String carNumber,
                                @Param("createdBy") String createdBy, @Param("frameNumber") String frameNumber,
                                @Param("isEnable") String isEnable, @Param("isRenewSuccess") String isRenewSuccess,
                                @Param("startTime") String startTime, @Param("endTime") String endTime
    );

    List getCarInfoAndQuoteList(@Param("carInfoId") String carInfoId, @Param("carNumber") String carNumber,
                                @Param("createdBy") String createdBy, @Param("frameNumber") String frameNumber,
                                @Param("isEnable") String isEnable, @Param("isRenewSuccess") String isRenewSuccess,
                                @Param("startTime") String startTime, @Param("endTime") String endTime
    );

    /**
     * 批量修改 isEnable
     *
     * @param carInfoIds
     * @param isEnable
     * @return
     */
    int updateBatchIsEnable(@Param("carInfoIds") List<String> carInfoIds, @Param("isEnable") String isEnable);

    String getCarCountByAccountId(@Param("carNumber") String carNumber,
                                  @Param("accountId") String accountId
    );
}