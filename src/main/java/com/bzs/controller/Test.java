package com.bzs.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bzs.utils.BiHu.ResultData;
import com.bzs.utils.BiHu.UserInfo;
import com.bzs.utils.MD5Utils;
import com.bzs.utils.ResultGenerator;
import com.bzs.utils.commons.ThirdAPI;
import com.bzs.utils.httpUtil.HttpClientUtil;
import com.bzs.utils.httpUtil.HttpResult;
import com.google.gson.JsonArray;

public class Test {
    public static void main(String[] args) {
        String a="苏AFX869";
        String param= ThirdAPI.BEFORE+"LicenseNo="+a+"&CarVin="+"WP1AG2920HKA10588"+ThirdAPI.AFTER;
        String SecCode = MD5Utils.md5(param + "d7eb7d66997");
        System.out.println(SecCode);
        param = param + "&SecCode=" + SecCode;
        System.out.println(param);
        String url=ThirdAPI.BIHUXUBAO+param;
        System.out.println(url);
        HttpResult httpResult = null;
        try {
            httpResult = HttpClientUtil.doGet(url,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(httpResult);
        System.out.println(httpResult.getCode());
        ResultData resultData= JSONObject.parseObject(httpResult.getBody(),ResultData.class);
//        UserInfo userInfo=resultData.getUserInfo();
//        System.out.println(ResultGenerator.genSuccessResult(resultData));
//        System.out.println(111);
//        System.out.println(ResultGenerator.genSuccessResult(userInfo));
//        System.out.println(111);
       /* String b="{\"UserInfo\":{\"CarUsedType\":1,\"LicenseNo\":\"苏ASY000\",\"LicenseOwner\":\"袁文清\",\"InsuredName\":\"袁文清\",\"PostedName\":\"袁文清\",\"IdType\":1,\"CredentislasNum\":\"320113196711090444\",\"CityCode\":8,\"EngineNo\":\"CJT300053\",\"ModleName\":\"凯宴CAYENNE 3.0T\",\"CarVin\":\"WP1AG2920HKA10588\",\"RegisterDate\":\"2017-07-01\",\"ForceExpireDate\":\"2019-06-30\",\"BusinessExpireDate\":\"2019-06-30\",\"NextForceStartDate\":\"2019-06-30\",\"NextBusinessStartDate\":\"2019-07-01\",\"PurchasePrice\":868000,\"SeatCount\":5,\"FuelType\":0,\"ProofType\":0,\"LicenseColor\":0,\"ClauseType\":0,\"RunRegion\":0,\"InsuredIdCard\":\"320113196711090444\",\"InsuredIdType\":1,\"InsuredMobile\":\"\",\"HolderIdCard\":\"320113196711090444\",\"HolderIdType\":1,\"HolderMobile\":\"\",\"RateFactor1\":0.85,\"RateFactor2\":0.75,\"RateFactor3\":0.85,\"RateFactor4\":1.0,\"IsPublic\":2,\"AutoMoldCode\":\"BSAABI0044\",\"Organization\":\"\"},\"SaveQuote\":{\"Source\":1,\"CheSun\":805504,\"SanZhe\":1000000,\"DaoQiang\":0,\"SiJi\":0,\"ChengKe\":0,\"BoLi\":0,\"HuaHen\":0,\"SheShui\":0,\"ZiRan\":0,\"BuJiMianCheSun\":1,\"BuJiMianSanZhe\":1,\"BuJiMianDaoQiang\":0,\"BuJiMianChengKe\":0,\"BuJiMianSiJi\":0,\"BuJiMianHuaHen\":0,\"BuJiMianSheShui\":0,\"BuJiMianZiRan\":0,\"BuJiMianJingShenSunShi\":0,\"HcSanFangTeYue\":0,\"HcJingShenSunShi\":0,\"HcXiuLiChang\":\"0\",\"HcXiuLiChangType\":\"-1\"},\"CustKey\":\"bzs20171117\",\"BusinessStatus\":1,\"StatusMessage\":\"续保成功\"}";
        JSONObject object=JSONObject.parseObject(b);
        object.getString("SaveQuote");
        JSONObject quote=JSONObject.parseObject(object.getString("SaveQuote"));
        System.out.println(quote.getString("CheSun"));
        if(Integer.valueOf(quote.getString("CheSun"))!=0){
            System.out.println("对");
        }
        if (quote.isEmpty()){
            System.out.println();
        }
        System.out.println(quote.getString("CheSun"));*/
    }
}
