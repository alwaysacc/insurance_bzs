package com.bzs.utils.bihujsontobean;

/**
 * @program: insurance_bzs
 * @description:
 * @author: dengl
 * @create: 2019-06-10 13:55
 */
public class CreditDetailInfoItems {
    private String endcaseTime;
    private String lossTime;
    private double payAmount;
    private String payCompanyName;
    private int payType;

    public String getEndcaseTime() {
        return endcaseTime;
    }

    public void setEndcaseTime(String endcaseTime) {
        this.endcaseTime = endcaseTime;
    }

    public String getLossTime() {
        return lossTime;
    }

    public void setLossTime(String lossTime) {
        this.lossTime = lossTime;
    }

    public double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(double payAmount) {
        this.payAmount = payAmount;
    }

    public String getPayCompanyName() {
        return payCompanyName;
    }

    public void setPayCompanyName(String payCompanyName) {
        this.payCompanyName = payCompanyName;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }
}
