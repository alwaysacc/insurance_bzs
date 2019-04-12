package com.bzs.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "check_info")
public class CheckInfo {
    @Id
    @Column(name = "check_info_id")
    private String checkInfoId;

    /**
     * 操作人id
     */
    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_time")
    private Date createTime;

    /**
     * 操作车辆id
     */
    @Column(name = "car_info_id")
    private String carInfoId;

    /**
     * 是否首次查询:默认0 不是 1是
     */
    @Column(name = "is_first_time")
    private String isFirstTime;

    /**
     * 查询方式0车牌查询1车架查询
     */
    @Column(name = "check_type")
    private String checkType;
    @Column(name = "send_time")
    private String sendTime;

    /**
     * @return check_info_id
     */
    public String getCheckInfoId() {
        return checkInfoId;
    }

    /**
     * @param checkInfoId
     */
    public void setCheckInfoId(String checkInfoId) {
        this.checkInfoId = checkInfoId;
    }

    /**
     * 获取操作人id
     *
     * @return create_by - 操作人id
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置操作人id
     *
     * @param createBy 操作人id
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取操作车辆id
     *
     * @return car_info_id - 操作车辆id
     */
    public String getCarInfoId() {
        return carInfoId;
    }

    /**
     * 设置操作车辆id
     *
     * @param carInfoId 操作车辆id
     */
    public void setCarInfoId(String carInfoId) {
        this.carInfoId = carInfoId;
    }

    /**
     * 获取是否首次查询:默认0 不是 1是
     *
     * @return is_first_time - 是否首次查询:默认0 不是 1是
     */
    public String getIsFirstTime() {
        return isFirstTime;
    }

    /**
     * 设置是否首次查询:默认0 不是 1是
     *
     * @param isFirstTime 是否首次查询:默认0 不是 1是
     */
    public void setIsFirstTime(String isFirstTime) {
        this.isFirstTime = isFirstTime;
    }

    /**
     * 获取查询方式0车牌查询1车架查询
     *
     * @return check_type - 查询方式0车牌查询1车架查询
     */
    public String getCheckType() {
        return checkType;
    }

    /**
     * 设置查询方式0车牌查询1车架查询
     *
     * @param checkType 查询方式0车牌查询1车架查询
     */
    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public CheckInfo() {
    }

    public CheckInfo(String checkInfoId) {
        this.checkInfoId = checkInfoId;
    }
}