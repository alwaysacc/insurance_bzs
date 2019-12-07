package com.bzs.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 通知人id
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 通知类型
     */
    private Integer type;

    /**
     * 状态 0未读 1已读
     */
    private Integer status;

    /**
     * 创建人
     */
    @Column(name = "create_user")
    private String createUser;

    /**
     * 更新人
     */
    @Column(name = "update_user")
    private String updateUser;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取通知标题
     *
     * @return title - 通知标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置通知标题
     *
     * @param title 通知标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取通知内容
     *
     * @return content - 通知内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置通知内容
     *
     * @param content 通知内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取通知人id
     *
     * @return user_id - 通知人id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置通知人id
     *
     * @param userId 通知人id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取通知类型
     *
     * @return type - 通知类型
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置通知类型
     *
     * @param type 通知类型
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取状态 0未读 1已读
     *
     * @return status - 状态 0未读 1已读
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态 0未读 1已读
     *
     * @param status 状态 0未读 1已读
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取创建人
     *
     * @return create_user - 创建人
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * 设置创建人
     *
     * @param createUser 创建人
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    /**
     * 获取更新人
     *
     * @return update_user - 更新人
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * 设置更新人
     *
     * @param updateUser 更新人
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
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
}