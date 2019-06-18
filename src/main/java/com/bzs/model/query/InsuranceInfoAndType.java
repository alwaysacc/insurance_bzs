package com.bzs.model.query;

import com.bzs.model.InsuranceTypeInfo;
import com.bzs.model.InsuredInfo;

import javax.persistence.Transient;
import java.util.List;

/**
 * @program: insurance_bzs
 * @description: 续保和险种
 * @author: dengl
 * @create: 2019-06-17 14:32
 */
public class InsuranceInfoAndType extends InsuredInfo {
    @Transient
    private List<InsuranceTypeInfo> insuranceTypeInfos;

    public List<InsuranceTypeInfo> getInsuranceTypeInfos() {
        return insuranceTypeInfos;
    }

    public void setInsuranceTypeInfos(List<InsuranceTypeInfo> insuranceTypeInfos) {
        this.insuranceTypeInfos = insuranceTypeInfos;
    }
}
