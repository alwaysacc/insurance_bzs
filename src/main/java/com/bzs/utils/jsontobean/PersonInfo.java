package com.bzs.utils.jsontobean;

/**
 * @program: insurance_bzs
 * @description: 报价参数
 * @author: dengl
 * @create: 2019-04-15 16:18
 */
public class PersonInfo {
    private String name;
    private String address;
    private String cardID;
    private String mobile;
    private String age;
    private String sex;
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getAddress() {
        return address;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }
    public String getCardID() {
        return cardID;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getMobile() {
        return mobile;
    }

    public void setAge(String age) {
        this.age = age;
    }
    public String getAge() {
        return age;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getSex() {
        return sex;
    }
}
