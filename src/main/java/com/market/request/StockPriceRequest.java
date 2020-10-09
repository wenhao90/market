package com.market.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StockPriceRequest {

    private String method;

    private String token;

    private String code;

    /**
     * 5m, 1d, 1w, 1M
     */
    private String unit;

    /**
     * 2018-12-04 09:45:00
     */
    private String date;

    private String end_date;

    private String fq_ref_date;

}
