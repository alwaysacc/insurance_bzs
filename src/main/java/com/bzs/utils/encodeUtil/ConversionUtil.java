package com.bzs.utils.encodeUtil;

import com.bzs.utils.dateUtil.DateUtil;
import org.apache.commons.lang.StringUtils;

/**
 * @program: insurance_bzs
 * @description: 进制转换
 * @author: dengl
 * @create: 2019-06-19 16:34
 */
public class ConversionUtil {
    public static Object StringToObject(String type, String data) {
        Object b=null;
        if (StringUtils.isNotBlank(data)) {
            if (StringUtils.isNotBlank(type)) {
                if (type.equalsIgnoreCase("string")) {
                    b=DateUtil.getStringDate(data);
                } else if (type.equalsIgnoreCase("integer")) {
                    b=Integer.valueOf(data);
                } else if (type.equalsIgnoreCase("double")) {
                    b=Double.valueOf(data);
                } else if (type.equalsIgnoreCase("float")) {
                    b=Float.valueOf(data);
                } else if (type.equalsIgnoreCase("short")) {
                    b=Short.valueOf(data);
                }else if (type.equalsIgnoreCase("byte")) {
                    b=Byte.valueOf(data);
                }else if (type.equalsIgnoreCase("boolean")) {
                    if(data.equalsIgnoreCase("true")){
                        b=true;
                    }else{
                        b=false;
                    }
                }else if (type.equalsIgnoreCase("date")) {
                    b=DateUtil.getStringToDate(data,"yyyy-MM-dd");
                    //b=DateUtil.stringToDate(data);

                }
                return b;
            }else{
                return data;
            }
        } else {
            return null;
        }

    }
}
