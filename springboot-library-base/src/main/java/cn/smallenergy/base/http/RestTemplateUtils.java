package cn.smallenergy.base.http;

import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.*;

import java.util.Map;

/**
 * @author xy
 */
public class RestTemplateUtils {
    /**请求超时时间*/
    private final  static int TIMI_OUT=60*1000;

    /**单例*/
    static class RestTemplateSingleton{
        final static RestTemplate INSTANCE =  new RestTemplate(getRequestFactory());
    }

    /**
     * 设置请求超时时间
     * @return
     */
    private static SimpleClientHttpRequestFactory getRequestFactory(){
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(TIMI_OUT);
        requestFactory.setReadTimeout(TIMI_OUT);
        return requestFactory;
    }
    /**
     * 获取单例实例
     */
    public static RestTemplate getInstance(){
        return RestTemplateSingleton.INSTANCE;
    }


    /**
     * delete 请求
     * 自定义 header
     * @param url 请求地址
     * @param body  请求入参
     * @param headerMap key:header属性，value;属性值 其他入参拼接
     * @param responseType  返回实体类型
     * @return
     */
    public static <T>RespBody delete(String url,String body, Map<String,String> headerMap,Class<T> responseType){
        return commonHttp(url,HttpMethod.DELETE,body,headerMap,responseType);
    }

    /**
     * delete 请求
     * 默认 header
     * @param url 请求地址
     * @param body  请求入参
     * @param responseType  返回实体类型
     * @return
     */
    public static <T>RespBody delete(String url,String body,Class<T> responseType){
        return delete(url,body,null,responseType);
    }


    /**
     * delete 请求
     * @param url       请求地址
     * @param params    删除占位入参{}
     */
    public static void delete(String url,String... params){
        RestTemplateUtils.getInstance().delete(url,params);
    }


    /**
     * put 请求
     * @param url       请求地址
     * @param obj       put入参
     * @param params    put占位入参
     */
    public static void put(String url,Object obj,String... params){
        RestTemplateUtils.getInstance().put(url,obj,params);
    }

    /**
     * put 请求
     * 默认 header
     * @param url  请求地址
     * @param body  请求入参
     * @param responseType  返回实体类型
     * @return
     */
    public static  <T> RespBody put(String url,String body,Class<T> responseType){
        return put(url,body,null,responseType);
    }


    /**
     * put 请求
     * 自定义 header
     * @param url 请求地址
     * @param body  请求入参
     * @param headerMap key:header属性，value;属性值 其他入参拼接
     * @param responseType  返回实体类型
     * @return
     */
    public static <T>RespBody put(String url,String body, Map<String,String> headerMap,Class<T> responseType){
        return commonHttp(url,HttpMethod.PUT,body,headerMap,responseType);
    }


    /**
     * post 请求
     * 自定义 header
     * @param url   请求地址
     * @param body  请求入参
     * @param headerMap key:header属性，value;属性值 其他入参拼接
     * @return
     */
    public static <T> RespBody post(String url, String body, Map<String,String> headerMap,Class<T> responseType){
        return commonHttp(url,HttpMethod.POST,body,headerMap,responseType);
    }

    /**
     * post 请求
     * 默认 header
     * @param url   请求地址
     * @param body  请求入参
     * @return
     */
    public static <T> RespBody post(String url, String body,Class<T> responseType){
        return commonHttp(url,HttpMethod.POST,body,null,responseType);
    }

    /**
     * get请求
     * 自定义 header
     * @param url   请求地址
     * @param headerMap   key:header属性，value;属性值 其他入参拼接
     * @return
     */
    public static <T> RespBody get(String url, Map<String,String> headerMap,Class<T> responseType) {
        return commonHttp(url,HttpMethod.GET,null,headerMap,responseType);
    }

    /**
     * get请求
     * 默认 header
     * @param url   请求地址
     * @return
     */
    public static  <T>RespBody get(String url,Class<T> responseType) {
        RespBody respBody = get(url,null,responseType);
        return respBody;
    }

    /**
     * 公共请求方法
     * @param url           请求地址
     * @param httpMethod    请求方式
     * @param body          请求入参
     * @param headerMap     自定义header
     * @param responseType  返回类型
     * @param <T>           返回类型泛型
     * @return
     */
    public static <T> RespBody commonHttp(String url,HttpMethod httpMethod ,String body, Map<String,String> headerMap,Class<T> responseType){
        HttpHeaders headers = getHeaders(headerMap);
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        RespBody respBody = new RespBody();
        try {
            ResponseEntity<T> response = RestTemplateUtils.getInstance().exchange(url, httpMethod, requestEntity, responseType);
            respBody.setRespCode(response.getStatusCodeValue());
            respBody.setEntity(response.getBody());
        } catch (RestClientResponseException e) {
            respBody.setRespCode(e.getRawStatusCode());
            respBody.setRespValue(e.getResponseBodyAsString());
        }catch (Throwable e1){
            respBody.setRespCode(400);
            respBody.setRespValue(e1.toString());
        }
        return respBody;
    }

    /**
     * 拼接 请求header
     * @param headerMap key:header属性，value;属性值 其他入参拼接
     * @return
     */
    private static HttpHeaders getHeaders(Map<String,String> headerMap){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Accpet-Encoding", "gzip");
        headers.add("Content-Encoding", "UTF-8");
        headers.add("Content-Type", "application/json; charset=UTF-8");
        if(headerMap!=null){
            for(Map.Entry<String, String> entry : headerMap.entrySet()){
                headers.add(entry.getKey(),entry.getValue());
            }
        }
        return headers;
    }


    /**
     * 返回信息实体
     */
   public static class RespBody<T>{
        private  int respCode;
        private String respValue;
        private T Entity;

        public T getEntity() {
            return Entity;
        }

        public void setEntity(T entity) {
            Entity = entity;
        }

        public int getRespCode() {
           return respCode;
       }

       public void setRespCode(int respCode) {
           this.respCode = respCode;
       }

       public String getRespValue() {
           return respValue;
       }

       public void setRespValue(String respValue) {
           this.respValue = respValue;
       }
        private RespBody(){}
    }
}


