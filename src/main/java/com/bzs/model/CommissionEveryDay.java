package com.bzs.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "commission_every_day")
public class CommissionEveryDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 保司商业佣金
     */
    @Column(name = "insurance_biz_percentage")
    private String insuranceBizPercentage;

    /**
     * 保司交强佣金
     */
    @Column(name = "insurance_force_percentage")
    private String insuranceForcePercentage;

    /**
     * 商业险佣金百分点
     */
    @Column(name = "biz_percentage")
    private String bizPercentage;

    /**
     * 商业险补贴
     */
    private String subsidy;

    /**
     * 交强险佣金百分点
     */
    @Column(name = "force_percentage")
    private String forcePercentage;

    /**
     * 下一级提成百分点
     */
    @Column(name = "level_one")
    private String levelOne;

    /**
     * 下二级提成百分点
     */
    @Column(name = "level_two")
    private String levelTwo;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 保司枚举值
     */
    private String source;

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
     * 获取保司商业佣金
     *
     * @return insurance_biz_percentage - 保司商业佣金
     */
    public String getInsuranceBizPercentage() {
        return insuranceBizPercentage;
    }

    /**
     * 设置保司商业佣金
     *
     * @param insuranceBizPercentage 保司商业佣金
     */
    public void setInsuranceBizPercentage(String insuranceBizPercentage) {
        this.insuranceBizPercentage = insuranceBizPercentage;
    }

    /**
     * 获取保司交强佣金
     *
     * @return insurance_force_percentage - 保司交强佣金
     */
    public String getInsuranceForcePercentage() {
        return insuranceForcePercentage;
    }

    /**
     * 设置保司交强佣金
     *
     * @param insuranceForcePercentage 保司交强佣金
     */
    public void setInsuranceForcePercentage(String insuranceForcePercentage) {
        this.insuranceForcePercentage = insuranceForcePercentage;
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
     * 获取商业险补贴
     *
     * @return subsidy - 商业险补贴
     */
    public String getSubsidy() {
        return subsidy;
    }

    /**
     * 设置商业险补贴
     *
     * @param subsidy 商业险补贴
     */
    public void setSubsidy(String subsidy) {
        this.subsidy = subsidy;
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
     * 获取下一级提成百分点
     *
     * @return level_one - 下一级提成百分点
     */
    public String getLevelOne() {
        return levelOne;
    }

    /**
     * 设置下一级提成百分点
     *
     * @param levelOne 下一级提成百分点
     */
    public void setLevelOne(String levelOne) {
        this.levelOne = levelOne;
    }

    /**
     * 获取下二级提成百分点
     *
     * @return level_two - 下二级提成百分点
     */
    public String getLevelTwo() {
        return levelTwo;
    }

    /**
     * 设置下二级提成百分点
     *
     * @param levelTwo 下二级提成百分点
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
     * 获取保司枚举值
     *
     * @return source - 保司枚举值
     */
    public String getSource() {
        return source;
    }

    /**
     * 设置保司枚举值
     *
     * @param source 保司枚举值
     */
    public void setSource(String source) {
        this.source = source;
    }
}