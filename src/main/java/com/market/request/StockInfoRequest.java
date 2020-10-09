package com.market.request;

import lombok.Data;

@Data
public class StockInfoRequest {

    private String method;

    private String token;

    private String code;

    public StockInfoRequest(String method, String token, String code) {
        this.method = method;
        this.token = token;
        this.code = code;
    }
}
