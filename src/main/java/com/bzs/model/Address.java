package com.bzs.model;

import java.util.Date;
import javax.persistence.*;

public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "userId")
    private String userId;

    @Column(name = "userName")
    private String userName;

    @Column(name = "userPhone")
    private String userPhone;

    @Column(name = "areaIdPath")
    private String areaIdPath;

    @Column(name = "areaIdName")
    private String areaIdName;


    @Column(name = "userAddress")
    private String userAddress;
    /**
     * 是否默认地址 0否 1是
     */
    @Column(name = "isDefault")
    private Integer isDefault;

    @Column(name = "createTime")
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getAreaIdPath() {
        return areaIdPath;
    }

    public void setAreaIdPath(String areaIdPath) {
        this.areaIdPath = areaIdPath;
    }

    public String getAreaIdName() {
        return areaIdName;
    }

    public void setAreaIdName(String areaIdName) {
        this.areaIdName = areaIdName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}