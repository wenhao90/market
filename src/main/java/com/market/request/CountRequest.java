package com.market.request;

import lombok.Data;

@Data
public class CountRequest {

    private String method;

    private String token;

    public CountRequest(String method, String token) {
        this.method = method;
        this.token = token;

    }
}
