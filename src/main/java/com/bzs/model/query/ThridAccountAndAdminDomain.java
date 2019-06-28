package com.bzs.model.query;

/**
 * @program: insurance_bzs
 * @description:
 * @author: dengl
 * @create: 2019-06-24 16:25
 */
public class ThridAccountAndAdminDomain {
    private String accountName;//爬取的账号
    private String accountPwd;//爬取的密码
    private String adminId;//当前账号的id
    private String thirdInsuranceId; //爬取账号的id
    private String name;//当前账号的的用户名
    private String loginName;//当前账号的登录名
    private String roleName;//当前账号的角色名称//

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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
