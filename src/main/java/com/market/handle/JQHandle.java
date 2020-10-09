package com.market.handle;

import com.market.config.User;
import com.market.request.LoginRequest;
import com.market.utils.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JQHandle {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    @Autowired
    private User user;

    @Autowired
    private HttpClientUtil clientUtil;

    /**
     * 获取token
     *
     * @return
     */
    public String auth() {
        LoginRequest request = new LoginRequest(HandleConstants.GET_TOKEN, user.getName(), user.getPassword());
        String token = (String) clientUtil.post(HandleConstants.JQ_URL, request, String.class);
        return token;
    }

}
