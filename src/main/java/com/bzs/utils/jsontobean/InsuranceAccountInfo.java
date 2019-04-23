package com.bzs.utils.jsontobean;

/**
 * @program: insurance_bzs
 * @description: 报价入参账号信息--仅平安使用
 * @author: dengl
 * @create: 2019-04-23 14:19
 */
public class InsuranceAccountInfo {
    private String account;
    private String password;
    public InsuranceAccountInfo() {
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public InsuranceAccountInfo(String account, String password) {
        this.account = account;
        this.password = password;
    }
}
