package com.bzs.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "commission_percentage")
public class CommissionPercentage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 账号
     */
    @Column(name = "account_id")
    private String accountId;

    /**
     * 佣金分成比例,百分点
     */
    private String percentage;

    /**
     * 等级1,2,3
     */
    private Integer level;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新日期
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 创建人
     */
    @Column(name = "create_by")
    private String createBy;

    /**
     * 更新人
     */
    @Column(name = "update_by")
    private String updateBy;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取账号
     *
     * @return account_id - 账号
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * 设置账号
     *
     * @param accountId 账号
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * 获取佣金分成比例,百分点
     *
     * @return percentage - 佣金分成比例,百分点
     */
    public String getPercentage() {
        return percentage;
    }

    /**
     * 设置佣金分成比例,百分点
     *
     * @param percentage 佣金分成比例,百分点
     */
    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    /**
     * 获取等级1,2,3
     *
     * @return level - 等级1,2,3
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * 设置等级1,2,3
     *
     * @param level 等级1,2,3
     */
    public void setLevel(Integer level) {
        this.level = level;
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
     * 获取更新日期
     *
     * @return update_time - 更新日期
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新日期
     *
     * @param updateTime 更新日期
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取创建人
     *
     * @return create_by - 创建人
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置创建人
     *
     * @param createBy 创建人
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取更新人
     *
     * @return update_by - 更新人
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置更新人
     *
     * @param updateBy 更新人
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public CommissionPercentage() {
    }

    public CommissionPercentage(String accountId, String percentage, Integer level, String createBy) {
        this.accountId = accountId;
        this.percentage = percentage;
        this.level = level;
        this.createBy = createBy;
    }
}