package com.bzs.utils.jsontobean;

/**
 * @program: insurance_bzs
 * @description: 报价参数
 * @author: dengl
 * @create: 2019-04-15 16:20
 */
public class InsurancesList {
    private String insuranceCode;
    private String insuranceName;
    private String insuredAmount;
    private String flag;
    public void setInsuranceCode(String insuranceCode) {
        this.insuranceCode = insuranceCode;
    }
    public String getInsuranceCode() {
        return insuranceCode;
    }

    public void setInsuranceName(String insuranceName) {
        this.insuranceName = insuranceName;
    }
    public String getInsuranceName() {
        return insuranceName;
    }

    public void setInsuredAmount(String insuredAmount) {
        this.insuredAmount = insuredAmount;
    }
    public String getInsuredAmount() {
        return insuredAmount;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
    public String getFlag() {
        return flag;
    }
}
