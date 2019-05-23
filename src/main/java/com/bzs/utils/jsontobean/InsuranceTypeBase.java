package com.bzs.utils.jsontobean;

import com.bzs.model.InsuranceTypeInfo;
import com.bzs.utils.UUIDS;
import com.bzs.utils.encodeUtil.EncodeUtil;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: insurance_bzs
 * @description: 投保险种的基础项
 * @author: dengl
 * @create: 2019-04-11 16:47
 */
public class InsuranceTypeBase {
    private String insuranceName;
    private String amount;
    private String bujimianpei;
    private String insuredPremium;
    private String code;

    public InsuranceTypeBase() {
    }

    public String getInsuranceName() {
        return insuranceName;
    }

    public void setInsuranceName(String insuranceName) {
        this.insuranceName = insuranceName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBujimianpei() {
        return bujimianpei;
    }

    public void setBujimianpei(String bujimianpei) {
        this.bujimianpei = bujimianpei;
    }

    public String getInsuredPremium() {
        return insuredPremium;
    }

    public void setInsuredPremium(String insuredPremium) {
        this.insuredPremium = insuredPremium;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    /**
     * @param data
     * @param typeId     投保或报价id
     * @param operatorId 执行人
     * @param infoType   0投保1报价
     * @return
     */
    public static List<InsuranceTypeInfo> getInsuranceTypeInfoList(RenewalData data, String typeId, String operatorId, String infoType) {
        if (null != data) {
            List<InsuranceTypeBase> insuranceTypeBasesList = new ArrayList<InsuranceTypeBase>();
            InsuranceTypeBase a = data.getA();
            InsuranceTypeBase b = data.getB();
            InsuranceTypeBase c = data.getC();
            InsuranceTypeBase d = data.getD();
            InsuranceTypeBase e=data.getE();
            InsuranceTypeBase f=data.getF();
            InsuranceTypeBase g=data.getG();
            InsuranceTypeBase h=data.getH();
            InsuranceTypeBase i=data.getI();
            InsuranceTypeBase j=data.getJ();
            InsuranceTypeBase k=data.getK();
            InsuranceTypeBase l=data.getL();
            InsuranceTypeBase m=data.getM();
            InsuranceTypeBase n=data.getN();
            InsuranceTypeBase o=data.getO();
            InsuranceTypeBase p=data.getP();
            String force=data.getJiaoqiangxian();
            if(StringUtils.isNotBlank(force)){
                InsuranceTypeBase itb=new InsuranceTypeBase();
                itb.setInsuranceName("交强险");
                itb.setAmount("1");
                insuranceTypeBasesList.add(itb);
            }
            insuranceTypeBasesList.add(a);
            insuranceTypeBasesList.add(b);
            insuranceTypeBasesList.add(c);
            insuranceTypeBasesList.add(d);
            insuranceTypeBasesList.add(e);
            insuranceTypeBasesList.add(f);
            insuranceTypeBasesList.add(g);
            insuranceTypeBasesList.add(h);
            insuranceTypeBasesList.add(i);
            insuranceTypeBasesList.add(j);
            insuranceTypeBasesList.add(k);
            insuranceTypeBasesList.add(l);
            insuranceTypeBasesList.add(m);
            insuranceTypeBasesList.add(n);
            insuranceTypeBasesList.add(o);
            insuranceTypeBasesList.add(p);
          return  base(infoType,typeId,operatorId,insuranceTypeBasesList);
        }
        return null;
    }
    public static List<InsuranceTypeInfo> base(String infoType,String typeId,String operatorId,List<InsuranceTypeBase> list){
       List<InsuranceTypeInfo> resultList = new ArrayList<InsuranceTypeInfo>();
        if (list != null&&list.size()>0) {
            for (int i=0;i<list.size();i++ ){
                InsuranceTypeBase base= (InsuranceTypeBase)list.get(i);
                if(null!=base){
                    String uuids=UUIDS.getDateUUID();
                    InsuranceTypeInfo info = new InsuranceTypeInfo(uuids);
                    //险种名称
                   // info.setInsuranceTypeId(uuids);
                    String name=base.getInsuranceName();
                    if(StringUtils.isNotBlank(name)){
                        name = EncodeUtil.unicodeToString(base.getInsuranceName());
                        info.setInsuranceName(name);
                    }else{
                       continue;
                    }
                    //保额
                    String account=base.getAmount();
                    if(StringUtils.isNotBlank(account)){
                        account=account.replaceAll(",","");
                        info.setInsuranceAmount(new BigDecimal(account));
                    }

                    //保费
                    String premium=base.getInsuredPremium();
                    if(StringUtils.isNotBlank(premium)){
                        premium=premium.replaceAll(",","");
                        info.setInsurancePremium(new BigDecimal(premium));
                    }
                    //不计免
                    String bjmp=base.getBujimianpei();
                    if(StringUtils.isNotBlank(bjmp)){
                        BigDecimal bjm = new BigDecimal(bjmp.replaceAll(" ",""));
                        info.setExcludingEeductible(bjm);
                    }
                    info.setInfoType(infoType);
                    info.setTypeId(typeId);
                    info.setCreatedBy(operatorId);
                    resultList.add(info);
                }

            }

        }
        return resultList;
    }
}
