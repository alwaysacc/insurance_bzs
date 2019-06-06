package com.bzs.utils.bihujsontobean;

import java.util.List;

/**
 * @program: insurance_bzs
 * @description: 壁虎获取报价信息专用
 * @author: dengl
 * @create: 2019-06-04 14:06
 */
public class Item {
    private InsuranceType CheDeng;
    private InsuranceType BuJiMianRenYuan;
    private InsuranceType BuJiMianFuJia;
    private long BuId;

    private Long Source;
    private int QuoteStatus;
    private String QuoteResult;
    private String RepeatSubmitResult;
    private InsuranceType CheSun;
    private InsuranceType SanZhe;
    private InsuranceType DaoQiang;
    private InsuranceType SiJi;
    private InsuranceType ChengKe;
    private InsuranceType BoLi;
    private InsuranceType HuaHen;
    private InsuranceType SheShui;
    private InsuranceType ZiRan;
    private InsuranceType BuJiMianCheSun;
    private InsuranceType BuJiMianSanZhe;
    private InsuranceType BuJiMianDaoQiang;
    private InsuranceType BuJiMianChengKe;
    private InsuranceType BuJiMianSiJi;
    private InsuranceType BuJiMianHuaHen;
    private InsuranceType BuJiMianSheShui;
    private InsuranceType BuJiMianZiRan;
    private InsuranceType BuJiMianJingShenSunShi;
    private InsuranceType HcSheBeiSunshi;
    private InsuranceType HcHuoWuZeRen;
    private InsuranceType HcJingShenSunShi;
    private InsuranceType HcSanFangTeYue;
    private InsuranceType HcXiuLiChang;
    private String HcXiuLiChangType;
    private String SubmitStatus;
    private String  SubmitResult;
    private String BizNo;
    private  String  ForceNo;
    private  String ChannelId;


    private double RateFactor1;
    private double RateFactor2;
    private double RateFactor3;
    private double RateFactor4;
    private InsuranceType Fybc;
    private InsuranceType FybcDays;
    private InsuranceType SheBeiSunShi;
    private InsuranceType BjmSheBeiSunShi;

    private List<String> SheBeis;

    private double BizRate;
    private double ForceRate;
    private double BizTotal;
    private double ForceTotal;
    private double TaxTotal;
    private  String  TotalRate;

    private int JiaoQiang;

    private int SeatCount;

    public Item() {
    }

    public InsuranceType getCheDeng() {
        return CheDeng;
    }

    public void setCheDeng(InsuranceType cheDeng) {
        CheDeng = cheDeng;
    }

    public InsuranceType getBuJiMianRenYuan() {
        return BuJiMianRenYuan;
    }

    public void setBuJiMianRenYuan(InsuranceType buJiMianRenYuan) {
        BuJiMianRenYuan = buJiMianRenYuan;
    }

    public InsuranceType getBuJiMianFuJia() {
        return BuJiMianFuJia;
    }

    public void setBuJiMianFuJia(InsuranceType buJiMianFuJia) {
        BuJiMianFuJia = buJiMianFuJia;
    }

    public long getBuId() {
        return BuId;
    }

    public void setBuId(long buId) {
        BuId = buId;
    }

    public Long getSource() {
        return Source;
    }

    public void setSource(Long source) {
        Source = source;
    }

    public int getQuoteStatus() {
        return QuoteStatus;
    }

    public void setQuoteStatus(int quoteStatus) {
        QuoteStatus = quoteStatus;
    }

    public String getQuoteResult() {
        return QuoteResult;
    }

    public void setQuoteResult(String quoteResult) {
        QuoteResult = quoteResult;
    }

    public String getRepeatSubmitResult() {
        return RepeatSubmitResult;
    }

    public void setRepeatSubmitResult(String repeatSubmitResult) {
        RepeatSubmitResult = repeatSubmitResult;
    }

    public InsuranceType getCheSun() {
        return CheSun;
    }

    public void setCheSun(InsuranceType cheSun) {
        CheSun = cheSun;
    }

    public InsuranceType getSanZhe() {
        return SanZhe;
    }

    public void setSanZhe(InsuranceType sanZhe) {
        SanZhe = sanZhe;
    }

    public InsuranceType getDaoQiang() {
        return DaoQiang;
    }

    public void setDaoQiang(InsuranceType daoQiang) {
        DaoQiang = daoQiang;
    }

    public InsuranceType getSiJi() {
        return SiJi;
    }

    public void setSiJi(InsuranceType siJi) {
        SiJi = siJi;
    }

    public InsuranceType getChengKe() {
        return ChengKe;
    }

    public void setChengKe(InsuranceType chengKe) {
        ChengKe = chengKe;
    }

    public InsuranceType getBoLi() {
        return BoLi;
    }

    public void setBoLi(InsuranceType boLi) {
        BoLi = boLi;
    }

    public InsuranceType getHuaHen() {
        return HuaHen;
    }

    public void setHuaHen(InsuranceType huaHen) {
        HuaHen = huaHen;
    }

    public InsuranceType getSheShui() {
        return SheShui;
    }

    public void setSheShui(InsuranceType sheShui) {
        SheShui = sheShui;
    }

    public InsuranceType getZiRan() {
        return ZiRan;
    }

    public void setZiRan(InsuranceType ziRan) {
        ZiRan = ziRan;
    }

    public InsuranceType getBuJiMianCheSun() {
        return BuJiMianCheSun;
    }

    public void setBuJiMianCheSun(InsuranceType buJiMianCheSun) {
        BuJiMianCheSun = buJiMianCheSun;
    }

    public InsuranceType getBuJiMianSanZhe() {
        return BuJiMianSanZhe;
    }

    public void setBuJiMianSanZhe(InsuranceType buJiMianSanZhe) {
        BuJiMianSanZhe = buJiMianSanZhe;
    }

    public InsuranceType getBuJiMianDaoQiang() {
        return BuJiMianDaoQiang;
    }

    public void setBuJiMianDaoQiang(InsuranceType buJiMianDaoQiang) {
        BuJiMianDaoQiang = buJiMianDaoQiang;
    }

    public InsuranceType getBuJiMianChengKe() {
        return BuJiMianChengKe;
    }

    public void setBuJiMianChengKe(InsuranceType buJiMianChengKe) {
        BuJiMianChengKe = buJiMianChengKe;
    }

    public InsuranceType getBuJiMianSiJi() {
        return BuJiMianSiJi;
    }

    public void setBuJiMianSiJi(InsuranceType buJiMianSiJi) {
        BuJiMianSiJi = buJiMianSiJi;
    }

    public InsuranceType getBuJiMianHuaHen() {
        return BuJiMianHuaHen;
    }

    public void setBuJiMianHuaHen(InsuranceType buJiMianHuaHen) {
        BuJiMianHuaHen = buJiMianHuaHen;
    }

    public InsuranceType getBuJiMianSheShui() {
        return BuJiMianSheShui;
    }

    public void setBuJiMianSheShui(InsuranceType buJiMianSheShui) {
        BuJiMianSheShui = buJiMianSheShui;
    }

    public InsuranceType getBuJiMianZiRan() {
        return BuJiMianZiRan;
    }

    public void setBuJiMianZiRan(InsuranceType buJiMianZiRan) {
        BuJiMianZiRan = buJiMianZiRan;
    }

    public InsuranceType getBuJiMianJingShenSunShi() {
        return BuJiMianJingShenSunShi;
    }

    public void setBuJiMianJingShenSunShi(InsuranceType buJiMianJingShenSunShi) {
        BuJiMianJingShenSunShi = buJiMianJingShenSunShi;
    }

    public InsuranceType getHcSheBeiSunshi() {
        return HcSheBeiSunshi;
    }

    public void setHcSheBeiSunshi(InsuranceType hcSheBeiSunshi) {
        HcSheBeiSunshi = hcSheBeiSunshi;
    }

    public InsuranceType getHcHuoWuZeRen() {
        return HcHuoWuZeRen;
    }

    public void setHcHuoWuZeRen(InsuranceType hcHuoWuZeRen) {
        HcHuoWuZeRen = hcHuoWuZeRen;
    }

    public InsuranceType getHcJingShenSunShi() {
        return HcJingShenSunShi;
    }

    public void setHcJingShenSunShi(InsuranceType hcJingShenSunShi) {
        HcJingShenSunShi = hcJingShenSunShi;
    }

    public InsuranceType getHcSanFangTeYue() {
        return HcSanFangTeYue;
    }

    public void setHcSanFangTeYue(InsuranceType hcSanFangTeYue) {
        HcSanFangTeYue = hcSanFangTeYue;
    }

    public InsuranceType getHcXiuLiChang() {
        return HcXiuLiChang;
    }

    public void setHcXiuLiChang(InsuranceType hcXiuLiChang) {
        HcXiuLiChang = hcXiuLiChang;
    }

    public String getHcXiuLiChangType() {
        return HcXiuLiChangType;
    }

    public void setHcXiuLiChangType(String hcXiuLiChangType) {
        HcXiuLiChangType = hcXiuLiChangType;
    }

    public String getSubmitStatus() {
        return SubmitStatus;
    }

    public void setSubmitStatus(String submitStatus) {
        SubmitStatus = submitStatus;
    }

    public String getSubmitResult() {
        return SubmitResult;
    }

    public void setSubmitResult(String submitResult) {
        SubmitResult = submitResult;
    }

    public String getBizNo() {
        return BizNo;
    }

    public void setBizNo(String bizNo) {
        BizNo = bizNo;
    }

    public String getForceNo() {
        return ForceNo;
    }

    public void setForceNo(String forceNo) {
        ForceNo = forceNo;
    }

    public String getChannelId() {
        return ChannelId;
    }

    public void setChannelId(String channelId) {
        ChannelId = channelId;
    }

    public double getRateFactor1() {
        return RateFactor1;
    }

    public void setRateFactor1(double rateFactor1) {
        RateFactor1 = rateFactor1;
    }

    public double getRateFactor2() {
        return RateFactor2;
    }

    public void setRateFactor2(double rateFactor2) {
        RateFactor2 = rateFactor2;
    }

    public double getRateFactor3() {
        return RateFactor3;
    }

    public void setRateFactor3(double rateFactor3) {
        RateFactor3 = rateFactor3;
    }

    public double getRateFactor4() {
        return RateFactor4;
    }

    public void setRateFactor4(double rateFactor4) {
        RateFactor4 = rateFactor4;
    }

    public InsuranceType getFybc() {
        return Fybc;
    }

    public void setFybc(InsuranceType fybc) {
        Fybc = fybc;
    }

    public InsuranceType getFybcDays() {
        return FybcDays;
    }

    public void setFybcDays(InsuranceType fybcDays) {
        FybcDays = fybcDays;
    }

    public InsuranceType getSheBeiSunShi() {
        return SheBeiSunShi;
    }

    public void setSheBeiSunShi(InsuranceType sheBeiSunShi) {
        SheBeiSunShi = sheBeiSunShi;
    }

    public InsuranceType getBjmSheBeiSunShi() {
        return BjmSheBeiSunShi;
    }

    public void setBjmSheBeiSunShi(InsuranceType bjmSheBeiSunShi) {
        BjmSheBeiSunShi = bjmSheBeiSunShi;
    }

    public List<String> getSheBeis() {
        return SheBeis;
    }

    public void setSheBeis(List<String> sheBeis) {
        SheBeis = sheBeis;
    }

    public double getBizRate() {
        return BizRate;
    }

    public void setBizRate(double bizRate) {
        BizRate = bizRate;
    }

    public double getForceRate() {
        return ForceRate;
    }

    public void setForceRate(double forceRate) {
        ForceRate = forceRate;
    }

    public double getBizTotal() {
        return BizTotal;
    }

    public void setBizTotal(double bizTotal) {
        BizTotal = bizTotal;
    }

    public double getForceTotal() {
        return ForceTotal;
    }

    public void setForceTotal(double forceTotal) {
        ForceTotal = forceTotal;
    }

    public double getTaxTotal() {
        return TaxTotal;
    }

    public void setTaxTotal(double taxTotal) {
        TaxTotal = taxTotal;
    }

    public String getTotalRate() {
        return TotalRate;
    }

    public void setTotalRate(String totalRate) {
        TotalRate = totalRate;
    }

    public int getJiaoQiang() {
        return JiaoQiang;
    }

    public void setJiaoQiang(int jiaoQiang) {
        JiaoQiang = jiaoQiang;
    }

    public int getSeatCount() {
        return SeatCount;
    }

    public void setSeatCount(int seatCount) {
        SeatCount = seatCount;
    }
}
