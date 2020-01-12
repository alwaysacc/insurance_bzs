package com.bzs.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "account_info")
public class AccountInfo implements Serializable {
    public static final String STATUS_VALID = "0";

    public static final String STATUS_LOCK = "1";
    private static final long serialVersionUID = -4852732617765810959L;
    /**
     * id
     */
    @Id
    @Column(name = "account_id")
    private String accountId;

    @Column(name = "role_id")
    private String roleId;
    /**
     * 用于记录角色ids
     */
    @Transient
    private String roleIds;

    @Column(name = "role_name")
    private String roleName;

    /**
     * 父级id
     */
    @Column(name = "parent_id")
    private String parentId;

    /**
     * 祖id
     */
    @Column(name = "ancestor_id")
    private String ancestorId;

    /**
     * 账号状态 0启用1禁用2待审核
     */
    @Column(name = "account_state")
    private int accountState;

    public String getSuperior() {
        return superior;
    }

    public void setSuperior(String superior) {
        this.superior = superior;
    }

    @Column(name = "superior")
    private String superior;

    /**
     * 账号登陆名
     */
    @Column(name = "login_name")
    private String loginName;

    /**
     * 账号密码
     */
    @Column(name = "login_pwd")
    private String loginPwd;

    /**
     * 真实姓名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 账号所属区域
     */
    @Column(name = "area_code")
    private String areaCode;

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

    /**
     * 是否删除0默认1删除
     */
    @Column(name = "delete_status")
    private Byte deleteStatus;

    /**
     * 更新人
     */
    @Column(name = "UPDATED_BY")
    private String updatedBy;

    /**
     * 创建人
     */
    @Column(name = "CREATED_BY")
    private String createdBy;

    /**
     * 乐观锁
     */
    @Column(name = "login_time")
    private Date loginTime;

    /**
     * 创建时间
     */
    @Column(name = "CREATED_TIME")
    private Date createdTime;

    /**
     * 更新时间
     */
    @Column(name = "UPDATED_TIME")
    private Date updatedTime;

    /**
     * 邀请码
     */
    @Column(name = "inviteCode")
    private Integer invitecode;

    /**
     * 上级邀请码
     */
    @Column(name = "superiorInviteCode")
    private Integer superiorinvitecode;
    /**
     * 关联的验证码
     */

    @Column(name = "association_level")
    private String associationLevel;
    @Column(name = "invite_code_level")
    private Integer inviteCodeLevel;
    /**
     * 余额
     */
    @Column(name = "balance_total")
    private BigDecimal balanceTotal;
    /**
     * 佣金总额
     */
    @Column(name = "commission_total")
    private BigDecimal commissionTotal;
    /**
     * 提成总额
     */
    @Column(name = "draw_percentage_total")
    private BigDecimal drawPercentageTotal;
    @Column(name = "verified_stat")
    private Integer verifiedStat;
    @Column(name = "openId")
    private String openId;
    @Column(name="isFeedBack")
    private Integer isFeedBack;

    public Integer getIsFeedBack() {
        return isFeedBack;
    }

    public void setIsFeedBack(Integer isFeedBack) {
        this.isFeedBack = isFeedBack;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getVerifiedStat() {
        return verifiedStat;
    }

    public void setVerifiedStat(Integer verifiedStat) {
        this.verifiedStat = verifiedStat;
    }


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
     * @return role_name
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * @param roleName
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
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
     * 获取祖id
     *
     * @return ancestor_id - 祖id
     */
    public String getAncestorId() {
        return ancestorId;
    }

    /**
     * 设置祖id
     *
     * @param ancestorId 祖id
     */
    public void setAncestorId(String ancestorId) {
        this.ancestorId = ancestorId;
    }

    /**
     * 获取账号状态 0启用1禁用2待审核
     *
     * @return account_state - 账号状态 0启用1禁用2待审核
     */
    public int getAccountState() {
        return accountState;
    }

    /**
     * 设置账号状态 0启用1禁用2待审核
     *
     * @param accountState 账号状态 0启用1禁用2待审核
     */
    public void setAccountState(int accountState) {
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
     * @return login_pwd - 账号密码
     */
    public String getLoginPwd() {
        return loginPwd;
    }

    /**
     * 设置账号密码
     *
     * @param loginPwd 账号密码
     */
    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
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
     * 获取是否删除0默认1删除
     *
     * @return delete_status - 是否删除0默认1删除
     */
    public Byte getDeleteStatus() {
        return deleteStatus;
    }

    /**
     * 设置是否删除0默认1删除
     *
     * @param deleteStatus 是否删除0默认1删除
     */
    public void setDeleteStatus(Byte deleteStatus) {
        this.deleteStatus = deleteStatus;
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
     * 获取乐观锁
     *
     * @return login_time - 乐观锁
     */
    public Date getLoginTime() {
        return loginTime;
    }

    /**
     * 设置乐观锁
     *
     * @param loginTime 乐观锁
     */
    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
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
     * 获取邀请码
     *
     * @return inviteCode - 邀请码
     */
    public Integer getInvitecode() {
        return invitecode;
    }

    /**
     * 设置邀请码
     *
     * @param invitecode 邀请码
     */
    public void setInvitecode(Integer invitecode) {
        this.invitecode = invitecode;
    }

    /**
     * 获取上级邀请码
     *
     * @return superiorInviteCode - 上级邀请码
     */
    public Integer getSuperiorinvitecode() {
        return superiorinvitecode;
    }

    /**
     * 设置上级邀请码
     *
     * @param superiorinvitecode 上级邀请码
     */
    public void setSuperiorinvitecode(Integer superiorinvitecode) {
        this.superiorinvitecode = superiorinvitecode;
    }

    public String getAssociationLevel() {
        return associationLevel;
    }

    public void setAssociationLevel(String associationLevel) {
        this.associationLevel = associationLevel;
    }

    public Integer getInviteCodeLevel() {
        return inviteCodeLevel;
    }

    public void setInviteCodeLevel(Integer inviteCodeLevel) {
        this.inviteCodeLevel = inviteCodeLevel;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public BigDecimal getBalanceTotal() {
        return balanceTotal;
    }

    public void setBalanceTotal(BigDecimal balanceTotal) {
        this.balanceTotal = balanceTotal;
    }

    public BigDecimal getCommissionTotal() {
        return commissionTotal;
    }

    public void setCommissionTotal(BigDecimal commissionTotal) {
        this.commissionTotal = commissionTotal;
    }

    public BigDecimal getDrawPercentageTotal() {
        return drawPercentageTotal;
    }

    public void setDrawPercentageTotal(BigDecimal drawPercentageTotal) {
        this.drawPercentageTotal = drawPercentageTotal;
    }
}