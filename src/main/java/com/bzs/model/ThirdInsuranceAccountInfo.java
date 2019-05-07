package com.bzs.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "third_insurance_account_info")
public class ThirdInsuranceAccountInfo {
    @Id
    @Column(name = "third_insurance_id")
    private String thirdInsuranceId;

    /**
     * 添加人id
     */
    @Column(name = "create_id")
    private String createId;

    /**
     * 账号
     */
    @Column(name = "account_name")
    private String accountName;

    /**
     * 密码
     */
    @Column(name = "account_pwd")
    private String accountPwd;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 保险公司vpn
     */
    private String vpn;

    /**
     * 账号id
     */
    @Column(name = "account_id")
    private String accountId;

    /**
     * 是否支付成功,0未支付1支付
     */
    @Column(name = "is_pay")
    private String isPay;

    /**
     * 账号类型默认0 1太保2平安4人保
     */
    @Column(name = "account_type")
    private String accountType;

    /**
     * 地址
     */
    private String ip;

    /**
     * 端口
     */
    private String port;

    /**
     * 状态,是否可用默认0待审核1启用2禁用3关闭(指不适用此账号,因为有可能存在多个同类型账号)
     */
    private String status;

    /**
     * 有效期起止日期
     */
    @Column(name = "enable_start_date")
    private String enableStartDate;

    /**
     * 有效期截止日期
     */
    @Column(name = "enable_end_date")
    private String enableEndDate;

    /**
     * @return third_insurance_id
     */
    public String getThirdInsuranceId() {
        return thirdInsuranceId;
    }

    /**
     * @param thirdInsuranceId
     */
    public void setThirdInsuranceId(String thirdInsuranceId) {
        this.thirdInsuranceId = thirdInsuranceId;
    }

    /**
     * 获取添加人id
     *
     * @return create_id - 添加人id
     */
    public String getCreateId() {
        return createId;
    }

    /**
     * 设置添加人id
     *
     * @param createId 添加人id
     */
    public void setCreateId(String createId) {
        this.createId = createId;
    }

    /**
     * 获取账号
     *
     * @return account_name - 账号
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * 设置账号
     *
     * @param accountName 账号
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * 获取密码
     *
     * @return account_pwd - 密码
     */
    public String getAccountPwd() {
        return accountPwd;
    }

    /**
     * 设置密码
     *
     * @param accountPwd 密码
     */
    public void setAccountPwd(String accountPwd) {
        this.accountPwd = accountPwd;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改时间
     *
     * @return update_time - 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取保险公司vpn
     *
     * @return vpn - 保险公司vpn
     */
    public String getVpn() {
        return vpn;
    }

    /**
     * 设置保险公司vpn
     *
     * @param vpn 保险公司vpn
     */
    public void setVpn(String vpn) {
        this.vpn = vpn;
    }

    /**
     * 获取账号id
     *
     * @return account_id - 账号id
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * 设置账号id
     *
     * @param accountId 账号id
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * 获取是否支付成功,0未支付1支付
     *
     * @return is_pay - 是否支付成功,0未支付1支付
     */
    public String getIsPay() {
        return isPay;
    }

    /**
     * 设置是否支付成功,0未支付1支付
     *
     * @param isPay 是否支付成功,0未支付1支付
     */
    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }

    /**
     * 获取账号类型默认0 1太保2平安4人保
     *
     * @return account_type - 账号类型默认0 1太保2平安4人保
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * 设置账号类型默认0 1太保2平安4人保
     *
     * @param accountType 账号类型默认0 1太保2平安4人保
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    /**
     * 获取地址
     *
     * @return ip - 地址
     */
    public String getIp() {
        return ip;
    }

    /**
     * 设置地址
     *
     * @param ip 地址
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 获取端口
     *
     * @return port - 端口
     */
    public String getPort() {
        return port;
    }

    /**
     * 设置端口
     *
     * @param port 端口
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * 获取状态,是否可用默认0待审核1启用2禁用3关闭(指不适用此账号,因为有可能存在多个同类型账号)
     *
     * @return status - 状态,是否可用默认0待审核1启用2禁用3关闭(指不适用此账号,因为有可能存在多个同类型账号)
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态,是否可用默认0待审核1启用2禁用3关闭(指不适用此账号,因为有可能存在多个同类型账号)
     *
     * @param status 状态,是否可用默认0待审核1启用2禁用3关闭(指不适用此账号,因为有可能存在多个同类型账号)
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取有效期起止日期
     *
     * @return enable_start_date - 有效期起止日期
     */
    public String getEnableStartDate() {
        return enableStartDate;
    }

    /**
     * 设置有效期起止日期
     *
     * @param enableStartDate 有效期起止日期
     */
    public void setEnableStartDate(String enableStartDate) {
        this.enableStartDate = enableStartDate;
    }

    /**
     * 获取有效期截止日期
     *
     * @return enable_end_date - 有效期截止日期
     */
    public String getEnableEndDate() {
        return enableEndDate;
    }

    /**
     * 设置有效期截止日期
     *
     * @param enableEndDate 有效期截止日期
     */
    public void setEnableEndDate(String enableEndDate) {
        this.enableEndDate = enableEndDate;
    }
}