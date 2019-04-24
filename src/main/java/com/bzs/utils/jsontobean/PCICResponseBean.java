package com.bzs.utils.jsontobean;

/**
 * @program: insurance_bzs
 * @description: 报价返回值 太保
 * @author: dengl
 * @create: 2019-04-16 10:37
 */
public class PCICResponseBean {
    private String state;
    private String  retMsg ;
    private String  retCode ;
    private String sendTime;
    private  ResponseData  data;

    public PCICResponseBean() {
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public ResponseData getData() {
        return data;
    }

    public void setData(ResponseData data) {
        this.data = data;
    }
}
