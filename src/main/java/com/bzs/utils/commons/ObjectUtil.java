package com.bzs.utils.commons;

import com.bzs.model.AccountInfo;
import com.bzs.model.ThirdInsuranceAccountInfo;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @program: insurance_bzs
 * @description: 判断对象是否为空
 * @author: dengl
 * @create: 2019-05-14 13:30
 */
public class ObjectUtil {
    /**
     * 判断属性是否为空 字符串“”判断不为空
     * @param bean
     * @param attributeName 属性名称
     * @return
     */
    public static boolean isEmpty(Object bean, String... attributeName) {
        List<String> list = Arrays.asList(attributeName);
        PropertyDescriptor origDescriptors[] = PropertyUtils.getPropertyDescriptors(bean);
        for (PropertyDescriptor origDescriptor : origDescriptors) {
            String name = origDescriptor.getName();
            if (list.contains(name)) {
                if ("class".equals(name)) {
                    continue;
                }
                if (PropertyUtils.isReadable(bean, name)) {
                    try {
                        Object value = PropertyUtils.getSimpleProperty(bean, name);
                        if (value == null) {
                            continue;
                        } else {
                            return false;
                        }
                    } catch (java.lang.IllegalArgumentException ie) {
                        ;
                    } catch (Exception e) {
                        ;
                    }
                }
            } else {
                continue;
            }
        }
        return true;
    }

    /**
     * 验证实体对象是否为空 字符串“”判断不为空
     * 如果对象属性为空，则判断该对象为空。
     * @param bean
     * @return
     */
    public static boolean isEmpty(Object bean) {
        PropertyDescriptor origDescriptors[] = PropertyUtils.getPropertyDescriptors(bean);
        for (PropertyDescriptor origDescriptor : origDescriptors) {
            String name = origDescriptor.getName();
            if ("class".equals(name)) {
                continue;
            }
            if (PropertyUtils.isReadable(bean, name)) {
                try {
                    Object value = PropertyUtils.getSimpleProperty(bean, name);
                    if (value == null) {
                        continue;
                    } else {
                        return false;
                    }
                } catch (java.lang.IllegalArgumentException ie) {
                    ;
                } catch (Exception e) {
                    ;
                }
            }
        }
        return true;
    }
    /**
     * 判断对象中属性值是否全为空 字符串“”判断为空
     * @param object
     * @return
     */

    public static boolean isEmptyIncludeQuotationMark(Object object) {
        if (null == object) {
            return true;
        }
        try {
            for (Field f : object.getClass().getDeclaredFields()) {
                f.setAccessible(true);
              /*  System.out.print(f.getName() + ":");
                System.out.println(f.get(object));*/
                if (f.get(object) != null && StringUtils.isNotBlank(f.get(object).toString())) {
                    System.out.print(f.getName() + ":");
                    System.out.println(f.get(object));
                    return false;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }



    public static void main(String[] args) {

        ThirdInsuranceAccountInfo a=    new ThirdInsuranceAccountInfo();
        if(null!=a){
            System.out.println("初始化对象后，直接null比较 不为空");//true 空
        }else{
            System.out.println("初始化对象后，直接null比较 空");//true 空
        }
        System.out.println("isEmpty(o)方法,初始化对象"+isEmpty(a));//true 空
        System.out.println("checkObjAllFieldsIsNull(o),初始化对象"+isEmptyIncludeQuotationMark(a));
        a.setStatus("");
        a.setAccountType("");
        System.out.println("checkObjAllFieldsIsNull(o)含有“”"+isEmptyIncludeQuotationMark(a));
        System.out.println("isEmpty(o)方法,含有“”"+isEmpty(a));//true 空
      if(isEmpty(a,"status","accountType","ip")){
          System.out.println("空");
      }else{
          System.out.println("不为空");
      }

    }


}
