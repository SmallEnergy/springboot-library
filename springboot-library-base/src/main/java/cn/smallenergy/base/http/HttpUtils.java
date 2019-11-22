package cn.smallenergy.base.http;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private static final HttpClient client = HttpClientBuilder.create()
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) "
                    + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36")
            .disableAuthCaching().build();
    private static final int TIME_OUT = 6000;
    private static final RequestConfig rc = RequestConfig.custom()
            .setConnectTimeout(TIME_OUT)
            .setSocketTimeout(TIME_OUT).build();

    private static void setHeader(HttpRequestBase base, Map<String, ? extends Object> header) {
        if (header != null && !header.isEmpty()) {
            for (Map.Entry<String, ? extends Object> e : header.entrySet()) {
                base.setHeader(e.getKey(), String.valueOf(e.getValue()));
            }
        }
        base.setConfig(rc);
    }

    private static String url(String url) {
        Preconditions.checkArgument(StringUtils.isNotBlank(url), "url有误!");
        if (url.startsWith("http")) {
            return url;
        }
        return "http://" + url;
    }

    public static String doPost(String url, String content) {
        return doPost(url, content, null, 0);
    }

    /**
     * 以 application/x-www-form-urlencoded 方式提交数据
     *
     * @param content 被提交的键值对
     *
     * @return
     */
    public static String doPost(String url, Map<String, ? extends Object> content,
                                Map<String, ? extends Object> header, int timeOut) {

        StringBuilder sb = new StringBuilder();
        if (content != null && !content.isEmpty()) {
            for (Map.Entry<String, ? extends Object> en : content.entrySet()) {
                sb.append(en.getKey()).append("=").append(String.valueOf(en.getValue())).append("&");
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        Map<String, String> h = Maps.newHashMap();
        if (header != null && !header.isEmpty()) {
            for (Map.Entry<String, ? extends Object> en : header.entrySet()) {
                h.put(en.getKey(), String.valueOf(en.getValue()));
            }
        }
        if (h.get("Content-Type") == null) {
            h.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        }
        return doPost(url, sb.toString(), header, timeOut);

    }

    public static String doPost(String url, String content,
                                Map<String, ? extends Object> header, int timeOut) {
        HttpPost post = new HttpPost(url = url(url));

        setHeader(post, header);
        if (timeOut > 0) {
            // 链接超时时间和传输超时时间定义一致. 都是传入的参数
            post.setConfig(RequestConfig.custom()
                    .setConnectTimeout(timeOut)
                    .setSocketTimeout(timeOut)
                    .build());
        }

        HttpResponse response;
        try {
            if (StringUtils.isNotBlank(content)) {
                StringEntity entity = new StringEntity(content, Charset.defaultCharset());
                post.setEntity(entity);
            }
            logger.info("Http Post请求{}, 请求Header:{}, 请求超时:{}", url, header, timeOut);
            response = client.execute(post);
            if (response == null) {
                logger.error("httpPost请求返回状态有误, 请求连接:{}, 请求异常.", url);
                post.abort();
                return null;
            }
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                logger.error("httpPost请求返回状态有误, 请求连接:{}, 请求异常, 响应头Code:{}",
                        url, response.getStatusLine().getStatusCode());
                return null;
            }
            String s = null;
            try {
                s = EntityUtils.toString(response.getEntity());
            } catch (IOException e) {
                post.abort();
                logger.error("httpPost请求返回值异常, 请求连接:{}, 请求参数:{}", url, content);
            }
            return s;
        } catch (IOException e) {
            logger.info("httpPost 请求异常, ", e);
        } finally {
            // 通过请求的释放委托给HttpClient, 交由系统配置的关闭策略. 而不是每次都关闭连接.
            // 当前系统中无需每次都关闭所有连接再池化连接池.
            post.releaseConnection();
        }
        return null;
    }

    /**
     * 每次请求都建立一次链接的Http Post
     *
     * @param url           被请求的url
     * @param content       请求内容
     * @param header        请求头信息
     * @param timeOut       请求超时时间
     * @return              响应text
     * @throws IOException
     */
    public static String doPostWithoutPool(String url, String content,
                                           Map<String, ? extends Object> header, int timeOut) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createSystem();
        try {
            HttpPost post = new HttpPost(url(url));
            setHeader(post, header);
            if (timeOut > 0) {
                // 链接超时时间和传输超时时间定义一致. 都是传入的参数
                post.setConfig(RequestConfig.custom()
                        .setConnectTimeout(timeOut)
                        .setSocketTimeout(timeOut).build());
            }

            // Create a custom response handler
            ResponseHandler<String> responseHandler = (final HttpResponse response) -> {

                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    logger.error("httpPost请求返回状态有误, 请求连接:{}, 请求参数:{}, 请求异常, 响应头Code:{}",
                            url, content, status);
                    return null;
                }

            };
            if (StringUtils.isNotBlank(content)) {
                StringEntity entity = new StringEntity(content, Charset.defaultCharset());
                post.setEntity(entity);
            }
            logger.info("Http Post请求{}, 请求Header:{}, 请求超时:{}", url, header, timeOut);

            return httpclient.execute(post, responseHandler);
        } finally {
            httpclient.close();
        }
    }

    public static String doGet(String url, Map<String, ? extends Object> body) {
        return doGet(url, null, body, 0);
    }

    public static String doGet(String url) {
        return doGet(url, null, null, 0);
    }

    public static String doGet(String url, Map<String, Object> header, Map<String, ? extends Object> body,
                               int timeOut) {

        StringBuilder sb = new StringBuilder(url = url(url));
        if (body != null && !body.isEmpty()) {
            if (!url.contains("?")) {
                sb.append("?");
            }
            for (Map.Entry<String, ? extends Object> e : body.entrySet()) {
                sb.append(e.getKey()).append("=").append(e.getValue()).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        HttpGet get = new HttpGet(sb.toString());

        setHeader(get, header);
        if (timeOut > 0) {
            get.setConfig(RequestConfig.custom()
                    .setConnectTimeout(timeOut)
                    .setSocketTimeout(timeOut)
                    .build());
        }

        HttpResponse response = null;
        try {
            response = client.execute(get);
            if (response == null) {
                get.abort();
                logger.error("httpGet请求返回状态有误, 请求连接:{}, 请求参数:{}", url, body);
                return null;
            }
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                logger.error("httpGet请求返回状态有误, 请求连接:{}, 请求参数:{}, 响应头:{}",
                        url, body, response.getStatusLine().getStatusCode());
                return null;
            }
            String s = null;
            try {
                s = EntityUtils.toString(response.getEntity());
            } catch (IOException e) {
                get.abort();
                logger.error("httpGet请求返回值异常, 请求连接:{}, 请求参数:{}", url, body, e);
            }
            return s;
        } catch (IOException e) {
            logger.error("httpGet 请求异常, ", e);
        } finally {
            // 通过请求的释放委托给HttpClient, 交由系统配置的关闭策略. 而不是每次都关闭连接.
            // 当前系统中无需每次都关闭所有连接再池化连接池.
            get.releaseConnection();
        }
        return null;
    }

    public static String doPostJson(String url, String content) {
        Map<String, Object> m = Maps.newHashMap();
        m.put("Content-Type", "application/json;charset=UTF-8");
        return doPost(url, content, m, 0);
    }

    /**
     * 获取ip.
     * */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = "";
        try {
            ip = request.getHeader("X-Real-IP");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("X-Forwarded-For");
            }
            if (ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        }catch (Exception e){
            logger.error("获取ie异常");
        }

        return ip;
    }
}
