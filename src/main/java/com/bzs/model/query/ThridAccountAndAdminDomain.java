package com.bzs.model.query;

/**
 * @program: insurance_bzs
 * @description:
 * @author: dengl
 * @create: 2019-06-24 16:25
 */
public class ThridAccountAndAdminDomain {
    private String accountName;
    private String accountPwd;
    private String adminId;
    private String thirdInsuranceId;
    private String name;
    private String loginName;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountPwd() {
        return accountPwd;
    }

    public void setAccountPwd(String accountPwd) {
        this.accountPwd = accountPwd;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getThirdInsuranceId() {
        return thirdInsuranceId;
    }

    public void setThirdInsuranceId(String thirdInsuranceId) {
        this.thirdInsuranceId = thirdInsuranceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
