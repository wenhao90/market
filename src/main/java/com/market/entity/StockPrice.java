package com.market.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("stock_price")
public class StockPrice {

    @Indexed
    private String code;

    @Indexed
    private String date;

    @Indexed
    private long timestamp;

    private double open;

    @Indexed
    private double close;

    private double high;

    private double low;

    private double volume;

    private double money;

    private double ma_20;

    private double ma_60;

    private double ma_120;

    private double ema_20;

    private double ema_60;

    private double ema_120;

}
