package com.bzs.utils.commons;

/**
 * @program: insurance_bzs
 * @description: 第三方接口
 * @author: dengl
 * @create: 2019-04-15 10:15
 */
public interface ThirdAPI {
    //以下是爬虫调用接口
    public static final String PICC_RENEWAL_NAME="picc_xubao";//人保续保接口
    public static final String PICC_QUOTE_NAME="picc_kuaisubaojia";//人保报价接口

    public static final String CPIC_RENEWAL_NAME="cpic_xubao";//太保续保接口
    public static final String CPIC_QUOTE_NAME="cpic_kuaisubaojia";//太保报价接口

    public static final String PAIC_RENEWAL_NAME="paic_xubao";//平安续保接口
    public static final String PAIC_QUOTE_NAME="paic_kuaisubaojia";//平安报价接口
}
