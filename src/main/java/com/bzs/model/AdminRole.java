package com.bzs.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "admin_role")
public class AdminRole {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 名称
     */
    private String name;

    /**
     * 角色编码
     */
    private String code;

    /**
     * 备注
     */
    private String remark;

    @Column(name = "data_scope")
    private String dataScope;

    private Integer level;

    /**
     * 是否可以删除，默认0可删除1不可删除
     */
    @Column(name = "is_enable_del")
    private Integer isEnableDel;

    /**
     * 获取ID
     *
     * @return id - ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(Long id) {
        this.id = id;
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
     * 获取名称
     *
     * @return name - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取角色编码
     *
     * @return code - 角色编码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置角色编码
     *
     * @param code 角色编码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return data_scope
     */
    public String getDataScope() {
        return dataScope;
    }

    /**
     * @param dataScope
     */
    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }

    /**
     * @return level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * @param level
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * 获取是否可以删除，默认0可删除1不可删除
     *
     * @return is_enable_del - 是否可以删除，默认0可删除1不可删除
     */
    public Integer getIsEnableDel() {
        return isEnableDel;
    }

    /**
     * 设置是否可以删除，默认0可删除1不可删除
     *
     * @param isEnableDel 是否可以删除，默认0可删除1不可删除
     */
    public void setIsEnableDel(Integer isEnableDel) {
        this.isEnableDel = isEnableDel;
    }
}