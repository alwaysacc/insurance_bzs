package com.bzs.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bzs.model.CardInfo;

public class JuntTest {
    public static void main(String[] args) {
       String msg="{\"realname\":\"孙鹏程\",\"sex\":\"男\",\"nation\":\"汉\",\"born\":\"19950926\",\"address\":\"河南省虞城县王集乡马庄村\",\"idcard\":\"411425199509265771\",\"side\":\"front\",\"orderid\":\"2019070515445637300\"}\n";
       String msg1="{\"begin\":\"20141027\",\"department\":\"虞城县公安局\",\"end\":\"20241027\",\"side\":\"back\",\"orderid\":\"2019070516014960744\"}";
        JSONObject jsonObject= JSON.parseObject(msg);
        JSONObject jsonObject1= JSON.parseObject(msg1);
        CardInfo cardInfo=(CardInfo) JSONObject.toJavaObject(jsonObject,CardInfo.class);
        cardInfo= JSONObject.toJavaObject(jsonObject1,CardInfo.class);
        System.out.println(cardInfo.toString());
    }
}
