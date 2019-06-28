package com.bzs.utils.aspect.validate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @program: insurance_bzs
 * @description:
 * @author: dengl
 * @create: 2019-06-26 15:10
 */
public class ValidateRequest extends MyRequest {
    /**
     * 用户名
     */
    @NotBlank
    private String userName;

    /**
     *性别
     */
    @NotNull
    private Integer sex;

    /**
     * 地址
     */
    private String address;
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
