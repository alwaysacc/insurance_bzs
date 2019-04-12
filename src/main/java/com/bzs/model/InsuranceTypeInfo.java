package com.bzs.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "insurance_type_info")
public class InsuranceTypeInfo {
    /**
     * id
     */
    @Id
    @Column(name = "insuiance_type_id")
    private String insuianceTypeId;

    /**
     * 乐观锁
     */
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
     * 承保险种名称
     */
    @Column(name = "insurance_name")
    private String insuranceName;

    /**
     * 保额
     */
    @Column(name = "insurance_amount")
    private BigDecimal insuranceAmount;

    /**
     * 保费
     */
    @Column(name = "insurance_premium")
    private BigDecimal insurancePremium;

    /**
     * 类型 0投保1报价（0）
     */
    @Column(name = "info_type")
    private String infoType;

    /**
     * 类型id info_type=0表示投保id，info_type=1表示报价id
     */
    @Column(name = "type_id")
    private String typeId;

    @Column(name = "excluding_deductible")
    private BigDecimal excludingEeductible;

    /**
     * 获取id
     *
     * @return insuiance_type_id - id
     */
    public String getInsuianceTypeId() {
        return insuianceTypeId;
    }

    /**
     * 设置id
     *
     * @param insuianceTypeId id
     */
    public void setInsuianceTypeId(String insuianceTypeId) {
        this.insuianceTypeId = insuianceTypeId;
    }

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
     * 获取承保险种名称
     *
     * @return insurance_name - 承保险种名称
     */
    public String getInsuranceName() {
        return insuranceName;
    }

    /**
     * 设置承保险种名称
     *
     * @param insuranceName 承保险种名称
     */
    public void setInsuranceName(String insuranceName) {
        this.insuranceName = insuranceName;
    }

    /**
     * 获取保额
     *
     * @return insurance_amount - 保额
     */
    public BigDecimal getInsuranceAmount() {
        return insuranceAmount;
    }

    /**
     * 设置保额
     *
     * @param insuranceAmount 保额
     */
    public void setInsuranceAmount(BigDecimal insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }

    /**
     * 获取保费
     *
     * @return insurance_premium - 保费
     */
    public BigDecimal getInsurancePremium() {
        return insurancePremium;
    }

    /**
     * 设置保费
     *
     * @param insurancePremium 保费
     */
    public void setInsurancePremium(BigDecimal insurancePremium) {
        this.insurancePremium = insurancePremium;
    }

    /**
     * 获取类型 0投保1报价（0）
     *
     * @return info_type - 类型 0投保1报价（0）
     */
    public String getInfoType() {
        return infoType;
    }

    /**
     * 设置类型 0投保1报价（0）
     *
     * @param infoType 类型 0投保1报价（0）
     */
    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    /**
     * 获取类型id info_type=0表示投保id，info_type=1表示报价id
     *
     * @return type_id - 类型id info_type=0表示投保id，info_type=1表示报价id
     */
    public String getTypeId() {
        return typeId;
    }

    /**
     * 设置类型id info_type=0表示投保id，info_type=1表示报价id
     *
     * @param typeId 类型id info_type=0表示投保id，info_type=1表示报价id
     */
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    /**
     * 不计免
     * @return
     */
    public BigDecimal getExcludingEeductible() {
        return excludingEeductible;
    }

    /**
     * 不计免
     * @param excludingEeductible
     */
    public void setExcludingEeductible(BigDecimal excludingEeductible) {
        this.excludingEeductible = excludingEeductible;
    }
}