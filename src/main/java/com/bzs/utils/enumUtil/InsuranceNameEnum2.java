package com.bzs.utils.enumUtil;

public enum InsuranceNameEnum2 {
    CPIC(1L),
    PAIC(2L),
    PICC(4L),
    ZKIC(8L),
    YGBX(16L),
    CCIC(32L),
    CLPC(64L),
    HAIC(128L),
    HTIC(256L),
    TPIC(512L),
    TAIC(1024L),//安盛天安
    CICP(2048L)
    ;
    private final Long code;
    InsuranceNameEnum2(Long code) {
        this.code=code;
    }


}
