package com.bzs.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "car_info")
public class CarInfo {
    /**
     * ID
     */
    @Id
    @Column(name = "car_info_id")
    private String carInfoId;

    /**
     * 车牌号
     */
    @Column(name = "car_number")
    private String carNumber;

    /**
     * 发动机号
     */
    @Column(name = "engine_number")
    private String engineNumber;

    /**
     * 车架号
     */
    @Column(name = "frame_number")
    private String frameNumber;

    /**
     * 注册日期
     */
    @Column(name = "register_date")
    private String registerDate;

    /**
     * 品牌型号
     */
    @Column(name = "brand_model")
    private String brandModel;

    /**
     * 车型
     */
    @Column(name = "car_model")
    private String carModel;

    /**
     * 新车购置价
     */
    @Column(name = "purchase_price")
    private Integer purchasePrice;

    /**
     * 座位数
     */
    @Column(name = "seat_number")
    private Integer seatNumber;

    /**
     * 排量
     */
    private BigDecimal displacement;

    /**
     * 过户车 0是   1否
     */
    @Column(name = "isTransfer_car")
    private Integer istransferCar;

    /**
     * 贷款车 0是   1否
     */
    @Column(name = "isLoan_car")
    private Integer isloanCar;

    /**
     * 备注
     */
    @Column(name = "remarks_car")
    private String remarksCar;

    /**
     * 本年跟进次数
     */
    @Column(name = "follow_count")
    private Integer followCount;

    /**
     * 最后跟进时间
     */
    @Column(name = "follow_time")
    private String followTime;

    /**
     * 最后跟进内容
     */
    @Column(name = "follow_content")
    private String followContent;

    /**
     * 计划回访时间
     */
    @Column(name = "plan_return_time")
    private String planReturnTime;

    /**
     * 客户状态 0回访1未回访
     */
    @Column(name = "customer_status")
    private Integer customerStatus;

    /**
     * 客户类别
     */
    @Column(name = "customer_type")
    private String customerType;

    /**
     * 业务员
     */
    private String salesman;

    /**
     * 创建人
     */
    @Column(name = "CREATED_BY")
    private String createdBy;

    /**
     * 创建时间
     */
    @Column(name = "CREATED_TIME")
    private Date createdTime;

    /**
     * 更新人
     */
    @Column(name = "UPDATED_BY")
    private String updatedBy;

    /**
     * 更新时间
     */
    @Column(name = "UPDATED_TIME")
    private Date updatedTime;

    /**
     * 手动选择的上年投保公司
     */
    @Column(name = "choice_last_year_insurance_name")
    private String choiceLastYearInsuranceName;

    /**
     * 手动选择的上年投保公司枚举值
     */
    @Column(name = "choice_last_year_source")
    private String choiceLastYearSource;

    /**
     * 投保的地区名称
     */
    @Column(name = "insured_area")
    private String insuredArea;

    /**
     * 车主
     */
    @Column(name = "license_owner")
    private String licenseOwner;

    /**
     * 车主证件号码
     */
    @Column(name = "license_owner_id_card")
    private String licenseOwnerIdCard;

    /**
     * 车主证件类型 证件类型 0：没有取到 1：身份证 2: 组织机构代码证 3：护照
     */
    @Column(name = "license_owner_id_card_type")
    private String licenseOwnerIdCardType;
    @Column(name = "mobile")
    private String mobile;
    /**
     * 车辆信息获取方式默认-1手动添加还未查询过，0使用车牌1使用车架2车牌和车架均使用
     */
    @Column(name="channel_type")
    private String  channelType;
    /**
     * 是否手动添加,默认0=查询获取,1是
     */
    @Column(name="is_addtion")
    private String isAddtion;

    /**
     * 获取ID
     *
     * @return car_info_id - ID
     */
    public String getCarInfoId() {
        return carInfoId;
    }

    /**
     * 设置ID
     *
     * @param carInfoId ID
     */
    public void setCarInfoId(String carInfoId) {
        this.carInfoId = carInfoId;
    }

    /**
     * 获取车牌号
     *
     * @return car_number - 车牌号
     */
    public String getCarNumber() {
        return carNumber;
    }

    /**
     * 设置车牌号
     *
     * @param carNumber 车牌号
     */
    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    /**
     * 获取发动机号
     *
     * @return engine_number - 发动机号
     */
    public String getEngineNumber() {
        return engineNumber;
    }

    /**
     * 设置发动机号
     *
     * @param engineNumber 发动机号
     */
    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    /**
     * 获取车架号
     *
     * @return frame_number - 车架号
     */
    public String getFrameNumber() {
        return frameNumber;
    }

    /**
     * 设置车架号
     *
     * @param frameNumber 车架号
     */
    public void setFrameNumber(String frameNumber) {
        this.frameNumber = frameNumber;
    }

    /**
     * 获取注册日期
     *
     * @return register_date - 注册日期
     */
    public String getRegisterDate() {
        return registerDate;
    }

    /**
     * 设置注册日期
     *
     * @param registerDate 注册日期
     */
    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    /**
     * 获取品牌型号
     *
     * @return brand_model - 品牌型号
     */
    public String getBrandModel() {
        return brandModel;
    }

    /**
     * 设置品牌型号
     *
     * @param brandModel 品牌型号
     */
    public void setBrandModel(String brandModel) {
        this.brandModel = brandModel;
    }

    /**
     * 获取车型
     *
     * @return car_model - 车型
     */
    public String getCarModel() {
        return carModel;
    }

    /**
     * 设置车型
     *
     * @param carModel 车型
     */
    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    /**
     * 获取新车购置价
     *
     * @return purchase_price - 新车购置价
     */
    public Integer getPurchasePrice() {
        return purchasePrice;
    }

    /**
     * 设置新车购置价
     *
     * @param purchasePrice 新车购置价
     */
    public void setPurchasePrice(Integer purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    /**
     * 获取座位数
     *
     * @return seat_number - 座位数
     */
    public Integer getSeatNumber() {
        return seatNumber;
    }

    /**
     * 设置座位数
     *
     * @param seatNumber 座位数
     */
    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    /**
     * 获取排量
     *
     * @return displacement - 排量
     */
    public BigDecimal getDisplacement() {
        return displacement;
    }

    /**
     * 设置排量
     *
     * @param displacement 排量
     */
    public void setDisplacement(BigDecimal displacement) {
        this.displacement = displacement;
    }

    /**
     * 获取过户车 0是   1否
     *
     * @return isTransfer_car - 过户车 0是   1否
     */
    public Integer getIstransferCar() {
        return istransferCar;
    }

    /**
     * 设置过户车 0是   1否
     *
     * @param istransferCar 过户车 0是   1否
     */
    public void setIstransferCar(Integer istransferCar) {
        this.istransferCar = istransferCar;
    }

    /**
     * 获取贷款车 0是   1否
     *
     * @return isLoan_car - 贷款车 0是   1否
     */
    public Integer getIsloanCar() {
        return isloanCar;
    }

    /**
     * 设置贷款车 0是   1否
     *
     * @param isloanCar 贷款车 0是   1否
     */
    public void setIsloanCar(Integer isloanCar) {
        this.isloanCar = isloanCar;
    }

    /**
     * 获取备注
     *
     * @return remarks_car - 备注
     */
    public String getRemarksCar() {
        return remarksCar;
    }

    /**
     * 设置备注
     *
     * @param remarksCar 备注
     */
    public void setRemarksCar(String remarksCar) {
        this.remarksCar = remarksCar;
    }

    /**
     * 获取本年跟进次数
     *
     * @return follow_count - 本年跟进次数
     */
    public Integer getFollowCount() {
        return followCount;
    }

    /**
     * 设置本年跟进次数
     *
     * @param followCount 本年跟进次数
     */
    public void setFollowCount(Integer followCount) {
        this.followCount = followCount;
    }

    /**
     * 获取最后跟进时间
     *
     * @return follow_time - 最后跟进时间
     */
    public String getFollowTime() {
        return followTime;
    }

    /**
     * 设置最后跟进时间
     *
     * @param followTime 最后跟进时间
     */
    public void setFollowTime(String followTime) {
        this.followTime = followTime;
    }

    /**
     * 获取最后跟进内容
     *
     * @return follow_content - 最后跟进内容
     */
    public String getFollowContent() {
        return followContent;
    }

    /**
     * 设置最后跟进内容
     *
     * @param followContent 最后跟进内容
     */
    public void setFollowContent(String followContent) {
        this.followContent = followContent;
    }

    /**
     * 获取计划回访时间
     *
     * @return plan_return_time - 计划回访时间
     */
    public String getPlanReturnTime() {
        return planReturnTime;
    }

    /**
     * 设置计划回访时间
     *
     * @param planReturnTime 计划回访时间
     */
    public void setPlanReturnTime(String planReturnTime) {
        this.planReturnTime = planReturnTime;
    }

    /**
     * 获取客户状态 0回访1未回访
     *
     * @return customer_status - 客户状态 0回访1未回访
     */
    public Integer getCustomerStatus() {
        return customerStatus;
    }

    /**
     * 设置客户状态 0回访1未回访
     *
     * @param customerStatus 客户状态 0回访1未回访
     */
    public void setCustomerStatus(Integer customerStatus) {
        this.customerStatus = customerStatus;
    }

    /**
     * 获取客户类别
     *
     * @return customer_type - 客户类别
     */
    public String getCustomerType() {
        return customerType;
    }

    /**
     * 设置客户类别
     *
     * @param customerType 客户类别
     */
    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    /**
     * 获取业务员
     *
     * @return salesman - 业务员
     */
    public String getSalesman() {
        return salesman;
    }

    /**
     * 设置业务员
     *
     * @param salesman 业务员
     */
    public void setSalesman(String salesman) {
        this.salesman = salesman;
    }

    /**
     * 获取创建人
     *
     * @return CREATED_BY - 创建人
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * 设置创建人
     *
     * @param createdBy 创建人
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * 获取创建时间
     *
     * @return CREATED_TIME - 创建时间
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * 设置创建时间
     *
     * @param createdTime 创建时间
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * 获取更新人
     *
     * @return UPDATED_BY - 更新人
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * 设置更新人
     *
     * @param updatedBy 更新人
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * 获取更新时间
     *
     * @return UPDATED_TIME - 更新时间
     */
    public Date getUpdatedTime() {
        return updatedTime;
    }

    /**
     * 设置更新时间
     *
     * @param updatedTime 更新时间
     */
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    /**
     * 获取手动选择的上年投保公司
     *
     * @return choice_last_year_insurance_name - 手动选择的上年投保公司
     */
    public String getChoiceLastYearInsuranceName() {
        return choiceLastYearInsuranceName;
    }

    /**
     * 设置手动选择的上年投保公司
     *
     * @param choiceLastYearInsuranceName 手动选择的上年投保公司
     */
    public void setChoiceLastYearInsuranceName(String choiceLastYearInsuranceName) {
        this.choiceLastYearInsuranceName = choiceLastYearInsuranceName;
    }

    /**
     * 获取手动选择的上年投保公司枚举值
     *
     * @return choice_last_year_source - 手动选择的上年投保公司枚举值
     */
    public String getChoiceLastYearSource() {
        return choiceLastYearSource;
    }

    /**
     * 设置手动选择的上年投保公司枚举值
     *
     * @param choiceLastYearSource 手动选择的上年投保公司枚举值
     */
    public void setChoiceLastYearSource(String choiceLastYearSource) {
        this.choiceLastYearSource = choiceLastYearSource;
    }

    /**
     * 获取投保的地区名称
     *
     * @return insured_area - 投保的地区名称
     */
    public String getInsuredArea() {
        return insuredArea;
    }

    /**
     * 设置投保的地区名称
     *
     * @param insuredArea 投保的地区名称
     */
    public void setInsuredArea(String insuredArea) {
        this.insuredArea = insuredArea;
    }

    /**
     * 获取车主
     *
     * @return license_owner - 车主
     */
    public String getLicenseOwner() {
        return licenseOwner;
    }

    /**
     * 设置车主
     *
     * @param licenseOwner 车主
     */
    public void setLicenseOwner(String licenseOwner) {
        this.licenseOwner = licenseOwner;
    }

    /**
     * 获取车主证件号码
     *
     * @return license_owner_id_card - 车主证件号码
     */
    public String getLicenseOwnerIdCard() {
        return licenseOwnerIdCard;
    }

    /**
     * 设置车主证件号码
     *
     * @param licenseOwnerIdCard 车主证件号码
     */
    public void setLicenseOwnerIdCard(String licenseOwnerIdCard) {
        this.licenseOwnerIdCard = licenseOwnerIdCard;
    }

    /**
     * 获取车主证件类型 证件类型 0：没有取到 1：身份证 2: 组织机构代码证 3：护照
     *
     * @return license_owner_id_card_type - 车主证件类型 证件类型 0：没有取到 1：身份证 2: 组织机构代码证 3：护照
     */
    public String getLicenseOwnerIdCardType() {
        return licenseOwnerIdCardType;
    }

    /**
     * 设置车主证件类型 证件类型 0：没有取到 1：身份证 2: 组织机构代码证 3：护照
     *
     * @param licenseOwnerIdCardType 车主证件类型 证件类型 0：没有取到 1：身份证 2: 组织机构代码证 3：护照
     */
    public void setLicenseOwnerIdCardType(String licenseOwnerIdCardType) {
        this.licenseOwnerIdCardType = licenseOwnerIdCardType;
    }

    public CarInfo() {

    }
    public CarInfo(String carInfoId) {
        this.carInfoId = carInfoId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getIsAddtion() {
        return isAddtion;
    }

    public void setIsAddtion(String isAddtion) {
        this.isAddtion = isAddtion;
    }
}