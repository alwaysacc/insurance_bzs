package com.bzs.utils.jsontobean;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @program: insurance_bzs
 * @description: 续保返回的实体对象
 * @author: dengl
 * @create: 2019-04-11 15:56
 */
@Data
public class RenewalBean {
    private String state;
    private RenewalData data;
    private String sendTime;
    private String message;
    private String retMsg;//人保出险 不在续保期内
    private  String retCode;//人保出险 不在续保期内
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
