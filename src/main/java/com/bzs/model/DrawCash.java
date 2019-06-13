package com.bzs.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "draw_cash")
public class DrawCash {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 订单号
     */
    @Column(name = "order_id")
    private String orderId;

    /**
     * 佣金分成比例,百分点
     */
    @Column(name = "commission_percentage_id")
    private Integer commissionPercentageId;

    /**
     * 默认0审核中,1通过2驳回3取消
     */
    private String status;

    /**
     * 流水批次号
     */
    @Column(name = "serial_no")
    private String serialNo;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 描述信息
     */
    private String descriton;

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
    private String type;
    private String cash;
    @Column(name = "force_cash")
    private String forceCash;
    @Column(name = "income_person")
    private String incomePerson;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getForceCash() {
        return forceCash;
    }

    public void setForceCash(String forceCash) {
        this.forceCash = forceCash;
    }

    public String getIncomePerson() {
        return incomePerson;
    }

    public void setIncomePerson(String incomePerson) {
        this.incomePerson = incomePerson;
    }

    public String getBizCash() {
        return bizCash;
    }

    public void setBizCash(String bizCash) {
        this.bizCash = bizCash;
    }

    @Column(name = "biz_cash")
    private String bizCash;


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
     * 获取订单号
     *
     * @return order_id - 订单号
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 设置订单号
     *
     * @param orderId 订单号
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取佣金分成比例,百分点
     *
     * @return commission_percentage_id - 佣金分成比例,百分点
     */
    public Integer getCommissionPercentageId() {
        return commissionPercentageId;
    }

    /**
     * 设置佣金分成比例,百分点
     *
     * @param commissionPercentageId 佣金分成比例,百分点
     */
    public void setCommissionPercentageId(Integer commissionPercentageId) {
        this.commissionPercentageId = commissionPercentageId;
    }

    /**
     * 获取默认0审核中,1通过2驳回3取消
     *
     * @return status - 默认0审核中,1通过2驳回3取消
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置默认0审核中,1通过2驳回3取消
     *
     * @param status 默认0审核中,1通过2驳回3取消
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取流水批次号
     *
     * @return serial_no - 流水批次号
     */
    public String getSerialNo() {
        return serialNo;
    }

    /**
     * 设置流水批次号
     *
     * @param serialNo 流水批次号
     */
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
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
     * 获取描述信息
     *
     * @return descriton - 描述信息
     */
    public String getDescriton() {
        return descriton;
    }

    /**
     * 设置描述信息
     *
     * @param descriton 描述信息
     */
    public void setDescriton(String descriton) {
        this.descriton = descriton;
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

    public DrawCash() {
    }

    public DrawCash(String orderId, Integer commissionPercentageId,String serialNo, String createBy) {
        this.orderId = orderId;
        this.commissionPercentageId = commissionPercentageId;
        this.serialNo = serialNo;
        this.createBy = createBy;
    }
}