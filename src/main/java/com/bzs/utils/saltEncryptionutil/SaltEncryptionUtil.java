package com.bzs.utils.saltEncryptionutil;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @program: insurance_bzs
 * @description: 加密
 * @author: dengl
 * @create: 2019-04-29 16:04
 */
public class SaltEncryptionUtil {
    public static String getEncryptionByName(String userName,String password ){
        //盐值用的用的是对用户名的加密（测试用的"lisi"）
        ByteSource credentialsSalt01 = ByteSource.Util.bytes(userName);
        String encryption ="MD5";
        SimpleHash simpleHash = new SimpleHash(encryption, password,
                credentialsSalt01, 2);//第四个参数加密次数
        System.out.println("加密后的值----->" + simpleHash.toString());
        return simpleHash.toString();
    }

    public static void main(String[] args) {
      String s=  getEncryptionByName("admin","123456");
        System.out.println(s);
    }
}
