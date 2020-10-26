package com.market.request;

import lombok.Data;

@Data
public class StockoMarginsecRequest {

    private String method;

    private String token;

    private String date;

    public StockoMarginsecRequest(String method, String token) {
        this.method = method;
        this.token = token;
        this.date = "";
    }

    public StockoMarginsecRequest(String method, String token, String date) {
        this.method = method;
        this.token = token;
        this.date = date;
    }
}
