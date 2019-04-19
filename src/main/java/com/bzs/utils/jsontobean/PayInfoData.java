package com.bzs.utils.jsontobean;

/**
 * @program: insurance_bzs
 * @description: 支付bean中的data
 * @author: dengl
 * @create: 2019-04-17 17:55
 */
public class PayInfoData {
    private String payTime;
    private String payUrl;
    private PayInfo payInfo;
    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public PayInfo getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(PayInfo payInfo) {
        this.payInfo = payInfo;
    }
}
