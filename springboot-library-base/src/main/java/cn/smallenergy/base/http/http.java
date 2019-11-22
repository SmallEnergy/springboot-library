package cn.smallenergy.base.http;

import cn.smallenergy.base.log.Log;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;

/**
 * httpClient请求工具类 封装常用请求方法
 *
 * @author wangwenchang
 */

public final class HttpClientUtil {

    private static int defaultSocketTimeout = 60 * 1000;
    private static int defaultConnectTimeout = 60 * 1000;
    private static int defaultConnectionRequestTimeout = 60 * 1000;
    private static CloseableHttpClient httpClient;
    private static final int MAX_CONN_PER_ROUTE = 5;
    private static Log log= Log.getInstance();
    private static final RequestConfig defaultRequestConfig = RequestConfig
            .custom()
            .setConnectionRequestTimeout(defaultConnectionRequestTimeout)
            .setSocketTimeout(defaultSocketTimeout)
            .setConnectTimeout(defaultConnectTimeout).build();

    static {
        httpClient = HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig)
                .setMaxConnPerRoute(MAX_CONN_PER_ROUTE)
                .setMaxConnTotal(2 * MAX_CONN_PER_ROUTE).build();
    }

    public static CloseableHttpClient getHttpClientInstance() {
        return httpClient;
    }

    public static CloseableHttpClient getHttpClientInsClient(
            RequestConfig requestConfig) {
        return HttpClients.custom().setDefaultRequestConfig(requestConfig)
                .setMaxConnPerRoute(MAX_CONN_PER_ROUTE)
                .setMaxConnTotal(2 * MAX_CONN_PER_ROUTE).build();
    }

    /**
     * httpClient 发送get请求
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String get(String url, Map<String, String> header) throws Exception {
        CloseableHttpResponse response = null;
        String result = null;
        try {
            HttpGet get = new HttpGet(url);
            if (header != null) {
                for (String key : header.keySet()) {
                    get.setHeader(key, header.get(key));
                }
            }
            response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                log.info("the response status is not ok ,the status is " + response.getStatusLine().getStatusCode() + ",the url is " + url);
            }
            result = EntityUtils.toString(response.getEntity());
            return result;
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != response) {
                response.close();
            }
        }
    }


    public static String get(String url) throws Exception {
        CloseableHttpResponse response = null;
        String result = null;
        try {
            HttpGet get = new HttpGet(url);
            response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                log.info("the response status is not ok ,the status is " + response.getStatusLine().getStatusCode() + ",the url is " + url);
            }
            result = EntityUtils.toString(response.getEntity());
            return result;
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != response) {
                response.close();
            }
        }
    }


    /**
     * 以json的形式发送post请求
     *
     * @param url
     * @param charset
     * @param paramJson
     * @return
     * @throws Exception
     */
    public static String postJson(String url, String charset, String paramJson, CloseableHttpClient httpClient)
            throws Exception {
        CloseableHttpResponse response = null;
        String result = null;
        HttpPost post = null;
        log.info("postjson url:{} ,param:{}", url, paramJson);
        try {
            StringEntity stringEntity = new StringEntity(paramJson);
            stringEntity.setContentType("application/json");
            stringEntity.setContentEncoding(charset);
            post = new HttpPost(url);
            post.setEntity(stringEntity);
            response = httpClient.execute(post);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                log.info("the response status is not ok ,the status is " + response.getStatusLine().getStatusCode() + ",the url is " + url);
            }
            result = EntityUtils.toString(response.getEntity());
            log.info("postjson url:{} ,param:{}, result:{}", url, paramJson, result);
            return result;
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != post) {
                post.releaseConnection();
            }
            if (null != response) {
                response.close();
            }
        }
    }

    /**
     * 以表单提交的方式发送post请求，contentType为application/x-www-form-urlencoded
     *
     * @param url
     * @param charset
     * @param paramsMap
     * @return
     * @throws Exception
     */
    public static String postHtmlForm(String url, String charset,
                                      Map<String, String> paramsMap) throws Exception {
        List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
        if (MapUtils.isNotEmpty(paramsMap)) {
            for (Entry<String, String> entry : paramsMap.entrySet()) {
                BasicNameValuePair nameValuePair = new BasicNameValuePair(entry.getKey(), entry.getValue());
                formparams.add(nameValuePair);
            }
        }
        CloseableHttpResponse response = null;
        try {
            UrlEncodedFormEntity urEntity = new UrlEncodedFormEntity(formparams, charset);
            urEntity.setContentType("application/x-www-form-urlencoded");
            urEntity.setContentEncoding(charset);
            HttpPost post = new HttpPost(url);
            post.setEntity(urEntity);
            response = httpClient.execute(post);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                log.info("the response status is not ok ,the status is " + response.getStatusLine().getStatusCode() + ",the url is " + url);
            }
            String result = EntityUtils.toString(response.getEntity());
            log.info("postform url:{} ,param:{}, result:{}", url, JSON.toJSONString(paramsMap), result);
            return result;
        } catch (Exception e) {
            throw e;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }


    public static String postFormData(String url, String charset,
                                      Map<String, String> paramsMap) throws Exception {
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        if (MapUtils.isNotEmpty(paramsMap)) {
            for (Entry<String, String> entry : paramsMap.entrySet()) {
                BasicNameValuePair nameValuePair = new BasicNameValuePair(entry.getKey(), entry.getValue());
                formparams.add(nameValuePair);
            }
        }
        CloseableHttpResponse response = null;
        try {
            HttpEntity entity = null;
            if (MapUtils.isNotEmpty(paramsMap)) {
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                for (Entry<String, String> entry : paramsMap.entrySet()) {
                    builder.addPart(entry.getKey(), new StringBody(entry.getValue(), Charset.forName("UTF-8")));
                }
                entity = builder.build();
            }

            HttpPost post = new HttpPost(url);
            post.setEntity(entity);

            response = httpClient.execute(post);
            String result = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                log.info("the response status is not ok ,the status is " + response.getStatusLine().getStatusCode() + ",the url is " + url);
            }
            log.info("postform url:{} ,param:{}, result:{}", url, JSON.toJSONString(paramsMap), result);
            return result;
        } catch (Exception e) {
            throw e;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    public static byte[] downloadByGet(String url) {
        CloseableHttpResponse response = null;
        String result = null;
        try {
            HttpGet get = new HttpGet(url);
            response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                log.info("the response status is not ok ,the status is " + response.getStatusLine().getStatusCode() + ",the url is " + url);
            }
            HttpEntity rs = response.getEntity();
            byte[] bytes = EntityUtils.toByteArray(rs);
            return bytes;
        } catch (Exception e) {
            log.error("download by get error" + url, e);
        }
        return null;
    }


    /**
     * 功能描述: 构造请求参数
     *
     * @return 返回类型:
     * @throws Exception
     */
    public static String buildQueryUrl(String url, Map<String, String> params) {
        try {
            if (null == params || params.isEmpty()) {
                return url;
            }
            StringBuilder sb = new StringBuilder(url);
            if (!url.contains("?")) {
                sb.append("?");
            }
            sb.append(map2Url(params));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("build query url error", e);
        }
    }


    /**
     * map构造url
     *
     * @return 返回类型:
     * @throws Exception
     */
    public static String map2Url(Map<String, String> paramToMap) throws Exception {

        if (MapUtils.isEmpty(paramToMap)) {
            return StringUtils.EMPTY;
        }

        StringBuffer url = new StringBuffer();
        boolean isFirst = true;
        for (Entry<String, String> entry : paramToMap.entrySet()) {
            if (isFirst) {
                isFirst = false;
            } else {
                url.append("&");
            }
            url.append(entry.getKey()).append("=");
            String value = entry.getValue();
            if (!StringUtils.isEmpty(value)) {
                url.append(URLEncoder.encode(value, StandardCharsets.UTF_8.name()));
            }
        }
        return url.toString();
    }


    public static Map<String, String> queryParamToMap(String url) {

        if (StringUtils.isBlank(url)) {
            return Collections.EMPTY_MAP;
        }
        if (url.contains("?")) {
            url = url.substring(url.indexOf("?") + 1, url.length());
        }

        Map<String, String> map = new HashMap<>(1);
        String[] params = url.split("&");
        for (int i = 0; i < params.length; i++) {
            String[] p = params[i].split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }

}