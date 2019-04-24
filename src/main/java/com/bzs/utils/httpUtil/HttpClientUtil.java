package com.bzs.utils.httpUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bzs.utils.jsontobean.PCICResponseBean;
import com.bzs.utils.jsontobean.RenewalBean;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.poi.hssf.record.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.*;

/**
 * @program: insurance_bzs
 * @description: http请求
 * @author: dengl
 * @create: 2019-04-11 09:46
 */
public class HttpClientUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private static final String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36";
    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";

    /**
     * @param strUrl 请求路径，不用加？
     * @param params 参数是json字符串
     * @param method 请求方式，“GET”或者"POST"
     * @return 返回请求内容
     * @throws Exception
     * @description get或者post请求，请求参数是json字符串格式
     */
    public static String net(String strUrl, String params, String method)
            throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if ((method == null || method.equals("GET")) && params != null) {
                strUrl = strUrl + "?" + params;
            }
            System.out.println("URL>>>" + strUrl);
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if (method == null || method.equals("GET")) {
                conn.setRequestMethod("GET");
            } else {
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("content-type", "text/html;charset=UTF-8");
            conn.setRequestProperty(
                    "User-agent",//伪造浏览器请求
                    "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36");
            conn.setUseCaches(false);
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params != null && method.equals("POST")) {
                System.out.println("参数>>>" + params);
                try {
                    DataOutputStream out = new DataOutputStream(
                            conn.getOutputStream());
                    out.writeBytes(params);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    /**
     * description get请求 java原生HttpURLConnection 未调试
     *
     * @param httpurl 请求地址加上参数
     * @return
     * @url https://www.cnblogs.com/hhhshct/p/8523697.html
     */

    public static String doGetHttpUrl(String httpurl) {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(httpurl);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 封装输入流is，并指定字符集
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            connection.disconnect();// 关闭远程连接
        }

        return result;
    }

    /**
     * @param httpUrl
     * @param param
     * @return
     * @description post请求 java原生HttpURLConnection 未调试
     */
    public static String doPostHttpURL(String httpUrl, String param) {

        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        try {
            URL url = new URL(httpUrl);
            // 通过远程url连接对象打开连接
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接请求方式
            connection.setRequestMethod("POST");
            // 设置连接主机服务器超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取主机服务器返回数据超时时间：60000毫秒
            connection.setReadTimeout(60000);

            // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
            connection.setDoInput(true);
            // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
            connection.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
            // 通过连接对象获取一个输出流
            os = connection.getOutputStream();
            // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
            if (StringUtils.isNotBlank(param)) {
                os.write(param.getBytes());
            }

            // 通过连接对象获取一个输入流，向远程读取
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 对输入流对象进行包装:charset根据工作项目组的要求来设置
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                StringBuffer sbf = new StringBuffer();
                String temp = null;
                // 循环遍历一行一行读取数据
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 断开与远程地址url的连接
            connection.disconnect();
        }
        return result;
    }


    /**
     * @param url
     * @param paramMap
     * @param type     请求方式JSON 或者正常请求
     * @return
     * @description apache  httpClient4.5 post请求
     */
    //在使用post请求时，可能传入的参数是json或者其他格式，
    // 此时我们则需要更改请求头及参数的设置信息，
    // 更改下面两列配置：
    // httpPost.setEntity(new StringEntity("你的json串"));
    // httpPost.addHeader("Content-Type", "application/json")。
    public static <T> HttpResult doPost(String url, Map<String, Object> paramMap, String type, Class<T> clz,String jsonStr) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        HttpResult result = new HttpResult();
        // 创建httpClient实例
        httpClient = HttpClients.createDefault();
        // 创建httpPost远程连接实例
        HttpPost httpPost = new HttpPost(url);
        // 配置请求参数实例
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(120000)// 设置连接主机服务超时时间
                .setConnectionRequestTimeout(120000)// 设置连接请求超时时间
                .setSocketTimeout(120000)// 设置读取数据连接超时时间
                .build();
        // 为httpPost实例设置配置
        httpPost.setConfig(requestConfig);
        if (StringUtils.isNotBlank(type) && "JSON".equals(type)) {
            httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
        } else {
            // 设置请求头
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        }
        if(StringUtils.isNotBlank(type) && "JSON".equals(type)){
            if (null != paramMap && paramMap.size() > 0) {
                // 通过map集成entrySet方法获取entity
                Set<Map.Entry<String, Object>> entrySet = paramMap.entrySet();
                // 循环遍历，获取迭代器
                Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
                JSONObject object = new JSONObject();
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> mapEntry = iterator.next();
                    object.put(mapEntry.getKey(), mapEntry.getValue().toString());
                }
                logger.info(jsonStr);
                StringEntity entity = new StringEntity(object.toJSONString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }else if(StringUtils.isNotBlank(jsonStr)) {
                logger.info(jsonStr);
                StringEntity entity = new StringEntity(jsonStr, "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }
        }else {
            // 封装post请求参数
            if (null != paramMap && paramMap.size() > 0) {
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                // 通过map集成entrySet方法获取entity
                Set<Map.Entry<String, Object>> entrySet = paramMap.entrySet();
                // 循环遍历，获取迭代器
                Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
                JSONObject object = new JSONObject();
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> mapEntry = iterator.next();
                    nvps.add(new BasicNameValuePair(mapEntry.getKey(), mapEntry.getValue().toString()));
                    object.put(mapEntry.getKey(), mapEntry.getValue().toString());
                }
                logger.info("请求参数"+object.toJSONString());
                // 为httpPost设置封装好的请求参数
                try {
                  httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    logger.error("调取远程接口异常", e);
                }
            }
        }
        try {
            // httpClient对象执行post请求,并返回响应参数对象
            httpResponse = httpClient.execute(httpPost);
            // 从响应对象中获取响应内容
            HttpEntity entity = httpResponse.getEntity();
            int code = httpResponse.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(entity);
            result.setCode(code);
            result.setBody(body);
           // System.out.println(body);
            logger.info("请求返回的内容>>>"+body);
            String message = "请求失败";
            if (code == 200) {
                message = "请求成功";
                if (null != clz) {
                    try{
                        T o = JSON.parseObject(body, clz);
                        result.setT(o);
                    }catch(Exception e){
                        logger.error("请求成功，JSON转换异常", e);
                    }
                }
            } else  if(code==404){
                result.setMessage("请检查接口地址");
            }else{
                result.setMessage(message);
            }

        } catch (ClientProtocolException e) {
            logger.error("调取远程接口异常", e);
            result.setMessage("接口请求异常");
            return result;
        } catch (SocketTimeoutException e) {
           // logger.error("调取远程接口时间超时", e);
            result.setMessage("接口请求超时");
            return result;
        }catch (HttpHostConnectException e) {
           // logger.error("调取远程接口时间超时", e);
            result.setMessage("接口请求超时");
            logger.info("接口请求异常");
            return result;
        }catch (IOException e) {
          //  logger.error("调取远程接口异常", e);
            result.setMessage("接口请求异常");
            logger.info("接口请求异常");
            return result;
        } finally {
            // 关闭资源
            if (null != httpResponse) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * get请求
     *
     * @param url
     * @param map
     * @return
     * @throws Exception
     */
    public static HttpResult doGet(String url, Map<String, Object> map)
            throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 声明URIBuilder
        URIBuilder uriBuilder = new URIBuilder(url);

        // 判断参数map是否为非空
        if (map != null) {
            // 遍历参数
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                // 设置参数
                uriBuilder.setParameter(entry.getKey(), entry.getValue()
                        .toString());
            }
        }

        // 2 创建httpGet对象，相当于设置url请求地址
        HttpGet httpGet = new HttpGet(uriBuilder.build());

        // 3 使用HttpClient执行httpGet，相当于按回车，发起请求
        CloseableHttpResponse response = httpClient.execute(httpGet);

        // 4 解析结果，封装返回对象httpResult，相当于显示相应的结果
        // 状态码
        // response.getStatusLine().getStatusCode();
        // 响应体，字符串，如果response.getEntity()为空，下面这个代码会报错,所以解析之前要做非空的判断
        // EntityUtils.toString(response.getEntity(), "UTF-8");
        HttpResult httpResult = null;
        // 解析数据封装HttpResult
        if (response.getEntity() != null) {
            httpResult = new HttpResult(response.getStatusLine()
                    .getStatusCode(), EntityUtils.toString(
                    response.getEntity(), "UTF-8"));
        } else {
            httpResult = new HttpResult(response.getStatusLine()
                    .getStatusCode(), "");
        }
        // 返回
        return httpResult;
    }

    //  CloseableHttpClient httpclient = HttpClients.createDefault();
    public static void main(String[] args) {
        String body="{\"state\": \"1\", \"data\": {\"underwritingRate\": \"0.85\", \"advDiscountRate\": \"0.487688\", \"ciEcompensationRate\": \"0.7876\", \"carshipTax\": \"300.00\", \"nonClaimDiscountRate\": \"0.85\", \"ciPremium\": \"855.00\", \"biPremium\": \"4,135.52\", \"proposalNo\": \"QNAJV23Y1419F014069H\", \"payInfo\": {\"payTime\": \"2019-04-16\", \"payUrl\": \"https://w.url.cn/s/A6hGmOU\\n\"}, \"biBeginDate\": \"2019-05-23\", \"channelRate\": \"0.750001\", \"ciBeginDate\": \"2019-05-22\", \"realDiscountRate\": \"0.487688\", \"biPremiumByDis\": \"2,016.85\", \"insurancesList\": [{\"amount\": \"113855\", \"insuranceName\": \"\", \"flag\": \"\", \"standardPremium\": \"2521.52\", \"insuredAmount\": \"Y\", \"insuranceCode\": \"A\", \"insuredPremium\": \"1229.72\"}, {\"amount\": \"500000\", \"insuranceName\": \"\", \"flag\": \"\", \"standardPremium\": \"1614.00\", \"insuredAmount\": \"500000\", \"insuranceCode\": \"B\", \"insuredPremium\": \"787.13\"}], \"carList\": [\"{\\\"actualValue\\\":132900,\\\"displacement\\\":\\\"1598\\\",\\\"engineDesc\\\":\\\"1.6L\\\",\\\"enginecapacity\\\":\\\"1598\\\",\\\"fullWeight\\\":\\\"1235\\\",\\\"moldCharacterCode\\\":\\\"SKAAFD0161\\\",\\\"name\\\":\\\"\\u65af\\u67ef\\u8fbeSVW71615GM\\\",\\\"oriEngineCapacity\\\":\\\"1598\\\",\\\"power\\\":\\\"\\\",\\\"purchaseValue\\\":132900,\\\"remark\\\":\\\"\\u624b\\u81ea\\u4e00\\u4f53 \\u8c6a\\u534e\\u7248 \\u56fd\\u2164\\\",\\\"seatCount\\\":\\\"5\\\",\\\"tonnage\\\":\\\"0\\\",\\\"transmission\\\":\\\"\\u624b\\u81ea\\u4e00\\u4f53\\\",\\\"year\\\":\\\"2018\\\"}\", \"{\\\"actualValue\\\":121900,\\\"displacement\\\":\\\"1598\\\",\\\"engineDesc\\\":\\\"1.6L\\\",\\\"enginecapacity\\\":\\\"1598\\\",\\\"fullWeight\\\":\\\"1235\\\",\\\"moldCharacterCode\\\":\\\"SKAAFD0164\\\",\\\"name\\\":\\\"\\u65af\\u67ef\\u8fbeSVW71615GM\\\",\\\"oriEngineCapacity\\\":\\\"1598\\\",\\\"power\\\":\\\"\\\",\\\"purchaseValue\\\":121900,\\\"remark\\\":\\\"\\u624b\\u81ea\\u4e00\\u4f53 \\u8212\\u9002\\u7248 \\u56fd\\u2164\\\",\\\"seatCount\\\":\\\"5\\\",\\\"tonnage\\\":\\\"0\\\",\\\"transmission\\\":\\\"\\u624b\\u81ea\\u4e00\\u4f53\\\",\\\"year\\\":\\\"2018\\\"}\"], \"trafficTransgressRate\": \"0.9\", \"refId\": \"201806241930025376\", \"biEcompensationRate\": \"0.3494\"}, \"sendTime\": \"2019-04-16\", \"retMsg\": \"\", \"retCode\": \"0000\"}";
        PCICResponseBean o = JSON.parseObject(body,  PCICResponseBean.class);
        System.out.println(o.toString());
    }
}
