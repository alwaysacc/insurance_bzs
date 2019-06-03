package com.bzs.controller;

import com.bzs.utils.MD5Utils;
import com.bzs.utils.commons.ThirdAPI;
import com.bzs.utils.httpUtil.HttpClientUtil;
import com.bzs.utils.httpUtil.HttpResult;

public class Test {
    public static void main(String[] args) {
        String a="苏ASY000";
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
    }
}
