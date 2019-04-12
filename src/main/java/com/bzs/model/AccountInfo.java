package com.bzs.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "account_info")
public class AccountInfo {
    /**
     * id
     */
    @Id
    @Column(name = "account_id")
    private String accountId;

    /**
     * 乐观锁
     */
    @Column(name = "REVISION")
    private String revision;

    /**
     * 创建人
     */
    @Column(name = "CREATED_BY")
    private String createdBy;

    /**
     * 创建时间
     */
    @Column(name = "CREATED_TIME")
    private Date createdTime;

    /**
     * 更新人
     */
    @Column(name = "UPDATED_BY")
    private String updatedBy;

    /**
     * 更新时间
     */
    @Column(name = "UPDATED_TIME")
    private Date updatedTime;

    /**
     * 父级id
     */
    @Column(name = "parent_id")
    private String parentId;

    /**
     * 账号所属区域
     */
    @Column(name = "area_code")
    private String areaCode;

    /**
     * 账号状态 0启用1禁用
     */
    @Column(name = "account_state")
    private String accountState;

    /**
     * 账号登陆名
     */
    @Column(name = "login_name")
    private String loginName;

    /**
     * 账号密码
     */
    private String pwd;

    /**
     * 真实姓名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 身份证号
     */
    @Column(name = "id_card")
    private String idCard;

    /**
     * 微信号
     */
    private String wechat;

    /**
     * 邮箱号
     */
    private String email;

    @Column(name = "role_id")
    private String roleId;

    @Column(name = "ancestor_id")
    private String ancestorId;

    /**
     * 获取id
     *
     * @return account_id - id
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * 设置id
     *
     * @param accountId id
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * 获取乐观锁
     *
     * @return REVISION - 乐观锁
     */
    public String getRevision() {
        return revision;
    }

    /**
     * 设置乐观锁
     *
     * @param revision 乐观锁
     */
    public void setRevision(String revision) {
        this.revision = revision;
    }

    /**
     * 获取创建人
     *
     * @return CREATED_BY - 创建人
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * 设置创建人
     *
     * @param createdBy 创建人
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * 获取创建时间
     *
     * @return CREATED_TIME - 创建时间
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * 设置创建时间
     *
     * @param createdTime 创建时间
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * 获取更新人
     *
     * @return UPDATED_BY - 更新人
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * 设置更新人
     *
     * @param updatedBy 更新人
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * 获取更新时间
     *
     * @return UPDATED_TIME - 更新时间
     */
    public Date getUpdatedTime() {
        return updatedTime;
    }

    /**
     * 设置更新时间
     *
     * @param updatedTime 更新时间
     */
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    /**
     * 获取父级id
     *
     * @return parent_id - 父级id
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * 设置父级id
     *
     * @param parentId 父级id
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取账号所属区域
     *
     * @return area_code - 账号所属区域
     */
    public String getAreaCode() {
        return areaCode;
    }

    /**
     * 设置账号所属区域
     *
     * @param areaCode 账号所属区域
     */
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    /**
     * 获取账号状态 0启用1禁用
     *
     * @return account_state - 账号状态 0启用1禁用
     */
    public String getAccountState() {
        return accountState;
    }

    /**
     * 设置账号状态 0启用1禁用
     *
     * @param accountState 账号状态 0启用1禁用
     */
    public void setAccountState(String accountState) {
        this.accountState = accountState;
    }

    /**
     * 获取账号登陆名
     *
     * @return login_name - 账号登陆名
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * 设置账号登陆名
     *
     * @param loginName 账号登陆名
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /**
     * 获取账号密码
     *
     * @return pwd - 账号密码
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * 设置账号密码
     *
     * @param pwd 账号密码
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * 获取真实姓名
     *
     * @return user_name - 真实姓名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置真实姓名
     *
     * @param userName 真实姓名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取手机号
     *
     * @return mobile - 手机号
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机号
     *
     * @param mobile 手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取身份证号
     *
     * @return id_card - 身份证号
     */
    public String getIdCard() {
        return idCard;
    }

    /**
     * 设置身份证号
     *
     * @param idCard 身份证号
     */
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    /**
     * 获取微信号
     *
     * @return wechat - 微信号
     */
    public String getWechat() {
        return wechat;
    }

    /**
     * 设置微信号
     *
     * @param wechat 微信号
     */
    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    /**
     * 获取邮箱号
     *
     * @return email - 邮箱号
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱号
     *
     * @param email 邮箱号
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return role_id
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * @param roleId
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    /**
     * @return ancestor_id
     */
    public String getAncestorId() {
        return ancestorId;
    }

    /**
     * @param ancestorId
     */
    public void setAncestorId(String ancestorId) {
        this.ancestorId = ancestorId;
    }
}