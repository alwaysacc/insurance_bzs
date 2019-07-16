package com.bzs.utils.base64Util;

import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

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

    public static String ImageToBase64(File file){
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
//        String imgFile = "E:\\photo\\timg.png";//待处理的图片
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try{
            in = new FileInputStream(file);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        //对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        //返回Base64编码过的字节数组字符串
        return encoder.encode(data);
    }

    //base64字符串转化成图片
    public static boolean Base64ToImage(String imgStr){
        //对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) //图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try{
            //Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for(int i=0;i<b.length;++i){
                if(b[i]<0){//调整异常数据
                    b[i]+=256;
                }
            }
            //生成png图片
            String imgFilePath = "E:\\photo\\new_timg.png";//新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        }catch (Exception e){
            return false;
        }
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
    public static String encodeImgageToBase64(String remark) {
        ByteArrayOutputStream outputStream = null;
        try {
            URL url = new URL(remark);
            BufferedImage bufferedImage = ImageIO.read(url);
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage,"jpg",outputStream);
        } catch (IOException e) {
            return remark;
        }
        BASE64Encoder encoder = new BASE64Encoder();
        String s= encoder.encode(outputStream.toByteArray());
//s = s.replaceAll("\\r\\n","");
//return encoder.encode(outputStream.toByteArray());// 返回Base64编码过的字节数组字符串
        return s;
    }
    public static void main(String[] args) {
        System.out.println("base64打印"+imgToBase64("D:/softwarepage/vue/pingan1.jpg"));
    }
}
