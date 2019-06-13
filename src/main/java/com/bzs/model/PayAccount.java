package com.bzs.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pay_account")
public class PayAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "account_id")
    private String accountId;

    /**
     * 0支付宝 1微信 2银行卡
     */
    private Integer type;

    private String amount;

    private String name;

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
     * 获取0支付宝 1微信 2银行卡
     *
     * @return type - 0支付宝 1微信 2银行卡
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置0支付宝 1微信 2银行卡
     *
     * @param type 0支付宝 1微信 2银行卡
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * @param amount
     */
    public void setAmount(String amount) {
        this.amount = amount;
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