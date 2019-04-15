package com.bzs.utils.jsontobean;

import java.util.Date;

/**
 * @program: insurance_bzs
 * @description: 续保返回的实体对象
 * @author: dengl
 * @create: 2019-04-11 15:56
 */
public class RenewalBean {
    private String state;
    private RenewalData data;
    private String sendTime;
    private String message;
    public void setState(String state) {
        this.state = state;
    }
    public String getState() {
        return state;
    }


    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
    public String getSendTime() {
        return sendTime;
    }

    public RenewalData getData() {
        return data;
    }

    public void setData(RenewalData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
