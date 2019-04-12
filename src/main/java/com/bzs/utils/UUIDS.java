package com.bzs.utils;

import java.util.Calendar;
import java.util.UUID;

/**
 *
 */
public class UUIDS {

    public static String getDateUUID() {
        Calendar Cld = Calendar.getInstance();
        int YY = Cld.get(Calendar.YEAR);
        int MM = Cld.get(Calendar.MONTH) + 1;
        int DD = Cld.get(Calendar.DATE);
        int HH = Cld.get(Calendar.HOUR_OF_DAY);
        int mm = Cld.get(Calendar.MINUTE);
        int SS = Cld.get(Calendar.SECOND);
        int MI = Cld.get(Calendar.MILLISECOND);
        int number = (int) (Math.random() * 999);
        if (number < 100) {
            number += 100;
        }
        String NMM = "";
        if (MM < 10) {
            NMM = "0" + MM;
        } else {
            NMM = "" + MM;
        }
        String NDD = "";
        if (DD < 10) {
            NDD = "0" + DD;
        } else {
            NDD = "" + DD;
        }
        String NHH = "";
        if (HH < 10) {
            NHH = "0" + HH;
        } else {
            NHH = "" + HH;
        }
        String Nmm = "";
        if (mm < 10) {
            Nmm = "0" + mm;
        } else {
            Nmm = "" + mm;
        }
        String NSS = "";
        if (SS < 10) {
            NSS = "0" + SS;
        } else {
            NSS = "" + SS;
        }
        if (MI < 100) {
            MI += 100;
        }
        System.out.println(MI);
        return YY + "" + NMM + "" + NDD + "" + NHH + "" + Nmm + "" + NSS + "" + MI + "" + number;
    }

    public static String getDateTime() {
        Calendar Cld = Calendar.getInstance();
        int YY = Cld.get(Calendar.YEAR);
        int MM = Cld.get(Calendar.MONTH) + 1;
        String NMM = "";
        if (MM < 10) {
            NMM = "0" + MM;
        } else {
            NMM = "" + MM;
        }

        int DD = Cld.get(Calendar.DATE);
        String NDD = "";
        if (DD < 10) {
            NDD = "0" + DD;
        } else {
            NDD = "" + DD;
        }
        int HH = Cld.get(Calendar.HOUR_OF_DAY);
        String NHH = "";
        if (HH < 10) {
            NHH = "0" + HH;
        } else {
            NHH = "" + HH;
        }
        int mm = Cld.get(Calendar.MINUTE);
        String Nmm = "";
        if (mm < 10) {
            Nmm = "0" + mm;
        } else {
            Nmm = "" + mm;
        }
        int SS = Cld.get(Calendar.SECOND);
        String NSS = "";
        if (SS < 10) {
            NSS = "0" + SS;
        } else {
            NSS = "" + SS;
        }
        return YY + "" + NMM + "" + NDD + "" + NHH + "" + Nmm + "" + NSS;
    }

    public static void main(String[] args) {
        String uuid = getUUID();
        System.out.println(uuid + ",长度=" + uuid.length());

    }

    /**
     * @description 获取UUID 并去除“-”号
     * @return
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }
}
