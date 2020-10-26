package com.market.request;

import lombok.Data;

@Data
public class StockoMessRequest {

    private String method;

    private String token;

    private String code;

    private String date;

    private String end_date;


    public StockoMessRequest(String method, String token,String code, String date) {
        this.method = method;
        this.token = token;
        this.date = date;
    }

}
