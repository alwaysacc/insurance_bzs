package com.bzs.utils.jsontobean;

/**
 * @program: insurance_bzs
 * @description: 报价并获取支付信息
 * @author: dengl
 * @create: 2019-04-16 15:36
 */
public class PayInfo {
    private String  payTime;
    private String payUrl;

    public PayInfo() {
    }

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
}
