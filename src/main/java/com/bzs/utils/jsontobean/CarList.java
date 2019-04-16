package com.bzs.utils.jsontobean;

/**
 * @program: insurance_bzs
 * @description: 报价返回 中使用
 * @author: dengl
 * @create: 2019-04-16 10:42
 */
public class CarList {
    private String carCode;
    private String carName;

    public String getCarCode() {
        return carCode;
    }

    public void setCarCode(String carCode) {
        this.carCode = carCode;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public CarList() {
    }
}
