package com.market.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("industry_price")
public class IndustryPrice {

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

}
