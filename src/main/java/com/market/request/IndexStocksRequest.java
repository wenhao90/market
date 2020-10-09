package com.market.request;

import lombok.Data;

@Data
public class IndexStocksRequest {

    private String method;

    private String token;

    private String code;

    private String date;

    public IndexStocksRequest(String method, String token, String code) {
        this.method = method;
        this.token = token;
        this.code = code;
    }
}
