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
    private String paymentNotice;
    private String serialNo;
    private  String payMsg;
    private String payEndDate;
    private String payNo;
    private String checkNo;
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public PayInfo getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(PayInfo payInfo) {
        this.payInfo = payInfo;
    }

    public String getPaymentNotice() {
        return paymentNotice;
    }

    public void setPaymentNotice(String paymentNotice) {
        this.paymentNotice = paymentNotice;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getPayMsg() {
        return payMsg;
    }

    public void setPayMsg(String payMsg) {
        this.payMsg = payMsg;
    }

    public String getPayEndDate() {
        return payEndDate;
    }

    public void setPayEndDate(String payEndDate) {
        this.payEndDate = payEndDate;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }
}
