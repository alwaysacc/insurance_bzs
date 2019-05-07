package com.bzs.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "third_insurance_account_date_info")
public class ThirdInsuranceAccountDateInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 创建人
     */
    @Column(name = "create_by")
    private String createBy;

    /**
     * 创建日期
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 开始时间
     */
    @Column(name = "start_time")
    private String startTime;

    /**
     * 结束时间
     */
    @Column(name = "end_time")
    private String endTime;

    /**
     * 有效期年
     */
    @Column(name = "enable_years")
    private Integer enableYears;

    /**
     * 有效期月
     */
    @Column(name = "enable_months")
    private Integer enableMonths;

    /**
     * 有效期天
     */
    @Column(name = "enable_days")
    private Integer enableDays;

    /**
     * 父账号账号id
     */
    @Column(name = "account_id")
    private String accountId;

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
     * 获取创建日期
     *
     * @return create_time - 创建日期
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建日期
     *
     * @param createTime 创建日期
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
     * 获取开始时间
     *
     * @return start_time - 开始时间
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * 设置开始时间
     *
     * @param startTime 开始时间
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取结束时间
     *
     * @return end_time - 结束时间
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * 设置结束时间
     *
     * @param endTime 结束时间
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取有效期年
     *
     * @return enable_years - 有效期年
     */
    public Integer getEnableYears() {
        return enableYears;
    }

    /**
     * 设置有效期年
     *
     * @param enableYears 有效期年
     */
    public void setEnableYears(Integer enableYears) {
        this.enableYears = enableYears;
    }

    /**
     * 获取有效期月
     *
     * @return enable_months - 有效期月
     */
    public Integer getEnableMonths() {
        return enableMonths;
    }

    /**
     * 设置有效期月
     *
     * @param enableMonths 有效期月
     */
    public void setEnableMonths(Integer enableMonths) {
        this.enableMonths = enableMonths;
    }

    /**
     * 获取有效期天
     *
     * @return enable_days - 有效期天
     */
    public Integer getEnableDays() {
        return enableDays;
    }

    /**
     * 设置有效期天
     *
     * @param enableDays 有效期天
     */
    public void setEnableDays(Integer enableDays) {
        this.enableDays = enableDays;
    }

    /**
     * 获取父账号账号id
     *
     * @return account_id - 父账号账号id
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * 设置父账号账号id
     *
     * @param accountId 父账号账号id
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}