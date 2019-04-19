package com.bzs.utils.jsontobean;

/**
 * @program: insurance_bzs
 * @description: 支付bean
 * @author: dengl
 * @create: 2019-04-17 17:53
 */
public class PayInfoBean {
    private String state;
    private PayInfoData data;
    private String sendTime;
    private String retMsg;
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
}
