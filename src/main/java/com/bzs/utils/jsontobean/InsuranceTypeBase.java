package com.bzs.utils.jsontobean;

/**
 * @program: insurance_bzs
 * @description: 投保险种的基础项
 * @author: dengl
 * @create: 2019-04-11 16:47
 */
public class InsuranceTypeBase {
    private String insuranceName;
    private String amount;
    private String bujimianpei;
    private String insuredPremium;

    public InsuranceTypeBase() {
    }

    public String getInsuranceName() {
        return insuranceName;
    }

    public void setInsuranceName(String insuranceName) {
        this.insuranceName = insuranceName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBujimianpei() {
        return bujimianpei;
    }

    public void setBujimianpei(String bujimianpei) {
        this.bujimianpei = bujimianpei;
    }

    public String getInsuredPremium() {
        return insuredPremium;
    }

    public void setInsuredPremium(String insuredPremium) {
        this.insuredPremium = insuredPremium;
    }
}
