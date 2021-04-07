package com.httpclient;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Demo3 {

    private static BasicCookieStore cookieStore;
    public static void main(String[] args) {
        //创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //需要请求的地址为：https://photo-ac.com/en/photo/748760/mother-and-child-drawing-9  ,然后点击下载图片的请求
        //创建httpGet对象，设置url地址：
        HttpPost httpPost = new HttpPost("https://smewyd.webank.com/cpmm-amfront/coreBusinessQuery/queryBusinessInfo.do");

        httpPost.addHeader("accept","application/json, text/plain, */*");
        httpPost.addHeader("accept-encoding","gzip, deflate, br");
        httpPost.addHeader("accept-language","zh-CN,zh;q=0.9");
        httpPost.addHeader("authorization","0BCmojA2RIMfOo9t3lZDDSklv7sMN8V");
        //httpPost.addHeader("content-length","215");
        httpPost.addHeader("content-type","application/json;charset=UTF-8");
        httpPost.addHeader("cookie"," __stripe_mid=06986f53-e033-4b1b-8d66-a293c203fa3e; _gcl_au=1.1.1964887636.1583134501; _ga=GA1.2.1122723002.1583134501; _gid=GA1.2.401037423.1583134501; __gads=ID=2d22c788acb05249:T=1583138393:S=ALNI_MbNYbfEpm4Fmb8SPRxoNwCmZxu_Dg; G_ENABLED_IDPS=google");
        httpPost.addHeader("referer","https://photo-ac.com/en/photo/748760/mother-and-child-drawing-9");
        httpPost.addHeader("sec-fetch-dest","empty");
        httpPost.addHeader("sec-fetch-mode","cors");
        httpPost.addHeader("sec-fetch-site","same-site");
        httpPost.addHeader("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.122 Safari/537.36");

        String str="{\"phone_no\":\"18838250051\",\"channel_id\":\"520\",\"enterprise_name\":\"郑州思念食品有限公司\",\"social_unity_credit_code\":\"\"}";

        //	StringEntity stringEntity = new StringEntity((str), "application/json", "utf-8");  //方法 被弃用了
        StringEntity stringEntity = new StringEntity(str, ContentType.APPLICATION_JSON);		//推荐的方法


        //设置表单的Entity对象到Post请求中
        httpPost.setEntity(stringEntity);

        CloseableHttpResponse response= null;
        try {
            //使用httpClient发起请求 获取 response
            response = httpClient.execute(httpPost);

            cookieStore  = new BasicCookieStore();
            httpClient = HttpClients.custom()
                    .setDefaultCookieStore(cookieStore)
                    .build();
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
