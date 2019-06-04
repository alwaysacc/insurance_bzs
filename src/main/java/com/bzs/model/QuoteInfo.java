package com.bzs.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "quote_info")
public class QuoteInfo {
    /**
     * 乐观锁
     */

    @Column(name = "REVISION")
    private Integer revision;

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
     * id
     */
    @Id
    @Column(name = "quote_id")
    private String quoteId;

    /**
     * 车辆信息表id
     */
    @Column(name = "car_info_id")
    private String carInfoId;

    /**
     * 报价状态 报价状态，-1=未报价， 0=报价失败，>0报价成功
     */
    @Column(name = "quote_status")
    private Integer quoteStatus;

    /**
     * 是否重复投保
     */
    @Column(name = "repeat_submit_result")
    private String repeatSubmitResult;

    /**
     * 核保状态 核保状态-1未核保 0=核保失败， 1=核保成功, 2=未到期未核保（无需再核保）,
     */
    @Column(name = "submit_status")
    private Integer submitStatus;

    /**
     * 交强车船险费率 交强车船险费率（核保通过之后才会有值）
     */
    @Column(name = "force_rate")
    private String forceRate;

    /**
     * 无赔偿优惠系数 无赔偿优惠系数
     */
    @Column(name = "no_reparation_sale_rate")
    private String noReparationSaleRate;

    /**
     * 自主渠道系数
     */
    @Column(name = "independent_channel_date")
    private String independentChannelDate;

    /**
     * 自主核保系数
     */
    @Column(name = "independent_submit_rate")
    private String independentSubmitRate;

    /**
     * 交通违法活动系数
     */
    @Column(name = "traffic_illegal_rate")
    private String trafficIllegalRate;

    /**
     * 折扣系数
     */
    @Column(name = "discount_rate")
    private String discountRate;

    /**
     * 报价渠道
     */
    @Column(name = "quote_channel")
    private String quoteChannel;

    /**
     * 车辆使用性质 使用性质 1：家庭自用车（默认）， 2：党政机关、事业团体， 3：非营业企业客车， 6：营业货车， 7：非营业货车
     */
    @Column(name = "car_used_type")
    private String carUsedType;

    /**
     * 车辆型号
     */
    @Column(name = "car_model")
    private String carModel;

    /**
     * 商业险保费合计
     */
    @Column(name = "biz_total")
    private BigDecimal bizTotal;

    /**
     * 交强险保费合计
     */
    @Column(name = "force_total")
    private BigDecimal forceTotal;

    /**
     * 车船税
     */
    @Column(name = "tax_total")
    private BigDecimal taxTotal;

    /**
     * 保费总额
     */
    @Column(name = "total")
    private BigDecimal total;


    /**
     * 报价保司名称
     */
    @Column(name = "quote_insurance_name")
    private String quoteInsuranceName;

    /**
     * 报价保司枚举值 1太保2平安4人保
     */
    @Column(name = "quote_source")
    private String quoteSource;

    /**
     * 商业险保单号
     */
    @Column(name = "biz_no")
    private String bizNo;

    /**
     * 交强险保单号
     */
    @Column(name = "force_no")
    private String forceNo;

    /**
     * 报价信息
     */
    @Column(name = "quote_result")
    private String quoteResult;

    /**
     * 核保状态描述 核保状态描述（备注字符最大长度：1000）
     */
    @Column(name = "SubmitResult")
    private String submitresult;
    /**
     *报价单号 获取支付接口用';
     */
    @Column(name = "proposal_no")
    private String proposalNo;
    /**
     * 支付地址
     */
    @Column(name = "pay_url")
    private String payUrl;
    /**
     * 支付日期
     */
    @Column(name = "pay_time")
    private String payTime;
    /**
     * 报价流水号
     */
    @Column(name = "ref_id")
    private String refId;
    /**
     * 建议折扣率
     */
    @Column(name = "adv_discount_rate")
    private String advDiscountRate;
    /**
     * 交强险起保日期
     */
    @Column(name = "force_start_time")
    private String forceStartTime;
    /**
     * 商业险起保日期
     */
    @Column(name = "biz_start_time")
    private String bizStartTime;
    /**
     * 交强险预期赔付率
     */
    @Column(name = "force_ecompensation_rate")
    private String forceEcompensationRate;
    /**
     * 商业险预期赔付率
     */
    @Column(name = "biz_ecompensation_rate")
    private String bizEcompensationRate;
    /**
     * 商业险标准保费
     */
    @Column(name = "biz_premium")
    private String bizPremium;
    /**
     * 商业险折后保费
     */
    @Column(name = "biz_premiumBy_dis")
    private String bizPremiumByDis;
    /**
     * 实际折扣率
     */
    @Column(name = "real_discount_rate")
    private String realDiscountRate;
    /**
     * 无赔款折扣系数
     */
    @Column(name = "non_claim_discount_rate")
    private String nonClaim_discountRate;
    /**
     * 太保支付单号
     */
    @Column(name = "pay_no")
    private String payNo;
    /**
     * 太保校验码
     */
    @Column(name = "check_no")
    private  String checkNo;

    /**
     * 交费通知单
     */
    @Column(name = "payment_notice")
    private  String paymentNotice;

    /**
     * 流水号
     */
    @Column(name = "serial_no")
    private String serialNo;
    /**
     * 最后支付日期
     */
    @Column(name = "pay_end_date")
    private  String payEndDate;

    /**
     * 支付描述-人保
     */
    @Column(name = "pay_msg")
    private String payMsg;

    /**
     * 不计免总额
     */
    @Column(name = "excluding_deductible_total")
    private BigDecimal excludingDeductibleTotal;
    /**
     * 报价专用
     */
    @Column(name = "buid")
    private String buid;
    /**
     * 新增totalRate
     */
    @Column(name = "total_rate")
    private String totalRate;


    /**
     * 获取乐观锁
     *
     * @return REVISION - 乐观锁
     */
    public Integer getRevision() {
        return revision;
    }

    /**
     * 设置乐观锁
     *
     * @param revision 乐观锁
     */
    public void setRevision(Integer revision) {
        this.revision = revision;
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
     * 获取id
     *
     * @return quote_id - id
     */
    public String getQuoteId() {
        return quoteId;
    }

    /**
     * 设置id
     *
     * @param quoteId id
     */
    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }

    /**
     * 获取车辆信息表id
     *
     * @return car_info_id - 车辆信息表id
     */
    public String getCarInfoId() {
        return carInfoId;
    }

    /**
     * 设置车辆信息表id
     *
     * @param carInfoId 车辆信息表id
     */
    public void setCarInfoId(String carInfoId) {
        this.carInfoId = carInfoId;
    }

    /**
     * 获取报价状态 报价状态，-1=未报价， 0=报价失败，>0报价成功
     *
     * @return quote_status - 报价状态 报价状态，-1=未报价， 0=报价失败，>0报价成功
     */
    public Integer getQuoteStatus() {
        return quoteStatus;
    }

    /**
     * 设置报价状态 报价状态，-1=未报价， 0=报价失败，>0报价成功
     *
     * @param quoteStatus 报价状态 报价状态，-1=未报价， 0=报价失败，>0报价成功
     */
    public void setQuoteStatus(Integer quoteStatus) {
        this.quoteStatus = quoteStatus;
    }

    /**
     * 获取是否重复投保
     *
     * @return repeat_submit_result - 是否重复投保
     */
    public String getRepeatSubmitResult() {
        return repeatSubmitResult;
    }

    /**
     * 设置是否重复投保
     *
     * @param repeatSubmitResult 是否重复投保
     */
    public void setRepeatSubmitResult(String repeatSubmitResult) {
        this.repeatSubmitResult = repeatSubmitResult;
    }

    /**
     * 获取核保状态 核保状态-1未核保 0=核保失败， 1=核保成功, 2=未到期未核保（无需再核保）,
     *
     * @return submit_status - 核保状态 核保状态-1未核保 0=核保失败， 1=核保成功, 2=未到期未核保（无需再核保）,
     */
    public Integer getSubmitStatus() {
        return submitStatus;
    }

    /**
     * 设置核保状态 核保状态-1未核保 0=核保失败， 1=核保成功, 2=未到期未核保（无需再核保）,
     *
     * @param submitStatus 核保状态 核保状态-1未核保 0=核保失败， 1=核保成功, 2=未到期未核保（无需再核保）,
     */
    public void setSubmitStatus(Integer submitStatus) {
        this.submitStatus = submitStatus;
    }

    /**
     * 获取交强车船险费率 交强车船险费率（核保通过之后才会有值）
     *
     * @return force_rate - 交强车船险费率 交强车船险费率（核保通过之后才会有值）
     */
    public String getForceRate() {
        return forceRate;
    }

    /**
     * 设置交强车船险费率 交强车船险费率（核保通过之后才会有值）
     *
     * @param forceRate 交强车船险费率 交强车船险费率（核保通过之后才会有值）
     */
    public void setForceRate(String forceRate) {
        this.forceRate = forceRate;
    }

    /**
     * 获取无赔偿优惠系数 无赔偿优惠系数
     *
     * @return no_reparation_sale_rate - 无赔偿优惠系数 无赔偿优惠系数
     */
    public String getNoReparationSaleRate() {
        return noReparationSaleRate;
    }

    /**
     * 设置无赔偿优惠系数 无赔偿优惠系数
     *
     * @param noReparationSaleRate 无赔偿优惠系数 无赔偿优惠系数
     */
    public void setNoReparationSaleRate(String noReparationSaleRate) {
        this.noReparationSaleRate = noReparationSaleRate;
    }

    /**
     * 获取自主渠道系数
     *
     * @return independent_channel_date - 自主渠道系数
     */
    public String getIndependentChannelDate() {
        return independentChannelDate;
    }

    /**
     * 设置自主渠道系数
     *
     * @param independentChannelDate 自主渠道系数
     */
    public void setIndependentChannelDate(String independentChannelDate) {
        this.independentChannelDate = independentChannelDate;
    }

    /**
     * 获取自主核保系数
     *
     * @return independent_submit_rate - 自主核保系数
     */
    public String getIndependentSubmitRate() {
        return independentSubmitRate;
    }

    /**
     * 设置自主核保系数
     *
     * @param independentSubmitRate 自主核保系数
     */
    public void setIndependentSubmitRate(String independentSubmitRate) {
        this.independentSubmitRate = independentSubmitRate;
    }

    /**
     * 获取交通违法活动系数
     *
     * @return traffic_illegal_rate - 交通违法活动系数
     */
    public String getTrafficIllegalRate() {
        return trafficIllegalRate;
    }

    /**
     * 设置交通违法活动系数
     *
     * @param trafficIllegalRate 交通违法活动系数
     */
    public void setTrafficIllegalRate(String trafficIllegalRate) {
        this.trafficIllegalRate = trafficIllegalRate;
    }

    /**
     * 获取折扣系数
     *
     * @return discount_rate - 折扣系数
     */
    public String getDiscountRate() {
        return discountRate;
    }

    /**
     * 设置折扣系数
     *
     * @param discountRate 折扣系数
     */
    public void setDiscountRate(String discountRate) {
        this.discountRate = discountRate;
    }

    /**
     * 获取报价渠道
     *
     * @return quote_channel - 报价渠道
     */
    public String getQuoteChannel() {
        return quoteChannel;
    }

    /**
     * 设置报价渠道
     *
     * @param quoteChannel 报价渠道
     */
    public void setQuoteChannel(String quoteChannel) {
        this.quoteChannel = quoteChannel;
    }

    /**
     * 获取车辆使用性质 使用性质 1：家庭自用车（默认）， 2：党政机关、事业团体， 3：非营业企业客车， 6：营业货车， 7：非营业货车
     *
     * @return car_used_type - 车辆使用性质 使用性质 1：家庭自用车（默认）， 2：党政机关、事业团体， 3：非营业企业客车， 6：营业货车， 7：非营业货车
     */
    public String getCarUsedType() {
        return carUsedType;
    }

    /**
     * 设置车辆使用性质 使用性质 1：家庭自用车（默认）， 2：党政机关、事业团体， 3：非营业企业客车， 6：营业货车， 7：非营业货车
     *
     * @param carUsedType 车辆使用性质 使用性质 1：家庭自用车（默认）， 2：党政机关、事业团体， 3：非营业企业客车， 6：营业货车， 7：非营业货车
     */
    public void setCarUsedType(String carUsedType) {
        this.carUsedType = carUsedType;
    }

    /**
     * 获取车辆型号
     *
     * @return car_model - 车辆型号
     */
    public String getCarModel() {
        return carModel;
    }

    /**
     * 设置车辆型号
     *
     * @param carModel 车辆型号
     */
    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    /**
     * 获取商业险保费合计
     *
     * @return biz_total - 商业险保费合计
     */
    public BigDecimal getBizTotal() {
        return bizTotal;
    }

    /**
     * 设置商业险保费合计
     *
     * @param bizTotal 商业险保费合计
     */
    public void setBizTotal(BigDecimal bizTotal) {
        this.bizTotal = bizTotal;
    }

    /**
     * 获取交强险保费合计
     *
     * @return force_total - 交强险保费合计
     */
    public BigDecimal getForceTotal() {
        return forceTotal;
    }

    /**
     * 设置交强险保费合计
     *
     * @param forceTotal 交强险保费合计
     */
    public void setForceTotal(BigDecimal forceTotal) {
        this.forceTotal = forceTotal;
    }

    /**
     * 获取车船税
     *
     * @return tax_total - 车船税
     */
    public BigDecimal getTaxTotal() {
        return taxTotal;
    }

    /**
     * 设置车船税
     *
     * @param taxTotal 车船税
     */
    public void setTaxTotal(BigDecimal taxTotal) {
        this.taxTotal = taxTotal;
    }

    /**
     * 获取保费总额
     *
     * @return total - 保费总额
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * 设置保费总额
     *
     * @param total 保费总额
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * 获取报价保司名称
     *
     * @return quote_insurance_name - 报价保司名称
     */
    public String getQuoteInsuranceName() {
        return quoteInsuranceName;
    }

    /**
     * 设置报价保司名称
     *
     * @param quoteInsuranceName 报价保司名称
     */
    public void setQuoteInsuranceName(String quoteInsuranceName) {
        this.quoteInsuranceName = quoteInsuranceName;
    }

    /**
     * 获取报价保司枚举值 1太保2平安4人保
     *
     * @return quote_source - 报价保司枚举值 1太保2平安4人保
     */
    public String getQuoteSource() {
        return quoteSource;
    }

    /**
     * 设置报价保司枚举值 1太保2平安4人保
     *
     * @param quoteSource 报价保司枚举值 1太保2平安4人保
     */
    public void setQuoteSource(String quoteSource) {
        this.quoteSource = quoteSource;
    }

    /**
     * 获取商业险保单号
     *
     * @return biz_no - 商业险保单号
     */
    public String getBizNo() {
        return bizNo;
    }

    /**
     * 设置商业险保单号
     *
     * @param bizNo 商业险保单号
     */
    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }

    /**
     * 获取交强险保单号
     *
     * @return force_no - 交强险保单号
     */
    public String getForceNo() {
        return forceNo;
    }

    /**
     * 设置交强险保单号
     *
     * @param forceNo 交强险保单号
     */
    public void setForceNo(String forceNo) {
        this.forceNo = forceNo;
    }

    /**
     * 获取报价信息
     *
     * @return quote_result - 报价信息
     */
    public String getQuoteResult() {
        return quoteResult;
    }

    /**
     * 设置报价信息
     *
     * @param quoteResult 报价信息
     */
    public void setQuoteResult(String quoteResult) {
        this.quoteResult = quoteResult;
    }

    /**
     * 获取核保状态描述 核保状态描述（备注字符最大长度：1000）
     *
     * @return SubmitResult - 核保状态描述 核保状态描述（备注字符最大长度：1000）
     */
    public String getSubmitresult() {
        return submitresult;
    }

    /**
     * 设置核保状态描述 核保状态描述（备注字符最大长度：1000）
     *
     * @param submitresult 核保状态描述 核保状态描述（备注字符最大长度：1000）
     */
    public void setSubmitresult(String submitresult) {
        this.submitresult = submitresult;
    }

    public String getProposalNo() {
        return proposalNo;
    }

    public void setProposalNo(String proposalNo) {
        this.proposalNo = proposalNo;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getAdvDiscountRate() {
        return advDiscountRate;
    }

    public void setAdvDiscountRate(String advDiscountRate) {
        this.advDiscountRate = advDiscountRate;
    }

    public String getForceStartTime() {
        return forceStartTime;
    }

    public void setForceStartTime(String forceStartTime) {
        this.forceStartTime = forceStartTime;
    }

    public String getBizStartTime() {
        return bizStartTime;
    }

    public void setBizStartTime(String bizStartTime) {
        this.bizStartTime = bizStartTime;
    }

    public String getForceEcompensationRate() {
        return forceEcompensationRate;
    }

    public void setForceEcompensationRate(String forceEcompensationRate) {
        this.forceEcompensationRate = forceEcompensationRate;
    }

    public String getBizEcompensationRate() {
        return bizEcompensationRate;
    }

    public void setBizEcompensationRate(String bizEcompensationRate) {
        this.bizEcompensationRate = bizEcompensationRate;
    }

    public String getBizPremium() {
        return bizPremium;
    }

    public void setBizPremium(String bizPremium) {
        this.bizPremium = bizPremium;
    }

    public String getBizPremiumByDis() {
        return bizPremiumByDis;
    }

    public void setBizPremiumByDis(String bizPremiumByDis) {
        this.bizPremiumByDis = bizPremiumByDis;
    }

    public String getRealDiscountRate() {
        return realDiscountRate;
    }

    public void setRealDiscountRate(String realDiscountRate) {
        this.realDiscountRate = realDiscountRate;
    }

    public String getNonClaim_discountRate() {
        return nonClaim_discountRate;
    }

    public void setNonClaim_discountRate(String nonClaim_discountRate) {
        this.nonClaim_discountRate = nonClaim_discountRate;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }

    public String getPaymentNotice() {
        return paymentNotice;
    }

    public void setPaymentNotice(String paymentNotice) {
        this.paymentNotice = paymentNotice;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getPayEndDate() {
        return payEndDate;
    }

    public void setPayEndDate(String payEndDate) {
        this.payEndDate = payEndDate;
    }

    public String getPayMsg() {
        return payMsg;
    }

    public void setPayMsg(String payMsg) {
        this.payMsg = payMsg;
    }

    public QuoteInfo() {
    }


    public QuoteInfo(String quoteId) {
        this.quoteId = quoteId;
    }

    public BigDecimal getExcludingDeductibleTotal() {
        return excludingDeductibleTotal;
    }

    public void setExcludingDeductibleTotal(BigDecimal excludingDeductibleTotal) {
        this.excludingDeductibleTotal = excludingDeductibleTotal;
    }

    public String getBuid() {
        return buid;
    }

    public void setBuid(String buid) {
        this.buid = buid;
    }

    public String getTotalRate() {
        return totalRate;
    }

    public void setTotalRate(String totalRate) {
        this.totalRate = totalRate;
    }
}