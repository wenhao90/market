package com.market.utils;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * @author wenhao
 * @date: 2019/1/17 17:54
 * @description: 远程调用的client
 */
@Component
public class HttpClientUtil<T> {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    @Autowired
    private RestTemplate restTemplate;

    /**
     * post 请求
     *
     * @param params
     * @param url
     * @return
     */
    public T post(String url, Object params, Class<T> responseClass) throws ResourceAccessException {
        ResponseEntity<T> response = restTemplate.postForEntity(url, getHttpEntityForPost(params), responseClass);
        return response.getBody();
    }

    /**
     * @param params
     * @return org.springframework.http.HttpEntity<java.lang.String>
     * @Description
     * @Date 15:51 2020/8/24 POST HttpEntity
     */
    private HttpEntity<String> getHttpEntityForPost(Object params) {
        return (HttpEntity<String>) new HttpEntity(JSON.toJSONString(params));
    }

}
