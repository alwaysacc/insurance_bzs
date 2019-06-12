package com.bzs.utils.stringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: insurance_bzs
 * @description: 自己定义的字符串工具类
 * @author: dengl
 * @create: 2019-06-12 09:58
 */
public class StringUtil {
    /**
     * 计算26个英文字母每个字母出现次数
     *
     * @param str
     */
    public static Map<Character, Integer> getEnlishCharacter(String str) {
        //创建26个空间大小的数组，存放26个字母
        int[] nums = new int[26];
        for (char i : str.toCharArray()) {
            //自动将char i转化成ascall码
            if (i >= 97 && i <= 122) {
                //利用数组的索引进行存储
                nums[i - 97]++;
            }
        }
        Map<Character, Integer> hm = new HashMap();
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                //i加上97并且再转化为char类型就可以显示相应的字符
                char j = (char) (i + 97);
                System.out.println(j + "====" + nums[i]);
                hm.put(j, nums[i]);
            }
        }
        return hm;
    }

    //计算每个字符出现的次数
    public static Map getCharacterCount(String str) {
        //将字符串转化为字符数组
        char[] chars = str.toCharArray();
        //创建一个HashMap名为hm
        Map<Character, Integer> hm = new HashMap();
        //定义一个字符串c，循环遍历遍历chars数组
        for (char c : chars) {
            //containsKey(c),当c不存在于hm中
            hm.put(c, hm.containsKey(c) ? hm.get(c) + 1 : 1);
        }
        return hm;
    }

    public static int getCharacterCount(String src, String des) {
        char[] chars = src.toCharArray();
        int count = 0;
        for (char c : chars) {
            if (Character.toString(c).equals(des)) {
                count++;
            }
        }
        System.out.println(count);
        return count;
    }


    public static void main(String[] args) {
        getCharacterCount("hjhj-676878-等k", "-+");
        Map<Character, Integer> map = getEnlishCharacter("hjhj-676878-等k");
        for (Character key : map.keySet()) {
            System.out.println(key + "====" + map.get(key));
            /*if(key.toString().equals("等1")){
                System.out.println("输出次数"+map.get(key));
            }*/
        }
    }
}
