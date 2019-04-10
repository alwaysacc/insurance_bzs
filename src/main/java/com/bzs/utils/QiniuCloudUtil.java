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

    private static final String DOMAIN = "pn7mfwsqh.bkt.clouddn.com";

    private static final String style = "自定义的图片样式";
    public String getUpToken() {
        return auth.uploadToken(bucketname, null, 3600, new StringMap().put("insertOnly", 0));
    }
    //base64方式上传
    public String put64image(byte[] base64, String key) throws Exception{
        String file64 = Base64.encodeToString(base64, 0);
        Integer l = base64.length;
        String url = "http://upload.qiniu.com/putb64/" + l + "/key/"+ UrlSafeBase64.encodeToString(key);
        //非华东空间需要根据注意事项 1 修改上传域名
        RequestBody rb = RequestBody.create(null, file64);
        Request request = new Request.Builder().
                url(url).
                addHeader("Content-Type", "application/octet-stream")
                .addHeader("Authorization", "UpToken " + getUpToken())
                .post(rb).build();
        //System.out.println(request.headers());

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        System.out.println(response);
        //如果不需要添加图片样式，使用以下方式
        //return DOMAIN + key;
        return  DOMAIN+"/"+key;
    }

}
