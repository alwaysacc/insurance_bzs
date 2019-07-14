package com.bzs.utils;

import cn.hutool.core.util.StrUtil;
import com.bzs.controller.JuntTest;
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
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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
}
