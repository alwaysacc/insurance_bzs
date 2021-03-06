package com.bzs.utils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: insurance_bzs
 * @description:
 * @author: dengl
 * @create: 2019-06-21 16:12
 */
public class WoDaoPaasJavaDemo {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String accountSid="000000005215478513r526919c7b10d";  //授权令牌
        String appId="00000000658adfee016614df04a804d4";  //应用Id
        String appToken="c104fa6d9d9028cc67a6a0a97e26730b"; //应用Token
        String Caller="15295032076";  //主叫号码
        String Callee="15518727891";  //被叫号码
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String TimeStr=sdf.format(d);
        //https//wdapi.yuntongxin.com
        String urlString="https://wdapi.yuntongxin.vip/20181221/rest/click/call/event/v1?sig="; //坐席外呼请求链接地址
        String  sig = GetMd5( accountSid + ":" + appId  +":"+ TimeStr);
        String auth=GetBase64(appId +":" + appToken +":"+ TimeStr);
        Map<String, String> infos = new HashMap<>();
        infos.put("Appid", appId);
        infos.put("AccountSid", accountSid);
        infos.put("Caller", Caller);
        infos.put("Callee",Callee);
        infos.put("IsDisplayCalleeNbr","false");
        String reString=sendPost(urlString+sig,infos,auth);
        System.out.println(reString);
    }
    public static  String GetMd5(String s)
    {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static  String GetBase64(String  str)
    {
        byte[] b = null;
        String s = null;
        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
        }

        if (b != null) {
            s = Base64.getEncoder().encodeToString(b);
        }
        return s;
    }

    /**
     * 向指定URL发送POST请求
     *
     * @param url
     * @param paramMap
     * @param auth
     * @return 响应结果
     */
    public static String sendPost(String url, Map<String, String> paramMap,String auth) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "application/json");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Authorization", auth);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());

            // 设置请求属性
            String param = "";
            if (paramMap != null && paramMap.size() > 0) {
                Iterator<String> ite = paramMap.keySet().iterator();
                param="{";
                int x=1;
                while (ite.hasNext()) {
                    String key = ite.next();// key
                    String value = paramMap.get(key);
                    if(x==1)
                    {
                        param +="\""+ key + "\":\"" + value + "\"";
                    }
                    else
                    {
                        param +=",\""+ key + "\":\"" + value + "\"";
                    }
                    x++;
                }
                param+="}";
                // param = param.substring(0, param.length() - 1);
            }
            System.out.println("param=" + param);
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            if ((line = in.readLine()) == null) {
                System.out.println("in ==null");
            }
            else
            {
                result+=line;
                //System.out.println("line="+line);
            }
            while ((line = in.readLine()) != null) {
                result += line;
            }
            return result;
        } catch (IOException e) {
            System.err.println("发送 POST 请求出现异常！" + e);
        }

        return result;
    }
}
