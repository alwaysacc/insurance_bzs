package com.bzs.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "commission_percentage")
public class CommissionPercentage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商业险佣金百分点
     */
    @Column(name = "biz_percentage")
    private String bizPercentage;

    /**
     * 交强险佣金百分点
     */
    @Column(name = "force_percentage")
    private String forcePercentage;

    /**
     * 一级提成百分点
     */
    @Column(name = "level_one")
    private String levelOne;

    /**
     * 二级提成百分点
     */
    @Column(name = "level_two")
    private String levelTwo;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

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
    /**
     * 保司枚举值
     */
    @Column(name = "source")
    private String source;

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
     * 获取商业险佣金百分点
     *
     * @return biz_percentage - 商业险佣金百分点
     */
    public String getBizPercentage() {
        return bizPercentage;
    }

    /**
     * 设置商业险佣金百分点
     *
     * @param bizPercentage 商业险佣金百分点
     */
    public void setBizPercentage(String bizPercentage) {
        this.bizPercentage = bizPercentage;
    }

    /**
     * 获取交强险佣金百分点
     *
     * @return force_percentage - 交强险佣金百分点
     */
    public String getForcePercentage() {
        return forcePercentage;
    }

    /**
     * 设置交强险佣金百分点
     *
     * @param forcePercentage 交强险佣金百分点
     */
    public void setForcePercentage(String forcePercentage) {
        this.forcePercentage = forcePercentage;
    }

    /**
     * 获取一级提成百分点
     *
     * @return level_one - 一级提成百分点
     */
    public String getLevelOne() {
        return levelOne;
    }

    /**
     * 设置一级提成百分点
     *
     * @param levelOne 一级提成百分点
     */
    public void setLevelOne(String levelOne) {
        this.levelOne = levelOne;
    }

    /**
     * 获取二级提成百分点
     *
     * @return level_two - 二级提成百分点
     */
    public String getLevelTwo() {
        return levelTwo;
    }

    /**
     * 设置二级提成百分点
     *
     * @param levelTwo 二级提成百分点
     */
    public void setLevelTwo(String levelTwo) {
        this.levelTwo = levelTwo;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}