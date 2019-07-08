package com.bzs.model;

import javax.persistence.*;

@Table(name = "card_info")
public class CardInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 姓名
     */
    private String realname;

    /**
     * 性别
     */
    private String sex;

    /**
     * 民族
     */
    private String nation;

    /**
     * 出生日期
     */
    private String born;

    /**
     * 地址
     */
    private String address;

    /**
     * 身份证号码
     */
    private String idcard;

    /**
     * 签发日期
     */
    private String begin;

    /**
     * 签发机关
     */
    private String department;

    /**
     * 失效日期
     */
    private String end;

    @Column(name = "account_id")
    private String accountId;

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
     * 获取姓名
     *
     * @return realname - 姓名
     */
    public String getRealname() {
        return realname;
    }

    /**
     * 设置姓名
     *
     * @param realname 姓名
     */
    public void setRealname(String realname) {
        this.realname = realname;
    }

    /**
     * 获取性别
     *
     * @return sex - 性别
     */
    public String getSex() {
        return sex;
    }

    /**
     * 设置性别
     *
     * @param sex 性别
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * 获取民族
     *
     * @return nation - 民族
     */
    public String getNation() {
        return nation;
    }

    /**
     * 设置民族
     *
     * @param nation 民族
     */
    public void setNation(String nation) {
        this.nation = nation;
    }

    /**
     * 获取出生日期
     *
     * @return born - 出生日期
     */
    public String getBorn() {
        return born;
    }

    /**
     * 设置出生日期
     *
     * @param born 出生日期
     */
    public void setBorn(String born) {
        this.born = born;
    }

    /**
     * 获取地址
     *
     * @return address - 地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置地址
     *
     * @param address 地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取身份证号码
     *
     * @return idcard - 身份证号码
     */
    public String getIdcard() {
        return idcard;
    }

    /**
     * 设置身份证号码
     *
     * @param idcard 身份证号码
     */
    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    /**
     * 获取签发日期
     *
     * @return begin - 签发日期
     */
    public String getBegin() {
        return begin;
    }

    /**
     * 设置签发日期
     *
     * @param begin 签发日期
     */
    public void setBegin(String begin) {
        this.begin = begin;
    }

    /**
     * 获取签发机关
     *
     * @return department - 签发机关
     */
    public String getDepartment() {
        return department;
    }

    /**
     * 设置签发机关
     *
     * @param department 签发机关
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * 获取失效日期
     *
     * @return end - 失效日期
     */
    public String getEnd() {
        return end;
    }

    /**
     * 设置失效日期
     *
     * @param end 失效日期
     */
    public void setEnd(String end) {
        this.end = end;
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

    @Override
    public String toString() {
        return "CardInfo{" +
                "id=" + id +
                ", realname='" + realname + '\'' +
                ", sex='" + sex + '\'' +
                ", nation='" + nation + '\'' +
                ", born='" + born + '\'' +
                ", address='" + address + '\'' +
                ", idcard='" + idcard + '\'' +
                ", begin='" + begin + '\'' +
                ", department='" + department + '\'' +
                ", end='" + end + '\'' +
                ", accountId='" + accountId + '\'' +
                '}';
    }
}