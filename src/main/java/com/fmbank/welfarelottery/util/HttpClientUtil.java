package com.fmbank.welfarelottery.util;

import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.net.URI;
import java.util.*;

/**
 * @ClassName: HttpClientUtil
 * @Description: httpclient 工具类
 * @author: jc
 * @date: 2021/12/25 17:56
 */
@Component
@Slf4j
public class HttpClientUtil {
    public static String doGet(String url, Map<String, Object> header, Map<String, String> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            if (header != null) {
                header.forEach((k, v) -> {
                    httpGet.addHeader(k, String.valueOf(v));
                });
            }
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

    public static String doGet(String url, Map<String, Object> header) {
        return doGet(url, header, null);
    }

    public static String doPost(String url, Map<String, Object> header, Map<String, String> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, "utf-8");
                httpPost.setEntity(entity);
            }
            if (header != null) {
                header.forEach((k, v) -> {
                    httpPost.addHeader(k, String.valueOf(v));
                });
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return resultString;
    }

    public static String doPost(String url, Map<String, Object> header) {
        return doPost(url, header, null);
    }

    public static String doPostJson(String url, String json) {
        return doPostJson(url, null, json);
    }

    public static String doPostJson(String url, Map<String, Object> header, String json) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            if (header != null) {
                header.forEach((k, v) -> {
                    httpPost.addHeader(k, String.valueOf(v));
                });
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return resultString;
    }

    /**
     * 登录功能
     */
    @Test
    public void login() {
        ArrayList<String> redBalls = BallRandomUtil.getDoubleColorBallNumber(1500);
        HashMap<String, Integer> map = new HashMap<>();
        for (String redBall : redBalls) {
            if (map.containsKey(redBall)) {
                map.put(redBall, map.get(redBall) + 1);
            } else {
                map.put(redBall, 1);
            }
        }
        map.forEach((key,value)->{
            if(map.get(key)>1){
                System.out.println("key:"+ key +"-value-"+ value);
            }
        });
        System.out.println("list size" + redBalls.size());
        System.out.println("map size" + map.size());

    }

}
