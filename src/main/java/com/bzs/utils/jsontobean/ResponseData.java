package com.bzs.utils.jsontobean;

import java.util.List;

/**
 * @program: insurance_bzs
 * @description: 返回值中的data
 * @author: dengl
 * @create: 2019-04-16 10:39
 */
public class ResponseData {
    private String  payUrl ;
    private String refId;
    private String ciBeginDate;
    private String ciPremium;
    private String  proposalNo ;
    private String ciEcompensationRate;
    private String carshipTax;
    private String biBeginDate;
    private String biPremium;
    private String biPremiumByDis;
    private String advDiscountRate;
    private String realDiscountRate;
    private String biEcompensationRate;
    private List<InsurancesList> insurancesList;
    private List<CarList> carList;

    public ResponseData() {
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getCiBeginDate() {
        return ciBeginDate;
    }

    public void setCiBeginDate(String ciBeginDate) {
        this.ciBeginDate = ciBeginDate;
    }

    public String getCiPremium() {
        return ciPremium;
    }

    public void setCiPremium(String ciPremium) {
        this.ciPremium = ciPremium;
    }

    public String getProposalNo() {
        return proposalNo;
    }

    public void setProposalNo(String proposalNo) {
        this.proposalNo = proposalNo;
    }

    public String getCiEcompensationRate() {
        return ciEcompensationRate;
    }

    public void setCiEcompensationRate(String ciEcompensationRate) {
        this.ciEcompensationRate = ciEcompensationRate;
    }

    public String getCarshipTax() {
        return carshipTax;
    }

    public void setCarshipTax(String carshipTax) {
        this.carshipTax = carshipTax;
    }

    public String getBiBeginDate() {
        return biBeginDate;
    }

    public void setBiBeginDate(String biBeginDate) {
        this.biBeginDate = biBeginDate;
    }

    public String getBiPremium() {
        return biPremium;
    }

    public void setBiPremium(String biPremium) {
        this.biPremium = biPremium;
    }

    public String getBiPremiumByDis() {
        return biPremiumByDis;
    }

    public void setBiPremiumByDis(String biPremiumByDis) {
        this.biPremiumByDis = biPremiumByDis;
    }

    public String getAdvDiscountRate() {
        return advDiscountRate;
    }

    public void setAdvDiscountRate(String advDiscountRate) {
        this.advDiscountRate = advDiscountRate;
    }

    public String getRealDiscountRate() {
        return realDiscountRate;
    }

    public void setRealDiscountRate(String realDiscountRate) {
        this.realDiscountRate = realDiscountRate;
    }

    public String getBiEcompensationRate() {
        return biEcompensationRate;
    }

    public void setBiEcompensationRate(String biEcompensationRate) {
        this.biEcompensationRate = biEcompensationRate;
    }

    public List<InsurancesList> getInsurancesList() {
        return insurancesList;
    }

    public void setInsurancesList(List<InsurancesList> insurancesList) {
        this.insurancesList = insurancesList;
    }

    public List<CarList> getCarList() {
        return carList;
    }

    public void setCarList(List<CarList> carList) {
        this.carList = carList;
    }
}
