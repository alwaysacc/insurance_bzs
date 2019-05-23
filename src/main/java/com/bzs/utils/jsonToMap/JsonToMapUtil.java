package com.bzs.utils.jsonToMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bzs.utils.jsontobean.InsuranceTypeBase;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: insurance_bzs
 * @description: json 转map
 * @author: dengl
 * @create: 2019-04-19 10:05
 */
public class JsonToMapUtil {

    public static Map jsonToMapOne(String jsonStr) {
        if (StringUtils.isNotBlank(jsonStr)) {
            System.out.println("这个是用JSON类来解析JSON字符串!!!");
            Map maps = (Map) JSON.parse(jsonStr);
           /* for (Object map : maps.entrySet()){
                System.out.println(((Map.Entry)map).getKey()+"     " + ((Map.Entry)map).getValue());
            }*/

            return maps;
        } else {
            return null;
        }
    }

    public static Map jsonToMapTow(String jsonStr) {
        System.out.println("这个是用JSON类的parseObject来解析JSON字符串!!!");
        if (StringUtils.isNotBlank(jsonStr)) {
            Map maps = JSON.parseObject(jsonStr);
           /* for (Object obj : maps.keySet()){
                System.out.println("key为："+obj+"值为："+maps.get(obj));
            }*/
            return maps;
        } else {
            return null;
        }
    }

    public static Map jsonToMapThree(String jsonStr) {
        System.out.println("这个是用JSON类的parseObject来解析JSON字符串!!!");
        if (StringUtils.isNotBlank(jsonStr)) {
            Map maps = JSON.parseObject(jsonStr, Map.class);
            /*for (Object obj : maps.keySet()){
                System.out.println("key为："+obj+"值为："+maps.get(obj));
            }*/
            return maps;
        } else {
            return null;
        }
    }

    public static Map jsonToMapFour(String jsonStr) {
        System.out.println("这个是用JSON类的parseObject来解析JSON字符串!!!");
        if (StringUtils.isNotBlank(jsonStr)) {
            Map maps = JSON.parseObject(jsonStr, Map.class);
            /*for (Object obj : maps.keySet()){
                System.out.println("key为："+obj+"值为："+maps.get(obj));
            }*/
            return maps;
        } else {
            return null;
        }
    }

    public static Map jsonToMapFive(String jsonStr) {
        System.out.println("这个是用JSON类的parseObject来解析JSON字符串!!!");
        if (StringUtils.isNotBlank(jsonStr)) {
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            System.out.println("这个是用JSONObject的parseObject方法来解析JSON字符串!!!");
            Map maps = new HashMap();
            for (Object map : jsonObject.entrySet()) {
                maps.put(((Map.Entry) map).getKey(), ((Map.Entry) map).getValue());
                // System.out.println(((Map.Entry)map).getKey()+"  "+((Map.Entry)map).getValue());
            }
            return maps;
        } else {
            return null;
        }
    }

    public static Map jsonToMapSix(String jsonStr) {
        System.out.println("这个是用JSON类的parseObject来解析JSON字符串!!!");
        if (StringUtils.isNotBlank(jsonStr)) {
            Map mapObj = JSONObject.parseObject(jsonStr, Map.class);
            System.out.println("这个是用JSONObject的parseObject方法并执行返回类型来解析JSON字符串!!!");
            for (Object map : mapObj.entrySet()) {
                System.out.println(((Map.Entry) map).getKey() + "  " + ((Map.Entry) map).getValue());
            }
            String strArr = "{{\"0\":\"zhangsan\",\"1\":\"lisi\",\"2\":\"wangwu\",\"3\":\"maliu\"}," +
                    "{\"00\":\"zhangsan\",\"11\":\"lisi\",\"22\":\"wangwu\",\"33\":\"maliu\"}}";
            //JSONArray.parse(strArr);
            System.out.println(mapObj);

            return mapObj;
        } else {
            return null;
        }
    }

    public static Map<String, Object> JSONObjectToMap(String jsonStr) {
        if (null != jsonStr) {
            //JSONObject  jsonObject = JSONObject.parseObject(str);
            Map<String, Object> params = JSONObject.parseObject(jsonStr, new TypeReference<Map<String, Object>>() {
            });
            return params;
        } else {
            return null;
        }
    }
    public static Map bodyJsonToMap(String jsonStr){
        Map maps = jsonToMapThree(jsonStr);
        Map result=new HashMap<String,Object>();
        // List<InsuranceTypeBase> lnsuranceTypeBaseList=new ArrayList<>();
        List<Map<String,Object>> listMap=new ArrayList<>();
        for (Object obj : maps.keySet()) {//第一层
            // System.out.println("key为：" + obj + "值为：" + maps.get(obj));
            if("class com.alibaba.fastjson.JSONObject".equals(maps.get(obj).getClass().toString())){//第一层的data
                JSONObject ss = (JSONObject) maps.get(obj);
                Map mapss = JSONObjectToMap(ss.toJSONString());
                for (Object objs : mapss.keySet()) {
                    if("class com.alibaba.fastjson.JSONObject".equals(mapss.get(objs).getClass().toString())){//第二层中的A、B对象等
                        JSONObject sss = (JSONObject) mapss.get(objs);
                        Map mapsss = JSONObjectToMap(sss.toJSONString());
                        listMap.add(mapsss);
                         /* String reg="[A-Z]";//26个英文字母代表险种
                        Pattern pattern = Pattern.compile(reg);
                        Matcher matcher = pattern.matcher(str);
                        InsuranceTypeBase base=new InsuranceTypeBase();
                        if (matcher.find()) {
                            for (Object objss : mapsss.keySet()) {//全部险种
                                System.out.println("key为：" + objss + "值为：" + mapsss.get(objss));
                                if("amount".equals(objss)){
                                    base.setAmount((String)mapsss.get(objss));
                                }else if("bujimianpei".equals(objss)){
                                        base.setBujimianpei((String)mapsss.get(objss));
                                }else if("bujimianpei".equals(objss)){
                                    base.setInsuranceName((String)mapsss.get(objss));
                                }
                            }
                        }
                        if(null!=base){
                            lnsuranceTypeBaseList.add(base);
                        }*/
                    }else{//第二层中的非对象
                        result.put(objs,mapss.get(objs));
                    }

                }
            }else{//第一层除去data
                result.put(obj,maps.get(obj));
            }
            // result.put("list",lnsuranceTypeBaseList);
            result.put("listMap",listMap);
        }
        return result;
    }
}
