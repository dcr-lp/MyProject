package com.httpclient;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 * HTTP工具箱
 *
 * @author gxw
 */
public final class HttpTookit {
    private static Logger log = LoggerFactory.getLogger(HttpTookit.class);

    private HttpTookit() {
    }

    /**
     * 执行一个HTTP POST请求，返回请求响应的HTML
     *
     * @param url
     *            请求的URL地址
     * @param params
     *            请求的查询参数,可以为null
     * @return 返回请求响应的HTML
     */
    public static String doPost(String url, Map<String, String> params) {
        return doPost(url, params, null);
    }

    /**
     * 执行一个HTTP POST请求，返回请求响应的HTML
     *
     * @param url
     *            请求的URL地址
     * @param params
     *            请求的查询参数,可以为null
     * @param headers
     *            请求自定义header,可以为null
     * @return 返回请求响应的HTML
     */
    public static String doJsonPost(String url, String params, Map<String, String> headers) {
        String submitResult = null;
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(url);

        client.getParams().setContentCharset("UTF-8");
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> e : headers.entrySet()) {
                method.setRequestHeader(e.getKey(), e.getValue());
            }
        }
        try {
            RequestEntity requestEntity = new StringRequestEntity(params, "application/json", "UTF-8");
            method.setRequestEntity(requestEntity);
            client.executeMethod(method);
            submitResult = method.getResponseBodyAsString();
        } catch (IOException e) {
            log.error("执行HTTP Post请求" + url + "时，发生异常！", e);
        }
        log.info("请求结果:".concat(submitResult));
        return submitResult;
    }

    /**
     * 执行一个HTTP POST请求，返回请求响应的HTML
     *
     * @param url
     *            请求的URL地址
     * @param params
     *            请求的查询参数,可以为null
     * @param headers
     *            请求的headers参数,可以为null
     * @return 返回请求响应的HTML
     */
    public static String doPost(String url, Map<String, String> params, Map<String, String> headers) {
        CloseableHttpClient client = null;
        try {
            CookieStore cookieStore = new BasicCookieStore();
            client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
            HttpPost httpPost = new HttpPost(url);

            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, String> elem = (Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
                httpPost.setEntity(entity);
            }

            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> e : headers.entrySet()) {
                    httpPost.setHeader(e.getKey(), e.getValue());
                }
            }
            CloseableHttpResponse response = client.execute(httpPost);

            List<Cookie> cookies = cookieStore.getCookies();
            StringBuilder cookieStr = new StringBuilder();
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                String value = cookie.getValue();
                cookieStr.append(name).append("=").append(value);
                System.out.println("cookie name=" + name + " value=" + value);
            }
            params.put("cookie", cookieStr.toString());
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            System.out.println("执行HTTP Post请求" + url + "时，发生异常！" + e.getMessage());
        }
        return null;
    }

    public static Map<String, String> getParams(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length > 0) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    map.put(paramName, paramValue);
                }
            }
        }
        Set<Map.Entry<String, String>> set = map.entrySet();
        log.debug("==============================================================");
        for (Map.Entry<String, String> entry : set) {
            log.debug(String.format(entry.getKey(), ":", entry.getValue()));
        }
        log.debug("=============================================================");
        return map;
    }
}
