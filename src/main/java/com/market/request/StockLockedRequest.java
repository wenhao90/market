package com.market.request;

import lombok.Data;

@Data
public class StockLockedRequest {

    private String method;

    private String token;

    private String code;

    private String date;

    private String end_date;


    public StockLockedRequest(String method, String token, String code, String date, String endDate) {
        this.method = method;
        this.token = token;
        this.code = code;
        this.date = date;
        this.end_date = endDate;
    }

}
