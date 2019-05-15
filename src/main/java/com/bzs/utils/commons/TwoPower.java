package com.bzs.utils.commons;

/**
 * @program: insurance_bzs
 * @description: 判断一个数是否是2的次幂
 * @author: dengl
 * @create: 2019-05-15 17:24
 */
public class TwoPower {
    /**
     * 递归算法实现
     *
     * @param num
     * @return
     */
    public static int is2Power(long num){
        if(num<2){
            return -1;
        }
        if(num==2){
            return 1;
        }else if(num%2==0){
            return is2Power(num / 2);
        }else{
            return -1;
        }
    }
    /**
     * 位与判断，最快
     *
     * @param num
     * @return
     */
    public static int anotherIs2Power(long num) {
        long s=System.currentTimeMillis();
        if(num < 2)
            return -1;

        if((num & num - 1) == 0 ) {
            long e=System.currentTimeMillis();
            System.out.println("总时间"+(e-s));
            return 1;

        }else {
            long e=System.currentTimeMillis();
            System.out.println("总时间"+(e-s));
            return -1;
        }

    }
    /**
     * 移位判断
     *
     * @param num
     * @return
     */
    public  static int binaryIs2Power(long num) {
        long s=System.currentTimeMillis();
        if(num < 2)
            return -1;

        int temp = 1;
        while (num > temp) {
            temp <<= 1;
        }
        long e=System.currentTimeMillis();
        System.out.println("总时间"+(e-s));
        return temp == num ? 1 : -1;
    }
    public static void main(String[] args) {
        long s=System.currentTimeMillis();
        is2Power(454654644);
        long e=System.currentTimeMillis();
       int ss= anotherIs2Power(454654644);
       System.out.println("结果"+ss);
        int sss= binaryIs2Power(454654644);
        System.out.println("结果"+sss);
    }
}


