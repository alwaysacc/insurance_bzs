package com.bzs.model.query;

import com.bzs.model.CarInfo;
import com.bzs.model.InsuranceTypeInfo;
import com.bzs.model.InsuredInfo;

import javax.persistence.Transient;
import java.util.List;

/**
 * @program: insurance_bzs
 * @description: 车辆信息和续保信息
 * @author: dengl
 * @create: 2019-05-16 16:44
 */
public class CarInfoAndInsuranceInfo extends CarInfo {
    @Transient
    private InsuredInfo insuredInfo;

    public InsuredInfo getInsuredInfo() {
        return insuredInfo;
    }

    public void setInsuredInfo(InsuredInfo insuredInfo) {
        this.insuredInfo = insuredInfo;
    }
    @Transient
    private List<InsuranceTypeInfo> insuranceTypeInfos;

    public List<InsuranceTypeInfo> getInsuranceTypeInfos() {
        return insuranceTypeInfos;
    }

    public void setInsuranceTypeInfos(List<InsuranceTypeInfo> insuranceTypeInfos) {
        this.insuranceTypeInfos = insuranceTypeInfos;
    }
}
