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
    public static  final String PORT="5000";
    public static final String salesPerson="杨杰";

    public static  final String PICC_HOST="http://192.168.1.102";
    public static final String PICC_PORT="5001";
    public static final String PICC_RENEWAL_NAME="picc_xubao";//人保续保接口
    public static final String PICC_QUOTE_NAME="picc_kuaisubaojia";//人保报价接口
    public static final String PICC_QUOTE="picc_quickQuote";//人保报价
    public static final String PICC_QUOTE_ALL="picc_accurateQuote";//人保报价和核保
    public static final String PICC_PAY="picc_payConfirm";//人保支付
    public static final String PICC_PAY_CANCEL="picc_zuofei";//人保支付

    public static  final String CPIC_HOST="http://47.103.61.241";
    public static final String CPIC_PORT="5000";
    public static final String CPIC_RENEWAL_NAME="cpic_xubao";//太保续保接口
    public static final String CPIC_QUOTE_NAME="cpic_kuaisubaojia";//太保报价接口
    public static final String CPIC_QUOTE_ALL="cpic_accurateOffer";//太平洋报价核保
    public static final String CPIC_PAY="cpic_zhifu";//太平洋支付
    public static final String CPIC_PAY_CANCEL="cpic_reject";//太保作废

//    public static  final String PAIC_HOST="http://47.103.61.241";
    public static  final String PAIC_HOST="http://192.168.1.106";
    public static final String PAIC_PORT="4000";
    public static final String PAIC_RENEWAL_NAME="paic_xubao";//平安续保接口
    public static final String PAIC_QUOTE_NAME="paic_kuaisubaojia";//平安报价接口
    public static final String PAIC_QUOTE_ALL="paic_xubaoOffer";//上一年续保平安，则使用此报价
    //public static final String PAIC_QUOTE_ALL="paic_accurateOffer";
    public static final String PAIC_PAY="paic_zhifu";//平安支付
    public static final String PAIC_ACCOUNT="JSHSBXDL-00002";
    public static final String PAIC_PWD="NJXXKJ147";
    public static final String PAIC_PAY_CANCEL="cpic_zuofei";//平安作废





    public static final String BIHUXUBAO="http://iu.91bihu.com/api/CarInsurance/getreinfo?";//续保
    public static final String GetNewVehicleInfo="http://iu.91bihu.com/api/CarInsurance/GetNewVehicleInfo?";//续保
    public static final String GetVehicleInfo="http://bao.91bihu.com/DoQuote/GetVehicleInfo?";//续保
    public static final String BEFORE="Group=1&";//
    public static final String AFTER="&CityCode=8&Agent=83696&CustKey=bzs20171117";//
    public static  final String BIHUURL="http://iu.91bihu.com";//壁虎URL
    public static  final String PostPrecisePrice="/api/CarInsurance/PostPrecisePrice?";//报价核保的基础接口
    public static  final String GetSpecialPrecisePrice="/api/CarInsurance/GetSpecialPrecisePrice?";//获取车辆的报价信息,只能单个获取

    public static  final String GetSubmitInfo="/api/CarInsurance/GetSubmitInfo?";//获取车辆的核保信息,只能单个获取

    public static final int AGENT=83696;//在壁虎的唯一标识
    public static final String CUSTKEY="bzs20171117";//Custkey是用来区分每一家合作商户的不同个体的请求的
    public static final String SECRETKEY="d7eb7d66997";//密匙


    public static final String PayURL="http://buc.91bihu.com";//密匙
    public static final String PayAddressURL=PayURL+"/api/PayOut/PayAddress?";//获取支付信息（支付地址）
    public static final String PayResult=PayURL+"/api/PayOut/PayInfo?";//4、	获取支付结果(到账查询)信息
    public static final String VoidPay=PayURL+"/api/PayOut/VoidPay?";//7、作废原支付方式

    public static final String GetContinuedPeriods=BIHUURL+"/api/City/GetContinuedPeriods?";//获取城市渠道续保期
    public static final String GetFirstVehicleInfo=BIHUURL+"/api/CarInsurance/GetFirstVehicleInfo?";//获取新车车型信息接口
    public static final String GetModelName=BIHUURL+"/api/CarInsurance/getmoldname?";//获取新车车型信息接口
    public static  final String GetCreditDetailInfo=BIHUURL+"/api/claim/GetCreditDetailInfo?";//获取车辆出险信息
    public static final String UploadMultipleImg=BIHUURL+"/api/UploadImg/UploadMultipleImg?";//核保上传图片
    //"http://123.207.50.164:5000/query"
    //爬取数据
    public static final String CRAWLINGIP="http://111.231.91.188:5000";

    public static final String CRAWLINGURL=CRAWLINGIP+"/query";//爬取的接口

}
