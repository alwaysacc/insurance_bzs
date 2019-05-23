package com.bzs.model;

import java.util.Date;
import javax.persistence.*;

public class Customer {
    /**
     * ID
     */
    @Id
    @Column(name = "customer_Id")
    private String customerId;

    /**
     * 客户名称
     */
    @Column(name = "customer_name")
    private String customerName;

    /**
     * 客户电话1
     */
    @Column(name = "customer_tel1")
    private Integer customerTel1;

    /**
     * 客户电话2
     */
    @Column(name = "customer_tel2")
    private Integer customerTel2;

    /**
     * 客户类别
     */
    @Column(name = "customer_sort")
    private String customerSort;

    /**
     * 投保地区
     */
    @Column(name = "insured_area")
    private String insuredArea;

    /**
     * 地址
     */
    @Column(name = "customer_address")
    private String customerAddress;

    /**
     * 客户备注1
     */
    @Column(name = "customer_remarks1")
    private String customerRemarks1;

    /**
     * 客户备注2
     */
    @Column(name = "customer_remarks2")
    private String customerRemarks2;

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
     * 状态 0正常1删除...
     */
    private Integer status;

    /**
     * 获取ID
     *
     * @return customer_Id - ID
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * 设置ID
     *
     * @param customerId ID
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * 获取客户名称
     *
     * @return customer_name - 客户名称
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * 设置客户名称
     *
     * @param customerName 客户名称
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * 获取客户电话1
     *
     * @return customer_tel1 - 客户电话1
     */
    public Integer getCustomerTel1() {
        return customerTel1;
    }

    /**
     * 设置客户电话1
     *
     * @param customerTel1 客户电话1
     */
    public void setCustomerTel1(Integer customerTel1) {
        this.customerTel1 = customerTel1;
    }

    /**
     * 获取客户电话2
     *
     * @return customer_tel2 - 客户电话2
     */
    public Integer getCustomerTel2() {
        return customerTel2;
    }

    /**
     * 设置客户电话2
     *
     * @param customerTel2 客户电话2
     */
    public void setCustomerTel2(Integer customerTel2) {
        this.customerTel2 = customerTel2;
    }

    /**
     * 获取客户类别
     *
     * @return customer_sort - 客户类别
     */
    public String getCustomerSort() {
        return customerSort;
    }

    /**
     * 设置客户类别
     *
     * @param customerSort 客户类别
     */
    public void setCustomerSort(String customerSort) {
        this.customerSort = customerSort;
    }

    /**
     * 获取投保地区
     *
     * @return insured_area - 投保地区
     */
    public String getInsuredArea() {
        return insuredArea;
    }

    /**
     * 设置投保地区
     *
     * @param insuredArea 投保地区
     */
    public void setInsuredArea(String insuredArea) {
        this.insuredArea = insuredArea;
    }

    /**
     * 获取地址
     *
     * @return customer_address - 地址
     */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     * 设置地址
     *
     * @param customerAddress 地址
     */
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    /**
     * 获取客户备注1
     *
     * @return customer_remarks1 - 客户备注1
     */
    public String getCustomerRemarks1() {
        return customerRemarks1;
    }

    /**
     * 设置客户备注1
     *
     * @param customerRemarks1 客户备注1
     */
    public void setCustomerRemarks1(String customerRemarks1) {
        this.customerRemarks1 = customerRemarks1;
    }

    /**
     * 获取客户备注2
     *
     * @return customer_remarks2 - 客户备注2
     */
    public String getCustomerRemarks2() {
        return customerRemarks2;
    }

    /**
     * 设置客户备注2
     *
     * @param customerRemarks2 客户备注2
     */
    public void setCustomerRemarks2(String customerRemarks2) {
        this.customerRemarks2 = customerRemarks2;
    }

    /**
     * 获取车辆信息ID
     *
     * @return car_info_id - 车辆信息ID
     */

    /**
     * 设置车辆信息ID
     *
     * @param carInfoId 车辆信息ID
     */

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
     * 获取状态 0正常1删除...
     *
     * @return status - 状态 0正常1删除...
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态 0正常1删除...
     *
     * @param status 状态 0正常1删除...
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}