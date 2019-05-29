package com.bzs.utils;

import com.qiniu.util.Auth;
import com.qiniu.util.Base64;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;

/**
 * @author Exrickx
 */
public class QiniuCloudUtil {

    private final static Logger log= LoggerFactory.getLogger(QiniuCloudUtil.class);

    /**
     * 生成上传凭证，然后准备上传
     */
    // 设置需要操作的账号的AK和SK
    private static final String ACCESS_KEY = "oUxCo16rl0TBVBqATrtf1UsyXv5gfTtWpfpwk51F";
    private static final String SECRET_KEY = "xuoWjBf86oaT2Jhjo6Ueu8ngSA61Lx_obBs_pp82";

    // 要上传的空间
    private static final String bucketname = "alwaysacc";

    // 密钥
    private static final Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

    private static final String DOMAIN = "alwaysacc.club";

    private static final String style = "自定义的图片样式";
    public String getUpToken() {
        return auth.uploadToken(bucketname, null, 3600, new StringMap().put("insertOnly", 0));
    }
    //base64方式上传
    public String put64image(String img, String key) throws Exception{
        String file = "D:\\rb.png";//图片路径
        FileInputStream fis = null;
        int l = (int) (new File(file).length());
        byte[] src = new byte[l];
        fis = new FileInputStream(new File(file));
        fis.read(src);
        String file64 = Base64.encodeToString(src, 0);
        String url = "http://upload.qiniup.com/putb64/" + -1+"/key/"+ UrlSafeBase64.encodeToString(key);
        //非华东空间需要根据注意事项 1 修改上传域名
        img ="data:image/png;base64,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";
        System.out.println(img.length());
        img=img.replaceAll("data:image/png;base64,","");
        System.out.println(img.length());
        RequestBody rb = RequestBody.create(null, img);
        System.out.println(rb);
        Request request = new Request.Builder().
                url(url).
                addHeader("Content-Type", "application/octet-stream")
                .addHeader("Authorization", "UpToken " + getUpToken())
                .post(rb).build();
        System.out.println(request.headers());
        OkHttpClient client = new OkHttpClient();
        okhttp3.Response response = client.newCall(request).execute();
        System.out.println(response);
      /*  String url = "http://upload.qiniup.com/putb64/" + -1+"/key/"+ UrlSafeBase64.encodeToString(key);
        //非华东空间需要根据注意事项 1 修改上传域名
        RequestBody rb = RequestBody.create(null, img);
        Request request = new Request.Builder().
                url(url).
                addHeader("Content-Type", "application/octet-stream")
                .addHeader("Authorization", "UpToken " + getUpToken())
                .post(rb).build();
        System.out.println(request.headers());
        OkHttpClient client = new OkHttpClient();
        okhttp3.Response response = client.newCall(request).execute();
        System.out.println(response);*/
        //return DOMAIN + key;
        return  DOMAIN+"/"+key;
    }

}
