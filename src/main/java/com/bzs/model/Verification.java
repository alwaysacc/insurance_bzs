package com.bzs.model;

import java.util.Date;
import javax.persistence.*;

public class Verification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 金额
     */
    private String money;

    /**
     * 审核状态0待审核1通过2驳回
     */
    private String status;

    /**
     * 审核状态0待审核1通过2驳回,
     */
    @Column(name = "pay_account_id")
    private Integer payAccountId;

    /**
     * 反馈信息
     */
    private String description;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 审核日期
     */
    @Column(name = "verification_time")
    private Date verificationTime;

    /**
     * 提交人
     */
    @Column(name = "create_by")
    private String createBy;

    /**
     * 审核人
     */
    @Column(name = "verification_by")
    private String verificationBy;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取金额
     *
     * @return money - 金额
     */
    public String getMoney() {
        return money;
    }

    /**
     * 设置金额
     *
     * @param money 金额
     */
    public void setMoney(String money) {
        this.money = money;
    }

    /**
     * 获取审核状态0待审核1通过2驳回
     *
     * @return status - 审核状态0待审核1通过2驳回
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置审核状态0待审核1通过2驳回
     *
     * @param status 审核状态0待审核1通过2驳回
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取审核状态0待审核1通过2驳回,
     *
     * @return pay_account_id - 审核状态0待审核1通过2驳回,
     */
    public Integer getPayAccountId() {
        return payAccountId;
    }

    /**
     * 设置审核状态0待审核1通过2驳回,
     *
     * @param payAccountId 审核状态0待审核1通过2驳回,
     */
    public void setPayAccountId(Integer payAccountId) {
        this.payAccountId = payAccountId;
    }

    /**
     * 获取反馈信息
     *
     * @return description - 反馈信息
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置反馈信息
     *
     * @param description 反馈信息
     */
    public void setDescription(String description) {
        this.description = description;
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
     * 获取审核日期
     *
     * @return verification_time - 审核日期
     */
    public Date getVerificationTime() {
        return verificationTime;
    }

    /**
     * 设置审核日期
     *
     * @param verificationTime 审核日期
     */
    public void setVerificationTime(Date verificationTime) {
        this.verificationTime = verificationTime;
    }

    /**
     * 获取提交人
     *
     * @return create_by - 提交人
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置提交人
     *
     * @param createBy 提交人
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取审核人
     *
     * @return verification_by - 审核人
     */
    public String getVerificationBy() {
        return verificationBy;
    }

    /**
     * 设置审核人
     *
     * @param verificationBy 审核人
     */
    public void setVerificationBy(String verificationBy) {
        this.verificationBy = verificationBy;
    }
}