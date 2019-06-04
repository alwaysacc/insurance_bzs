package com.bzs.service;

import com.bzs.model.InsuredInfo;
import com.bzs.utils.Result;
import com.bzs.utils.Service;
import com.bzs.utils.jsontobean.RenewalBean;
import org.apache.poi.hssf.record.formula.functions.T;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


/**
 * Created by denglei on 2019/04/10 17:09:11.
 */
public interface InsuredInfoService extends Service<InsuredInfo> {
    /**
     * 通过车牌或者车架续保
     *
     * @param checkType      续保方式0车牌1车架
     * @param carNo          车牌号
     * @param idCard         证件号
     * @param vinNo          车架号
     * @param engineNo       引擎号
     * @param lastYearSource 上年投保的保险公司枚举值1太保2平安4人保
     * @param insuredArea    投保区域
     * @return
     */
    public Result checkByCarNoOrVinNo(String checkType, String carNo, String idCard, String vinNo, String engineNo, Long lastYearSource, String insuredArea, String createdBy);

    /**
     * 废弃 在使用 checkByCarNoOrVinNo
     * @param checkType
     * @param carNo
     * @param idCard
     * @param vinNo
     * @param engineNo
     * @param lastYearSource
     * @param insuredArea
     * @param createdBy
     * @return
     */
    public Result checkByCarNoOrVinNo2(String checkType, String carNo, String idCard, String vinNo, String engineNo, Long lastYearSource, String insuredArea, String createdBy,String carInfoId);
    public Result checkByCarNoOrVinNo3(String checkType, String carNo, String idCard, String vinNo, String engineNo, Long lastYearSource, String insuredArea, String createdBy);

    /**
     * 插入或者 更新
     * @param insuredInfo
     * @return
     */
    public int insertOrUpdate(InsuredInfo insuredInfo);

    Result WX_checkByCarNoOrVinNo(String checkType, String carNo, String idCard, String vinNo, String engineNo, Long lastYearSource, String insuredArea,String createBy);
}
