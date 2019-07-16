package com.bzs.utils;

import cn.hutool.core.util.StrUtil;
import com.bzs.controller.JuntTest;
import com.bzs.utils.base64Util.Base64Util;
import com.bzs.utils.juheUtil.JuHeHttpUtil;
import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.Base64;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @author Exrickx
 */
public class QiniuCloudUtil {

    private final static Logger log = LoggerFactory.getLogger(QiniuCloudUtil.class);

    /**
     * 生成上传凭证，然后准备上传
     */
    // 设置需要操作的账号的AK和SK
    private static final String ACCESS_KEY = "tv0LcKvcugd3Uu2NaiS7h8pIVDKe3qTGM5ken33x";
    private static final String SECRET_KEY = "xXEptltAH3yYAZ7pMm49-ioRNi_uPC2-C225QWXQ";

    // 要上传的空间
    private static final String bucketname = "bts";

    // 密钥
    private static final Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

    private static final String DOMAIN = "img.cdn.baozhishun.com";

    private static final String style = "自定义的图片样式";

    public static String getUpToken() {
        return auth.uploadToken(bucketname, null, 3600, new StringMap().put("insertOnly", 0));
    }

    public static String put64image(MultipartFile file, String name){
        Configuration cfg = new Configuration(Zone.zone0());
        UploadManager uploadManager = new UploadManager(cfg);
        String upToken = auth.uploadToken(bucketname,name);
        Response response = null;
        DefaultPutRet putRet=null;
        try {
            response = uploadManager.put(file.getBytes(), name, upToken);
//            putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        } catch (Exception e) {
            return e.getMessage();
        }
//        String fileName=file.getOriginalFilename();
//        String fileTyle=fileName.substring(fileName.lastIndexOf("."),fileName.length());
        return "http://"+DOMAIN+"/"+name;
    }

    public String getDownloadUrl(String targetUrl) {
        String downloadUrl = auth.privateDownloadUrl(targetUrl);
        return downloadUrl;
    }

    /**
     * 下载
     */
    public void download(String targetUrl) {
        //获取downloadUrl
        String downloadUrl = getDownloadUrl(targetUrl);
        //本地保存路径
        String filePath = "D://";
        download(downloadUrl, filePath);
    }


    /**
     * 通过发送http get 请求获取文件资源
     * @param url
     * @param filepath
     * @return
     */
    private static void download(String url, String filepath) {
        File file = new File("");
        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder().url(url).build();
        okhttp3.Response resp = null;
        try {
            String filePath = file.getCanonicalPath()+"/";
            resp = client.newCall(req).execute();
            System.out.println(resp.isSuccessful());
            if(resp.isSuccessful()) {
                ResponseBody body = resp.body();
                InputStream is = body.byteStream();
                byte[] data = readInputStream(is);
                File imgFile = new File(filePath + "123.png"); //下载到本地的图片命名
                FileOutputStream fops = new FileOutputStream(imgFile);
                fops.write(data);
                fops.close();
                String i=Base64Util.ImageToBase64(imgFile);
                System.out.println(i);
                System.out.println( JuHeHttpUtil.accountVerified(i,"front"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unexpected code " + resp);
        }
    }

    /**
     * 读取字节输入流内容
     * @param is
     * @return
     */
    private static byte[] readInputStream(InputStream is) {
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        byte[] buff = new byte[1024 * 2];
        int len = 0;
        try {
            while((len = is.read(buff)) != -1) {
                writer.write(buff, 0, len);
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toByteArray();
    }


    /**
     * 主函数：测试
     * @param args
     */
    public static void main(String[] args) {
        //构造私有空间的需要生成的下载的链接；
        //格式： http://私有空间绑定的域名/空间下的文件名
        String targetUrl = "http://img.cdn.baozhishun.com/cecb572a22c3eabca16839564e941cd.jpg";
        String img=Base64Util.encodeImgageToBase64(targetUrl);
        System.out.println(img);
        System.out.println(JuHeHttpUtil.accountVerified(img,"back"));

        String filePath = "D://";//外链域名下的图片路径
//        new QiniuCloudUtil().download(targetUrl,filePath);
    }
}
