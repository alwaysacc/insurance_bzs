package com.bzs.utils.commons;

/**
 * @program: insurance_bzs
 * @description: 去除字符串中的空格
 * @author: dengl
 * @create: 2019-04-15 11:18
 */
public class ReplaceUtil {
    //q去除首位空格
    public static String trim(String str){
        return  str.trim();
    }
    //q去除所有空格包括首尾、中间
    public static String trimAll(String str){
        return  str.replaceAll(" ", "");
    }

    //q去除 可以替换大部分空白字符， 不限于空格
    //\s 可以匹配空格、制表符、换页符等空白字符的其中任意一个
    public static String trimAllMore(String str){
        return  str.replaceAll("\\s*", "");
    }
}
