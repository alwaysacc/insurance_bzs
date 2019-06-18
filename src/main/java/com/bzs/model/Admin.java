package com.bzs.model;

import java.util.Date;
import javax.persistence.*;

public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "login_name")
    private String loginName;

    @Column(name = "login_pwd")
    private String loginPwd;

    private String mobile;

    /**
     * 性别 0男 1女 2保密
     */
    private String sex;

    /**
     * 描述
     */
    private String descripition;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "last_login_time")
    private Date lastLoginTime;

    /**
     * 状态 0锁定 1有效
     */
    private String status;

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
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return login_name
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * @param loginName
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /**
     * @return login_pwd
     */
    public String getLoginPwd() {
        return loginPwd;
    }

    /**
     * @param loginPwd
     */
    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    /**
     * @return mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取性别 0男 1女 2保密
     *
     * @return sex - 性别 0男 1女 2保密
     */
    public String getSex() {
        return sex;
    }

    /**
     * 设置性别 0男 1女 2保密
     *
     * @param sex 性别 0男 1女 2保密
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * 获取描述
     *
     * @return descripition - 描述
     */
    public String getDescripition() {
        return descripition;
    }

    /**
     * 设置描述
     *
     * @param descripition 描述
     */
    public void setDescripition(String descripition) {
        this.descripition = descripition;
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
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return last_login_time
     */
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * @param lastLoginTime
     */
    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    /**
     * 获取状态 0锁定 1有效
     *
     * @return status - 状态 0锁定 1有效
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态 0锁定 1有效
     *
     * @param status 状态 0锁定 1有效
     */
    public void setStatus(String status) {
        this.status = status;
    }
}