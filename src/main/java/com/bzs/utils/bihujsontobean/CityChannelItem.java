package com.bzs.utils.bihujsontobean;

/**
 * @program: insurance_bzs
 * @description: 城市渠道续保期Item
 * @author: dengl
 * @create: 2019-06-10 10:03
 */
public class CityChannelItem {
    private int CityCode;
    private int ForceDays;
    private int BizDays;
    public int getCityCode() {
        return CityCode;
    }

    public void setCityCode(int cityCode) {
        CityCode = cityCode;
    }

    public int getForceDays() {
        return ForceDays;
    }

    public void setForceDays(int forceDays) {
        ForceDays = forceDays;
    }

    public int getBizDays() {
        return BizDays;
    }

    public void setBizDays(int bizDays) {
        BizDays = bizDays;
    }
}
