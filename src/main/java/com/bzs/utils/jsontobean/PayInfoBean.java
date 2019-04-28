package com.bzs.utils.jsontobean;

import lombok.Data;

/**
 * @program: insurance_bzs
 * @description: 支付bean
 * @author: dengl
 * @create: 2019-04-17 17:53
 */
@Data
public class PayInfoBean {
    private String state;
    private PayInfoData data;
    private String sendTime;
    private String retMsg;
    private String retCode;
    private String payNo;
    private String checkNo;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public PayInfoData getData() {
        return data;
    }

    public void setData(PayInfoData data) {
        this.data = data;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
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
