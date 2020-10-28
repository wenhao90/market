package com.market.request;

import lombok.Data;

@Data
public class StockMarginsecRequest {

    private String method;

    private String token;

    private String date;

    public StockMarginsecRequest(String method, String token) {
        this.method = method;
        this.token = token;
        this.date = "";
    }

    public StockMarginsecRequest(String method, String token, String date) {
        this.method = method;
        this.token = token;
        this.date = date;
    }
}
