package com.bzs.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "privilege_info")
public class PrivilegeInfo {
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
    @Column(name = "privilege_id")
    private String privilegeId;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限代码 0超级管理员1省管理员2市管理员4区管理员8部门管理员16团队管理员，使用2的次幂
     */
    private String code;

    /**
     * 描述
     */
    @Column(name = "p_des")
    private String pDes;

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
     * @return privilege_id - id
     */
    public String getPrivilegeId() {
        return privilegeId;
    }

    /**
     * 设置id
     *
     * @param privilegeId id
     */
    public void setPrivilegeId(String privilegeId) {
        this.privilegeId = privilegeId;
    }

    /**
     * 获取权限名称
     *
     * @return name - 权限名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置权限名称
     *
     * @param name 权限名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取权限代码 0超级管理员1省管理员2市管理员4区管理员8部门管理员16团队管理员，使用2的次幂
     *
     * @return code - 权限代码 0超级管理员1省管理员2市管理员4区管理员8部门管理员16团队管理员，使用2的次幂
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置权限代码 0超级管理员1省管理员2市管理员4区管理员8部门管理员16团队管理员，使用2的次幂
     *
     * @param code 权限代码 0超级管理员1省管理员2市管理员4区管理员8部门管理员16团队管理员，使用2的次幂
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取描述
     *
     * @return p_des - 描述
     */
    public String getpDes() {
        return pDes;
    }

    /**
     * 设置描述
     *
     * @param pDes 描述
     */
    public void setpDes(String pDes) {
        this.pDes = pDes;
    }
}