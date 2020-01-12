package com.bzs.utils;


import java.util.HashMap;

public class ResultMap extends HashMap {
    private static final char UNDERLINE = '_';
    /**
     * 下划线转驼峰
     */
    public static String lineToHump(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        if (param.indexOf(UNDERLINE) != -1) {
            int len = param.length();
            StringBuilder sb = new StringBuilder(len);
            for (int i = 0; i < len; i++) {
                char c = Character.toLowerCase(param.charAt(i));
                if (c == UNDERLINE) {
                    if (++i < len) {
                        sb.append(Character.toUpperCase(param.charAt(i)));
                    }
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        } else {
            return param;
        }
    }
    @Override
    public Object put(Object key, Object value) {
        key = lineToHump(String.valueOf(key));
        return super.put(key, value);
    }
}
