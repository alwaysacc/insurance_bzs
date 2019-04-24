package com.bzs.utils.commons;

/**
 * @program: insurance_bzs
 * @description: 第三方接口
 * @author: dengl
 * @create: 2019-04-15 10:15
 */
public interface ThirdAPI {
    //以下是爬虫调用接口
    public static  final String HOST="http://192.168.1.106";

    public static final String PICC_RENEWAL_NAME="picc_xubao";//人保续保接口
    public static final String PICC_QUOTE_NAME="picc_kuaisubaojia";//人保报价接口
    public static final String PICC_QUOTE="picc_quickQuote";//人保报价
    public static final String PICC_QUOTE_ALL="picc_accurateQuote";//人保报价报价保和核保
    public static final String PICC_PAY="picc_zhifu";//人保支付
    public static final String PICC_PORT="5001";

    public static final String CPIC_RENEWAL_NAME="cpic_xubao";//太保续保接口
    public static final String CPIC_QUOTE_NAME="cpic_kuaisubaojia";//太保报价接口
    public static final String CPIC_QUOTE_ALL="cpic_accurateOffer";//太平洋报价核保
    public static final String CPIC_PAY="cpic_zhifu";//太平洋支付
    public static final String PORT="5000";

    public static final String PAIC_RENEWAL_NAME="paic_xubao";//平安续保接口
    public static final String PAIC_QUOTE_NAME="paic_kuaisubaojia";//平安报价接口
    public static final String PAIC_QUOTE_ALL="paic_xubaoOffer";//上一年续保平安，则使用此报价
    //public static final String PAIC_QUOTE_ALL="paic_accurateOffer";
    public static final String PAIC_PAY="paic_zhifu";//平安支付
    public static final String PAIC_PORT="5002";


}