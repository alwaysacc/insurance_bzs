package com.bzs.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "insurance_follow_info")
public class InsuranceFollowInfo {
    /**
     * 乐观锁
     */
    @Id
    @Column(name = "REVISION")
    private Integer revision;

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
     * id
     */
    @Column(name = "insurance_follow_id")
    private String insuranceFollowId;

    /**
     * 跟进人名称
     */
    @Column(name = "created_name")
    private String createdName;

    /**
     * 跟进的报价信息id
     */
    @Column(name = "quote_id")
    private String quoteId;

    /**
     * 备注信息
     */
    @Column(name = "remark_info")
    private String remarkInfo;

    /**
     * 跟进保司枚举值
     */
    private String source;

    /**
     * 车辆信息id
     */
    @Column(name = "car_info_id")
    private String carInfoId;

    /**
     * 获取乐观锁
     *
     * @return REVISION - 乐观锁
     */
    public Integer getRevision() {
        return revision;
    }

    /**
     * 设置乐观锁
     *
     * @param revision 乐观锁
     */
    public void setRevision(Integer revision) {
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
     * 获取id
     *
     * @return insurance_follow_id - id
     */
    public String getInsuranceFollowId() {
        return insuranceFollowId;
    }

    /**
     * 设置id
     *
     * @param insuranceFollowId id
     */
    public void setInsuranceFollowId(String insuranceFollowId) {
        this.insuranceFollowId = insuranceFollowId;
    }

    /**
     * 获取跟进人名称
     *
     * @return created_name - 跟进人名称
     */
    public String getCreatedName() {
        return createdName;
    }

    /**
     * 设置跟进人名称
     *
     * @param createdName 跟进人名称
     */
    public void setCreatedName(String createdName) {
        this.createdName = createdName;
    }

    /**
     * 获取跟进的报价信息id
     *
     * @return quote_id - 跟进的报价信息id
     */
    public String getQuoteId() {
        return quoteId;
    }

    /**
     * 设置跟进的报价信息id
     *
     * @param quoteId 跟进的报价信息id
     */
    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }

    /**
     * 获取备注信息
     *
     * @return remark_info - 备注信息
     */
    public String getRemarkInfo() {
        return remarkInfo;
    }

    /**
     * 设置备注信息
     *
     * @param remarkInfo 备注信息
     */
    public void setRemarkInfo(String remarkInfo) {
        this.remarkInfo = remarkInfo;
    }

    /**
     * 获取跟进保司枚举值
     *
     * @return source - 跟进保司枚举值
     */
    public String getSource() {
        return source;
    }

    /**
     * 设置跟进保司枚举值
     *
     * @param source 跟进保司枚举值
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * 获取车辆信息id
     *
     * @return car_info_id - 车辆信息id
     */
    public String getCarInfoId() {
        return carInfoId;
    }

    /**
     * 设置车辆信息id
     *
     * @param carInfoId 车辆信息id
     */
    public void setCarInfoId(String carInfoId) {
        this.carInfoId = carInfoId;
    }
}