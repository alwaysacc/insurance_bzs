package com.bzs.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "order_info")
public class OrderInfo {
    /**
     * 订单号
     */
    @Id
    @Column(name = "order_id")
    private String orderId;

    /**
     * 订单生成日期
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 订单完成日期
     */
    @Column(name = "finish_time")
    private Date finishTime;

    /**
     * 创建者id
     */
    @Column(name = "account_id")
    private String accountId;

    /**
     * 支付状态0待支付,1完成2取消3退款4支付超时5支付失败
     */
    @Column(name = "pay_status")
    private Integer payStatus;

    /**
     * 支付用途：1购买账号,2保单
     */
    @Column(name = "pay_type")
    private String payType;

    /**
     * 支付用途1=账号id,2=报价id
     */
    @Column(name = "pay_type_id")
    private String payTypeId;

    /**
     * 支付方式1支付宝2微信3pos
     */
    private String payment;

    /**
     * 支付金额
     */
    @Column(name = "pay_money")
    private BigDecimal payMoney;

    @Column(name = "car_info_id")
    private String carInfoId;

    @Column(name = "delivery_way")
    private Integer deliveryWay;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "contact_tel")
    private Integer contactTel;

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
     * 获取订单生成日期
     *
     * @return create_time - 订单生成日期
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置订单生成日期
     *
     * @param createTime 订单生成日期
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取订单完成日期
     *
     * @return finish_time - 订单完成日期
     */
    public Date getFinishTime() {
        return finishTime;
    }

    /**
     * 设置订单完成日期
     *
     * @param finishTime 订单完成日期
     */
    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    /**
     * 获取创建者id
     *
     * @return account_id - 创建者id
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * 设置创建者id
     *
     * @param accountId 创建者id
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * 获取支付状态0待支付,1完成2取消3退款4支付超时5支付失败
     *
     * @return pay_status - 支付状态0待支付,1完成2取消3退款4支付超时5支付失败
     */
    public Integer getPayStatus() {
        return payStatus;
    }

    /**
     * 设置支付状态0待支付,1完成2取消3退款4支付超时5支付失败
     *
     * @param payStatus 支付状态0待支付,1完成2取消3退款4支付超时5支付失败
     */
    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    /**
     * 获取支付用途：1购买账号,2保单
     *
     * @return pay_type - 支付用途：1购买账号,2保单
     */
    public String getPayType() {
        return payType;
    }

    /**
     * 设置支付用途：1购买账号,2保单
     *
     * @param payType 支付用途：1购买账号,2保单
     */
    public void setPayType(String payType) {
        this.payType = payType;
    }

    /**
     * 获取支付方式1支付宝2微信3pos
     *
     * @return payment - 支付方式1支付宝2微信3pos
     */
    public String getPayment() {
        return payment;
    }

    /**
     * 设置支付方式1支付宝2微信3pos
     *
     * @param payment 支付方式1支付宝2微信3pos
     */
    public void setPayment(String payment) {
        this.payment = payment;
    }

    /**
     * 获取支付金额
     *
     * @return pay_money - 支付金额
     */
    public BigDecimal getPayMoney() {
        return payMoney;
    }

    /**
     * 设置支付金额
     *
     * @param payMoney 支付金额
     */
    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    /**
     * @return car_info_id
     */
    public String getCarInfoId() {
        return carInfoId;
    }

    /**
     * @param carInfoId
     */
    public void setCarInfoId(String carInfoId) {
        this.carInfoId = carInfoId;
    }

    /**
     * @return delivery_way
     */
    public Integer getDeliveryWay() {
        return deliveryWay;
    }

    /**
     * @param deliveryWay
     */
    public void setDeliveryWay(Integer deliveryWay) {
        this.deliveryWay = deliveryWay;
    }

    /**
     * @return delivery_address
     */
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    /**
     * @param deliveryAddress
     */
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    /**
     * @return contact_name
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * @param contactName
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * @return contact_tel
     */
    public Integer getContactTel() {
        return contactTel;
    }

    /**
     * @param contactTel
     */
    public void setContactTel(Integer contactTel) {
        this.contactTel = contactTel;
    }

    public String getPayTypeId() {
        return payTypeId;
    }

    public void setPayTypeId(String payTypeId) {
        this.payTypeId = payTypeId;
    }
}