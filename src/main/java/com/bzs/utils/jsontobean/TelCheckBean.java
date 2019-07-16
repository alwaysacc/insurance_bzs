package com.bzs.utils.jsontobean;

import lombok.Data;

@Data
public class TelCheckBean {
    private String realname;
    private String mobile;
    private String idcard;
    private int res;
    private String resmsg;
    private String orderid;
    private String type;
    private String province;
    private String city;
    private String rescode;
}
