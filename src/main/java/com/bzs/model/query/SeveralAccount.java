package com.bzs.model.query;

import javax.persistence.Column;
import java.math.BigDecimal;

/**
 * @program: insurance_bzs
 * @description: 三级账号信息
 * @author: dengl
 * @create: 2019-06-13 17:30
 */
public class SeveralAccount {
    private String userName;//自己的名字
    private String accountId;//自己的id
    private String bUserName;//父一级名字
    private String bAccountId;//父一级id
    private String cUserName;//父二级名字
    private String cAccountId;//父二级id

    private BigDecimal balanceTotal;//自己的余额
    /**
     * 佣金总额
     */
    private BigDecimal commissionTotal;//自己的佣金
    /**
     * 提成总额
     */
    private BigDecimal bBalanceTotal;//父一级的余额
    private BigDecimal bDrawPercentageTotal;//父一级的提成
    private BigDecimal cBalanceTotal;//父二级的余额
    private BigDecimal cDrawPercentageTotal;//父二级的提成
    private int level;
    private int level1;
    private int level2;



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getbUserName() {
        return bUserName;
    }

    public void setbUserName(String bUserName) {
        this.bUserName = bUserName;
    }

    public String getbAccountId() {
        return bAccountId;
    }

    public void setbAccountId(String bAccountId) {
        this.bAccountId = bAccountId;
    }

    public String getcUserName() {
        return cUserName;
    }

    public void setcUserName(String cUserName) {
        this.cUserName = cUserName;
    }

    public String getcAccountId() {
        return cAccountId;
    }

    public void setcAccountId(String cAccountId) {
        this.cAccountId = cAccountId;
    }

    public BigDecimal getBalanceTotal() {
        return balanceTotal;
    }

    public void setBalanceTotal(BigDecimal balanceTotal) {
        this.balanceTotal = balanceTotal;
    }

    public BigDecimal getCommissionTotal() {
        return commissionTotal;
    }

    public void setCommissionTotal(BigDecimal commissionTotal) {
        this.commissionTotal = commissionTotal;
    }

    public BigDecimal getbBalanceTotal() {
        return bBalanceTotal;
    }

    public void setbBalanceTotal(BigDecimal bBalanceTotal) {
        this.bBalanceTotal = bBalanceTotal;
    }

    public BigDecimal getbDrawPercentageTotal() {
        return bDrawPercentageTotal;
    }

    public void setbDrawPercentageTotal(BigDecimal bDrawPercentageTotal) {
        this.bDrawPercentageTotal = bDrawPercentageTotal;
    }

    public BigDecimal getcBalanceTotal() {
        return cBalanceTotal;
    }

    public void setcBalanceTotal(BigDecimal cBalanceTotal) {
        this.cBalanceTotal = cBalanceTotal;
    }

    public BigDecimal getcDrawPercentageTotal() {
        return cDrawPercentageTotal;
    }

    public void setcDrawPercentageTotal(BigDecimal cDrawPercentageTotal) {
        this.cDrawPercentageTotal = cDrawPercentageTotal;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel2() {
        return level2;
    }

    public void setLevel2(int level2) {
        this.level2 = level2;
    }

    public int getLevel1() {
        return level1;
    }

    public void setLevel1(int level1) {
        this.level1 = level1;
    }
}
