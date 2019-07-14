package com.bzs.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "id_card_img")
public class IdCardImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "back_path")
    private String backPath;

    @Column(name = "front_path")
    private String frontPath;

    private Integer stat;

    private String msg;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

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
     * @return back_path
     */
    public String getBackPath() {
        return backPath;
    }

    /**
     * @param backPath
     */
    public void setBackPath(String backPath) {
        this.backPath = backPath;
    }

    /**
     * @return front_path
     */
    public String getFrontPath() {
        return frontPath;
    }

    /**
     * @param frontPath
     */
    public void setFrontPath(String frontPath) {
        this.frontPath = frontPath;
    }

    /**
     * @return stat
     */
    public Integer getStat() {
        return stat;
    }

    /**
     * @param stat
     */
    public void setStat(Integer stat) {
        this.stat = stat;
    }

    /**
     * @return msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @return account_id
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * @param accountId
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
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