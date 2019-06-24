package com.bzs.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "crawling_car_info")
public class CrawlingCarInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 车牌
     */
    @Column(name = "car_no")
    private String carNo;

    /**
     * 新车牌
     */
    @Column(name = "new_car_no")
    private String newCarNo;

    /**
     * 是否有新车牌0默认1有
     */
    @Column(name = "is_new_car_no")
    private String isNewCarNo;

    /**
     * 车主
     */
    @Column(name = "car_owner")
    private String carOwner;

    /**
     * 新车主
     */
    @Column(name = "new_car_owner")
    private String newCarOwner;

    /**
     * 是否有新车主0默认1有
     */
    @Column(name = "is_new_car_owner")
    private String isNewCarOwner;

    /**
     * 车架号
     */
    @Column(name = "vin_no")
    private String vinNo;

    /**
     * 新车架号
     */
    @Column(name = "new_vin_no")
    private String newVinNo;

    /**
     * 是否有新车架0默认1有
     */
    @Column(name = "is_new_vin_no")
    private String isNewVinNo;

    /**
     * 品牌
     */
    @Column(name = "brand")
    private String brand;

    /**
     * 车辆型号
     */
    @Column(name = "model")
    private String model;

    /**
     * 发动机号
     */
    @Column(name = "engine_no")
    private String engineNo;

    /**
     * 登记日期
     */
    @Column(name = "register_date")
    private String registerDate;

    /**
     * 过户日期
     */
    @Column(name = "transfer_date")
    private String transferDate;

    /**
     * 交强险承保公司
     */
    @Column(name = "force_company")
    private String forceCompany;

    /**
     * 交强险到期日期
     */
    @Column(name = "force_end_date")
    private String forceEndDate;

    /**
     * 商业险承保公司
     */
    @Column(name = "biz_company")
    private String bizCompany;

    /**
     * 商业险到期日期
     */
    @Column(name = "biz_end_date")
    private String bizEndDate;

    /**
     * 出险次数
     */
    @Column(name = "out_danger_count")
    private String outDangerCount;

    /**
     * 违章次数
     */
    @Column(name = "break_reles_count")
    private String breakRelesCount;

    /**
     * 证件号
     */
    @Column(name = "id_card")
    private String idCard;

    /**
     * 电话
     */
    private String mobile;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建人
     */
    @Column(name = "create_by")
    private String createBy;

    /**
     * 批次号
     */
    @Column(name = "series_no")
    private String seriesNo;

    /**
     * 是否已经爬取0未爬取1车牌爬取2车架爬取3车牌车架都爬取
     */
    @Column(name = "is_drawling")
    private String isDrawling;

    /**
     * 是否上次暂停数据默认0不是1是
     */
    @Column(name = "is_last_drawling")
    private String isLastDrawling;
    private String status;

    /**
     * 本次上传的数据的序号
     */
    @Column(name = "index_no")
    private Integer indexNo;

    @Column(name = "result_message")
    private String resultMessage;
    @Column(name = "biz_start_date")
    private String bizStartDate;
    @Column(name = "force_start_date")
    private String forceStartDate;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取车牌
     *
     * @return car_no - 车牌
     */
    public String getCarNo() {
        return carNo;
    }

    /**
     * 设置车牌
     *
     * @param carNo 车牌
     */
    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    /**
     * 获取新车牌
     *
     * @return new_car_no - 新车牌
     */
    public String getNewCarNo() {
        return newCarNo;
    }

    /**
     * 设置新车牌
     *
     * @param newCarNo 新车牌
     */
    public void setNewCarNo(String newCarNo) {
        this.newCarNo = newCarNo;
    }

    /**
     * 获取是否有新车牌0默认1有
     *
     * @return is_new_car_no - 是否有新车牌0默认1有
     */
    public String getIsNewCarNo() {
        return isNewCarNo;
    }

    /**
     * 设置是否有新车牌0默认1有
     *
     * @param isNewCarNo 是否有新车牌0默认1有
     */
    public void setIsNewCarNo(String isNewCarNo) {
        this.isNewCarNo = isNewCarNo;
    }

    /**
     * 获取车主
     *
     * @return car_owner - 车主
     */
    public String getCarOwner() {
        return carOwner;
    }

    /**
     * 设置车主
     *
     * @param carOwner 车主
     */
    public void setCarOwner(String carOwner) {
        this.carOwner = carOwner;
    }

    /**
     * 获取新车主
     *
     * @return new_car_owner - 新车主
     */
    public String getNewCarOwner() {
        return newCarOwner;
    }

    /**
     * 设置新车主
     *
     * @param newCarOwner 新车主
     */
    public void setNewCarOwner(String newCarOwner) {
        this.newCarOwner = newCarOwner;
    }

    /**
     * 获取是否有新车主0默认1有
     *
     * @return is_new_car_owner - 是否有新车主0默认1有
     */
    public String getIsNewCarOwner() {
        return isNewCarOwner;
    }

    /**
     * 设置是否有新车主0默认1有
     *
     * @param isNewCarOwner 是否有新车主0默认1有
     */
    public void setIsNewCarOwner(String isNewCarOwner) {
        this.isNewCarOwner = isNewCarOwner;
    }

    /**
     * 获取车架号
     *
     * @return vin_no - 车架号
     */
    public String getVinNo() {
        return vinNo;
    }

    /**
     * 设置车架号
     *
     * @param vinNo 车架号
     */
    public void setVinNo(String vinNo) {
        this.vinNo = vinNo;
    }

    /**
     * 获取新车架号
     *
     * @return new_vin_no - 新车架号
     */
    public String getNewVinNo() {
        return newVinNo;
    }

    /**
     * 设置新车架号
     *
     * @param newVinNo 新车架号
     */
    public void setNewVinNo(String newVinNo) {
        this.newVinNo = newVinNo;
    }

    /**
     * 获取是否有新车架0默认1有
     *
     * @return is_new_vin_no - 是否有新车架0默认1有
     */
    public String getIsNewVinNo() {
        return isNewVinNo;
    }

    /**
     * 设置是否有新车架0默认1有
     *
     * @param isNewVinNo 是否有新车架0默认1有
     */
    public void setIsNewVinNo(String isNewVinNo) {
        this.isNewVinNo = isNewVinNo;
    }

    /**
     * 获取品牌
     *
     * @return brand - 品牌
     */
    public String getBrand() {
        return brand;
    }

    /**
     * 设置品牌
     *
     * @param brand 品牌
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * 获取车辆型号
     *
     * @return model - 车辆型号
     */
    public String getModel() {
        return model;
    }

    /**
     * 设置车辆型号
     *
     * @param model 车辆型号
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * 获取发动机号
     *
     * @return engine_no - 发动机号
     */
    public String getEngineNo() {
        return engineNo;
    }

    /**
     * 设置发动机号
     *
     * @param engineNo 发动机号
     */
    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    /**
     * 获取登记日期
     *
     * @return register_date - 登记日期
     */
    public String getRegisterDate() {
        return registerDate;
    }

    /**
     * 设置登记日期
     *
     * @param registerDate 登记日期
     */
    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    /**
     * 获取过户日期
     *
     * @return transfer_date - 过户日期
     */
    public String getTransferDate() {
        return transferDate;
    }

    /**
     * 设置过户日期
     *
     * @param transferDate 过户日期
     */
    public void setTransferDate(String transferDate) {
        this.transferDate = transferDate;
    }

    /**
     * 获取交强险承保公司
     *
     * @return force_company - 交强险承保公司
     */
    public String getForceCompany() {
        return forceCompany;
    }

    /**
     * 设置交强险承保公司
     *
     * @param forceCompany 交强险承保公司
     */
    public void setForceCompany(String forceCompany) {
        this.forceCompany = forceCompany;
    }

    /**
     * 获取交强险到期日期
     *
     * @return force_end_date - 交强险到期日期
     */
    public String getForceEndDate() {
        return forceEndDate;
    }

    /**
     * 设置交强险到期日期
     *
     * @param forceEndDate 交强险到期日期
     */
    public void setForceEndDate(String forceEndDate) {
        this.forceEndDate = forceEndDate;
    }

    /**
     * 获取商业险承保公司
     *
     * @return biz_company - 商业险承保公司
     */
    public String getBizCompany() {
        return bizCompany;
    }

    /**
     * 设置商业险承保公司
     *
     * @param bizCompany 商业险承保公司
     */
    public void setBizCompany(String bizCompany) {
        this.bizCompany = bizCompany;
    }

    /**
     * 获取商业险到期日期
     *
     * @return biz_end_date - 商业险到期日期
     */
    public String getBizEndDate() {
        return bizEndDate;
    }

    /**
     * 设置商业险到期日期
     *
     * @param bizEndDate 商业险到期日期
     */
    public void setBizEndDate(String bizEndDate) {
        this.bizEndDate = bizEndDate;
    }

    /**
     * 获取出险次数
     *
     * @return out_danger_count - 出险次数
     */
    public String getOutDangerCount() {
        return outDangerCount;
    }

    /**
     * 设置出险次数
     *
     * @param outDangerCount 出险次数
     */
    public void setOutDangerCount(String outDangerCount) {
        this.outDangerCount = outDangerCount;
    }

    /**
     * 获取违章次数
     *
     * @return break_reles_count - 违章次数
     */
    public String getBreakRelesCount() {
        return breakRelesCount;
    }

    /**
     * 设置违章次数
     *
     * @param breakRelesCount 违章次数
     */
    public void setBreakRelesCount(String breakRelesCount) {
        this.breakRelesCount = breakRelesCount;
    }

    /**
     * 获取证件号
     *
     * @return id_card - 证件号
     */
    public String getIdCard() {
        return idCard;
    }

    /**
     * 设置证件号
     *
     * @param idCard 证件号
     */
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    /**
     * 获取电话
     *
     * @return mobile - 电话
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置电话
     *
     * @param mobile 电话
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
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
     * 获取创建人
     *
     * @return create_by - 创建人
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置创建人
     *
     * @param createBy 创建人
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取批次号
     *
     * @return series_no - 批次号
     */
    public String getSeriesNo() {
        return seriesNo;
    }

    /**
     * 设置批次号
     *
     * @param seriesNo 批次号
     */
    public void setSeriesNo(String seriesNo) {
        this.seriesNo = seriesNo;
    }

    /**
     * 获取是否已经爬取0未爬取1车牌爬取2车架爬取3车牌车架都爬取
     *
     * @return is_drawling - 是否已经爬取0未爬取1车牌爬取2车架爬取3车牌车架都爬取
     */
    public String getIsDrawling() {
        return isDrawling;
    }

    /**
     * 设置是否已经爬取0未爬取1车牌爬取2车架爬取3车牌车架都爬取
     *
     * @param isDrawling 是否已经爬取0未爬取1车牌爬取2车架爬取3车牌车架都爬取
     */
    public void setIsDrawling(String isDrawling) {
        this.isDrawling = isDrawling;
    }

    /**
     * 获取是否上次暂停数据默认0不是1是
     *
     * @return is_last_drawling - 是否上次暂停数据默认0不是1是
     */
    public String getIsLastDrawling() {
        return isLastDrawling;
    }

    /**
     * 设置是否上次暂停数据默认0不是1是
     *
     * @param isLastDrawling 是否上次暂停数据默认0不是1是
     */
    public void setIsLastDrawling(String isLastDrawling) {
        this.isLastDrawling = isLastDrawling;
    }

    /**
     * 获取本次上传的数据的序号
     *
     * @return index_no - 本次上传的数据的序号
     */
    public Integer getIndexNo() {
        return indexNo;
    }

    /**
     * 设置本次上传的数据的序号
     *
     * @param indexNo 本次上传的数据的序号
     */
    public void setIndexNo(Integer indexNo) {
        this.indexNo = indexNo;
    }

    public CrawlingCarInfo() {
    }

    public CrawlingCarInfo(String carNo, String carOwner, String vinNo, String idCard, String mobile, String createBy, String seriesNo, Integer indexNo) {
        this.carNo = carNo;
        this.carOwner = carOwner;
        this.vinNo = vinNo;
        this.idCard = idCard;
        this.mobile = mobile;
        this.createBy = createBy;
        this.seriesNo = seriesNo;
        this.indexNo = indexNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public String getBizStartDate() {
        return bizStartDate;
    }

    public void setBizStartDate(String bizStartDate) {
        this.bizStartDate = bizStartDate;
    }

    public String getForceStartDate() {
        return forceStartDate;
    }

    public void setForceStartDate(String forceStartDate) {
        this.forceStartDate = forceStartDate;
    }
}