package com.bzs.utils.bihujsontobean;

/**
 * @program: insurance_bzs
 * @description: 壁虎获取报价信息专用
 * @author: dengl
 * @create: 2019-06-04 14:08
 */
public class InsuranceType {
    private double BaoE;
    private double BaoFei;

    public InsuranceType() {
        super();
    }

    public void setBaoE(double BaoE) {
        this.BaoE = BaoE;
    }

    public double getBaoE() {
        return BaoE;
    }

    public void setBaoFei(double BaoFei) {
        this.BaoFei = BaoFei;
    }

    public double getBaoFei() {
        return BaoFei;
    }
}
