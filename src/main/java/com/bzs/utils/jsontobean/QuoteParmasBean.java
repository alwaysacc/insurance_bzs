package com.bzs.utils.jsontobean;

/**
 * @program: insurance_bzs
 * @description: 报价参数bean
 * @author: dengl
 * @create: 2019-04-15 16:15
 */
public class QuoteParmasBean {
    private String sendTime;
    private String refId;
    private String pay;
    private String flag;
    private  ParamsData data;
    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public ParamsData getData() {
        return data;
    }

    public void setData(ParamsData data) {
        this.data = data;
    }
}
