package com.bzs.utils.base64Util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

/**
 * @program: insurance_bzs
 * @description:
 * @author: dengl
 * @create: 2019-06-10 14:54
 */
public class Base64Util {
    /**
     * 加密BASE64
     *
     * @param imagePath
     * @return
     */
    public static String imgToBase64(String imagePath) {
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(imagePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    /**
     * 解密
     *
     * @param base64str
     * @param savepath
     * @return
     */
    public static boolean GenerateImage(String base64str, String savepath) { // 对字节数组字符串进行Base64解码并生成图片
        if (base64str == null) // 图像数据为空
            return false;
        // System.out.println("开始解码");
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(base64str);
            // System.out.println("解码完成");
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            // System.out.println("开始生成图片");
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(savepath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println("base64打印"+imgToBase64("D:/softwarepage/vue/pingan1.jpg"));
    }
}
