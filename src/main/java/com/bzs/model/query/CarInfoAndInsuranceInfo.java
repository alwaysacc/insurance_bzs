package com.bzs.model.query;

import com.bzs.model.CarInfo;
import com.bzs.model.InsuranceTypeInfo;
import com.bzs.model.InsuredInfo;
import com.bzs.model.QuoteInfo;
import lombok.Data;

import javax.persistence.Transient;
import java.util.List;

/**
 * @program: insurance_bzs
 * @description: 车辆信息和续保信息
 * @author: dengl
 * @create: 2019-05-16 16:44
 */
@Data
public class CarInfoAndInsuranceInfo extends CarInfo {
    private InsuranceInfoAndType insuredInfo;
    @Transient
    private List<QuoteInfo> quoteInfoList;//报价列表
    private String userName;
    public InsuranceInfoAndType getInsuredInfo() {
        return insuredInfo;
    }

    public void setInsuredInfo(InsuranceInfoAndType insuredInfo) {
        this.insuredInfo = insuredInfo;
    }

    public List<QuoteInfo> getQuoteInfoList() {
        return quoteInfoList;
    }

    public void setQuoteInfoList(List<QuoteInfo> quoteInfoList) {
        this.quoteInfoList = quoteInfoList;
    }
}
