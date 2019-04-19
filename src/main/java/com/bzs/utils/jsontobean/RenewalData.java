package com.bzs.utils.jsontobean;

import com.bzs.model.InsuranceTypeInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: insurance_bzs
 * @description: 续保data
 * @author: dengl
 * @create: 2019-04-11 16:08
 */
public class RenewalData {
    private A a;
    private String frameNo;//车架号
    private B b;
    private C c;
    private D d;
    private E e;
    private F f;
    private G g;
    private H h;
    private I i;
    private J j;
    private K k;
    private L l;
    private M m;
    private N n;
    private O o;
    private P p;
    private String name;//车主
    private String vehicleFgwCode;//车辆型号
    private String mobile;//手机号
    private String biPremium;//商业险保费
    private String firstRegisterDate;//车辆注册日期
    private String engineNo;//发动机号
    private String cardID;//车主证件号
    private String biBeginDate;//商业险下次起保日期
    private String ciPremium;//交强险保费
    private String ciBeginDate;//交强险下次起保日期
    private String jiaoqiangxian;//交强险；
   private String source;//上年续保枚举值
   private  String carNo;//车牌号
   private List<InsuranceTypeInfo>list=new ArrayList<>();


    public RenewalData() {
    }

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }

    public String getFrameNo() {
        return frameNo;
    }

    public void setFrameNo(String frameNo) {
        this.frameNo = frameNo;
    }

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }

    public C getC() {
        return c;
    }

    public void setC(C c) {
        this.c = c;
    }

    public D getD() {
        return d;
    }

    public void setD(D d) {
        this.d = d;
    }

    public E getE() {
        return e;
    }

    public void setE(E e) {
        this.e = e;
    }

    public F getF() {
        return f;
    }

    public void setF(F f) {
        this.f = f;
    }

    public G getG() {
        return g;
    }

    public void setG(G g) {
        this.g = g;
    }

    public H getH() {
        return h;
    }

    public void setH(H h) {
        this.h = h;
    }

    public I getI() {
        return i;
    }

    public void setI(I i) {
        this.i = i;
    }

    public J getJ() {
        return j;
    }

    public void setJ(J j) {
        this.j = j;
    }

    public K getK() {
        return k;
    }

    public void setK(K k) {
        this.k = k;
    }

    public L getL() {
        return l;
    }

    public void setL(L l) {
        this.l = l;
    }

    public M getM() {
        return m;
    }

    public void setM(M m) {
        this.m = m;
    }

    public N getN() {
        return n;
    }

    public void setN(N n) {
        this.n = n;
    }

    public O getO() {
        return o;
    }

    public void setO(O o) {
        this.o = o;
    }

    public P getP() {
        return p;
    }

    public void setP(P p) {
        this.p = p;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVehicleFgwCode() {
        return vehicleFgwCode;
    }

    public void setVehicleFgwCode(String vehicleFgwCode) {
        this.vehicleFgwCode = vehicleFgwCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBiPremium() {
        return biPremium;
    }

    public void setBiPremium(String biPremium) {
        this.biPremium = biPremium;
    }

    public String getFirstRegisterDate() {
        return firstRegisterDate;
    }

    public void setFirstRegisterDate(String firstRegisterDate) {
        this.firstRegisterDate = firstRegisterDate;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public String getCardID() {
        return cardID;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    public String getBiBeginDate() {
        return biBeginDate;
    }

    public void setBiBeginDate(String biBeginDate) {
        this.biBeginDate = biBeginDate;
    }

    public String getCiPremium() {
        return ciPremium;
    }

    public void setCiPremium(String ciPremium) {
        this.ciPremium = ciPremium;
    }

    public String getCiBeginDate() {
        return ciBeginDate;
    }

    public void setCiBeginDate(String ciBeginDate) {
        this.ciBeginDate = ciBeginDate;
    }

    public String getJiaoqiangxian() {
        return jiaoqiangxian;
    }

    public void setJiaoqiangxian(String jiaoqiangxian) {
        this.jiaoqiangxian = jiaoqiangxian;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<InsuranceTypeInfo> getList() {
        return list;
    }

    public void setList(List<InsuranceTypeInfo> list) {
        this.list = list;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }
}
