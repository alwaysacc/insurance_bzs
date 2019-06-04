package com.bzs.utils.bihujsontobean;

/**
 * @program: insurance_bzs
 * @description: 壁虎获取报价信息专用
 * @author: dengl
 * @create: 2019-06-04 14:08
 */
public class InsuranceType {
    private int BaoE;
    private int BaoFei;

    public InsuranceType() {
        super();
    }

    public void setBaoE(int BaoE) {
        this.BaoE = BaoE;
    }

    public int getBaoE() {
        return BaoE;
    }

    public void setBaoFei(int BaoFei) {
        this.BaoFei = BaoFei;
    }

    public int getBaoFei() {
        return BaoFei;
    }
}
