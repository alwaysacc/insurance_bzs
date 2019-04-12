package com.bzs.utils.encodeUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * @program: insurance_bzs
 * @description: unicode和中文相互转化
 * @author: dengl
 * @create: 2019-04-11 14:45
 */
public class EncodeUtil {
    private static final Logger logger = LoggerFactory.getLogger(EncodeUtil.class);

    /*
     * unicode编码转中文
     */
    public static String unicodeToString(final String dataStr) {
        try {
            final StringBuffer buffer = new StringBuffer(dataStr == null ? "" : dataStr);
            if (StringUtils.isNotBlank(dataStr) && dataStr.contains("\\u")) {
                buffer.delete(0, buffer.length());
                int start = 0;
                int end = 0;
                while (start > -1) {
                    end = dataStr.indexOf("\\u", start + 2);
                    String a = "";//如果夹着非unicode编码的字符串，存放在这
                    String charStr = "";
                    if (end == -1) {
                        if (dataStr.substring(start + 2, dataStr.length()).length() > 4) {
                            charStr = dataStr.substring(start + 2, start + 6);
                            a = dataStr.substring(start + 6, dataStr.length());
                        } else {
                            charStr = dataStr.substring(start + 2, dataStr.length());
                        }
                    } else {
                        charStr = dataStr.substring(start + 2, end);
                    }
                    char letter = (char) Integer.parseInt(charStr.trim(), 16); // 16进制parse整形字符串。
                    buffer.append(new Character(letter).toString());
                    if (StringUtils.isNotBlank(a)) {
                        buffer.append(a);
                    }
                    start = end;
                }
            }
            return buffer.toString();
        } catch (Exception e) {
            logger.error("unicode编码转中文异常", e);
        }
        return dataStr;
    }

    /**
     * 中文转Unicode编码
     *
     * @param gbString
     * @return
     */
    public static String stringToUnicode(final String gbString) {   //gbString = "测试"
        char[] utfBytes = gbString.toCharArray();   //utfBytes = [测, 试]
        String unicodeBytes = "";
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
            String hexB = Integer.toHexString(utfBytes[byteIndex]);   //转换为16进制整型字符串
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        //System.out.println("unicodeBytes is: " + unicodeBytes);
        return unicodeBytes;
    }

    /**
     * 汉字转UTF-8编码
     *
     * @param s
     * @return
     */
    public static String convertStringToUTF8(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        try {
            char c;
            for (int i = 0; i < s.length(); i++) {
                c = s.charAt(i);
                if (c >= 0 && c <= 255) {
                    sb.append(c);
                } else {
                    byte[] b;
                    b = Character.toString(c).getBytes("utf-8");
                    for (int j = 0; j < b.length; j++) {
                        int k = b[j];
                        //转换为unsigned integer  无符号integer
					/*if (k < 0)
						k += 256;*/
                        k = k < 0 ? k + 256 : k;
                        //返回整数参数的字符串表示形式 作为十六进制（base16）中的无符号整数
                        //该值以十六进制（base16）转换为ASCII数字的字符串
                        sb.append(Integer.toHexString(k).toUpperCase());

                        // url转置形式
                        // sb.append("%" +Integer.toHexString(k).toUpperCase());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * UTF-8转汉字
     *
     * @param s
     * @return
     */
    public static String convertUTF8ToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        try {
            s = s.toUpperCase();
            int total = s.length() / 2;
            //标识字节长度
            int pos = 0;
            byte[] buffer = new byte[total];
            for (int i = 0; i < total; i++) {
                int start = i * 2;
                //将字符串参数解析为第二个参数指定的基数中的有符号整数。
                buffer[i] = (byte) Integer.parseInt(s.substring(start, start + 2), 16);
                pos++;
            }
            //通过使用指定的字符集解码指定的字节子阵列来构造一个新的字符串。
            //新字符串的长度是字符集的函数，因此可能不等于子数组的长度。
            return new String(buffer, 0, pos, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 将unicode转换为utf-8
     *
     * @param unicode
     * @return
     */
    public static String UnicodeToUtf8(String unicode) {
        String s = EncodeUtil.convertStringToUTF8(EncodeUtil.unicodeToString(unicode));
        return s;
    }

    public static String UTF8ToUnicode(String utf8Code) {
        //utf-8先转String String再转unicode
        String s = EncodeUtil.stringToUnicode(EncodeUtil.convertUTF8ToString(utf8Code));
        return s;
    }
    public static void main(String[] args) {
        String unicode = "\\u673a\\u52a8\\u8f66\\u635f\\u5931\\u4fdd\\u9669\\u5b98\\u65b9\\u56de\\u590d\\u56de\\u590d \\uff1b\\u2018\\uff1b\\u2018\\u2019\\u20194545枚举";
        String cn = unicodeToString(unicode);
        String unicode2 = stringToUnicode(cn);
        System.out.println(cn);
        System.out.println(unicode2);
    }
}
