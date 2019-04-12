package com.bzs.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "insured_info")
public class InsuredInfo {
    /**
     * id
     */
    @Id
    @Column(name = "insured_id")
    private String insuredId;

    /**
     * 创建人
     */
    @Column(name = "create_id")
    private String createId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新人
     */
    @Column(name = "update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 选择的投保公司
     */
    @Column(name = "insurance_company")
    private String insuranceCompany;

    /**
     * 选择的投保公司枚举值
     */
    @Column(name = "choice_insurance_source")
    private String choiceInsuranceSource;

    /**
     * 机构名称
     */
    @Column(name = "mechanism_number")
    private String mechanismNumber;

    /**
     * 商业险到期时间
     */
    @Column(name = "busines_expire_date")
    private String businesExpireDate;

    /**
     * 交强险到期时间
     */
    @Column(name = "force_expire_date")
    private String forceExpireDate;

    /**
     * 商业险起保时间
     */
    @Column(name = "next_busines_start_date")
    private String nextBusinesStartDate;

    /**
     * 交强险起保时间
     */
    @Column(name = "next_force_start_date")
    private String nextForceStartDate;

    /**
     * 交强险是否投保
     */
    @Column(name = "force_is_insured")
    private String forceIsInsured;

    /**
     * 商业险是否投保
     */
    @Column(name = "biz_is_insured")
    private String bizIsInsured;

    /**
     * 上年投保公司
     */
    @Column(name = "last_year_insurance_company")
    private String lastYearInsuranceCompany;

    /**
     * 上年投保公司的枚举值
     */
    @Column(name = "last_year_source")
    private String lastYearSource;

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

    /**
     * 投保人姓名
     */
    @Column(name = "posted_name")
    private String postedName;

    /**
     * 投报人证件号码
     */
    @Column(name = "holder_id_card")
    private String holderIdCard;

    /**
     * 投保人证件类型
     */
    @Column(name = "holder_id_card_type")
    private String holderIdCardType;

    /**
     * 被保险人
     */
    @Column(name = "insured_name")
    private String insuredName;

    /**
     * 被保险人证件号码
     */
    @Column(name = "insured__id_card")
    private Integer insuredIdCard;

    /**
     * 被保险人证件类型
     */
    @Column(name = "insured_id_card_type")
    private Integer insuredIdCardType;

    /**
     * 商业险保单号
     */
    @Column(name = "busines_number")
    private Integer businesNumber;

    /**
     * 交强险保单号
     */
    @Column(name = "traffic_number")
    private Integer trafficNumber;

    /**
     * 上一年交强险出险次数
     */
    @Column(name = "force_last_year_out_danger_count")
    private String forceLastYearOutDangerCount;

    /**
     * 上一年商业险出险次数
     */
    @Column(name = "biz_last_year_out_danger_count")
    private String bizLastYearOutDangerCount;

    /**
     * 上一年交强险出险情况
     */
    @Column(name = "force_last_year_out_danger")
    private String forceLastYearOutDanger;

    /**
     * 上一年商业险出险情况
     */
    @Column(name = "biz_last_year_out_danger")
    private String bizLastYearOutDanger;

    /**
     * 车辆信息表id 关联相关车辆的续保信息
     */
    @Column(name = "car_info_id")
    private String carInfoId;

    /**
     * 获取id
     *
     * @return insured_id - id
     */
    public String getInsuredId() {
        return insuredId;
    }

    /**
     * 设置id
     *
     * @param insuredId id
     */
    public void setInsuredId(String insuredId) {
        this.insuredId = insuredId;
    }

    /**
     * 获取创建人
     *
     * @return account_id - 创建人
     */
    public String getCreateId() {
        return createId;
    }

    /**
     * 设置创建人
     *
     * @param createId 创建人
     */
    public void setCreateId(String createId) {
        this.createId = createId;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新人
     *
     * @return update_by - 更新人
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置更新人
     *
     * @param updateBy 更新人
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取选择的投保公司
     *
     * @return insurance_company - 选择的投保公司
     */
    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    /**
     * 设置选择的投保公司
     *
     * @param insuranceCompany 选择的投保公司
     */
    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    /**
     * 获取选择的投保公司枚举值
     *
     * @return choice_insurance_source - 选择的投保公司枚举值
     */
    public String getChoiceInsuranceSource() {
        return choiceInsuranceSource;
    }

    /**
     * 设置选择的投保公司枚举值
     *
     * @param choiceInsuranceSource 选择的投保公司枚举值
     */
    public void setChoiceInsuranceSource(String choiceInsuranceSource) {
        this.choiceInsuranceSource = choiceInsuranceSource;
    }

    /**
     * 获取机构名称
     *
     * @return mechanism_number - 机构名称
     */
    public String getMechanismNumber() {
        return mechanismNumber;
    }

    /**
     * 设置机构名称
     *
     * @param mechanismNumber 机构名称
     */
    public void setMechanismNumber(String mechanismNumber) {
        this.mechanismNumber = mechanismNumber;
    }

    /**
     * 获取商业险到期时间
     *
     * @return busines_expire_date - 商业险到期时间
     */
    public String getBusinesExpireDate() {
        return businesExpireDate;
    }

    /**
     * 设置商业险到期时间
     *
     * @param businesExpireDate 商业险到期时间
     */
    public void setBusinesExpireDate(String businesExpireDate) {
        this.businesExpireDate = businesExpireDate;
    }

    /**
     * 获取交强险到期时间
     *
     * @return force_expire_date - 交强险到期时间
     */
    public String getForceExpireDate() {
        return forceExpireDate;
    }

    /**
     * 设置交强险到期时间
     *
     * @param forceExpireDate 交强险到期时间
     */
    public void setForceExpireDate(String forceExpireDate) {
        this.forceExpireDate = forceExpireDate;
    }

    /**
     * 获取商业险起保时间
     *
     * @return next_busines_start_date - 商业险起保时间
     */
    public String getNextBusinesStartDate() {
        return nextBusinesStartDate;
    }

    /**
     * 设置商业险起保时间
     *
     * @param nextBusinesStartDate 商业险起保时间
     */
    public void setNextBusinesStartDate(String nextBusinesStartDate) {
        this.nextBusinesStartDate = nextBusinesStartDate;
    }

    /**
     * 获取交强险起保时间
     *
     * @return next_force_start_date - 交强险起保时间
     */
    public String getNextForceStartDate() {
        return nextForceStartDate;
    }

    /**
     * 设置交强险起保时间
     *
     * @param nextForceStartDate 交强险起保时间
     */
    public void setNextForceStartDate(String nextForceStartDate) {
        this.nextForceStartDate = nextForceStartDate;
    }

    /**
     * 获取交强险是否投保
     *
     * @return force_is_insured - 交强险是否投保
     */
    public String getForceIsInsured() {
        return forceIsInsured;
    }

    /**
     * 设置交强险是否投保
     *
     * @param forceIsInsured 交强险是否投保
     */
    public void setForceIsInsured(String forceIsInsured) {
        this.forceIsInsured = forceIsInsured;
    }

    /**
     * 获取商业险是否投保
     *
     * @return biz_is_insured - 商业险是否投保
     */
    public String getBizIsInsured() {
        return bizIsInsured;
    }

    /**
     * 设置商业险是否投保
     *
     * @param bizIsInsured 商业险是否投保
     */
    public void setBizIsInsured(String bizIsInsured) {
        this.bizIsInsured = bizIsInsured;
    }

    /**
     * 获取上年投保公司
     *
     * @return last_year_insurance_company - 上年投保公司
     */
    public String getLastYearInsuranceCompany() {
        return lastYearInsuranceCompany;
    }

    /**
     * 设置上年投保公司
     *
     * @param lastYearInsuranceCompany 上年投保公司
     */
    public void setLastYearInsuranceCompany(String lastYearInsuranceCompany) {
        this.lastYearInsuranceCompany = lastYearInsuranceCompany;
    }

    /**
     * 获取上年投保公司的枚举值
     *
     * @return last_year_source - 上年投保公司的枚举值
     */
    public String getLastYearSource() {
        return lastYearSource;
    }

    /**
     * 设置上年投保公司的枚举值
     *
     * @param lastYearSource 上年投保公司的枚举值
     */
    public void setLastYearSource(String lastYearSource) {
        this.lastYearSource = lastYearSource;
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

    /**
     * 获取投保人姓名
     *
     * @return posted_name - 投保人姓名
     */
    public String getPostedName() {
        return postedName;
    }

    /**
     * 设置投保人姓名
     *
     * @param postedName 投保人姓名
     */
    public void setPostedName(String postedName) {
        this.postedName = postedName;
    }

    /**
     * 获取投报人证件号码
     *
     * @return holder_id_card - 投报人证件号码
     */
    public String getHolderIdCard() {
        return holderIdCard;
    }

    /**
     * 设置投报人证件号码
     *
     * @param holderIdCard 投报人证件号码
     */
    public void setHolderIdCard(String holderIdCard) {
        this.holderIdCard = holderIdCard;
    }

    /**
     * 获取投保人证件类型
     *
     * @return holder_id_card_type - 投保人证件类型
     */
    public String getHolderIdCardType() {
        return holderIdCardType;
    }

    /**
     * 设置投保人证件类型
     *
     * @param holderIdCardType 投保人证件类型
     */
    public void setHolderIdCardType(String holderIdCardType) {
        this.holderIdCardType = holderIdCardType;
    }

    /**
     * 获取被保险人
     *
     * @return insured_name - 被保险人
     */
    public String getInsuredName() {
        return insuredName;
    }

    /**
     * 设置被保险人
     *
     * @param insuredName 被保险人
     */
    public void setInsuredName(String insuredName) {
        this.insuredName = insuredName;
    }

    /**
     * 获取被保险人证件号码
     *
     * @return insured__id_card - 被保险人证件号码
     */
    public Integer getInsuredIdCard() {
        return insuredIdCard;
    }

    /**
     * 设置被保险人证件号码
     *
     * @param insuredIdCard 被保险人证件号码
     */
    public void setInsuredIdCard(Integer insuredIdCard) {
        this.insuredIdCard = insuredIdCard;
    }

    /**
     * 获取被保险人证件类型
     *
     * @return insured_id_card_type - 被保险人证件类型
     */
    public Integer getInsuredIdCardType() {
        return insuredIdCardType;
    }

    /**
     * 设置被保险人证件类型
     *
     * @param insuredIdCardType 被保险人证件类型
     */
    public void setInsuredIdCardType(Integer insuredIdCardType) {
        this.insuredIdCardType = insuredIdCardType;
    }

    /**
     * 获取商业险保单号
     *
     * @return busines_number - 商业险保单号
     */
    public Integer getBusinesNumber() {
        return businesNumber;
    }

    /**
     * 设置商业险保单号
     *
     * @param businesNumber 商业险保单号
     */
    public void setBusinesNumber(Integer businesNumber) {
        this.businesNumber = businesNumber;
    }

    /**
     * 获取交强险保单号
     *
     * @return traffic_number - 交强险保单号
     */
    public Integer getTrafficNumber() {
        return trafficNumber;
    }

    /**
     * 设置交强险保单号
     *
     * @param trafficNumber 交强险保单号
     */
    public void setTrafficNumber(Integer trafficNumber) {
        this.trafficNumber = trafficNumber;
    }

    /**
     * 获取上一年交强险出险次数
     *
     * @return force_last_year_out_danger_count - 上一年交强险出险次数
     */
    public String getForceLastYearOutDangerCount() {
        return forceLastYearOutDangerCount;
    }

    /**
     * 设置上一年交强险出险次数
     *
     * @param forceLastYearOutDangerCount 上一年交强险出险次数
     */
    public void setForceLastYearOutDangerCount(String forceLastYearOutDangerCount) {
        this.forceLastYearOutDangerCount = forceLastYearOutDangerCount;
    }

    /**
     * 获取上一年商业险出险次数
     *
     * @return biz_last_year_out_danger_count - 上一年商业险出险次数
     */
    public String getBizLastYearOutDangerCount() {
        return bizLastYearOutDangerCount;
    }

    /**
     * 设置上一年商业险出险次数
     *
     * @param bizLastYearOutDangerCount 上一年商业险出险次数
     */
    public void setBizLastYearOutDangerCount(String bizLastYearOutDangerCount) {
        this.bizLastYearOutDangerCount = bizLastYearOutDangerCount;
    }

    /**
     * 获取上一年交强险出险情况
     *
     * @return force_last_year_out_danger - 上一年交强险出险情况
     */
    public String getForceLastYearOutDanger() {
        return forceLastYearOutDanger;
    }

    /**
     * 设置上一年交强险出险情况
     *
     * @param forceLastYearOutDanger 上一年交强险出险情况
     */
    public void setForceLastYearOutDanger(String forceLastYearOutDanger) {
        this.forceLastYearOutDanger = forceLastYearOutDanger;
    }

    /**
     * 获取上一年商业险出险情况
     *
     * @return biz_last_year_out_danger - 上一年商业险出险情况
     */
    public String getBizLastYearOutDanger() {
        return bizLastYearOutDanger;
    }

    /**
     * 设置上一年商业险出险情况
     *
     * @param bizLastYearOutDanger 上一年商业险出险情况
     */
    public void setBizLastYearOutDanger(String bizLastYearOutDanger) {
        this.bizLastYearOutDanger = bizLastYearOutDanger;
    }

    /**
     * 获取车辆信息表id 关联相关车辆的续保信息
     *
     * @return car_info_id - 车辆信息表id 关联相关车辆的续保信息
     */
    public String getCarInfoId() {
        return carInfoId;
    }

    /**
     * 设置车辆信息表id 关联相关车辆的续保信息
     *
     * @param carInfoId 车辆信息表id 关联相关车辆的续保信息
     */
    public void setCarInfoId(String carInfoId) {
        this.carInfoId = carInfoId;
    }

    public InsuredInfo() {
    }
    public  InsuredInfo(String insuredId){
        this.insuredId=insuredId;
    }
}