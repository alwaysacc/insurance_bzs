package com.bzs.utils.bihujsontobean;

import java.util.List;

/**
 * @program: insurance_bzs
 * @description: 出险rootBean
 * @author: dengl
 * @create: 2019-06-10 13:52
 */
public class CreditDetailInfoJsonBean extends  BaseJsonBean{
    private String claimCount;
    private String forceCliamCount;
    private String bizClaimCount;
    private List<CreditDetailInfoItems> list;

    public String getClaimCount() {
        return claimCount;
    }

    public void setClaimCount(String claimCount) {
        this.claimCount = claimCount;
    }

    public String getForceCliamCount() {
        return forceCliamCount;
    }

    public void setForceCliamCount(String forceCliamCount) {
        this.forceCliamCount = forceCliamCount;
    }

    public String getBizClaimCount() {
        return bizClaimCount;
    }

    public void setBizClaimCount(String bizClaimCount) {
        this.bizClaimCount = bizClaimCount;
    }

    public List<CreditDetailInfoItems> getList() {
        return list;
    }

    public void setList(List<CreditDetailInfoItems> list) {
        this.list = list;
    }
}
