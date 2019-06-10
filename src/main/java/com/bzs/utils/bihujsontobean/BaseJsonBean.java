package com.bzs.utils.bihujsontobean;

/**
 * @program: insurance_bzs
 * @description: 基础的JsonRootBean
 * @author: dengl
 * @create: 2019-06-10 10:00
 */
public class BaseJsonBean {
    private String CustKey;
    private int BusinessStatus;
    private String StatusMessage;

    public String getCustKey() {
        return CustKey;
    }

    public void setCustKey(String custKey) {
        CustKey = custKey;
    }

    public int getBusinessStatus() {
        return BusinessStatus;
    }

    public void setBusinessStatus(int businessStatus) {
        BusinessStatus = businessStatus;
    }

    public String getStatusMessage() {
        return StatusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        StatusMessage = statusMessage;
    }
}
