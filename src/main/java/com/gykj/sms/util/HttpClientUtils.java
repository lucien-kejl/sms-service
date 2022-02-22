package com.gykj.sms.util;

import com.alibaba.fastjson.JSON;
import org.omg.CORBA.Object;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;



/**
 * @author 李蒙
 * @date 2020/3/23 17:59
 */
public class HttpClientUtils<T> {
    private static Logger log = org.slf4j.LoggerFactory.getLogger(HttpClientUtils.class);

    private RestTemplate restTemplate = new RestTemplate();


    //header头部信息
    private static Map<String, String> HEADER_MAP = new HashMap<String, String>(){{
        put("Content-Type", "application/json");
    }};

    private Map<String,String> ftokenMap= new HashMap<>();

    public static final HttpClientUtils HC = new HttpClientUtils();

    private HttpClientUtils(){

    }

    /**
     * @param body   参数
     * @param headerMap header参数信息
     * @return 返回请求体
     */
    public HttpEntity setHeaders(T body, Map<String, String> headerMap) {
        HttpHeaders headers =  null;
        if (Objects.nonNull(headerMap) && headerMap.size()>0){
            headers =  new HttpHeaders();

            HttpHeaders finalHeaders = headers;
            headerMap.forEach((k, v) -> {
                finalHeaders.add(k, v);
            });
        }

        return new HttpEntity(body, headers);
    }

    /**
     * @param url        路径
     * @param httpMethod 发送方式
     * @param httpEntity 请求体
     * @return 返回响应体
     */
    public ResponseEntity<String> send(String url, HttpMethod httpMethod, HttpEntity httpEntity)  throws Exception {
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, String.class);
            return responseEntity;
        }catch (Exception e){
            throw e;
        }
    }



    /**
     * 过滤value值为null或空字符
     * @param map
     */
    public Map  mapFilter(Map map){
        if (Objects.nonNull(map) && map.size()>0){
            Iterator<Object> iterator = map.values().iterator();
            while (iterator.hasNext()) {
                Object value = iterator.next();
                if (Objects.isNull(value)||Objects.equals("",value)){
                    iterator.remove();
                }
            }
        }
        return map;
    }

    /**
     * 结果转化具体信息
     * @param data
     * @param tClass
     * @param <T>
     * @return
     */
    public  <T> T  strToBean(String data,Class<T> tClass) {
        try {
            return JSON.parseObject(data, tClass);
        }catch (Exception e){
            log.error("类型转换异常",e);
        }
       return  null;

    }

    public  List<String> getCookieList(HttpServletRequest request) {
        List<String> cookieList = new ArrayList<>();

        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return cookieList;
        }

        for (Cookie cookie : cookies) {
            cookieList.add(cookie.getName() + "=" + cookie.getValue());
        }

        return cookieList;
    }

    /**
     * @param param 参数
     * @return 判断标识
     */
    public  boolean isNull (String ... param){
        AtomicBoolean flag= new AtomicBoolean(false);
        Arrays.stream(param).forEach(p->{
            if (Objects.isNull(p) || StringUtils.isEmpty(p)){
                flag.set(true);
            }
        });
        return flag.get();
    }

    public  boolean isNull (Object ... param){
        AtomicBoolean flag= new AtomicBoolean(false);
        Arrays.stream(param).forEach(p->{
            if (Objects.isNull(p) || StringUtils.isEmpty(p.toString())){
                flag.set(true);
            }
        });
        return flag.get();
    }

    /**
     * url参数拼接
     * @param url      路径
     * @param paramMap 参数
     * @return 带参urk
     */
    public  String spliceUrl(String url, Map paramMap) {

        StringBuilder splice = new StringBuilder("?");
        paramMap.forEach((k, v) -> {
            splice.append(k).append("=").append(v).append("&");
        });
        if (splice.toString().endsWith("&")) {
            url += splice.toString().substring(0, splice.length() - 1);
        }
        return url;
    }

}