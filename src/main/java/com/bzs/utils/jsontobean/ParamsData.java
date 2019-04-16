package com.bzs.utils.jsontobean;

import java.util.List;

/**
 * @program: insurance_bzs
 * @description: 报价参数data
 * @author: dengl
 * @create: 2019-04-15 16:17
 */
public class ParamsData {
    private String salesPerson;
    private PersonInfo personInfo;
    private CarInfo carInfo;
    private String ciBeginDate;
    private String biBeginDate;
    private List<InsurancesList> insurancesList;
    public void setSalesPerson(String salesPerson) {
        this.salesPerson = salesPerson;
    }
    public String getSalesPerson() {
        return salesPerson;
    }

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }
    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setCarInfo(CarInfo carInfo) {
        this.carInfo = carInfo;
    }
    public CarInfo getCarInfo() {
        return carInfo;
    }

    public void setCiBeginDate(String ciBeginDate) {
        this.ciBeginDate = ciBeginDate;
    }
    public String  getCiBeginDate() {
        return ciBeginDate;
    }

    public void setBiBeginDate(String biBeginDate) {
        this.biBeginDate = biBeginDate;
    }
    public String getBiBeginDate() {
        return biBeginDate;
    }

    public void setInsurancesList(List<InsurancesList> insurancesList) {
        this.insurancesList = insurancesList;
    }
    public List<InsurancesList> getInsurancesList() {
        return insurancesList;
    }
}
