package com.bzs.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "follow_info")
public class FollowInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 跟进状态
     */
    @Column(name = "follow_stat")
    private String followStat;

    /**
     * 下次跟进时间
     */
    @Column(name = "next_follow_date")
    private Date nextFollowDate;

    /**
     * 跟进内容
     */
    @Column(name = "follow_content")
    private String followContent;

    @Column(name = "car_info_id")
    private String carInfoId;

    @Column(name = "create_time")
    private Date createTime;

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
     * 获取跟进状态
     *
     * @return follow_stat - 跟进状态
     */
    public String getFollowStat() {
        return followStat;
    }

    /**
     * 设置跟进状态
     *
     * @param followStat 跟进状态
     */
    public void setFollowStat(String followStat) {
        this.followStat = followStat;
    }

    /**
     * 获取下次跟进时间
     *
     * @return next_follow_date - 下次跟进时间
     */
    public Date getNextFollowDate() {
        return nextFollowDate;
    }

    /**
     * 设置下次跟进时间
     *
     * @param nextFollowDate 下次跟进时间
     */
    public void setNextFollowDate(Date nextFollowDate) {
        this.nextFollowDate = nextFollowDate;
    }

    /**
     * 获取跟进内容
     *
     * @return follow_content - 跟进内容
     */
    public String getFollowContent() {
        return followContent;
    }

    /**
     * 设置跟进内容
     *
     * @param followContent 跟进内容
     */
    public void setFollowContent(String followContent) {
        this.followContent = followContent;
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
}