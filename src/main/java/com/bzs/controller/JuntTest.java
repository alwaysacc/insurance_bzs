package com.bzs.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.asymmetric.Sign;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bzs.model.CardInfo;
import com.bzs.utils.QiniuCloudUtil;
import com.bzs.utils.juheUtil.JuHeHttpUtil;
import javax.crypto.Mac;

import com.qiniu.util.StringUtils;
import com.qiniu.util.UrlSafeBase64;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static org.bouncycastle.crypto.tls.MACAlgorithm.hmac_sha1;

public class JuntTest {
    public static void main(String[] args) {
      /* String msg="{\"realname\":\"孙鹏程\",\"sex\":\"男\",\"nation\":\"汉\",\"born\":\"19950926\",\"address\":\"河南省虞城县王集乡马庄村\",\"idcard\":\"411425199509265771\",\"side\":\"front\",\"orderid\":\"2019070515445637300\"}\n";
       String msg1="{\"begin\":\"20141027\",\"department\":\"虞城县公安局\",\"end\":\"20241027\",\"side\":\"back\",\"orderid\":\"2019070516014960744\"}";
        JSONObject jsonObject= JSON.parseObject(msg);
        JSONObject jsonObject1= JSON.parseObject(msg1);
        CardInfo cardInfo=(CardInfo) JSONObject.toJavaObject(jsonObject,CardInfo.class);
        cardInfo= JSONObject.toJavaObject(jsonObject1,CardInfo.class);
        System.out.println(cardInfo.toString());*/
       /* String result=JuHeHttpUtil.telecom("孙鹏程","411425199509265771","15518727891",1,1,1,"1");
//       {"realname":"孙鹏程","mobile":"15518727891","idcard":"411425199509265771","res":1,"resmsg":"三要素身份验证一致","orderid":"J201907111432204621iH","type":"联通","province":"河南省","city":"商丘市","rescode":"11"}

        System.out.println(result);*/
/*        try {
            String path = "D:\\123.jpg";
            File file = new File(path,"-"+".jpg");
            System.out.println(QiniuCloudUtil.put64image("123", (MultipartFile) file));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        String url="http://img.cdn.baozhishun.com/哈哈哈.jpg";
        System.out.println(url);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(String.valueOf(DateUtil.offsetDay(new Date(), 3)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();
        url=url+"?e="+ts;
//        Sign sign = hmac_sha1(url, "tv0LcKvcugd3Uu2NaiS7h8pIVDKe3qTGM5ken33x");
//        EncodedSign = urlsafe_base64_encode(Sign);
        System.out.println(ts);
        System.out.println(url);
        System.out.println(DateUtil.date(ts));
    }
  /*  public String privateDownloadUrl(String baseUrl, long expires) {
        long deadline = System.currentTimeMillis() / 1000 + expires;
        return privateDownloadUrlWithDeadline(baseUrl, deadline);
    }

    public String privateDownloadUrlWithDeadline(String baseUrl, long deadline) {
        StringBuilder b = new StringBuilder();
        b.append(baseUrl);
        int pos = baseUrl.indexOf("?");
        if (pos > 0) {
            b.append("&e=");
        } else {
            b.append("?e=");
        }
        b.append(deadline);
        String token = sign(StringUtils.utf8Bytes(b.toString()));
        b.append("&token=");
        b.append(token);
        return b.toString();
    }
//    public String sign(byte[] data) {
////        Mac mac = createMac();
////        String encodedSign = UrlSafeBase64.encodeToString(mac.doFinal(data));
////        return "tv0LcKvcugd3Uu2NaiS7h8pIVDKe3qTGM5ken33x" + ":" + encodedSign;
//    }
*//*    private Mac createMac() {
        Mac mac;
        try {
            mac = javax.crypto.Mac.getInstance("HmacSHA1");
            mac.init(secretKey);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
        return mac;
    }*/
}