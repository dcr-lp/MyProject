package com.httpclient;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;


import java.io.IOException;
import java.util.List;


public class Demo2 {

    //private static CookieStore cookieStore;
    CookieStore cookieStore = new BasicCookieStore();
    @Test
    public void testLogin() throws IOException {

        //创建Httpclient对象
        //CloseableHttpClient httpClient = HttpClients.createDefault();

        //CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

        //需要请求的地址为：https://photo-ac.com/en/photo/748760/mother-and-child-drawing-9  ,然后点击下载图片的请求
        //创建httpGet对象，设置url地址：
        HttpPost httpPost = new HttpPost("https://smewyd.webank.com/cpmm-amfront/coreBusinessQuery/login.do");

        String str = "{\"channel_id\":520,\"otp_code\":941156,\"phone_no\":18838250051}";

        //	StringEntity stringEntity = new StringEntity((str), "application/json", "utf-8");  //方法 被弃用了
        StringEntity stringEntity = new StringEntity(str, ContentType.APPLICATION_JSON);        //推荐的方法

        //设置表单的Entity对象到Post请求中
        httpPost.setEntity(stringEntity);

        CloseableHttpResponse response = null;
        try {
            //使用httpClient发起请求 获取 response
            response = httpClient.execute(httpPost);

            List<Cookie> cookieList = cookieStore.getCookies();
            for (Cookie cookie : cookieList) {
                String name = cookie.getName();
                String value = cookie.getValue();
                System.out.println("cookie name = " + name + ", cookie value = " + value);
            }
            //解析响应
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "utf8");
                System.out.println(content);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

            try {
                //关闭httpClient
                httpClient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                //关闭response
                response.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    @Test
    public void testBusinessInfo() {
        //创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(this.cookieStore).build();

        //需要请求的地址为：https://photo-ac.com/en/photo/748760/mother-and-child-drawing-9  ,然后点击下载图片的请求
        //创建httpGet对象，设置url地址：
        HttpPost httpPost = new HttpPost("https://smewyd.webank.com/cpmm-amfront/coreBusinessQuery/queryBusinessInfo.do");

        httpPost.setHeader("cookie", String.valueOf(cookieStore));
        String str = "{\"phone_no\":\"18838250051\",\"channel_id\":\"520\",\"enterprise_name\":\"郑州思念食品有限公司\",\"social_unity_credit_code\":\"\"}";

        //	StringEntity stringEntity = new StringEntity((str), "application/json", "utf-8");  //方法 被弃用了
        StringEntity stringEntity = new StringEntity(str, ContentType.APPLICATION_JSON);		//推荐的方法


        //设置表单的Entity对象到Post请求中
        httpPost.setEntity(stringEntity);

        CloseableHttpResponse response= null;
        try {
            //使用httpClient发起请求 获取 response
            response = httpClient.execute(httpPost);

            //解析响应
            if(response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(),"utf8");
                System.out.println(content);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {

            try {
                //关闭httpClient
                httpClient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                //关闭response
                response.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


    }


}