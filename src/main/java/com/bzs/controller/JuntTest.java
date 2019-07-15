package com.bzs.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.asymmetric.Sign;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bzs.model.CardInfo;
import com.bzs.utils.QiniuCloudUtil;
import com.bzs.utils.juheUtil.JuHeHttpUtil;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.qiniu.util.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static org.bouncycastle.crypto.tls.MACAlgorithm.hmac_sha1;

public class JuntTest {
  /*  public static void main(String[] args) {
      *//* String msg="{\"realname\":\"孙鹏程\",\"sex\":\"男\",\"nation\":\"汉\",\"born\":\"19950926\",\"address\":\"河南省虞城县王集乡马庄村\",\"idcard\":\"411425199509265771\",\"side\":\"front\",\"orderid\":\"2019070515445637300\"}\n";
       String msg1="{\"begin\":\"20141027\",\"department\":\"虞城县公安局\",\"end\":\"20241027\",\"side\":\"back\",\"orderid\":\"2019070516014960744\"}";
        JSONObject jsonObject= JSON.parseObject(msg);
        JSONObject jsonObject1= JSON.parseObject(msg1);
        CardInfo cardInfo=(CardInfo) JSONObject.toJavaObject(jsonObject,CardInfo.class);
        cardInfo= JSONObject.toJavaObject(jsonObject1,CardInfo.class);
        System.out.println(cardInfo.toString());*//*
       *//* String result=JuHeHttpUtil.telecom("孙鹏程","411425199509265771","15518727891",1,1,1,"1");
//       {"realname":"孙鹏程","mobile":"15518727891","idcard":"411425199509265771","res":1,"resmsg":"三要素身份验证一致","orderid":"J201907111432204621iH","type":"联通","province":"河南省","city":"商丘市","rescode":"11"}

        System.out.println(result);*//*
*//*        try {
            String path = "D:\\123.jpg";
            File file = new File(path,"-"+".jpg");
            System.out.println(QiniuCloudUtil.put64image("123", (MultipartFile) file));
        } catch (Exception e) {
            e.printStackTrace();
        }*//*
        String url="http://img.cdn.baozhishun.com/哈哈哈.jpg";
        long deadline = System.currentTimeMillis() + 3600;
        StringBuilder b = new StringBuilder();
        b.append(url);
        int pos = url.indexOf("?");
        if (pos > 0) {
            b.append("&e=");
        } else {
            b.append("?e=");
        }
        b.append(deadline);
        Mac mac = null;
        try {
            mac = Mac.getInstance("HmacSHA1");
            byte[] sk = StringUtils.utf8Bytes("xXEptltAH3yYAZ7pMm49-ioRNi_uPC2-C225QWXQ");
            SecretKeySpec secretKeySpec = new SecretKeySpec(sk, "HmacSHA1");
            mac.init(secretKeySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        b.append("&token=");

        String encodedSign = UrlSafeBase64.encodeToString(mac.doFinal(StringUtils.utf8Bytes(b.toString())));
        String token="tv0LcKvcugd3Uu2NaiS7h8pIVDKe3qTGM5ken33x" + ":" + encodedSign;
        b.append(token);
        url= b.toString();
        System.out.println(url);
        System.out.println(DateUtil.date(deadline));
    *//*    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(String.valueOf(DateUtil.offsetDay(new Date(), 3)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();
        url=url+"?e="+ts;*//*
//        Sign sign = hmac_sha1(url, "tv0LcKvcugd3Uu2NaiS7h8pIVDKe3qTGM5ken33x");
//        EncodedSign = urlsafe_base64_encode(Sign);
//        System.out.println(ts);
//        System.out.println(url);
//        System.out.println(DateUtil.date(ts));
    }*/
    String ak = "tv0LcKvcugd3Uu2NaiS7h8pIVDKe3qTGM5ken33x";
    String sk = "xXEptltAH3yYAZ7pMm49-ioRNi_uPC2-C225QWXQ";    // 密钥配置
    Auth auth = Auth.create(ak, sk);    // TODO Auto-generated constructor stub
    String bucketname = "bts";    //空间名
    String key = "999";    //上传的图片名
    public String getUpToken() {
        return auth.uploadToken(bucketname, null, 3600, new StringMap().put("insertOnly", 1));
    }
    public void put64image() throws Exception {
        String file = "blob:http://192.168.1.12:8080/63822cb8-bdc4-4b53-914b-1031dee9b330";//图片路径
        FileInputStream fis = null;
        int l = (int) (new File(file).length());
        byte[] src = new byte[l];
        fis = new FileInputStream(new File(file));
        fis.read(src);
        String file64 = Base64.encodeToString(src, 0);
        String url = "http://upload.qiniup.com/putb64/" + l+"/key/"+ UrlSafeBase64.encodeToString(key);
        //非华东空间需要根据注意事项 1 修改上传域名
        RequestBody rb = RequestBody.create(null, file64);
        Request request = new Request.Builder().
                url(url).
                addHeader("Content-Type", "application/octet-stream")
                .addHeader("Authorization", "UpToken " + getUpToken())
                .post(rb).build();
        System.out.println(request.headers());
        OkHttpClient client = new OkHttpClient();
        okhttp3.Response response = client.newCall(request).execute();
        System.out.println(response);
    }
    public static void main(String[] args) throws Exception {
        Date date = DateUtil.date();
//获得年的部分
        DateUtil.year(date);
//获得月份，从0开始计数
        DateUtil.month(date);
//获得月份枚举
        DateUtil.monthEnum(date);
        System.out.println(DateUtil.month(date));
        System.out.println(DateUtil.dayOfMonth(date));
    }
}
