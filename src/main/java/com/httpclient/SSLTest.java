package com.httpclient;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
//import com.xylink.cms.common.util.UrlUtil;
public class SSLTest {
    private static Logger log = Logger.getLogger(SSLTest.class);
    static int TimeOutTime = 20000;
    private static String paramsJson;

    /* final static String cacertFilePath = "/Users/hecj/server.jks";
     final static String cacertFilePassword = "xxx";*/
    @Test
    public void sslTest() {
        //String params = "userName=xxx&password=xxx";
        try {
            sendPost("https://smewyd.webank.com/cpmm-amfront/coreBusinessQuery/getOtp.do");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * POST方式发起http请求
     */
    public static String sendPost(String url) throws Exception {
        long time = System.currentTimeMillis();
        CloseableHttpResponse httpResponse = null;
        CloseableHttpClient httpClient = (CloseableHttpClient) getHttpClient();
        HttpContext httpContext = new BasicHttpContext();
        StringBuffer result = new StringBuffer();
        try {
            HttpPost post = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TimeOutTime)
                    .setConnectTimeout(TimeOutTime).build();// 设置请求和传输超时时间
            post.setConfig(requestConfig);
            log.info("执行post请求..." + post.getURI());
            // 创建参数列表
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            /*if (params != null) {
                Map<String, String> props = UrlUtil.url2map(params);
                for (String key : props.keySet()) {
                    nameValuePairs.add(new BasicNameValuePair(key, props.get(key)));
                }
            }*/
           /* nameValuePairs.add(new BasicNameValuePair("channel_id","520"));
            nameValuePairs.add(new BasicNameValuePair("otp_code",""));*/
            nameValuePairs.add(new BasicNameValuePair("phone_no","18838250051"));
            // url格式编码
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
            post.setEntity(uefEntity);

            //RequestEntity entity = new StringRequestEntity(str, "text/html", "utf-8");
            //post.setRequestEntity(entity);

            // 执行请求
            httpResponse = httpClient.execute(post, httpContext);
            HttpEntity entity = httpResponse.getEntity();
            if (null != entity) {
                result.append(new String(EntityUtils.toString(entity).getBytes("utf-8"), "utf-8"));
            }
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                httpResponse.close();
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.info("http请求时间：" + (System.currentTimeMillis() - time) / 1000d + "s");
        }
        return result.toString();
    }
    /**
     * Create a httpClient instance
     */
    public static HttpClient getHttpClient() {
        CloseableHttpClient client = null;
        try {
            X509TrustManager tm =new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new X509TrustManager[]{tm}, new SecureRandom());
            client = HttpClients.custom().setSslcontext(sslContext)
                    .setHostnameVerifier(SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER).build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return client;
    }
}


