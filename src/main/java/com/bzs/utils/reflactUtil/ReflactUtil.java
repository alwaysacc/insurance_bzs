package com.bzs.utils.reflactUtil;

import com.bzs.utils.enumUtil.DataTypeEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @program: insurance_bzs
 * @description:
 * @author: dengl
 * @create: 2019-06-19 16:20
 */
public class ReflactUtil {
    private static Logger loger = LoggerFactory.getLogger(ReflactUtil.class);
    public static String getFiledTypeByFiledName(String filedName, Object obj) {
        try {
            List<Map<String, Object>> list = getFiledsInfo(filedName, true, obj);
            if (CollectionUtils.isNotEmpty(list)) {
                if (list.size() > 0) {
                    return (String) list.get(0).get("type");
                } else {
                    return null;
                }

            } else {
                return null;
            }
        } catch (Exception e) {
            loger.info("属性类型获取异常");
            loger.error(e.getMessage(), e);
        }
        return filedName;

    }



    /**
     * @description 只有filedName有值并且isFiledName=true才查询单个，否则查询所有属性
     * @param filedName
     *            查询的属性名
     * @param isFiledName
     *            是否根据属性名查询
     * @param object
     *            查询的对象
     * @return 对象每个属性的名称、值、类型
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws Exception
     */
    public static List<Map<String, Object>> getFiledsInfo(String filedName,
                                                          boolean isFiledName, Object object) throws NoSuchMethodException,
            SecurityException, Exception {
        // 我们项目的所有实体类都继承BaseDomain （所有实体基类：该类只是串行化一下）
        // 不需要的自己去掉即可
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        if (object != null) {// if (object!=null ) ----begin
            // 拿到该类
            Class<?> clz = object.getClass();
            // 获取实体类的所有属性，返回Field数组
            Field[] fields = clz.getDeclaredFields();

            for (Field field : fields) {// --for() begin
                // System.out.println(field.getGenericType());// 打印该类的所有属性类型
                Map<String, Object> map = new HashMap<String, Object>();
                Object value = null;
                String type = null;
                // 如果类型是String
                if (field.getGenericType().toString()
                        .equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                    type = DataTypeEnum.STRING.getValue();
                    // 拿到该属性的gettet方法
                    /**
                     * 这里需要说明一下：他是根据拼凑的字符来找你写的getter方法的
                     * 在Boolean值的时候是isXXX（默认使用ide生成getter的都是isXXX）
                     * 如果出现NoSuchMethod异常 就说明它找不到那个gettet方法 需要做个规范
                     */
                    Method m = (Method) object.getClass().getMethod(
                            "get" + getMethodName(field.getName()));

                    String val = (String) m.invoke(object);// 调用getter方法获取属性值
                    value = val;

                }

                // 如果类型是Integer
                if (field.getGenericType().toString()
                        .equals("class java.lang.Integer")) {
                    type = DataTypeEnum.INTEGER.getValue();
                    Method m = (Method) object.getClass().getMethod(
                            "get" + getMethodName(field.getName()));
                    Integer val = (Integer) m.invoke(object);
                    value = val;

                }

                // 如果类型是Double
                if (field.getGenericType().toString()
                        .equals("class java.lang.Double")) {
                    type = DataTypeEnum.DOUBLE.getValue();
                    Method m = (Method) object.getClass().getMethod(
                            "get" + getMethodName(field.getName()));
                    Double val = (Double) m.invoke(object);
                    value = val;

                }

                // 如果类型是Boolean 是封装类
                if (field.getGenericType().toString()
                        .equals("class java.lang.Boolean")) {
                    type = DataTypeEnum.BOOLEAN.getValue();
                    Method m = (Method) object.getClass().getMethod(
                            field.getName());
                    Boolean val = (Boolean) m.invoke(object);
                    value = val;

                }

                // 如果类型是boolean 基本数据类型不一样 这里有点说名如果定义名是 isXXX的 那就全都是isXXX的
                // 反射找不到getter的具体名
                if (field.getGenericType().toString().equals("boolean")) {
                    type = DataTypeEnum.BOOLEAN.getValue();
                    Method m = (Method) object.getClass().getMethod(
                            field.getName());
                    Boolean val = (Boolean) m.invoke(object);
                    value = val;

                }
                // 如果类型是Date
                if (field.getGenericType().toString()
                        .equals("class java.util.Date")) {
                    type = DataTypeEnum.DATE.getValue();
                    Method m = (Method) object.getClass().getMethod(
                            "get" + getMethodName(field.getName()));
                    Date val = (Date) m.invoke(object);
                    value = val;
                }
                // 如果类型是Short
                if (field.getGenericType().toString()
                        .equals("class java.lang.Short")) {
                    type = DataTypeEnum.SHORT.getValue();
                    Method m = (Method) object.getClass().getMethod(
                            "get" + getMethodName(field.getName()));
                    Short val = (Short) m.invoke(object);
                    value = val;

                }
                // 如果类型是Long
                if (field.getGenericType().toString()
                        .equals("class java.lang.Long")) {
                    type = DataTypeEnum.LONG.getValue();
                    Method m = (Method) object.getClass().getMethod(
                            "get" + getMethodName(field.getName()));
                    Short val = (Short) m.invoke(object);
                    value = val;

                }
                // 如果类型是Float
                if (field.getGenericType().toString()
                        .equals("class java.lang.Float")) {
                    type = DataTypeEnum.FLOAT.getValue();
                    Method m = (Method) object.getClass().getMethod(
                            "get" + getMethodName(field.getName()));
                    Short val = (Short) m.invoke(object);
                    value = val;

                }
                // 如果类型是Byte
                if (field.getGenericType().toString()
                        .equals("class java.lang.Byte")) {
                    type = DataTypeEnum.BYTE.getValue();
                    Method m = (Method) object.getClass().getMethod(
                            "get" + getMethodName(field.getName()));
                    Short val = (Short) m.invoke(object);
                    value = val;
                }
                map.put("value", value);
                map.put("name", field.getName());
                map.put("type", type);
                // 如果还需要其他的类型请自己做扩展
                if (StringUtils.isNotBlank(filedName) && isFiledName) {
                    if (filedName.equalsIgnoreCase(field.getName())) {
                        listMap.add(map);
                    }
                } else {
                    listMap.add(map);
                }

            }// for() --end

        }// if (object!=null ) ----end
        return listMap;
    }
    /**
     * @description 将map转化为对象
     * @param map
     *            数据源，要被复制到obj中
     * @param obj
     *            目标对象，把map的数据封装到此对象中
     * @return 返回 obj
     */

    public static Object finalObject(Map<String, Object> map, Object obj) {
        Class<?> cla = obj.getClass();
        Field[] fields = cla.getDeclaredFields();
        if (map != null && obj != null) {
            for (Map.Entry<String, Object> entity : map.entrySet()) {
                String name = entity.getKey();
                Object value = entity.getValue();
                for (Field field : fields) {
                    // 设置私有属性可操作
                    field.setAccessible(true);
                    // 属性名称
                    String objname = field.getName();
                    //System.out.println(objname);
                    // 设置get/set后的名称
                    String n = objname.substring(0, 1).toUpperCase()
                            + objname.substring(1);
                    // 获取类型名称
                    String type = field.getGenericType().toString();
                    //System.out.println(type);
                    if (type.equals("class java.lang.String")) {
                        try {
                            // 设置get方法
                            Method m = cla.getMethod("get" + n);
                            // 获取值
                            String objvalue = (String) m.invoke(obj);
                            if (objvalue == null && name.equals(objname)) {
                                m = cla.getMethod("set" + n, String.class);
                                m.invoke(obj, value);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
					/*if (type.equals("class java.math.BigDecimal")) {
						try {
							// 设置get方法
							Method m = cla.getMethod("get" + n);
							// 获取值
							String objvalue = (String) m.invoke(obj);
							if (objvalue == null && name.equals(objname)) {
								m = cla.getMethod("set" + n, BigDecimal.class);
								m.invoke(obj, value);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}*/
                    if (type.equals("class java.lang.Boolean")) {
                        try {
                            Method m = cla.getMethod("get" + n);
                            Boolean objvalue = (Boolean) m.invoke(obj);
                            if (objvalue == null && name.equals(objname)) {
                                m = cla.getMethod("set" + n, Boolean.class);
                                m.invoke(obj, value);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (type.equals("class java.lang.Short")) {
                        try {
                            Method m = cla.getMethod("get" + n);
                            Short objvalue = (Short) m.invoke(obj);
                            if (objvalue == null && name.equals(objname)) {
                                m = cla.getMethod("set" + n, Short.class);
                                m.invoke(obj, value);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (type.equals("class java.lang.Double")) {
                        try {
                            Method m = cla.getMethod("get" + n);
                            Double objvalue = (Double) m.invoke(obj);
                            System.out.println(objvalue);
                            System.out.println(value);
                            if (objvalue == null && name.equals(objname)) {
                                m = cla.getMethod("set" + n, Double.class);
                                m.invoke(obj, (Double)value);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (type.equals("class java.util.Date")) {
                        try {
                            Method m = cla.getMethod("get" + n);
                            Date objvalue = (Date) m.invoke(obj);
                            if (objvalue == null && name.equals(objname)) {
                                m = cla.getMethod("set" + n, Date.class);
                                m.invoke(obj, value);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (type.equals("class java.lang.Integer")) {
                        try {
                            Method m = cla.getMethod("get" + n);
                            Integer objvalue = (Integer) m.invoke(obj);
                            if (objvalue == null && name.equals(objname)) {
                                m = cla.getMethod("set" + n, Integer.class);
                                m.invoke(obj, value);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }
        return obj;
    }


    // 把一个字符串的第一个字母大写、效率是最高的、
    private static String getMethodName(String fildeName) throws Exception {
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }
}
